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

import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.junit.AfterClass
import org.junit.BeforeClass
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.File

open class BaseSeleniumTests {

    companion object {

        private const val PORT = 777

        private lateinit var server: ApplicationEngine

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            val homePath = System.getProperty("COMPOSE_WEB_INTEGRATION_TESTS_DISTRIBUTION")

            server = embeddedServer(Netty, port = PORT) {
                routing {
                    static {
                        staticRootFolder = File(homePath)
                        files(".")
                        default("index.html")
                    }
                }
            }.start()
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            server.stop(1000, 1000)
        }
    }

    private val path = "http://localhost:$PORT"

    internal lateinit var driver: RemoteWebDriver

    private val options = ChromeOptions().apply {
        setHeadless(true)
        addArguments("--no-sandbox")
    }

    fun openTestPage(test: String) {
        driver.get("$path?test=$test")
    }

    @BeforeTest
    fun before() {
        driver = ChromeDriver(options)
    }

    @AfterTest
    fun after() {
        driver.close()
        driver.quit()
    }
}