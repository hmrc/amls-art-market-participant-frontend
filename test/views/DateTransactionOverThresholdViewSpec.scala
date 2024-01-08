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

package views

import java.time.LocalDate

import forms.DateTransactionOverThresholdFormProvider
import models.{NormalMode, UserAnswers}
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.DateTransactionOverThresholdView

class DateTransactionOverThresholdViewSpec extends QuestionViewBehaviours[LocalDate] {

  val messageKeyPrefix = "dateTransactionOverThreshold"

  val form = new DateTransactionOverThresholdFormProvider()()

  "DateTransactionOverThresholdView view" must {

    val application = applicationBuilder(userAnswers = Some(UserAnswers())).build()

    val view = application.injector.instanceOf[DateTransactionOverThresholdView]

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    "include the correct content" in {
      val document = asDocument(applyView(form))

      assertTitleEqualsMessage(document, "title", "When was the first sale of art for €10,000 or more on or after 10 January 2020?")
      assertPageTitleEqualsMessage(document, "When was the first sale of art for €10,000 or more on or after 10 January 2020?")
    }
  }
}
