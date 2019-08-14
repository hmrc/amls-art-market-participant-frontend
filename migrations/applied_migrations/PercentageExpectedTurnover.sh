#!/bin/bash

echo ""
echo "Applying migration PercentageExpectedTurnover"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /percentageExpectedTurnover                        controllers.PercentageExpectedTurnoverController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /percentageExpectedTurnover                        controllers.PercentageExpectedTurnoverController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePercentageExpectedTurnover                  controllers.PercentageExpectedTurnoverController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePercentageExpectedTurnover                  controllers.PercentageExpectedTurnoverController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "percentageExpectedTurnover.title = How much of your turnover do you expect to come from sales of art for �€10,000 or more?" >> ../conf/messages.en
echo "percentageExpectedTurnover.heading = How much of your turnover do you expect to come from sales of art for �€10,000 or more?" >> ../conf/messages.en
echo "percentageExpectedTurnover.zeroToTwenty = 0% to 20%" >> ../conf/messages.en
echo "percentageExpectedTurnover.twentyOneToForty = 21% to 40%" >> ../conf/messages.en
echo "percentageExpectedTurnover.checkYourAnswersLabel = How much of your turnover do you expect to come from sales of art for �€10,000 or more?" >> ../conf/messages.en
echo "percentageExpectedTurnover.error.required = Select percentageExpectedTurnover" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPercentageExpectedTurnoverUserAnswersEntry: Arbitrary[(PercentageExpectedTurnoverPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PercentageExpectedTurnoverPage.type]";\
    print "        value <- arbitrary[PercentageExpectedTurnover].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPercentageExpectedTurnoverPage: Arbitrary[PercentageExpectedTurnoverPage.type] =";\
    print "    Arbitrary(PercentageExpectedTurnoverPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPercentageExpectedTurnover: Arbitrary[PercentageExpectedTurnover] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(PercentageExpectedTurnover.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PercentageExpectedTurnoverPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def percentageExpectedTurnover: Option[AnswerRow] = userAnswers.get(PercentageExpectedTurnoverPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"percentageExpectedTurnover.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(messages(s\"percentageExpectedTurnover.$x\")),";\
     print "        routes.PercentageExpectedTurnoverController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration PercentageExpectedTurnover completed"
