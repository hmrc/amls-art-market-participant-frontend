package utils

import java.time.format.DateTimeFormatter

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages._
import play.api.i18n.Messages
import play.twirl.api.{Html, HtmlFormat}
import viewmodels.AnswerRow
import CheckYourAnswersHelper._

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  def typeOfParticipant: Option[AnswerRow] = userAnswers.get(TypeOfParticipantPage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("typeOfParticipant.checkYourAnswersLabel")),
        Html(x.map(value => HtmlFormat.escape(messages(s"typeOfParticipant.$value")).toString).mkString(",<br>")),
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

  def boughtOrSoldOverThreshold: Option[AnswerRow] = userAnswers.get(BoughtOrSoldOverThresholdPage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("boughtOrSoldOverThreshold.checkYourAnswersLabel")),
        yesOrNo(x),
        routes.BoughtOrSoldOverThresholdController.onPageLoad(CheckMode).url
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
