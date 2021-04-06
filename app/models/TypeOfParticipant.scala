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

sealed trait TypeOfParticipant

object TypeOfParticipant extends Enumerable.Implicits {

  case object ArtAuctioneer extends WithName("artAuctioneer") with TypeOfParticipant
  case object ArtGalleryOwner extends WithName("artGalleryOwner") with TypeOfParticipant
  case object ArtAgent extends WithName("artAgent") with TypeOfParticipant
  case object ArtDealer extends WithName("artDealer") with TypeOfParticipant
  case object SomethingElse extends WithName("somethingElse") with TypeOfParticipant

  val values: Seq[TypeOfParticipant] = Seq(
    ArtAuctioneer,
    ArtGalleryOwner,
    ArtAgent,
    ArtDealer,
    SomethingElse
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("typeOfParticipant", value.toString)
  }

  implicit val enumerable: Enumerable[TypeOfParticipant] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
