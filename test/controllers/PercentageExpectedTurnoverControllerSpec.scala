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

package controllers

import base.SpecBase
import forms.PercentageExpectedTurnoverFormProvider
import models.PercentageExpectedTurnover.ZeroToTwenty
import models.{NormalMode, PercentageExpectedTurnover, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.PercentageExpectedTurnoverPage
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.AMLSFrontEndSessionRepository
import views.html.PercentageExpectedTurnoverView

import scala.concurrent.Future

class PercentageExpectedTurnoverControllerSpec extends SpecBase with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  lazy val percentageExpectedTurnoverRoute = routes.PercentageExpectedTurnoverController.onPageLoad(NormalMode).url

  val formProvider = new PercentageExpectedTurnoverFormProvider()
  val form         = formProvider()

  "PercentageExpectedTurnover Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request = FakeRequest(GET, percentageExpectedTurnoverRoute)

      val result = route(application, request).value

      val view = application.injector.instanceOf[PercentageExpectedTurnoverView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, NormalMode)(request, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers =
        UserAnswers().set(PercentageExpectedTurnoverPage, PercentageExpectedTurnover.values.head).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request = FakeRequest(GET, percentageExpectedTurnoverRoute)

      val view = application.injector.instanceOf[PercentageExpectedTurnoverView]

      val result = route(application, request).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(PercentageExpectedTurnover.values.head), NormalMode)(request, messages).toString

      application.stop()
    }

    "redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[AMLSFrontEndSessionRepository]

      when(mockSessionRepository.set(any(), any())(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[AMLSFrontEndSessionRepository].toInstance(mockSessionRepository)
          )
          .build()

      val request =
        FakeRequest(POST, percentageExpectedTurnoverRoute)
          .withFormUrlEncodedBody(("value", ZeroToTwenty.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request =
        FakeRequest(POST, percentageExpectedTurnoverRoute)
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val view = application.injector.instanceOf[PercentageExpectedTurnoverView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, NormalMode)(request, messages).toString

      application.stop()
    }

    "raise an error for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, percentageExpectedTurnoverRoute)

      val exception = intercept[Exception] {
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
      }

      exception.getMessage must include("Required data not found")

      application.stop()
    }

    "raise an error for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, percentageExpectedTurnoverRoute)
          .withFormUrlEncodedBody(("value", PercentageExpectedTurnover.values.head.toString))

      val exception = intercept[Exception] {
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
      }

      exception.getMessage must include("Required data not found")

      application.stop()
    }
  }
}
