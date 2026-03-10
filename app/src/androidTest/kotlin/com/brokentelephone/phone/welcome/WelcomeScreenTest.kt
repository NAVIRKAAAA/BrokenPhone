package com.brokentelephone.phone.welcome

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.brokentelephone.game.features.welcome.WelcomeScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WelcomeScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun welcomeScreen_displaysGreeting() {
        composeTestRule.setContent {
            WelcomeScreen(
                onNavigateToDashboard = {},
                onGetStarted = {},
                onSignIn = {},
            )
        }

        composeTestRule.onNodeWithText("Get Started").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Continue as Guest").assertIsDisplayed()
    }

    @Test
    fun welcomeScreen_getStarted_triggersCallback() {
        var clicked = false
        composeTestRule.setContent {
            WelcomeScreen(
                onNavigateToDashboard = {},
                onGetStarted = { clicked = true },
                onSignIn = {},
            )
        }

        composeTestRule.onNodeWithText("Get Started").performClick()
        assertTrue(clicked)
    }

    @Test
    fun welcomeScreen_signIn_triggersCallback() {
        var clicked = false
        composeTestRule.setContent {
            WelcomeScreen(
                onNavigateToDashboard = {},
                onGetStarted = {},
                onSignIn = { clicked = true },
            )
        }

        composeTestRule.onNodeWithText("Sign In").performClick()
        assertTrue(clicked)
    }

    @Test
    fun welcomeScreen_continueAsGuest_triggersCallback() {
        var clicked = false
        composeTestRule.setContent {
            WelcomeScreen(
                onNavigateToDashboard = { clicked = true },
                onGetStarted = {},
                onSignIn = {},
            )
        }

        composeTestRule.onNodeWithText("Continue as Guest").performClick()
        assertTrue(clicked)
    }
}