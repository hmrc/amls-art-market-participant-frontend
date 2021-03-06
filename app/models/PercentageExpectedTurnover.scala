/*
 * Copyright 2021 HM Revenue & Customs
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

import viewmodels.RadioOption

sealed trait PercentageExpectedTurnover

object PercentageExpectedTurnover extends Enumerable.Implicits {

  case object ZeroToTwenty extends WithName("zeroToTwenty") with PercentageExpectedTurnover
  case object TwentyOneToForty extends WithName("twentyOneToForty") with PercentageExpectedTurnover
  case object FortyOneToSixty extends WithName("fortyOneToSixty") with PercentageExpectedTurnover
  case object SixtyOneToEighty extends WithName("sixtyOneToEighty") with PercentageExpectedTurnover
  case object EightyOneToOneHundred extends WithName("eightyOneToOneHundred") with PercentageExpectedTurnover

  val values: Seq[PercentageExpectedTurnover] = Seq(
    ZeroToTwenty, TwentyOneToForty, FortyOneToSixty, SixtyOneToEighty, EightyOneToOneHundred
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("percentageExpectedTurnover", value.toString)
  }

  implicit val enumerable: Enumerable[PercentageExpectedTurnover] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
