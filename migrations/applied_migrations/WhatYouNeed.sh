#!/bin/bash

echo ""
echo "Applying migration WhatYouNeed"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /whatYouNeed                       controllers.WhatYouNeedController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatYouNeed.title = whatYouNeed" >> ../conf/messages.en
echo "whatYouNeed.heading = whatYouNeed" >> ../conf/messages.en

echo "Migration WhatYouNeed completed"
