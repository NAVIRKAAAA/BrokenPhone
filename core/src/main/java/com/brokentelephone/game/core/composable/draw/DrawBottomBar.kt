package com.brokentelephone.game.core.composable.draw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.color_picker.BrushColorPicker
import com.brokentelephone.game.core.model.draw.BrushSize
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun DrawBottomBar(
    selectedBrushSize: BrushSize,
    selectedColor: Color,
    canUndo: Boolean,
    canRedo: Boolean,
    canClear: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onClear: () -> Unit,
    onBrushSizeChange: (BrushSize) -> Unit,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column() {

        Spacer(modifier = Modifier.height(16.dp))

        BrushColorPicker(
            selectedColor = selectedColor,
            onColorSelected = onColorChange,
        )
        
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
}

@Preview
@Composable
private fun DrawBottomBarPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            DrawBottomBar(
                selectedBrushSize = BrushSize.MEDIUM,
                selectedColor = Color.Black,
                canUndo = true,
                canRedo = false,
                canClear = true,
                onUndo = {},
                onRedo = {},
                onClear = {},
                onBrushSizeChange = {},
                onColorChange = {},
            )
        }
    }
}