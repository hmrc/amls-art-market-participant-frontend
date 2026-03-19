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

package models

import org.scalatest.matchers.must.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json._

class RichJsObjectSpec extends AnyWordSpec {

  "RichJsObject" must {

    "setObject" must {
      "set a value and return a JsObject" in {
        val obj    = Json.obj("foo" -> "bar")
        val result = obj.setObject(JsPath \ "baz", JsString("qux"))
        result mustEqual JsSuccess(Json.obj("foo" -> "bar", "baz" -> "qux"))
      }

      "return an error when path is empty" in {
        val obj    = Json.obj()
        val result = obj.setObject(JsPath, Json.obj())
        result mustBe a[JsError]
      }
    }

    "removeObject" must {
      "remove a value and return a JsObject" in {
        val obj    = Json.obj("foo" -> "bar", "baz" -> "qux")
        val result = obj.removeObject(JsPath \ "baz")
        result mustEqual JsSuccess(Json.obj("foo" -> "bar"))
      }

      "return an error when key does not exist" in {
        val obj    = Json.obj("foo" -> "bar")
        val result = obj.removeObject(JsPath \ "missing")
        result mustBe a[JsError]
      }
    }
  }
}
