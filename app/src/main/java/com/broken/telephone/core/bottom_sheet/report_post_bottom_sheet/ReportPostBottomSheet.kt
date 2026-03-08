package com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.content.ReportPostBottomSheetButton
import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.core.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPostBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onReportClick: (ReportPostType) -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    fun hideAndThen(type: ReportPostType) {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            onDismissRequest()
            onReportClick(type)
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        ReportPostBottomSheetContent(
            onReportClick = { hideAndThen(it) },
        )

    }

}

@Composable
fun ReportPostBottomSheetContent(
    modifier: Modifier = Modifier,
    onReportClick: (ReportPostType) -> Unit = {},
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .navigationBarsPadding()
    ) {

        Text(
            text = stringResource(R.string.report_post_bottom_sheet_title),
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val types = ReportPostType.entries
        types.forEachIndexed { index, type ->
            ReportPostBottomSheetButton(
                text = stringResource(type.labelResId),
                modifier = Modifier
                    .clickable { onReportClick(type) }
                    .padding(horizontal = 16.dp),
            )

            if (index < types.lastIndex) {
                HorizontalDivider(color = MaterialTheme.appColors.divider)
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ReportPostBottomSheetPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        ReportPostBottomSheet(onDismissRequest = {})
    }
}
