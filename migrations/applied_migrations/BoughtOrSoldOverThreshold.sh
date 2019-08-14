#!/bin/bash

echo ""
echo "Applying migration BoughtOrSoldOverThreshold"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /boughtOrSoldOverThreshold                        controllers.BoughtOrSoldOverThresholdController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /boughtOrSoldOverThreshold                        controllers.BoughtOrSoldOverThresholdController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeBoughtOrSoldOverThreshold                  controllers.BoughtOrSoldOverThresholdController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeBoughtOrSoldOverThreshold                  controllers.BoughtOrSoldOverThresholdController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "boughtOrSoldOverThreshold.title = boughtOrSoldOverThreshold" >> ../conf/messages.en
echo "boughtOrSoldOverThreshold.heading = boughtOrSoldOverThreshold" >> ../conf/messages.en
echo "boughtOrSoldOverThreshold.checkYourAnswersLabel = boughtOrSoldOverThreshold" >> ../conf/messages.en
echo "boughtOrSoldOverThreshold.error.required = Select yes if boughtOrSoldOverThreshold" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBoughtOrSoldOverThresholdUserAnswersEntry: Arbitrary[(BoughtOrSoldOverThresholdPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[BoughtOrSoldOverThresholdPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBoughtOrSoldOverThresholdPage: Arbitrary[BoughtOrSoldOverThresholdPage.type] =";\
    print "    Arbitrary(BoughtOrSoldOverThresholdPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(BoughtOrSoldOverThresholdPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def boughtOrSoldOverThreshold: Option[AnswerRow] = userAnswers.get(BoughtOrSoldOverThresholdPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"boughtOrSoldOverThreshold.checkYourAnswersLabel\")),";\
     print "        yesOrNo(x),";\
     print "        routes.BoughtOrSoldOverThresholdController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration BoughtOrSoldOverThreshold completed"
