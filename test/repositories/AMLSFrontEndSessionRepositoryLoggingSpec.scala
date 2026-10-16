/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package repositories

import base.SpecBase
import ch.qos.logback.classic.Level
import connectors.AMLSConnector
import models.UserAnswers
import play.api.libs.json.{JsObject, Json}
import play.api.{Configuration, Logger}
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.tools.LogCapturing

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AMLSFrontEndSessionRepositoryLoggingSpec extends SpecBase with LogCapturing {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private lazy val configuration: Configuration =
    applicationBuilder(userAnswers = None).build().injector.instanceOf[Configuration]

  private val repoLogger: Logger = Logger(classOf[DefaultAMLSFrontEndSessionRepository])

  private def repositoryWith(
    getResult: => Future[Option[JsObject]] = Future.successful(None),
    setResult: => Future[HttpResponse] = Future.successful(HttpResponse(200, "ok"))
  ): DefaultAMLSFrontEndSessionRepository = {
    val connector = new AMLSConnector(configuration, null.asInstanceOf[HttpClientV2]) {
      override def get(credId: String)(implicit hc: HeaderCarrier): Future[Option[JsObject]]                  = getResult
      override def set(credId: String, userAnswers: UserAnswers)(implicit hc: HeaderCarrier): Future[HttpResponse] = setResult
    }
    new DefaultAMLSFrontEndSessionRepository(connector, configuration)
  }

  "DefaultAMLSFrontEndSessionRepository logging" must {

    "return None and log a warning with the exception attached when fetching session data fails" in {
      val repo = repositoryWith(getResult = Future.failed(new RuntimeException("backend down")))

      withCaptureOfLoggingFrom(repoLogger) { logs =>
        repo.get("credId").futureValue mustBe None

        val event = logs.find(_.getFormattedMessage.contains("[DefaultAMLSFrontEndSessionRepository][get] - Failed to fetch or parse session data"))
        event.map(_.getLevel) mustBe Some(Level.WARN)
        Option(event.get.getThrowableProxy).map(_.getMessage) mustBe Some("backend down")
      }
    }

    "return None and log a warning when the saved session data cannot be parsed" in {
      val repo = repositoryWith(getResult = Future.successful(Some(Json.obj("data" -> "not-an-object"))))

      withCaptureOfLoggingFrom(repoLogger) { logs =>
        repo.get("credId").futureValue mustBe None

        logs.exists(e =>
          e.getLevel == Level.WARN &&
            e.getFormattedMessage.contains("[DefaultAMLSFrontEndSessionRepository][get] - Failed to fetch or parse session data")
        ) mustBe true
      }
    }

    "return the saved answers and log no warning when fetching succeeds" in {
      val saved = Json.toJson(UserAnswers()).as[JsObject]
      val repo  = repositoryWith(getResult = Future.successful(Some(saved)))

      withCaptureOfLoggingFrom(repoLogger) { logs =>
        repo.get("credId").futureValue mustBe Some(UserAnswers())
        logs.exists(_.getLevel == Level.WARN) mustBe false
      }
    }

    "return false and log a warning with the exception attached when saving session data fails" in {
      val repo = repositoryWith(setResult = Future.failed(new RuntimeException("backend down")))

      withCaptureOfLoggingFrom(repoLogger) { logs =>
        repo.set("credId", UserAnswers()).futureValue mustBe false

        val event = logs.find(_.getFormattedMessage.contains("[DefaultAMLSFrontEndSessionRepository][set] - Failed to save session data"))
        event.map(_.getLevel) mustBe Some(Level.WARN)
        Option(event.get.getThrowableProxy).map(_.getMessage) mustBe Some("backend down")
      }
    }

    "return true and log no warning when saving succeeds" in {
      val repo = repositoryWith()

      withCaptureOfLoggingFrom(repoLogger) { logs =>
        repo.set("credId", UserAnswers()).futureValue mustBe true
        logs.exists(_.getLevel == Level.WARN) mustBe false
      }
    }
  }
}
