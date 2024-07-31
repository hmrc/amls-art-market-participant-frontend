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

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class DateTransactionOverThresholdFormProvider @Inject() extends Mappings {

  def apply(): Form[LocalDate] =
    Form(
      "value" -> localDate(
        oneInvalidKey = "error.date.hvd.invalid.one",
        multipleInvalidKey = "error.date.hvd.invalid.multiple",
        oneRequiredKey = "error.date.hvd.one",
        twoRequiredKey = "error.date.hvd.two",
        allRequiredKey = "error.date.hvd.all",
        realDateKey = "error.date.hvd.real"
      ).verifying(
        minDate(DateTransactionOverThresholdFormProvider.ampStartDate, "dateTransactionOverThreshold.error.startdate"),
        maxDate(LocalDate.now(ZoneOffset.UTC), "dateTransactionOverThreshold.error.future"))
    )
}

object DateTransactionOverThresholdFormProvider {

  val ampStartDate = LocalDate.of(2020, 1, 10)
}