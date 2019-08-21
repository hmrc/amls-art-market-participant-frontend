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

import java.time.LocalDate

import models.UserAnswers
import pages.behaviours.PageBehaviours

class BoughtOrSoldOverThresholdPageSpec extends PageBehaviours {

  "BoughtOrSoldOverThresholdPage" must {

    beRetrievable[Boolean](BoughtOrSoldOverThresholdPage)

    beSettable[Boolean](BoughtOrSoldOverThresholdPage)

    beRemovable[Boolean](BoughtOrSoldOverThresholdPage)

    "cleanup the DateTransactionOverThresholdPage value where false selected" in {

      val testDate = LocalDate.now

      val answerDateQuestion = UserAnswers("id").set(DateTransactionOverThresholdPage, testDate).success.value
      val updatedAnswers = answerDateQuestion.set(BoughtOrSoldOverThresholdPage, false).success.value

      updatedAnswers.get(DateTransactionOverThresholdPage) must be(empty)
    }

    "not cleanup the DateTransactionOverThresholdPage value where true selected" in {

      val testDate = LocalDate.now

      val answerDateQuestion = UserAnswers("id").set(DateTransactionOverThresholdPage, testDate).success.value
      val updatedAnswers = answerDateQuestion.set(BoughtOrSoldOverThresholdPage, true).success.value

      updatedAnswers.get(DateTransactionOverThresholdPage) mustNot be(empty)
    }
  }
}
