package com.brokentelephone.game.features.edit_email.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.text_field.SignUpTextFieldValue
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.SaveTopBar
import com.brokentelephone.game.features.edit_email.R
import com.brokentelephone.game.features.edit_email.model.EditEmailState
import kotlinx.coroutines.delay

@Composable
fun EditEmailContent(
    state: EditEmailState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onEmailChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val emailFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(150)
        emailFocus.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        SaveTopBar(
            title = stringResource(R.string.edit_email_title),
            saveButtonText = stringResource(R.string.edit_email_button_save),
            isSaveEnabled = state.isSaveEnabled,
            isLoading = false,
            onBackClick = onBackClick,
            onSaveClick = {
                focusManager.clearFocus()
                onSaveClick()
            },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding(),
        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = stringResource(R.string.edit_email_confirm_body),
//                fontFamily = FontFamily(Font(CoreR.font.nunito_regular)),
//                fontSize = 13.sp,
//                lineHeight = 19.sp,
//                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
//                modifier = Modifier.padding(horizontal = 16.dp),
//            )

            Spacer(modifier = Modifier.height(16.dp))

            SignUpTextFieldValue(
                value = state.email,
                onValueChange = onEmailChange,
                error = state.emailError,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .focusRequester(emailFocus),
                label = stringResource(R.string.edit_email_label),
                hint = stringResource(R.string.edit_email_hint),
                onClearClick = if (state.email.text.isNotBlank()) {
                    { onEmailChange(TextFieldValue("")) }
                } else null,
                onImeAction = {
                    focusManager.clearFocus()
                    onSaveClick()
                },
            )

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Preview
@Composable
fun EditEmailContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        EditEmailContent(
            state = EditEmailState(
                email = TextFieldValue("alex@example.com"),
                initialEmail = "alex@example.com",
            ),
            onBackClick = {},
            onSaveClick = {},
            onEmailChange = {},
        )
    }
}
