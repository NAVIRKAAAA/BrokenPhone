package com.broken.phone.sign_up

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.broken.telephone.features.sign_up.content.SignUpContent
import com.broken.telephone.features.sign_up.model.SignUpState
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val signUpButton get() = composeTestRule.onNode(hasText("Sign Up") and hasClickAction())

    @Test
    fun signUpScreen_displaysAllFields() {
        composeTestRule.setContent {
            SignUpContent(
                state = SignUpState(),
                onBackClick = {},
            )
        }

        composeTestRule.onAllNodesWithText("Sign Up")[0].assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirm Password").assertIsDisplayed()
    }

    @Test
    fun signUpScreen_signUpButton_isDisabledByDefault() {
        composeTestRule.setContent {
            SignUpContent(
                state = SignUpState(),
                onBackClick = {},
            )
        }

        signUpButton.assertIsNotEnabled()
    }

    @Test
    fun signUpScreen_signUpButton_isEnabledWhenAllFieldsFilled() {
        composeTestRule.setContent {
            SignUpContent(
                state = SignUpState(
                    email = "test@example.com",
                    password = "password123",
                    confirmPassword = "password123",
                ),
                onBackClick = {},
            )
        }

        signUpButton.assertIsEnabled()
    }

    @Test
    fun signUpScreen_signUpButton_isDisabledWhenLoading() {
        composeTestRule.setContent {
            SignUpContent(
                state = SignUpState(
                    email = "test@example.com",
                    password = "password123",
                    confirmPassword = "password123",
                    isLoading = true,
                ),
                onBackClick = {},
            )
        }

        signUpButton.assertIsNotEnabled()
    }

    @Test
    fun signUpScreen_signUpButton_triggersCallback() {
        var clicked = false
        composeTestRule.setContent {
            SignUpContent(
                state = SignUpState(
                    email = "test@example.com",
                    password = "password123",
                    confirmPassword = "password123",
                ),
                onBackClick = {},
                onSignUpClick = { clicked = true },
            )
        }

        signUpButton.performClick()
        assertTrue(clicked)
    }

    @Test
    fun signUpScreen_displaysEmailError() {
        val errorMessage = "Invalid email address"
        composeTestRule.setContent {
            SignUpContent(
                state = SignUpState(emailError = errorMessage),
                onBackClick = {},
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun signUpScreen_displaysPasswordError() {
        val errorMessage = "Password is too short"
        composeTestRule.setContent {
            SignUpContent(
                state = SignUpState(passwordError = errorMessage),
                onBackClick = {},
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun signUpScreen_displaysConfirmPasswordError() {
        val errorMessage = "Passwords do not match"
        composeTestRule.setContent {
            SignUpContent(
                state = SignUpState(confirmPasswordError = errorMessage),
                onBackClick = {},
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun signUpScreen_emailField_triggersCallback() {
        var inputValue = ""
        composeTestRule.setContent {
            SignUpContent(
                state = SignUpState(),
                onBackClick = {},
                onEmailChanged = { inputValue = it },
            )
        }

        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        assertTrue(inputValue == "test@example.com")
    }
}
