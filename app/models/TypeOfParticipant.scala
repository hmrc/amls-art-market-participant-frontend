/*
 * Copyright 2023 HM Revenue & Customs
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
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem

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

  def options(implicit messages: Messages): Seq[CheckboxItem] = values.zipWithIndex.map {
    case (participantValue, index) =>
      CheckboxItem(
        content = Text(messages(s"typeOfParticipant.${participantValue.toString}")),
        value = participantValue.toString,
        id = Some(s"value_$index"),
        name = Some(s"value[$index]")
      )
  }

  implicit val enumerable: Enumerable[TypeOfParticipant] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
