#!/bin/bash

echo ""
echo "Applying migration DateTransactionOverThreshold"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /dateTransactionOverThreshold                  controllers.DateTransactionOverThresholdController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /dateTransactionOverThreshold                  controllers.DateTransactionOverThresholdController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDateTransactionOverThreshold                        controllers.DateTransactionOverThresholdController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDateTransactionOverThreshold                        controllers.DateTransactionOverThresholdController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "dateTransactionOverThreshold.title = DateTransactionOverThreshold" >> ../conf/messages.en
echo "dateTransactionOverThreshold.heading = DateTransactionOverThreshold" >> ../conf/messages.en
echo "dateTransactionOverThreshold.checkYourAnswersLabel = DateTransactionOverThreshold" >> ../conf/messages.en
echo "dateTransactionOverThreshold.error.required.all = Enter the dateTransactionOverThreshold" >> ../conf/messages.en
echo "dateTransactionOverThreshold.error.required.two = The dateTransactionOverThreshold" must include {0} and {1} >> ../conf/messages.en
echo "dateTransactionOverThreshold.error.required = The dateTransactionOverThreshold must include {0}" >> ../conf/messages.en
echo "dateTransactionOverThreshold.error.invalid = Enter a real DateTransactionOverThreshold" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDateTransactionOverThresholdUserAnswersEntry: Arbitrary[(DateTransactionOverThresholdPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DateTransactionOverThresholdPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDateTransactionOverThresholdPage: Arbitrary[DateTransactionOverThresholdPage.type] =";\
    print "    Arbitrary(DateTransactionOverThresholdPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DateTransactionOverThresholdPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def dateTransactionOverThreshold: Option[AnswerRow] = userAnswers.get(DateTransactionOverThresholdPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"dateTransactionOverThreshold.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(x.format(dateFormatter)),";\
     print "        routes.DateTransactionOverThresholdController.onPageLoad(CheckMode).url";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration DateTransactionOverThreshold completed"
