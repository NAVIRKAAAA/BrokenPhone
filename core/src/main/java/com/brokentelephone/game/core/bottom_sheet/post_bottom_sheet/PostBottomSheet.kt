package com.brokentelephone.game.core.bottom_sheet.post_bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.bottom_sheet.post_bottom_sheet.content.PostBottomSheetButton
import com.brokentelephone.game.core.model.bottom_shet.PostBottomSheetAction
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostBottomSheet(
    onDismissRequest: () -> Unit,
    actions: List<PostBottomSheetAction>,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onActionClick: (PostBottomSheetAction) -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    fun hideAndThen(action: PostBottomSheetAction) {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            onDismissRequest()
            onActionClick(action)
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        PostBottomSheetContent(
            actions = actions,
            onActionClick = { hideAndThen(it) },
        )

    }

}

@Composable
fun PostBottomSheetContent(
    actions: List<PostBottomSheetAction>,
    modifier: Modifier = Modifier,
    onActionClick: (PostBottomSheetAction) -> Unit = {},
) {
    val errorColor = MaterialTheme.colorScheme.error

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .navigationBarsPadding()
    ) {

        actions.forEachIndexed { index, action ->
            val textColor = when (action) {
                PostBottomSheetAction.NOT_INTERESTED, PostBottomSheetAction.COPY_LINK -> MaterialTheme.colorScheme.onSurface
                PostBottomSheetAction.BLOCK, PostBottomSheetAction.REPORT, PostBottomSheetAction.DELETE -> errorColor
            }
            val iconColor = when (action) {
                PostBottomSheetAction.NOT_INTERESTED, PostBottomSheetAction.COPY_LINK -> MaterialTheme.colorScheme.onSurfaceVariant
                PostBottomSheetAction.BLOCK, PostBottomSheetAction.REPORT, PostBottomSheetAction.DELETE -> errorColor
            }

            PostBottomSheetButton(
                text = stringResource(action.labelResId),
                iconResId = action.iconResId,
                modifier = Modifier
                    .clickable { onActionClick(action) }
                    .padding(horizontal = 16.dp),
                textColor = textColor,
                iconColor = iconColor,
            )

            if (index < actions.lastIndex) {
                HorizontalDivider(color = MaterialTheme.appColors.divider)
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PostBottomSheetContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            PostBottomSheet(
                onDismissRequest = {},
                actions = PostBottomSheetAction.entries,
            )
        }
    }
}
