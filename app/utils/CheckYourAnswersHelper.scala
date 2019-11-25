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

import java.time.format.DateTimeFormatter

import controllers.routes
import models.{CheckMode, TypeOfParticipant, UserAnswers}
import pages._
import play.api.i18n.Messages
import play.twirl.api.{Html, HtmlFormat}
import viewmodels.AnswerRow
import CheckYourAnswersHelper._

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  private def typeOfParticipantHtml(x: Seq[TypeOfParticipant]): Html = {
    val detailAnswer = userAnswers.get(TypeOfParticipantDetailPage).getOrElse("")

    val ifBullet = if (x.size == 1) "<ul class=\"list\">" else "<ul class=\"list list-bullet\">"

    val values = x.map(value => value.toString).toList.sorted

    Html(Html(ifBullet + values.map(value => if(value == "somethingElse") {
     if (!detailAnswer.isEmpty) {
      Html("<li>" + detailAnswer + "</li>")
     } else {
       detailAnswer
     }
    } else{
      Html("<li>" + messages(s"typeOfParticipant.$value")).toString
    }).mkString("</li>")) + "</ul>")
  }

  def typeOfParticipant: Option[AnswerRow] = userAnswers.get(TypeOfParticipantPage) map {
    x: Seq[TypeOfParticipant] =>
      AnswerRow(
        HtmlFormat.escape(messages("typeOfParticipant.checkYourAnswersLabel")),
        typeOfParticipantHtml(x),
        routes.TypeOfParticipantController.onPageLoad(CheckMode).url
      )
  }

  def percentageExpectedTurnover: Option[AnswerRow] = userAnswers.get(PercentageExpectedTurnoverPage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("percentageExpectedTurnover.checkYourAnswersLabel")),
        HtmlFormat.escape(messages(s"percentageExpectedTurnover.$x")),
        routes.PercentageExpectedTurnoverController.onPageLoad(CheckMode).url
      )
  }

  def identifyLinkedTransactions: Option[AnswerRow] = userAnswers.get(IdentifyLinkedTransactionsPage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("identifyLinkedTransactions.checkYourAnswersLabel")),
        yesOrNo(x),
        routes.IdentifyLinkedTransactionsController.onPageLoad(CheckMode).url
      )
  }

  def dateTransactionOverThreshold: Option[AnswerRow] = userAnswers.get(DateTransactionOverThresholdPage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("dateTransactionOverThreshold.checkYourAnswersLabel")),
        HtmlFormat.escape(x.format(dateFormatter)),
        routes.DateTransactionOverThresholdController.onPageLoad(CheckMode).url
      )
  }

  def soldOverThreshold: Option[AnswerRow] = userAnswers.get(SoldOverThresholdPage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("soldOverThreshold.checkYourAnswersLabel")),
        yesOrNo(x),
        routes.SoldOverThresholdController.onPageLoad(CheckMode).url
      )
  }

  private def yesOrNo(answer: Boolean)(implicit messages: Messages): Html =
    if (answer) {
      HtmlFormat.escape(messages("site.yes"))
    } else {
      HtmlFormat.escape(messages("site.no"))
    }
}

object CheckYourAnswersHelper {

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
}
