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

import javax.inject.{Inject, Singleton}

import play.api.mvc.Call
import controllers.routes
import pages._
import models._

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case TypeOfParticipantPage            => _ => routes.BoughtOrSoldOverThresholdController.onPageLoad(NormalMode)
    case BoughtOrSoldOverThresholdPage    =>      artSoldOverThresholdRoute
    case DateTransactionOverThresholdPage => _ => routes.IdentifyLinkedTransactionsController.onPageLoad(NormalMode)
    case IdentifyLinkedTransactionsPage   => _ => routes.PercentageExpectedTurnoverController.onPageLoad(NormalMode)
    case PercentageExpectedTurnoverPage   => _ => routes.CheckYourAnswersController.onPageLoad()
    case _                                => _ => routes.IndexController.onPageLoad()
  }

  private def artSoldOverThresholdRoute(answers: UserAnswers): Call = answers.get(BoughtOrSoldOverThresholdPage) match {
    case Some(true)  => routes.DateTransactionOverThresholdController.onPageLoad(NormalMode)
    case Some(false) => routes.IdentifyLinkedTransactionsController.onPageLoad(NormalMode)
    case None        => routes.SessionExpiredController.onPageLoad()
  }

  private def artSoldOverThresholdRouteEdit(answers: UserAnswers): Call = answers.get(BoughtOrSoldOverThresholdPage) match {
    case Some(true)  => routes.DateTransactionOverThresholdController.onPageLoad(CheckMode)
    case Some(false) => routes.CheckYourAnswersController.onPageLoad()
    case None        => routes.SessionExpiredController.onPageLoad()
  }

  private val checkRouteMap: Page => UserAnswers => Call = {
    case BoughtOrSoldOverThresholdPage => artSoldOverThresholdRouteEdit
    case _ => _ => routes.CheckYourAnswersController.onPageLoad()
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
