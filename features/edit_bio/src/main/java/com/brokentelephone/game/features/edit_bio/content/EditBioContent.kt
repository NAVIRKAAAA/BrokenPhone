package com.brokentelephone.game.features.edit_bio.content

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.text_field.SignUpTextFieldValue
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.SaveTopBar
import com.brokentelephone.game.features.edit_bio.EditBioViewModel
import com.brokentelephone.game.features.edit_bio.model.EditBioState
import kotlinx.coroutines.delay

@Composable
fun EditBioContent(
    state: EditBioState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onBioChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val bioFocus = remember { FocusRequester() }
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(state.bio, TextRange(state.bio.length)))
    }

    LaunchedEffect(Unit) {
        delay(150)
        bioFocus.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        SaveTopBar(
            title = stringResource(R.string.edit_bio_title),
            saveButtonText = stringResource(R.string.edit_bio_button_save),
            isSaveEnabled = state.isSaveEnabled,
            isLoading = state.isLoading,
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
            Spacer(modifier = Modifier.height(16.dp))

            SignUpTextFieldValue(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    onBioChange(newValue.text)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .focusRequester(bioFocus),
                label = stringResource(R.string.edit_bio_label),
                hint = stringResource(R.string.edit_bio_hint),
                onClearClick = if (state.bio.isNotBlank()) {
                    {
                        textFieldValue = TextFieldValue("")
                        onBioChange("")
                    }
                } else null,
                onImeAction = {
                    focusManager.clearFocus()
                    onSaveClick()
                },
                maxLength = EditBioViewModel.MAX_BIO_LENGTH,
                minLines = 3,
                maxLines = Int.MAX_VALUE,
                singleLine = false
            )

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Preview
@Composable
fun EditBioContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        EditBioContent(
            state = EditBioState(bio = "I love drawing!"),
            onBackClick = {},
            onSaveClick = {},
            onBioChange = {},
        )
    }
}
