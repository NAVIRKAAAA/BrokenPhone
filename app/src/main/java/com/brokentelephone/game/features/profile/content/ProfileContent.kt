package com.brokentelephone.game.features.profile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.data.repository.MockPostRepository
import com.brokentelephone.game.features.dashboard.model.toUi
import com.brokentelephone.game.features.profile.model.ProfileState
import com.brokentelephone.game.features.profile.model.ProfileTab
import com.brokentelephone.game.features.profile.model.UserUi
import com.brokentelephone.game.features.welcome.content.WelcomeButton

@Composable
fun ProfileContent(
    state: ProfileState,
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSignInClick: () -> Unit,
    onGetStartedClick: () -> Unit,
    onTabSelect: (ProfileTab) -> Unit,
    onScrollDirectionChange: (Boolean) -> Unit,
    onPostClick: (postId: String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(
        initialPage = state.selectedTab.ordinal,
        pageCount = { ProfileTab.entries.size }
    )

    LaunchedEffect(state.selectedTab) {
        pagerState.animateScrollToPage(state.selectedTab.ordinal)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onTabSelect(ProfileTab.entries[page])
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        ProfileTopBar(
            title = stringResource(R.string.profile_title),
            onEditClick = onEditClick,
            onSettingsClick = onSettingsClick,
            showEditButton = state.isAuth,
        )

        AccountInfoSection(
            username = state.user?.username.orEmpty(),
            avatarUrl = state.user?.avatarUrl,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            postsCount = state.myPosts.size,
            contributions = state.myContributions.size,
            isAuth = state.isAuth
        )

        if(state.isAuth) {

            PrimaryTabRow(
                selectedTabIndex = state.selectedTab.ordinal,
                containerColor = Color.Transparent,
                divider = {
                    HorizontalDivider(color = MaterialTheme.appColors.divider)
                }
            ) {
                ProfileTab.entries.forEachIndexed { index, tab ->
                    val isSelected = state.selectedTab.ordinal == index
                    val color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    Tab(
                        selected = isSelected,
                        onClick = { onTabSelect(tab) },
                        text = {
                            Text(
                                text = stringResource(tab.labelResId),
                                textAlign = TextAlign.Start,
                                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                                fontSize = 17.sp,
                                lineHeight = 25.sp,
                                color = color
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) { page ->
                when (ProfileTab.entries[page]) {
                    ProfileTab.POSTS -> ProfilePostsPage(
                        posts = state.myPosts,
                        isLoading = state.isLoading,
                        onScrollDirectionChange = onScrollDirectionChange,
                        onPostClick = onPostClick,
                        onMoreClick = onMoreClick,
                    )

                    ProfileTab.CONTRIBUTIONS -> ProfilePostsPage(
                        posts = state.myContributions,
                        isLoading = state.isLoading,
                        onScrollDirectionChange = onScrollDirectionChange,
                        onPostClick = onPostClick,
                        onMoreClick = onMoreClick,
                    )
                }
            }

        } else {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                WelcomeButton(
                    text = stringResource(R.string.profile_sign_in),
                    onClick = onSignInClick,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f).height(48.dp)
                )


                Spacer(modifier = Modifier.width(12.dp))


                WelcomeButton(
                    text = stringResource(R.string.welcome_get_started),
                    onClick = onGetStartedClick,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f).height(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.profile_guest_note),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun ProfileContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        ProfileContent(
            state = ProfileState(
                user = UserUi(
                    id = "user-1",
                    username = "Alex",
                    avatarUrl = null,
                    email = "",
                    createdAt = 0
                ),
                myPosts = MockPostRepository.mockList.map { it.toUi() },
                myContributions = MockPostRepository.mockList.map { it.toUi() },
                isAuth = true
            ),
            onEditClick = {},
            onSettingsClick = {},
            onSignInClick = {},
            onGetStartedClick = {},
            onTabSelect = {},
            onScrollDirectionChange = {},
            onPostClick = {},
            onMoreClick = {},
        )
    }
}
