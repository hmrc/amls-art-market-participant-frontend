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

package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class PercentageExpectedTurnoverSpec extends AnyWordSpec with ScalaCheckPropertyChecks with OptionValues {

  "PercentageExpectedTurnover" should {

    "deserialise valid values" in {

      val gen = Gen.oneOf(PercentageExpectedTurnover.values.toSeq)

      forAll(gen) {
        percentageExpectedTurnover =>

          JsString(percentageExpectedTurnover.toString).validate[PercentageExpectedTurnover].asOpt.value mustEqual percentageExpectedTurnover
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!PercentageExpectedTurnover.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[PercentageExpectedTurnover] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(PercentageExpectedTurnover.values.toSeq)

      forAll(gen) {
        percentageExpectedTurnover =>

          Json.toJson(percentageExpectedTurnover) mustEqual JsString(percentageExpectedTurnover.toString)
      }
    }
  }
}
