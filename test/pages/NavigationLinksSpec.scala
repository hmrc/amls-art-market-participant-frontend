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
import forms._
import models.{NormalMode, PercentageExpectedTurnover, TypeOfParticipant}
import org.jsoup.nodes.Document
import play.api.data.Form
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList
import views.behaviours.ViewBehaviours
import views.html._

class NavigationLinksSpec extends ViewBehaviours {

  val signOutMsg: String                   = "Sign out"
  val returnToAboutYourBusinessMsg: String = "Return to about your business"
  val acceptAndCompleteMsg: String         = "Accept and complete section"

  "WhatYouNeed view" must {
    val view = viewFor[WhatYouNeedView](Some(emptyUserAnswers))

    val applyView = view.apply()(fakeRequest, messages)

    assertContainNavigationLinks(asDocument(applyView))
  }

  "BoughtOrSoldOverThreshold page" must {

    val form = new TypeOfParticipantFormProvider()()

    val view = viewFor[TypeOfParticipantView](Some(emptyUserAnswers))

    def applyView(form: Form[Seq[TypeOfParticipant]]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    assertContainNavigationLinks(asDocument(applyView(form)))
  }

  "DateTransactionOverThreshold page" must {

    val form = new DateTransactionOverThresholdFormProvider()()

    val view = viewFor[DateTransactionOverThresholdView](Some(emptyUserAnswers))

    def applyView(form: Form[LocalDate]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    assertContainNavigationLinks(asDocument(applyView(form)))
  }

  "IdentifyLinkedTransactions page" must {

    val form = new IdentifyLinkedTransactionsFormProvider()()

    val view = viewFor[IdentifyLinkedTransactionsView](Some(emptyUserAnswers))

    def applyView(form: Form[Boolean]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    assertContainNavigationLinks(asDocument(applyView(form)))
  }

  "PercentageExpectedTurnover page" must {

    val form = new PercentageExpectedTurnoverFormProvider()()

    val view = viewFor[PercentageExpectedTurnoverView](Some(emptyUserAnswers))

    def applyView(form: Form[PercentageExpectedTurnover]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    assertContainNavigationLinks(asDocument(applyView(form)))
  }

  "TypeOfParticipantDetail page" must {

    val form = new TypeOfParticipantDetailFormProvider()()

    val view = viewFor[TypeOfParticipantDetailView](Some(emptyUserAnswers))

    def applyView(form: Form[String]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    assertContainNavigationLinks(asDocument(applyView(form)))
  }

  "TypeOfParticipant page" must {

    val form = new TypeOfParticipantFormProvider()()

    val view = viewFor[TypeOfParticipantView](Some(emptyUserAnswers))

    def applyView(form: Form[Seq[TypeOfParticipant]]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    assertContainNavigationLinks(asDocument(applyView(form)))
  }

  "CheckYourAnswers page" must {
    val view = viewFor[CheckYourAnswersView](Some(emptyUserAnswers))

    val applyView = view.apply(SummaryList(), NormalMode)(fakeRequest, messages)

    val document = asDocument(applyView)

    "display Sign out link" in {
      assertContainsMessages(document, signOutMsg)
    }

    "display AcceptAndComplete button" in {
      assertElementContainsMessage(document, "button", messages(acceptAndCompleteMsg))
    }
  }

  def assertContainNavigationLinks(document: Document): Unit = {
    "display Sign out link" in {
      assertContainsMessages(document, signOutMsg)
    }

    "display Back to About your Business link" in {
      assertContainsMessages(document, messages(returnToAboutYourBusinessMsg))
    }
  }
}
