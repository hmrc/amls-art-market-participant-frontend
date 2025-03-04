/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package views

import views.behaviours.ViewBehaviours
import views.html.WhatYouNeedView

class WhatYouNeedViewSpec extends ViewBehaviours {

  "WhatYouNeed view" must {

    val view = viewFor[WhatYouNeedView](Some(emptyUserAnswers))

    val applyView = view.apply()(fakeRequest, messages)

    behave like normalPage(applyView, "whatYouNeed")

    behave like pageWithBackLink(applyView)

    "include the correct content" in {
      val document = asDocument(applyView)

      assertTitleEqualsMessage(document, "title", "What you need")
      assertPageTitleEqualsMessage(document, "What you need")

      assertContainsText(document, "Automatic saving")
      assertContainsText(
        document,
        "Information is saved automatically. If you sign out, you’ll have 28 days to complete your application."
      )

      assertContainsText(document, "In the section you’ll need to tell us:")
      assertContainsText(document, "the role you carry out in the art market")
      assertContainsText(
        document,
        "the date of your first sale of art for €10,000 or more on or after 10 January 2020, if you have made any"
      )
      assertContainsText(document, "if you can identify when a sale is made up of multiple payments")
      assertContainsText(
        document,
        "the percentage of your turnover you estimate will come from sales of art for €10,000 or more in the next 12 months"
      )
    }
  }
}
