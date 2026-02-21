package com.broken.telephone.core.bottom_sheet.post_bottom_sheet

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.R
import com.broken.telephone.core.bottom_sheet.post_bottom_sheet.content.PostBottomSheetButton
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onNotInterestedClick: () -> Unit = {},
    onBlockClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    fun hideAndThen(action: () -> Unit) {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            onDismissRequest()
            action()
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        PostBottomSheetContent(
            onNotInterestedClick = { hideAndThen(onNotInterestedClick) },
            onBlockClick = { hideAndThen(onBlockClick) },
            onReportClick = { hideAndThen(onReportClick) },
        )

    }

}

@Composable
fun PostBottomSheetContent(
    modifier: Modifier = Modifier,
    onNotInterestedClick: () -> Unit = {},
    onBlockClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
) {

    Column(
        modifier = modifier.fillMaxWidth()
            .padding(bottom = 8.dp)
            .navigationBarsPadding()
    ) {

        PostBottomSheetButton(
            text = "Not Interested",
            iconResId = R.drawable.ic_not_interested,
            modifier = Modifier
                .clickable(onClick = onNotInterestedClick)
                .padding(horizontal = 16.dp),
            textColor = Color.Black,
            iconColor = Color(0xFF666666)
        )

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        PostBottomSheetButton(
            text = "Block",
            iconResId = R.drawable.ic_block,
            modifier = Modifier
                .clickable(onClick = onBlockClick)
                .padding(horizontal = 16.dp),
            textColor = MaterialTheme.colorScheme.error,
            iconColor = MaterialTheme.colorScheme.error
        )

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        PostBottomSheetButton(
            text = "Report",
            iconResId = R.drawable.ic_report,
            modifier = Modifier
                .clickable(onClick = onReportClick)
                .padding(horizontal = 16.dp),
            textColor = MaterialTheme.colorScheme.error,
            iconColor = MaterialTheme.colorScheme.error
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PostBottomSheetContentPreview() {
    BrokenTelephoneTheme() {
        PostBottomSheet(onDismissRequest = {})
    }
}
