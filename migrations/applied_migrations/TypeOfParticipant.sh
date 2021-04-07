#!/bin/bash

echo ""
echo "Applying migration TypeOfParticipant"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /typeOfParticipant                        controllers.TypeOfParticipantController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /typeOfParticipant                        controllers.TypeOfParticipantController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTypeOfParticipant                  controllers.TypeOfParticipantController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTypeOfParticipant                  controllers.TypeOfParticipantController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "typeOfParticipant.title = What type of art market participant are you?" >> ../conf/messages.en
echo "typeOfParticipant.heading = What type of art market participant are you?" >> ../conf/messages.en
echo "typeOfParticipant.artGalleryOwner = Art gallery" >> ../conf/messages.en
echo "typeOfParticipant.artDealer = Art dealer" >> ../conf/messages.en
echo "typeOfParticipant.checkYourAnswersLabel = What type of art market participant are you?" >> ../conf/messages.en
echo "typeOfParticipant.error.required = Select typeOfParticipant" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTypeOfParticipantUserAnswersEntry: Arbitrary[(TypeOfParticipantPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TypeOfParticipantPage.type]";\
    print "        value <- arbitrary[TypeOfParticipant].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTypeOfParticipantPage: Arbitrary[TypeOfParticipantPage.type] =";\
    print "    Arbitrary(TypeOfParticipantPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTypeOfParticipant: Arbitrary[TypeOfParticipant] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(TypeOfParticipant.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TypeOfParticipantPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def typeOfParticipant: Option[AnswerRow] = userAnswers.get(TypeOfParticipantPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"typeOfParticipant.checkYourAnswersLabel\")),";\
     print "        Html(x.map(value => HtmlFormat.escape(messages(s\"typeOfParticipant.$value\")).toString).mkString(\",<br>\")),";\
     print "        routes.TypeOfParticipantController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration TypeOfParticipant completed"
