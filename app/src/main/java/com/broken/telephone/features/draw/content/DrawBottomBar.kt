package com.broken.telephone.features.draw.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.R
import com.broken.telephone.features.draw.model.BrushSize

@Composable
fun DrawBottomBar(
    selectedBrushSize: BrushSize,
    canUndo: Boolean,
    canRedo: Boolean,
    canClear: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onClear: () -> Unit,
    onBrushSizeChange: (BrushSize) -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onUndo,
            enabled = canUndo
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_undo),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )

        }

        IconButton(
            onClick = onRedo,
            enabled = canRedo
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_redo),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )

        }

        IconButton(
            onClick = onClear,
            enabled = canClear,
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_trash),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )

        }

        BrushSizeComponent(
            selectedBrushSize = selectedBrushSize,
            onClick = onBrushSizeChange
        )

    }

}

@Preview
@Composable
fun DrawBottomBarPreview() {
    DrawBottomBar(
        selectedBrushSize = BrushSize.MEDIUM,
        canUndo = true,
        canRedo = false,
        canClear = true,
        onUndo = {},
        onRedo = {},
        onClear = {},
        onBrushSizeChange = {},
    )
}