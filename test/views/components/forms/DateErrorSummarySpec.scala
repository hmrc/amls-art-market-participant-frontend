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

package views.components.forms

import forms.DateTransactionOverThresholdFormProvider
import org.jsoup.Jsoup
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.i18n.{Messages, MessagesApi}
import play.api.test.{FakeRequest, Injecting}
import views.html.components.DateErrorSummary

import java.time.LocalDate

class DateErrorSummarySpec extends PlaySpec with GuiceOneAppPerSuite with Matchers with Injecting  {

  implicit lazy val messagesApi: MessagesApi = app.injector.instanceOf(classOf[MessagesApi])
  implicit lazy val messages: Messages = messagesApi.preferred(FakeRequest())

  val component: DateErrorSummary = inject[DateErrorSummary]
  val exampleForm = new DateTransactionOverThresholdFormProvider()
  val errorForm: Form[LocalDate] =
    exampleForm().bind(Map("dateOfChange.day" -> "xx", "dateOfChange.month" -> "yy", "dateOfChange.year" -> "zz"))

  "The DateErrorSummary component" when {

    "there are errors in the form" should {

      "have a title" in {
        Jsoup.parse(component(errorForm, "dateOfChange").body).select("h2").text() mustBe "There is a problem"
      }

      "have an error message" in {
        Jsoup.parse(component(errorForm, "dateOfChange").body).select("a").text() mustBe
          "Date of the change must only contain the numbers 0-9"
      }

      "have an error link to the ID of the individual field that has the error" in {
        val monthErrorForm =
          exampleForm().bind(Map("dateOfChange.day" -> "1", "dateOfChange.month" -> "ff", "dateOfChange.year" -> "2000"))
        Jsoup.parse(component(monthErrorForm, "dateOfChange").body).select("a").attr("href") mustBe "#dateOfChange.month"
      }

      "have an error link to the ID of the first individual field that has the error when there are multiple" in {
        val dayMonthErrorForm =
          exampleForm().bind(Map("dateOfChange.day" -> "ff", "dateOfChange.month" -> "ff", "dateOfChange.year" -> "2000"))
        Jsoup.parse(component(dayMonthErrorForm, "dateOfChange").body).select("a").attr("href") mustBe "#dateOfChange.day"
      }

      "append '.day' to the error link when the date is real but there is a validation error in the form's business logic" in {
        val dateTooEarlyErrorForm =
          exampleForm().bind(Map("dateOfChange.day" -> "1", "dateOfChange.month" -> "1", "dateOfChange.year" -> "1800"))
        Jsoup.parse(component(dateTooEarlyErrorForm, "dateOfChange").body).select("a").attr("href") mustBe "#dateOfChange.day"
      }
    }

    "there are no errors in the form" should {

      "not render" in {
        Jsoup.parse(component(exampleForm(), "dateOfChange").body).select("div").size() mustBe 0
      }
    }
  }
}