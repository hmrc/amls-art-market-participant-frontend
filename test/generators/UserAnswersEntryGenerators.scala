package generators

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

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

  implicit lazy val arbitraryBoughtOrSoldOverThresholdUserAnswersEntry: Arbitrary[(BoughtOrSoldOverThresholdPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[BoughtOrSoldOverThresholdPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }
}
