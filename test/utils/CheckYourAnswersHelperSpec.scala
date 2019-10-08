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

package utils

import base.SpecBase
import models.UserAnswers
import play.api.libs.json.Json
import play.twirl.api.Html
import viewmodels.AnswerRow

class CheckYourAnswersHelperSpec extends SpecBase {

  val userAnswers: UserAnswers = UserAnswers( Json.obj("typeOfParticipant" -> Seq(
    "artGalleryOwner",
    "artDealer",
    "artAgent",
    "artAuctioneer",
    "somethingElse"),
    "typeOfParticipantDetail"  -> "sdfsdf",
    "boughtOrSoldOverThreshold" -> true,
    "dateTransactionOverThreshold" -> "2010-01-01",
    "identifyLinkedTransactions" -> false,
    "percentageExpectedTurnover" -> "zeroToTwenty"
  ))

  val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswers)(messages)

  "CheckYourAnswersHelper" must {
    "have typeOfParticipant method which" when {
      "called" must {
        "return AnswerRow" in {

          val expected = AnswerRow(Html("What type of art market participant are you?"),
            Html("<ul class=\"list list-bullet\"><li>Art gallery owner</li><li>Art dealer</li><li>Art agent</li><li>Art auctioneer</li><li>sdfsdf</li></ul>"),
            "/anti-money-laundering/art-market-participant/change-type")

          checkYourAnswersHelper.typeOfParticipant.value mustBe expected
        }
      }
    }

    "have boughtOrSoldOverThreshold method which" when {
      "called" must {
        "return AnswerRow" in {

          val expected = AnswerRow(Html("Has your business made a sale or purchase of art for €10,000 or more after 10 January 2020?"),
            Html("Yes"),
            "/anti-money-laundering/art-market-participant/change-sale-or-purchase")

          checkYourAnswersHelper.boughtOrSoldOverThreshold.value mustBe expected
        }
      }
    }

    "have dateTransactionOverThreshold method which" when {
      "called" must {
        "return AnswerRow" in {

          val expected = AnswerRow(Html("When was the first sale or purchase of art for €10,000 or more after 10 January 2020?"),
            Html("1 January 2010"),
            "/anti-money-laundering/art-market-participant/change-first-sale-or-purchase")

          checkYourAnswersHelper.dateTransactionOverThreshold.value mustBe expected
        }
      }
    }

    "have identifyLinkedTransactions method which" when {
      "called" must {
        "return AnswerRow" in {

          val expected = AnswerRow(Html("Are you able to identify multiple payments linked to a single sale or purchase?"),
            Html("No"),
            "/anti-money-laundering/art-market-participant/change-identify-linked-payments")

          checkYourAnswersHelper.identifyLinkedTransactions.value mustBe expected
        }
      }
    }

    "have percentageExpectedTurnover method which" when {
      "called" must {
        "return AnswerRow" in {

          val expected = AnswerRow(Html("How much of your turnover do you expect to come from sales of art for €10,000 or more?"),
            Html("0% to 20%"),
            "/anti-money-laundering/art-market-participant/change-turnover-from-art-sales")

          checkYourAnswersHelper.percentageExpectedTurnover.value mustBe expected
        }
      }
    }
  }
}
