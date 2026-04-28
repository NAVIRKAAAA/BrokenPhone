package com.brokentelephone.game.core.profile

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors

@Composable
fun ProfileTabRowNewVTwo(
    tabs: List<ProfileTab>,
    selectedIndex: Int,
    onTabSelect: (ProfileTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var innerWidthPx by remember { mutableIntStateOf(0) }
    val pillWidth: Dp = remember(innerWidthPx, tabs.size) {
        if (tabs.isNotEmpty()) with(density) { (innerWidthPx / tabs.size).toDp() } else 0.dp
    }

    val indicatorAnim = remember { Animatable(0f) }
    var initialSnapDone by remember { mutableStateOf(false) }

    LaunchedEffect(pillWidth, selectedIndex) {
        if (pillWidth <= 0.dp) return@LaunchedEffect
        val targetPx = with(density) { (pillWidth * selectedIndex).toPx() }
        if (!initialSnapDone) {
            indicatorAnim.snapTo(targetPx)
            initialSnapDone = true
        } else {
            indicatorAnim.animateTo(targetPx, animationSpec = tween(durationMillis = 250))
        }
    }

    val indicatorOffset = with(density) { indicatorAnim.value.toDp() }

    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.appColors.profileTabRowBg)
            .padding(4.dp)
            .height(IntrinsicSize.Min)
            .onSizeChanged { innerWidthPx = it.width },
    ) {
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(pillWidth)
                .fillMaxHeight()
                .clip(shape)
                .background(MaterialTheme.colorScheme.background),
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedIndex == index
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(shape)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) { onTabSelect(tab) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = stringResource(tab.labelResId),
                        fontFamily = FontFamily(Font(R.font.nunito_medium)),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileTabRowNewVTwoPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        var selectedIndex by remember { mutableIntStateOf(0) }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            ProfileTabRowNewVTwo(
                tabs = ProfileTab.entries,
                selectedIndex = selectedIndex,
                onTabSelect = { tab -> selectedIndex = ProfileTab.entries.indexOf(tab) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileTabRowNewVTwoDarkPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        var selectedIndex by remember { mutableIntStateOf(1) }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            ProfileTabRowNewVTwo(
                tabs = ProfileTab.entries,
                selectedIndex = selectedIndex,
                onTabSelect = { tab -> selectedIndex = ProfileTab.entries.indexOf(tab) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
