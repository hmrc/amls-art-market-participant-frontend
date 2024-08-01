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

package forms

import java.time.{LocalDate, ZoneOffset}
import forms.behaviours.DateBehaviours
import org.scalatest.matchers.must.Matchers.contain
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import play.api.data.FormError

class DateTransactionOverThresholdFormProviderSpec extends DateBehaviours {

  val form = new DateTransactionOverThresholdFormProvider()()
  val minDateError = "error.date.fs.startdate"
  val maxDateError = "error.date.fs.future"

  ".value" must {

    behave like mandatoryDateField(form, "value", "error.date.fs.all")

    if(LocalDate.now(ZoneOffset.UTC).isAfter(DateTransactionOverThresholdFormProvider.ampStartDate)) {

      val validData = datesBetween(
        min = DateTransactionOverThresholdFormProvider.ampStartDate,
        max = LocalDate.now(ZoneOffset.UTC)
      )

      behave like dateField(form, "value", validData)

      behave like dateFieldWithMin(form, "value", DateTransactionOverThresholdFormProvider.ampStartDate, FormError("value", minDateError))

      behave like dateFieldWithMax(form, "value", LocalDate.now(ZoneOffset.UTC), FormError("value", maxDateError))

      "fail to bind a date with a missing value" in {

        val formError = FormError("value", "dateTransactionOverThreshold.error.required", List("month"))

        val validDate = DateTransactionOverThresholdFormProvider.ampStartDate

        val data = Map(
          "value.day"   -> validDate.getDayOfMonth.toString,
          "value.month" -> "",
          "value.year"  -> validDate.getYear.toString
        )

        val result = form.bind(data)
        result.errors should contain only formError
      }

      "fail to bind a date with two missing values" in {

        val formError = FormError("value", "dateTransactionOverThreshold.error.required.two", List("month", "year"))

        val validDate = DateTransactionOverThresholdFormProvider.ampStartDate

        val data = Map(
          "value.day"   -> validDate.getDayOfMonth.toString,
          "value.month" -> "",
          "value.year"  -> ""
        )

        val result = form.bind(data)
        result.errors should contain only formError
      }
    }
  }
}