/*
 * Copyright 2020 HM Revenue & Customs
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
import models.TypeOfParticipant.SomethingElse
import pages._
import models._

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case TypeOfParticipantPage            =>      typeOfParticipantRoute
    case TypeOfParticipantDetailPage      => _ => routes.SoldOverThresholdController.onPageLoad(NormalMode)
    case SoldOverThresholdPage            =>      artSoldOverThresholdRoute
    case DateTransactionOverThresholdPage => _ => routes.IdentifyLinkedTransactionsController.onPageLoad(NormalMode)
    case IdentifyLinkedTransactionsPage   => _ => routes.PercentageExpectedTurnoverController.onPageLoad(NormalMode)
    case PercentageExpectedTurnoverPage   => _ => routes.CheckYourAnswersController.onPageLoad()
  }

  private def typeOfParticipantRoute(answers: UserAnswers): Call = {
    answers.get(TypeOfParticipantPage) map { ans =>
      ans.contains(SomethingElse) match {
        case true  => routes.TypeOfParticipantDetailController.onPageLoad(NormalMode)
        case false => routes.SoldOverThresholdController.onPageLoad(NormalMode)
      }
    }
  }.getOrElse(throw new Exception("Unable to navigate to page"))

  private def typeOfParticipantRouteCheckMode(answers: UserAnswers): Call = {
    answers.get(TypeOfParticipantPage) map { ans =>
      ans.contains(SomethingElse) match {
        case true  => routes.TypeOfParticipantDetailController.onPageLoad(CheckMode)
        case false => routes.CheckYourAnswersController.onPageLoad()
      }
    }
  }.getOrElse(throw new Exception("Unable to navigate to page"))

  private def artSoldOverThresholdRoute(answers: UserAnswers): Call = answers.get(SoldOverThresholdPage) match {
    case Some(true)  => routes.DateTransactionOverThresholdController.onPageLoad(NormalMode)
    case Some(false) => routes.IdentifyLinkedTransactionsController.onPageLoad(NormalMode)
    case None        => throw new Exception("Unable to navigate to page")
  }

  private def artSoldOverThresholdRouteCheckMode(answers: UserAnswers): Call = answers.get(SoldOverThresholdPage) match {
    case Some(true)  => routes.DateTransactionOverThresholdController.onPageLoad(CheckMode)
    case Some(false) => routes.CheckYourAnswersController.onPageLoad()
    case None        => throw new Exception("Unable to navigate to page")
  }

  private val checkRouteMap: Page => UserAnswers => Call = {
    case TypeOfParticipantPage         => typeOfParticipantRouteCheckMode
    case SoldOverThresholdPage => artSoldOverThresholdRouteCheckMode
    case _ => _ => routes.CheckYourAnswersController.onPageLoad()
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
