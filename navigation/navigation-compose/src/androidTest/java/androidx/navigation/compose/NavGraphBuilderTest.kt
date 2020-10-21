/*
 * Copyright 2020 The Android Open Source Project
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

package androidx.navigation.compose

import android.net.Uri
import androidx.compose.ui.platform.ContextAmbient
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.navDeepLink
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.ui.test.createComposeRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@LargeTest
@RunWith(AndroidJUnit4::class)
class NavGraphBuilderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCurrentBackStackEntryNavigate() {
        lateinit var navController: TestNavHostController
        val key = "key"
        val arg = "myarg"
        composeTestRule.setContent {
            navController = TestNavHostController(ContextAmbient.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(navController, startDestination = FIRST_DESTINATION) {
                composable(FIRST_DESTINATION) { }
                composable(SECOND_DESTINATION) { }
            }
        }

        composeTestRule.runOnUiThread {
            navController.navigate(generateId(SECOND_DESTINATION), bundleOf(key to arg))
            assertThat(navController.currentBackStackEntry!!.arguments!!.getString(key))
                .isEqualTo(arg)
        }
    }

    @Test
    fun testDefaultArguments() {
        lateinit var navController: TestNavHostController
        val key = "key"
        val defaultArg = "default"
        composeTestRule.setContent {
            navController = TestNavHostController(ContextAmbient.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(navController, startDestination = FIRST_DESTINATION) {
                composable(FIRST_DESTINATION) { }
                composable(
                    SECOND_DESTINATION,
                    arguments = listOf(navArgument(key) { defaultValue = defaultArg })
                ) { }
            }
        }

        composeTestRule.runOnUiThread {
            navController.navigate(generateId(SECOND_DESTINATION))
            assertThat(navController.currentBackStackEntry!!.arguments!!.getString(key))
                .isEqualTo(defaultArg)
        }
    }

    @Test
    fun testDeepLink() {
        lateinit var navController: TestNavHostController
        val uriString = "https://www.example.com"
        val deeplink = NavDeepLinkRequest.Builder.fromUri(Uri.parse(uriString)).build()
        composeTestRule.setContent {
            navController = TestNavHostController(ContextAmbient.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(navController, startDestination = FIRST_DESTINATION) {
                composable(FIRST_DESTINATION) { }
                composable(
                    SECOND_DESTINATION,
                    deepLinks = listOf(navDeepLink { uriPattern = uriString })
                ) { }
            }
        }

        composeTestRule.runOnUiThread {
            navController.navigate(Uri.parse(uriString))
            assertThat(navController.currentBackStackEntry!!.destination.hasDeepLink(deeplink))
                .isTrue()
        }
    }
}

private const val FIRST_DESTINATION = 1
private const val SECOND_DESTINATION = 2