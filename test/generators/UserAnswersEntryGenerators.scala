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

package generators

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryTypeOfParticipantDetailUserAnswersEntry: Arbitrary[(TypeOfParticipantDetailPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[TypeOfParticipantDetailPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryTypeOfParticipantUserAnswersEntry: Arbitrary[(TypeOfParticipantPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[TypeOfParticipantPage.type]
        value <- arbitrary[TypeOfParticipant].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPercentageExpectedTurnoverUserAnswersEntry: Arbitrary[(PercentageExpectedTurnoverPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PercentageExpectedTurnoverPage.type]
        value <- arbitrary[PercentageExpectedTurnover].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryIdentifyLinkedTransactionsUserAnswersEntry: Arbitrary[(IdentifyLinkedTransactionsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[IdentifyLinkedTransactionsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDateTransactionOverThresholdUserAnswersEntry: Arbitrary[(DateTransactionOverThresholdPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DateTransactionOverThresholdPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryBoughtOrSoldOverThresholdUserAnswersEntry: Arbitrary[(SoldOverThresholdPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[SoldOverThresholdPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }
}
