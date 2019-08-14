package generators

import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

  implicit lazy val arbitraryTypeOfParticipantPage: Arbitrary[TypeOfParticipantPage.type] =
    Arbitrary(TypeOfParticipantPage)

  implicit lazy val arbitraryPercentageExpectedTurnoverPage: Arbitrary[PercentageExpectedTurnoverPage.type] =
    Arbitrary(PercentageExpectedTurnoverPage)

  implicit lazy val arbitraryIdentifyLinkedTransactionsPage: Arbitrary[IdentifyLinkedTransactionsPage.type] =
    Arbitrary(IdentifyLinkedTransactionsPage)

  implicit lazy val arbitraryDateTransactionOverThresholdPage: Arbitrary[DateTransactionOverThresholdPage.type] =
    Arbitrary(DateTransactionOverThresholdPage)

  implicit lazy val arbitraryBoughtOrSoldOverThresholdPage: Arbitrary[BoughtOrSoldOverThresholdPage.type] =
    Arbitrary(BoughtOrSoldOverThresholdPage)
}
