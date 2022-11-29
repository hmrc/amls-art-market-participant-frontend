/*
 * Copyright 2022 HM Revenue & Customs
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

import forms.behaviours.StringFieldBehaviours
import forms.mappings.Constraints
import play.api.data.FormError

class TypeOfParticipantDetailFormProviderSpec extends StringFieldBehaviours with Constraints {

  val requiredKey = "typeOfParticipantDetail.error.required"
  val punctuationKey = "typeOfParticipantDetail.error.punctuation"
  val lengthKey = "typeOfParticipantDetail.error.length"
  val maxLength = 255

  val form = new TypeOfParticipantDetailFormProvider()()

  ".value" must {

    val fieldName = "value"

    val underMaxLength = """!@£$%^&*()?"';|Lorem ipsum dolor sit amet"""

    val overMaxLength = """Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor.
      Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.
      Donec quam felis, ultricies nec, pellentesque eu, pretium quis,."""

    val invalidString = """<p>Some HTML content</p>"""

    "bind valid data" in {
      val result = form.bind(Map(fieldName -> underMaxLength)).apply(fieldName)
      result.value.value shouldBe underMaxLength
    }

    s"not bind strings longer than $maxLength characters" in {
      val result = form.bind(Map(fieldName -> overMaxLength)).apply(fieldName)
      result.errors shouldEqual Seq(FormError( fieldName, Seq(lengthKey), Seq(maxLength)))
    }

    s"not bind strings with invalid characters" in {
      val result = form.bind(Map(fieldName -> invalidString)).apply(fieldName)
      result.errors shouldEqual Seq(FormError( fieldName, Seq(punctuationKey), Seq(basicPunctuationRegex)))
    }

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
