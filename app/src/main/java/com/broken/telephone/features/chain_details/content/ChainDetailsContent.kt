package com.broken.telephone.features.chain_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.data.repository.MockPostRepository
import com.broken.telephone.features.chain_details.model.ChainDetailsState
import com.broken.telephone.features.chain_details.model.toUi
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun ChainDetailsContent(
    state: ChainDetailsState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    val chainSize = state.chains.size
    val maxGenerations = state.post?.maxGenerations ?: 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        ChainDetailsTopBar(
            title = stringResource(R.string.chain_details_title),
            onBackClick = onBackClick,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            state = lazyListState,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {

            itemsIndexed(
                items = state.chains,
                key = { _, item -> item.id }
            ) { index, postUi ->

                Column() {

                    if (index != 0) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    ChainDetailsElement(
                        post = postUi,
                        isHidden = chainSize != maxGenerations && postUi.authorId != state.currentUserId
                    )

                    if (index != state.chains.lastIndex) {

                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 52.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_down),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier

                            )


//                            Spacer(modifier = Modifier.width(4.dp))
//
//                            Text(
//                                text = (index + 1).toString(),
//                                textAlign = TextAlign.Center,
//                                fontFamily = FontFamily(Font(R.font.inter_medium)),
//                                fontSize = 16.sp,
//                                lineHeight = 24.sp,
//                                color = MaterialTheme.colorScheme.primary,
//                            )
                        }

                    }
                }

            }

            item {
                Spacer(modifier = Modifier.height(32.dp))

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(24.dp))
                if(chainSize == maxGenerations) {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            6.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.ic_complete),
                            contentDescription = null,
                            tint = Color(0xFF22C55E),
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = stringResource(R.string.chain_details_complete),
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF22C55E)
                        )

                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = stringResource(
                        R.string.chain_details_generations,
                        chainSize,
                        maxGenerations
                    ),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = Color(0xFF999999),
                )

                Spacer(
                    modifier = Modifier
                        .height(32.dp)
                        .navigationBarsPadding()
                )
            }

        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun ChainDetailsContentPreview() {
    BrokenTelephoneTheme() {
        val userId = "user_id"
        ChainDetailsContent(
            state = ChainDetailsState(
                postId = "1",
                chains = MockPostRepository.chainsMockList.mapIndexed { index, entry ->
                    entry.toUi(
                        id = Uuid.random().toString(),
                        generation = index,
                        maxGenerations = MockPostRepository.chainsMockList.size,
                        textTimeLimit = 0,
                        drawingTimeLimit = 0
                    ).copy(
                        authorId = if(index == 1) userId else index.toString()
                    )
                }.subList(0, 3),
                currentUserId = userId
            ),
            onBackClick = {},
        )
    }
}
