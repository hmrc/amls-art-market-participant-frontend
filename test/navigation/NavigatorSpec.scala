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

package navigation

import base.SpecBase
import controllers.routes
import pages._
import models._

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" when {

    "in Normal mode" must {

      "go to Index from a page that doesn't exist in the route map" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, NormalMode, UserAnswers("id")) mustBe routes.IndexController.onPageLoad()
      }

      "go from Type of Participant to Art Sold Over Threshold" in {

        val answers = UserAnswers("id")

        navigator.nextPage(TypeOfParticipantPage, NormalMode, answers)
            .mustBe(routes.BoughtOrSoldOverThresholdController.onPageLoad(NormalMode))
      }

      "go from Art Sold Over Threshold to Date Transaction Over Threshold where yes" in {
        val answers = UserAnswers("id").set(BoughtOrSoldOverThresholdPage, true).success.value

        navigator.nextPage(BoughtOrSoldOverThresholdPage, NormalMode, answers)
          .mustBe(routes.DateTransactionOverThresholdController.onPageLoad(NormalMode))
      }

      "go from Art Sold Over Threshold to Identify Linked Transactions where no" in {
        val answers = UserAnswers("id").set(BoughtOrSoldOverThresholdPage, false).success.value

        navigator.nextPage(BoughtOrSoldOverThresholdPage, NormalMode, answers)
          .mustBe(routes.IdentifyLinkedTransactionsController.onPageLoad(NormalMode))
      }

      "go from Date Transaction Over Threshold to Identify Linked Transactions page" in{

        val answers = UserAnswers("id")

        navigator.nextPage(DateTransactionOverThresholdPage, NormalMode, answers)
          .mustBe(routes.IdentifyLinkedTransactionsController.onPageLoad(NormalMode))
      }

      "go from Identify Linked Transactions to Percentage Turnover From Sales Over Threshold page" in{

        val answers = UserAnswers("id")

        navigator.nextPage(IdentifyLinkedTransactionsPage, NormalMode, answers)
          .mustBe(routes.PercentageExpectedTurnoverController.onPageLoad(NormalMode))
      }

      "go from Percentage Turnover Sales Over Threshold to Check your answers page" in{

        val answers = UserAnswers("id")

        navigator.nextPage(PercentageExpectedTurnoverPage, NormalMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }
    }

    "in Check mode" must {

      "go to CheckYourAnswers from a page that doesn't exist in the edit route map" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "go from Type of Participant to Check Your Answers" in {

        val answers = UserAnswers("id")

        navigator.nextPage(TypeOfParticipantPage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from Art Sold Over Threshold to Check Your Answers where no" in {
        val answers = UserAnswers("id").set(BoughtOrSoldOverThresholdPage, false).success.value

        navigator.nextPage(BoughtOrSoldOverThresholdPage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from Art Sold Over Threshold to Date Transaction Over Threshold where answer yes" in {
        val answers = UserAnswers("id").set(BoughtOrSoldOverThresholdPage, true).success.value

        navigator.nextPage(BoughtOrSoldOverThresholdPage, CheckMode, answers)
          .mustBe(routes.DateTransactionOverThresholdController.onPageLoad(CheckMode))
      }

      "go from Date Transaction Over Threshold to Check Your Answers" in{

        val answers = UserAnswers("id")

        navigator.nextPage(DateTransactionOverThresholdPage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from Identify Linked Transactions to Check Your Answers" in{

        val answers = UserAnswers("id")

        navigator.nextPage(IdentifyLinkedTransactionsPage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from Percentage Turnover Sales Over Threshold to Check your answers page" in{

        val answers = UserAnswers("id")

        navigator.nextPage(PercentageExpectedTurnoverPage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }
    }
  }
}
