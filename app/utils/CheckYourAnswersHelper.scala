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

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages._
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.{Actions, Key, Value}
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.{Content, HtmlContent, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{ActionItem, SummaryListRow}
import utils.CheckYourAnswersHelper._

import java.time.format.DateTimeFormatter

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  def getAllRows: Seq[SummaryListRow] = Seq(
    typeOfParticipant,
    soldOverThreshold,
    dateTransactionOverThreshold,
    identifyLinkedTransactions,
    percentageExpectedTurnover
  ).flatten

  def typeOfParticipant: Option[SummaryListRow] = {

    val participantValues: Option[Content] = userAnswers.get(TypeOfParticipantPage) match {
      case Some(value) if value.size > 1 =>

        Some(
          HtmlContent(
            "<ul class=\"govuk-list govuk-list--bullet\">" +
              value.map { x =>
                s"<li>${messages(s"typeOfParticipant.${x.toString}")}</li>"
              }.mkString +
            "</ul>"
          )
        )
      case Some(value) => Some(Text(messages(s"typeOfParticipant.${value.head.toString}")))
      case None => None
    }

    participantValues map { values =>
      SummaryListRow(
        key = Key(Text(messages("typeOfParticipant.checkYourAnswersLabel"))),
        value = Value(values),
        actions = Some(Actions(items = Seq(
          ActionItem(
            href = routes.TypeOfParticipantController.onPageLoad(CheckMode).url,
            content = Text(messages("site.edit"))
          )
        )))
      )
    }
  }

  def percentageExpectedTurnover: Option[SummaryListRow] = userAnswers.get(PercentageExpectedTurnoverPage) map {
    x =>
      SummaryListRow(
        key = Key(Text(messages("percentageExpectedTurnover.checkYourAnswersLabel"))),
        value = Value(Text(messages(s"percentageExpectedTurnover.$x"))),
        actions = Some(Actions(items = Seq(
          ActionItem(
            href = routes.PercentageExpectedTurnoverController.onPageLoad(CheckMode).url,
            content = Text(messages("site.edit"))
          )
        )))
      )
  }

  def identifyLinkedTransactions: Option[SummaryListRow] = userAnswers.get(IdentifyLinkedTransactionsPage) map {
    x =>
      SummaryListRow(
        key = Key(Text(messages("identifyLinkedTransactions.checkYourAnswersLabel"))),
        value = Value(yesOrNo(x)),
        actions = withChangeLink(routes.IdentifyLinkedTransactionsController.onPageLoad(CheckMode).url)
      )
  }

  def dateTransactionOverThreshold: Option[SummaryListRow] = userAnswers.get(DateTransactionOverThresholdPage) map {
    x =>
      SummaryListRow(
        key = Key(Text(messages("dateTransactionOverThreshold.checkYourAnswersLabel"))),
        value = Value(HtmlContent(x.format(dateFormatter))),
        actions = withChangeLink(routes.DateTransactionOverThresholdController.onPageLoad(CheckMode).url)
      )
  }

  def soldOverThreshold: Option[SummaryListRow] = userAnswers.get(SoldOverThresholdPage) map {
    x =>
      SummaryListRow(
        key = Key(Text(messages("soldOverThreshold.checkYourAnswersLabel"))),
        value = Value(yesOrNo(x)),
        actions = withChangeLink(routes.SoldOverThresholdController.onPageLoad(CheckMode).url)
      )
  }

  private def yesOrNo(answer: Boolean)(implicit messages: Messages): Content =
    if (answer) {
      Text(messages("site.yes"))
    } else {
      Text(messages("site.no"))
    }

  private def withChangeLink(url: String) =
    Some(
      Actions(
        items =
          Seq(
            ActionItem(
              href = url,
              content = Text(messages("site.edit"))
            )
          )
      )
    )
}

object CheckYourAnswersHelper {

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
}
