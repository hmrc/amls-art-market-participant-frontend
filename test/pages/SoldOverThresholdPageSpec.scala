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

import java.time.LocalDate
import models.UserAnswers
import org.scalatest.matchers.must.Matchers.{be, convertToAnyMustWrapper, empty}
import pages.behaviours.PageBehaviours

class SoldOverThresholdPageSpec extends PageBehaviours {

  "SoldOverThresholdPage" should {

    beRetrievable[Boolean](SoldOverThresholdPage)

    beSettable[Boolean](SoldOverThresholdPage)

    beRemovable[Boolean](SoldOverThresholdPage)

    "cleanup the DateTransactionOverThresholdPage value where false selected" in {

      val testDate = LocalDate.now

      val answerDateQuestion = UserAnswers().set(DateTransactionOverThresholdPage, testDate).success.value
      val updatedAnswers     = answerDateQuestion.set(SoldOverThresholdPage, false).success.value

      updatedAnswers.get(DateTransactionOverThresholdPage) must be(empty)
    }

    "not cleanup the DateTransactionOverThresholdPage value where true selected" in {

      val testDate = LocalDate.now

      val answerDateQuestion = UserAnswers().set(DateTransactionOverThresholdPage, testDate).success.value
      val updatedAnswers     = answerDateQuestion.set(SoldOverThresholdPage, true).success.value

      updatedAnswers.get(DateTransactionOverThresholdPage) mustNot be(empty)
    }
  }
}
