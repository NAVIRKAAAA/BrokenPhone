package com.brokentelephone.game.features.dashboard.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.avatar.AvatarComponent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.dashboard.model.DashboardSort

@Composable
fun DashboardTopBar(
    name: String,
    avatarUrl: String?,
    selectedSort: DashboardSort,
    onSortSelected: (DashboardSort) -> Unit,
    onTitleClick: () -> Unit,
    isScrolled: Boolean,
    modifier: Modifier = Modifier,
) {
    var isSortMenuVisible by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isScrolled) Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onTitleClick,
                ) else Modifier
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        AnimatedContent(
            targetState = isScrolled,
            transitionSpec = {
                if (targetState) {
                    slideInVertically { -it } + fadeIn() togetherWith slideOutVertically { it } + fadeOut()
                } else {
                    slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
                }
            },
            modifier = Modifier.weight(1f),
        ) { scrolled ->
            if (scrolled) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(vertical = 8.dp),
                ) {
                    AvatarComponent(avatarUrl = avatarUrl, size = 40.dp)

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = name,
                        fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                        fontSize = 17.sp,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.dashboard_title, name),
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .statusBarsPadding()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Button,
                            onClick = onTitleClick,
                        ),
                )
            }
        }

        Box(modifier = Modifier.statusBarsPadding().padding(vertical = 8.dp)) {
            IconButton(onClick = { isSortMenuVisible = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }

            DropdownMenu(
                expanded = isSortMenuVisible,
                onDismissRequest = { isSortMenuVisible = false },
                containerColor = MaterialTheme.colorScheme.background,
            ) {
                DashboardSort.entries.forEach { sort ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(sort.labelResId),
                                fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                                fontSize = 15.sp,
                                color = if (sort == selectedSort) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onBackground,
                            )
                        },
                        trailingIcon = {
                            if (sort == selectedSort) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_check),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        },
                        onClick = {
                            onSortSelected(sort)
                            isSortMenuVisible = false
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DashboardTopBarPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            DashboardTopBar(
                name = "Alex",
                avatarUrl = null,
                selectedSort = DashboardSort.LATEST,
                onSortSelected = {},
                onTitleClick = {},
                isScrolled = false,
            )
        }
    }
}

@Preview
@Composable
private fun DashboardTopBarScrolledPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            DashboardTopBar(
                name = "Alex",
                avatarUrl = null,
                selectedSort = DashboardSort.LATEST,
                onSortSelected = {},
                onTitleClick = {},
                isScrolled = true,
            )
        }
    }
}
