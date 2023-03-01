/*
 * Copyright 2023 HM Revenue & Customs
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
import uk.gov.hmrc.govukfrontend.views.Aliases.{ActionItem, Actions, HtmlContent, Key, Text, Value}
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Content
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.AnswerRow

class CheckYourAnswersHelperSpec extends SpecBase {

  val userAnswers: UserAnswers = UserAnswers( Json.obj("typeOfParticipant" -> Seq(
    "artAuctioneer",
    "artGalleryOwner",
    "artAgent",
    "artDealer",
    "somethingElse"),
    "typeOfParticipantDetail"  -> "sdfsdf",
    "soldOverThreshold" -> true,
    "dateTransactionOverThreshold" -> "2010-01-01",
    "identifyLinkedTransactions" -> false,
    "percentageExpectedTurnover" -> "zeroToTwenty"
  ))


  val userAnswersSingle: UserAnswers = UserAnswers( Json.obj("typeOfParticipant" -> Seq(
    "artGalleryOwner"),
    "typeOfParticipantDetail"  -> "sdfsdf",
    "soldOverThreshold" -> true,
    "dateTransactionOverThreshold" -> "2010-01-01",
    "identifyLinkedTransactions" -> false,
    "percentageExpectedTurnover" -> "zeroToTwenty"
  ))

  val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswers)(messages)

  "CheckYourAnswersHelper" must {
    "have typeOfParticipant method which" when {
      "called" must {
        "return SummaryListRow in alphabetical order" in {


          val expected = createSummaryListRow(
            "typeOfParticipant.heading",
            HtmlContent("<ul class=\"govuk-list govuk-list--bullet\"><li>Auction house</li><li>Art gallery</li><li>Art adviser or agent</li><li>Art dealer</li><li>sdfsdf</li></ul>"),
            "change-type"
          )

          checkYourAnswersHelper.typeOfParticipant.value mustBe expected
        }
        "return SummaryListRow without bullets when only one user answer" in {

          val checkYourAnswersHelperSingle = new CheckYourAnswersHelper(userAnswersSingle)(messages)


          val expected = createSummaryListRow(
            "typeOfParticipant.heading",
            Text("Art gallery"),
            "change-type"
          )

          checkYourAnswersHelperSingle.typeOfParticipant.value mustBe expected
        }
      }
    }

    "have soldOverThreshold method which" when {
      "called" must {
        "return SummaryListRow" in {

          val expected = createSummaryListRow(
            "soldOverThreshold.heading",
            Text("Yes"),
            "change-sale"
          )

          checkYourAnswersHelper.soldOverThreshold.value mustBe expected
        }
      }
    }

    "have dateTransactionOverThreshold method which" when {
      "called" must {
        "return SummaryListRow" in {

          val expected = createSummaryListRow(
            "dateTransactionOverThreshold.heading",
            HtmlContent("1 January 2010"),
            "change-first-sale"
          )

          checkYourAnswersHelper.dateTransactionOverThreshold.value mustBe expected
        }
      }
    }

    "have identifyLinkedTransactions method which" when {
      "called" must {
        "return SummaryListRow" in {

          val expected = createSummaryListRow(
            "identifyLinkedTransactions.heading",
            Text("No"),
            "change-identify-linked-payments"
          )

          checkYourAnswersHelper.identifyLinkedTransactions.value mustBe expected
        }
      }
    }

    "have percentageExpectedTurnover method which" when {
      "called" must {
        "return SummaryListRow" in {

          val expected = createSummaryListRow(
            "percentageExpectedTurnover.heading",
            Text("0% to 20%"),
            "change-turnover-from-art-sales"
          )

          checkYourAnswersHelper.percentageExpectedTurnover.value mustBe expected
        }
      }
    }
  }

  private def createSummaryListRow(heading: String, answer: Content, changeUrl: String) =
    SummaryListRow(
      key = Key(content = Text(messages(heading))),
      value = Value(content = answer),
      actions = Some(Actions(
        items = List(ActionItem(
          href = s"/anti-money-laundering/art-market-participant/$changeUrl",
          content = Text(messages("site.edit")))
        )
      ))
    )
}
