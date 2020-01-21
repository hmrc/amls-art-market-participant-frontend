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

package views

import forms.TypeOfParticipantFormProvider
import models.{NormalMode, TypeOfParticipant}
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.CheckboxViewBehaviours
import views.html.TypeOfParticipantView

class TypeOfParticipantViewSpec extends CheckboxViewBehaviours[TypeOfParticipant] {

  val messageKeyPrefix = "typeOfParticipant"

  val form = new TypeOfParticipantFormProvider()()

  "TypeOfParticipantView" must {

    val view = viewFor[TypeOfParticipantView](Some(emptyUserAnswers))

    def applyView(form: Form[Seq[TypeOfParticipant]]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like checkboxPage(form, applyView, messageKeyPrefix, TypeOfParticipant.options)

    "include the correct content" in {
      val document = asDocument(applyView(form))

      assertTitleEqualsMessage(document, "title", "What type of art market participant are you?")
      assertPageTitleEqualsMessage(document, "What type of art market participant are you?")
    }
  }
}
