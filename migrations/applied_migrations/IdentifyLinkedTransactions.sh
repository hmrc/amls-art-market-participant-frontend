#!/bin/bash

echo ""
echo "Applying migration IdentifyLinkedTransactions"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /identifyLinkedTransactions                        controllers.IdentifyLinkedTransactionsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /identifyLinkedTransactions                        controllers.IdentifyLinkedTransactionsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeIdentifyLinkedTransactions                  controllers.IdentifyLinkedTransactionsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeIdentifyLinkedTransactions                  controllers.IdentifyLinkedTransactionsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "identifyLinkedTransactions.title = identifyLinkedTransactions" >> ../conf/messages.en
echo "identifyLinkedTransactions.heading = identifyLinkedTransactions" >> ../conf/messages.en
echo "identifyLinkedTransactions.checkYourAnswersLabel = identifyLinkedTransactions" >> ../conf/messages.en
echo "identifyLinkedTransactions.error.required = Select yes if identifyLinkedTransactions" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIdentifyLinkedTransactionsUserAnswersEntry: Arbitrary[(IdentifyLinkedTransactionsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[IdentifyLinkedTransactionsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIdentifyLinkedTransactionsPage: Arbitrary[IdentifyLinkedTransactionsPage.type] =";\
    print "    Arbitrary(IdentifyLinkedTransactionsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(IdentifyLinkedTransactionsPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def identifyLinkedTransactions: Option[AnswerRow] = userAnswers.get(IdentifyLinkedTransactionsPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"identifyLinkedTransactions.checkYourAnswersLabel\")),";\
     print "        yesOrNo(x),";\
     print "        routes.IdentifyLinkedTransactionsController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration IdentifyLinkedTransactions completed"
