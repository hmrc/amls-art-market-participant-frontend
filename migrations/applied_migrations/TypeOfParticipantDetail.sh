#!/bin/bash

echo ""
echo "Applying migration TypeOfParticipantDetail"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /typeOfParticipantDetail                        controllers.TypeOfParticipantDetailController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /typeOfParticipantDetail                        controllers.TypeOfParticipantDetailController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTypeOfParticipantDetail                  controllers.TypeOfParticipantDetailController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTypeOfParticipantDetail                  controllers.TypeOfParticipantDetailController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "typeOfParticipantDetail.title = typeOfParticipantDetail" >> ../conf/messages.en
echo "typeOfParticipantDetail.heading = typeOfParticipantDetail" >> ../conf/messages.en
echo "typeOfParticipantDetail.checkYourAnswersLabel = typeOfParticipantDetail" >> ../conf/messages.en
echo "typeOfParticipantDetail.error.required = Enter typeOfParticipantDetail" >> ../conf/messages.en
echo "typeOfParticipantDetail.error.length = TypeOfParticipantDetail must be 256 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTypeOfParticipantDetailUserAnswersEntry: Arbitrary[(TypeOfParticipantDetailPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TypeOfParticipantDetailPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTypeOfParticipantDetailPage: Arbitrary[TypeOfParticipantDetailPage.type] =";\
    print "    Arbitrary(TypeOfParticipantDetailPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TypeOfParticipantDetailPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def typeOfParticipantDetail: Option[AnswerRow] = userAnswers.get(TypeOfParticipantDetailPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"typeOfParticipantDetail.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(x),";\
     print "        routes.TypeOfParticipantDetailController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration TypeOfParticipantDetail completed"
