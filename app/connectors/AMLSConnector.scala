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

import config.Service
import models.UserAnswers
import play.api.Configuration
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse, StringContextOps}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AMLSConnector @Inject()(config: Configuration,
                              implicit val httpClientV2: HttpClientV2)
                             (implicit ec: ExecutionContext) {

  private val baseUrl = config.get[Service]("microservice.services.amls-frontend")
  private[connectors] val amlsUrl: String = s"$baseUrl/amp"

  def get(credId: String)(implicit hc: HeaderCarrier): Future[Option[JsObject]] = {
    val getUrl = s"$amlsUrl/get/$credId"

    httpClientV2.get(url"$getUrl").execute[Option[JsObject]]
  }

  def set(credId: String, userAnswers: UserAnswers)(implicit hc: HeaderCarrier): Future[HttpResponse] = {
    val putUrl = s"$amlsUrl/set/$credId"
    val hcWithExtra: HeaderCarrier = hc.withExtraHeaders("Csrf-Token" -> "nocheck")

    httpClientV2.put(url"$putUrl")(hcWithExtra)
      .withBody(Json.toJson(userAnswers))
      .execute[HttpResponse]
  }
}
