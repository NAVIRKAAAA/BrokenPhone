package com.brokentelephone.game.core.composable.profile

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.ext.modifier.coloredShadow
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun ProfileTabRowNew(
    tabs: List<ProfileTab>,
    selectedIndex: Int,
    onTabSelect: (ProfileTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val tabWidths = remember { mutableStateOf(List(tabs.size) { 0.dp }) }
    val tabOffsets = remember { mutableStateOf(List(tabs.size) { 0.dp }) }

    val indicatorWidth by animateDpAsState(
        targetValue = tabWidths.value.getOrElse(selectedIndex) { 0.dp },
        animationSpec = tween(durationMillis = 250),
        label = "indicatorWidth",
    )
    val indicatorOffset by animateDpAsState(
        targetValue = tabOffsets.value.getOrElse(selectedIndex) { 0.dp },
        animationSpec = tween(durationMillis = 250),
        label = "indicatorOffset",
    )

    Box(
        modifier = modifier
            .coloredShadow(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.14f),
                blurRadius = 16f,
                offsetY = 0.dp,
                offsetX = 0.dp,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background)
            .padding(4.dp)
            .height(IntrinsicSize.Min),
    ) {
        // Animated pill indicator behind the tab text
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(indicatorWidth)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedIndex == index
                Text(
                    text = stringResource(tab.labelResId),
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onTabSelect(tab) }
                        .onGloballyPositioned { coords ->
                            val widthDp = with(density) { coords.size.width.toDp() }
                            val offsetDp = with(density) { coords.positionInParent().x.toDp() }
                            val updated = tabWidths.value.toMutableList()
                            updated[index] = widthDp
                            tabWidths.value = updated
                            val updatedOffsets = tabOffsets.value.toMutableList()
                            updatedOffsets[index] = offsetDp
                            tabOffsets.value = updatedOffsets
                        }
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
private fun ProfileTabRowNewPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        var selectedIndex by remember { mutableStateOf(1) }
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            ProfileTabRowNew(
                tabs = ProfileTab.entries,
                selectedIndex = selectedIndex,
                onTabSelect = { tab -> selectedIndex = ProfileTab.entries.indexOf(tab) },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
            )
        }
    }
}
