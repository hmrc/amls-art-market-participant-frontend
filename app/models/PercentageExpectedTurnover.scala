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

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait PercentageExpectedTurnover

object PercentageExpectedTurnover extends Enumerable.Implicits {

  case object ZeroToTwenty extends WithName("zeroToTwenty") with PercentageExpectedTurnover
  case object TwentyOneToForty extends WithName("twentyOneToForty") with PercentageExpectedTurnover
  case object FortyOneToSixty extends WithName("fortyOneToSixty") with PercentageExpectedTurnover
  case object SixtyOneToEighty extends WithName("sixtyOneToEighty") with PercentageExpectedTurnover
  case object EightyOneToOneHundred extends WithName("eightyOneToOneHundred") with PercentageExpectedTurnover

  val values: Seq[PercentageExpectedTurnover] = Seq(
    ZeroToTwenty,
    TwentyOneToForty,
    FortyOneToSixty,
    SixtyOneToEighty,
    EightyOneToOneHundred
  )

  def options(implicit messages: Messages): Seq[RadioItem] = values.zipWithIndex.map {
    case (percentageExpectedTurnover, index) =>
      RadioItem(
        content = Text(messages(s"percentageExpectedTurnover.${percentageExpectedTurnover.toString}")),
        id = Some(s"value_$index"),
        value = Some(percentageExpectedTurnover.toString)
      )
  }

  implicit val enumerable: Enumerable[PercentageExpectedTurnover] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
