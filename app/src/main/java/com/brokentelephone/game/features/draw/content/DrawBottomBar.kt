package com.brokentelephone.game.features.draw.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.draw.model.BrushSize

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
            enabled = canUndo,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_undo),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )

        }

        IconButton(
            onClick = onRedo,
            enabled = canRedo,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground
            )
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
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground
            )
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
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
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
    }
}