/*
 * Copyright 2024 HM Revenue & Customs
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

package connectors

import base.SpecBase
import models.TypeOfParticipant.SomethingElse
import models.UserAnswers
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.TypeOfParticipantPage
import play.api.Configuration
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}

import java.time.{LocalDate, LocalDateTime, ZoneOffset}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AMLSConnectorSpec extends SpecBase with MockitoSugar {

  implicit val hc: HeaderCarrier = HeaderCarrier()
  val amlsConnector = new AMLSConnector(config = mock[Configuration], httpClient = mock[HttpClient])
  val dateVal       = LocalDateTime.now
  val answers       = UserAnswers().set(TypeOfParticipantPage,  Seq(SomethingElse)).success.value

  val completeData  = Json.obj(
    "typeOfParticipant"            -> Seq("artGalleryOwner"),
    "soldOverThreshold"    -> true,
    "dateTransactionOverThreshold" -> LocalDate.now,
    "identifyLinkedTransactions"   -> true,
    "percentageExpectedTurnover"   -> "fortyOneToSixty"
  )

  val completeJson  = Json.obj(
    "_id"         -> "someid",
    "data"        -> completeData,
    "lastUpdated" -> Json.obj("$date" -> dateVal.atZone(ZoneOffset.UTC).toInstant.toEpochMilli)
  )

  "GET" must {
    "successfully fetch cache" in {

      when {
        amlsConnector.httpClient.GET[Option[JsObject]](any(), any(), any())(any(), any(), any())
      } thenReturn Future.successful(Some(completeJson))

      whenReady(amlsConnector.get("someid")) {
        _ mustBe Some(completeJson)
      }
    }
  }

  "POST" must {
    "successfully write cache" in {
      val putUrl = s"${amlsConnector.url}/set/someid"

      amlsConnector.set("someid", answers)
      verify(amlsConnector.httpClient).PUT(eqTo(putUrl), eqTo(answers), any())(any(), any(), any(), any())
    }
  }
}