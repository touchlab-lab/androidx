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
import org.junit.runner.RunWith
import org.junit.runner.notification.RunNotifier
import org.junit.runners.Suite
import org.junit.runners.model.RunnerBuilder
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

open class BaseSeleniumTests {

    companion object {

        const val PORT = 7777

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            ServerHelper.startServer(this)
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            ServerHelper.stopServer(this)
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

@RunWith(SuiteTestRunnerWithLocalhost::class)
@Suite.SuiteClasses(
    value = [
        SeleniumTests::class,
        SeleniumTests2::class
    ]
)
class AllSeleniumTestsSuite

private object ServerHelper {
    private lateinit var server: ApplicationEngine

    private var lock: Any? = null

    /**
     * @param lock - guarantees that a server is started only once
     */
    fun startServer(lock: Any) {
        if (this.lock != null) return
        this.lock = lock

        val homePath = System.getProperty("COMPOSE_WEB_INTEGRATION_TESTS_DISTRIBUTION")

        println("--- Starting localhost:${BaseSeleniumTests.PORT} using files in $homePath")

        server = embeddedServer(Netty, port = BaseSeleniumTests.PORT) {
            routing {
                static {
                    staticRootFolder = File(homePath)
                    files(".")
                    default("index.html")
                }
            }
        }.start()
    }

    /**
     * @param lock - guarantees that a server is stopped only by the same caller that started it
     */
    fun stopServer(lock: Any) {
        if (this.lock != lock) return
        this.lock = null
        println("--- Stopping server")
        server.stop(1000, 1000)
    }
}

class SuiteTestRunnerWithLocalhost(
    klass: Class<*>?,
    builder: RunnerBuilder?
) : Suite(klass, builder) {
    override fun run(notifier: RunNotifier?) {
        ServerHelper.startServer(this)
        super.run(notifier)
        ServerHelper.stopServer(this)
    }
}