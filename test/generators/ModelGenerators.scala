package generators

import models._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

trait ModelGenerators {

  implicit lazy val arbitraryTypeOfParticipant: Arbitrary[TypeOfParticipant] =
    Arbitrary {
      Gen.oneOf(TypeOfParticipant.values.toSeq)
    }

  implicit lazy val arbitraryPercentageExpectedTurnover: Arbitrary[PercentageExpectedTurnover] =
    Arbitrary {
      Gen.oneOf(PercentageExpectedTurnover.values.toSeq)
    }
}
