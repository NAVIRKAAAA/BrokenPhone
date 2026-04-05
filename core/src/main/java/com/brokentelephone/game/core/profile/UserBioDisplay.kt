package com.brokentelephone.game.core.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberMemberSince

private const val MAX_BIO_PREVIEW_CHARS = 100

@Composable
fun UserBioDisplay(
    bio: String,
    createdAt: Long?,
    modifier: Modifier = Modifier,
) {
    if (bio.isBlank() && createdAt != null) {

        val memberSince = rememberMemberSince(createdAt)

        Text(
            text = stringResource(R.string.profile_member_since, memberSince),
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier,
        )
        return
    }

    if (bio.isBlank()) return

    val isLong = bio.length > MAX_BIO_PREVIEW_CHARS
    var expanded by rememberSaveable { mutableStateOf(false) }

    if (!isLong || expanded) {
        Text(
            text = bio,
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier,
        )
    } else {
        val moreLabel = stringResource(R.string.bio_more)
        val primaryColor = MaterialTheme.colorScheme.primary
        val textColor = MaterialTheme.colorScheme.onSurfaceVariant
        val text = buildAnnotatedString {
            withStyle(SpanStyle(color = textColor)) {
                append(bio.take(MAX_BIO_PREVIEW_CHARS))
                append("... ")
            }
            withStyle(SpanStyle(color = primaryColor)) {
                append(moreLabel)
            }
        }
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            modifier = modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { expanded = true },
        )
    }
}

@Composable
fun UserBioDisplayAsSingleLine(
    bio: String,
    createdAt: Long,
    modifier: Modifier = Modifier,
) {
    if (bio.isBlank()) {

        val memberSince = rememberMemberSince(createdAt)

        Text(
            text = stringResource(R.string.profile_member_since, memberSince),
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        return
    }

    Text(
        text = bio,
        fontFamily = FontFamily(Font(R.font.nunito_regular)),
        fontSize = 13.sp,
        lineHeight = 20.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview
@Composable
private fun UserBioDisplayNoBioPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            UserBioDisplay(bio = "Lalala", createdAt = null)
        }
    }
}

@Preview
@Composable
private fun UserBioDisplayShortBioPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            UserBioDisplay(bio = "I love drawing and creative games!", createdAt = 1740000000000L)
        }
    }
}

@Preview
@Composable
private fun UserBioDisplayLongBioPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            UserBioDisplay(
                bio = "I love drawing, creative games, and exploring new ideas. Always up for a challenge and meeting new people through fun activities!",
                createdAt = 1740000000000L,
            )
        }
    }
}
