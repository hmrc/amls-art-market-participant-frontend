/*
 * Copyright 2020 HM Revenue & Customs
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

import java.time.{LocalDate, ZoneOffset}

import base.SpecBase
import forms.DateTransactionOverThresholdFormProvider
import models.{NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import pages.DateTransactionOverThresholdPage
import play.api.inject.bind
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded, Call}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.{AMLSFrontEndSessionRepository}
import views.html.DateTransactionOverThresholdView

import scala.concurrent.Future

class DateTransactionOverThresholdControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new DateTransactionOverThresholdFormProvider()
  private def form = formProvider()

  def onwardRoute = Call("GET", "/foo")

  val validAnswer = DateTransactionOverThresholdFormProvider.ampStartDate.plusDays(1)

  lazy val dateTransactionOverThresholdRoute = routes.DateTransactionOverThresholdController.onPageLoad(NormalMode).url

  override val emptyUserAnswers = UserAnswers()

  def getRequest(): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, dateTransactionOverThresholdRoute)

  def postRequest(): FakeRequest[AnyContentAsFormUrlEncoded] =
    FakeRequest(POST, dateTransactionOverThresholdRoute)
      .withFormUrlEncodedBody(
        "value.day"   -> validAnswer.getDayOfMonth.toString,
        "value.month" -> validAnswer.getMonthValue.toString,
        "value.year"  -> validAnswer.getYear.toString
      )

  "DateTransactionOverThreshold Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val result = route(application, getRequest).value

      val view = application.injector.instanceOf[DateTransactionOverThresholdView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, NormalMode)(getRequest, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = UserAnswers().set(DateTransactionOverThresholdPage, validAnswer).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val view = application.injector.instanceOf[DateTransactionOverThresholdView]

      val result = route(application, getRequest).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(validAnswer), NormalMode)(getRequest, messages).toString

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

      val result = route(application, postRequest).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request =
        FakeRequest(POST, dateTransactionOverThresholdRoute)
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val view = application.injector.instanceOf[DateTransactionOverThresholdView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, NormalMode)(request, messages).toString

      application.stop()
    }

    "raise an error for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val exception = intercept[Exception]{
        val result = route(application, postRequest).value

        status(result) mustEqual SEE_OTHER
      }

      exception.getMessage must include("Required data not found")

      application.stop()
    }

    "raise an error for a POST if no existing data is found" in {
      val application = applicationBuilder(userAnswers = None).build()

      val exception = intercept[Exception]{
        val result = route(application, postRequest).value

        status(result) mustEqual SEE_OTHER
      }

      exception.getMessage must include("Required data not found")

      application.stop()
    }
  }
}
