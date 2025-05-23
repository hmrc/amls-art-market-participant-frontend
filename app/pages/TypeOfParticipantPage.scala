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

package pages

import models.TypeOfParticipant.SomethingElse
import models.{TypeOfParticipant, UserAnswers}
import play.api.libs.json.JsPath

import scala.util.Try

case object TypeOfParticipantPage extends QuestionPage[Seq[TypeOfParticipant]] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "typeOfParticipant"

  override def cleanup(value: Option[Seq[TypeOfParticipant]], userAnswers: UserAnswers): Try[UserAnswers] = {
    value map { ans =>
      ans.contains(SomethingElse) match {
        case true => super.cleanup(value, userAnswers)
        case _    => userAnswers.remove(TypeOfParticipantDetailPage)
      }
    }
  }.getOrElse(super.cleanup(value, userAnswers))
}
