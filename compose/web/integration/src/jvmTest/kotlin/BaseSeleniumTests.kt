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
import org.junit.Ignore
import org.junit.internal.builders.JUnit4Builder
import org.junit.runner.RunWith
import org.junit.runner.Runner
import org.junit.runner.notification.RunNotifier
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.Suite
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.RunnerBuilder
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.util.Objects

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

    val driver: RemoteWebDriver
        get() = WebDriverProvider.threadLocalDriver.get()

    fun openTestPage(test: String) {
        driver.get("$path?test=$test")
    }

    val wait = WebDriverWait(driver, 1)

    fun waitTextToBe(textId: String, value: String) {
        wait.until(ExpectedConditions.textToBe(By.id(textId), value))
    }
}

@RunWith(SuiteTestRunnerWithLocalhost::class)
@Suite.SuiteClasses(
    value = [
        SeleniumTests::class,
        SeleniumTests2::class
    ]
)
@Ignore
class AllSeleniumTestsSuite

private object WebDriverProvider {
    val chromeDriver: RemoteWebDriver by lazy {
        createChromeDriver()
    }

    val firefoxDriver: RemoteWebDriver by lazy {
        createFirefoxDriver()
    }

    val allDrivers: List<RemoteWebDriver> by lazy {
        listOf(firefoxDriver, chromeDriver)
    }

    // ThreadLocal might help when we decide to run the tests in parallel
    val threadLocalDriver = ThreadLocal<RemoteWebDriver>()

    private fun createChromeDriver(): RemoteWebDriver {
        val options = ChromeOptions().apply {
            setHeadless(true)
            addArguments("--no-sandbox")
        }

        return ChromeDriver(options)
    }

    private fun createFirefoxDriver(): RemoteWebDriver {
        val options = FirefoxOptions().apply {
            setHeadless(true)
        }
        return FirefoxDriver(options)
    }
}

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
) : Suite(
    klass,
    JUnit4BuilderWithSeleniumDrivers(
        drivers = WebDriverProvider.allDrivers
    )
) {
    override fun run(notifier: RunNotifier?) {
        ServerHelper.startServer(this)
        super.run(notifier)
        ServerHelper.stopServer(this)

        WebDriverProvider.allDrivers.forEach {
            it.quit()
        }
    }
}

private class JUnit4BuilderWithSeleniumDrivers(
    val drivers: List<RemoteWebDriver>
) : JUnit4Builder() {
    override fun runnerForClass(testClass: Class<*>?): Runner {
        return ClassTestRunnerWithSeleniumWebDrivers(
            drivers = drivers,
            klass = testClass
        )
    }
}

@Suppress("EqualsOrHashCode")
private class FrameworkMethodWithSeleniumDriver(
    val driver: RemoteWebDriver,
    private val method: FrameworkMethod
) : FrameworkMethod(method.method) {

    override fun getName(): String {
        return super.getName() + " [${getDriverName(driver)}]"
    }

    override fun hashCode(): Int {
        return Objects.hash(method, getDriverName(driver))
    }

    private fun getDriverName(driver: RemoteWebDriver): String {
        return when (driver) {
            is ChromeDriver -> "chrome"
            is FirefoxDriver -> "firefox"
            else -> "other"
        }
    }
}

private class ClassTestRunnerWithSeleniumWebDrivers(
    val drivers: List<RemoteWebDriver>,
    klass: Class<*>?
) : BlockJUnit4ClassRunner(klass) {

    override fun runChild(method: FrameworkMethod?, notifier: RunNotifier?) {
        WebDriverProvider.threadLocalDriver.set(
            (method as FrameworkMethodWithSeleniumDriver).driver
        )
        super.runChild(method, notifier)
    }

    override fun getChildren(): MutableList<FrameworkMethod> {
        return super.getChildren().flatMap { method ->
            drivers.map { driver -> FrameworkMethodWithSeleniumDriver(driver, method) }
        }.toMutableList()
    }
}