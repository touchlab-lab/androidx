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
package org.jetbrains.compose.web.tests.integration

import org.openqa.selenium.By
import org.openqa.selenium.interactions.Actions
import kotlin.test.Test
import kotlin.test.assertEquals
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.junit.BeforeClass
import org.junit.AfterClass

class IntegrationTests {

    companion object : WithChromeDriver {
        override val driver: RemoteWebDriver = ChromeDriver(
            ChromeOptions().apply {
                setHeadless(true)
                addArguments("--no-sandbox")
            }
        )

        @BeforeClass
        @JvmStatic
        fun setup() {
            ServerLauncher.startServer(this)
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            ServerLauncher.stopServer(this)
        }
    }

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

        waitTextToBe(textId = "result", value = "Hello")
    }

    @Test
    fun `multiple clicks on button update the counter after every click`() {
        openTestPage("buttonClicksUpdateCounterValue")

        val button = driver.findElement(By.id("btn"))

        waitTextToBe(textId = "txt", value = "0")
        repeat(3) {
            button.click()
            waitTextToBe(textId = "txt", value = (it + 1).toString())
        }
    }

    @Test
    fun `hovering the box updates the text`() {
        openTestPage("hoverOnDivUpdatesText")

        val box = driver.findElement(By.id("box"))
        waitTextToBe(textId = "txt", value = "not hovered")

        val actions = Actions(driver)

        actions.moveToElement(box).perform()
        waitTextToBe(textId = "txt", value = "hovered")

        actions.moveByOffset(300, 0).perform()
        waitTextToBe(textId = "txt", value = "not hovered")
    }
}
