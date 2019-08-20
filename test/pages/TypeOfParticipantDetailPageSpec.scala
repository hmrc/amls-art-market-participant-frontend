/*
 * Copyright 2019 HM Revenue & Customs
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

import models.TypeOfParticipant.{ArtGalleryOwner, SomethingElse}
import models.UserAnswers
import pages.behaviours.PageBehaviours


class TypeOfParticipantDetailPageSpec extends PageBehaviours {

  "TypeOfParticipantDetailPage" must {

    beRetrievable[String](TypeOfParticipantDetailPage)

    beSettable[String](TypeOfParticipantDetailPage)

    beRemovable[String](TypeOfParticipantDetailPage)

    "cleanup the TypeOfParticipantPage value where not something else" in {

      val typeOfParticipant = Seq(ArtGalleryOwner)
      val answerParticipantDetailQuestion = UserAnswers("id").set(TypeOfParticipantDetailPage, "something").success.value
      val updatedAnswers = answerParticipantDetailQuestion.set(TypeOfParticipantPage, typeOfParticipant).success.value

      updatedAnswers.get(TypeOfParticipantDetailPage) must be(empty)
    }

    "not cleanup the TypeOfParticipantPage value where something else" in {

      val typeOfParticipant = Seq(SomethingElse)
      val answerParticipantDetailQuestion = UserAnswers("id").set(TypeOfParticipantDetailPage, "something").success.value
      val updatedAnswers = answerParticipantDetailQuestion.set(TypeOfParticipantPage, typeOfParticipant).success.value

      updatedAnswers.get(TypeOfParticipantDetailPage) mustNot be(empty)
    }
  }
}
