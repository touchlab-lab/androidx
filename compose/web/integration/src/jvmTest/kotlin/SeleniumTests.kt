/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.Ignore
import org.openqa.selenium.By
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.test.Test
import kotlin.test.assertEquals

@Ignore("Ignored temporarily, before we install chromedriver on CI")
class SeleniumTests : BaseSeleniumTests() {

    @Test
    fun `text contains Hello World`() {
        openTestPage("helloWorldText")
        assertEquals(
            expected = "Hello World!",
            actual = driver.findElementByTagName("div").text
        )
    }

    @Test
    fun `text area input gets printed`() {
        openTestPage("textAreaInputGetsPrinted")

        val input = driver.findElement(By.id("input"))
        input.sendKeys("Hello")

        val text = driver.findElement(By.id("result"))
        assertEquals(expected = "Hello", actual = text.text)
    }

    @Test
    fun `multiple clicks on button update the counter after every click`() {
        openTestPage("buttonClicksUpdateCounterValue")

        val wait = WebDriverWait(driver, 1)
        fun waitTextToBe(t: String) = wait.until(ExpectedConditions.textToBe(By.id("txt"), t))

        val button = driver.findElement(By.id("btn"))

        waitTextToBe("0")
        repeat(3) {
            button.click()
            waitTextToBe((it + 1).toString())
        }
    }

    @Test
    fun `hovering the box updates the text`() {
        openTestPage("hoverOnDivUpdatesText")

        val wait = WebDriverWait(driver, 1)
        fun waitTextToBe(t: String) = wait.until(ExpectedConditions.textToBe(By.id("txt"), t))

        val box = driver.findElement(By.id("box"))
        waitTextToBe("not hovered")

        val actions = Actions(driver)

        actions.moveToElement(box).perform()
        waitTextToBe("hovered")

        actions.moveByOffset(300, 0).perform()
        waitTextToBe("not hovered")
    }
}