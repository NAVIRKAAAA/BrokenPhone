package com.brokentelephone.game.core.utils

import android.icu.text.RelativeDateTimeFormatter
import android.os.Build
import android.text.format.DateUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.brokentelephone.game.core.theme.LocalAppLanguage
import com.brokentelephone.game.domain.model.settings.Language
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun rememberRelativeTime(timestamp: Long): String {
    val language = LocalAppLanguage.current
    val locale = when (language) {
        Language.ENGLISH -> Locale.ENGLISH
        Language.UKRAINIAN -> Locale.forLanguageTag("uk")
    }

    return remember(timestamp, language) {

        val now = System.currentTimeMillis()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            val diff = now - timestamp

            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            val uLocale = android.icu.util.ULocale.forLocale(locale)
            val formatter = RelativeDateTimeFormatter.getInstance(
                uLocale,
                null,
                RelativeDateTimeFormatter.Style.SHORT,
                android.icu.text.DisplayContext.CAPITALIZATION_FOR_STANDALONE,
            )

            when {
                seconds < 60 ->
                    formatter.format(
                        -seconds.toDouble(),
                        RelativeDateTimeFormatter.RelativeDateTimeUnit.SECOND
                    )

                minutes < 60 ->
                    formatter.format(
                        -minutes.toDouble(),
                        RelativeDateTimeFormatter.RelativeDateTimeUnit.MINUTE
                    )

                hours < 24 ->
                    formatter.format(
                        -hours.toDouble(),
                        RelativeDateTimeFormatter.RelativeDateTimeUnit.HOUR
                    )

                else ->
                    formatter.format(
                        -days.toDouble(),
                        RelativeDateTimeFormatter.RelativeDateTimeUnit.DAY
                    )
            }

        } else {
            DateUtils.getRelativeTimeSpanString(
                timestamp,
                now,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL
            ).toString()
        }
    }
}

@Composable
fun rememberMemberSince(timestamp: Long): String {
    val language = LocalAppLanguage.current
    val locale = when (language) {
        Language.ENGLISH -> Locale.ENGLISH
        Language.UKRAINIAN -> Locale.forLanguageTag("uk")
    }
    return remember(timestamp, language) {
        SimpleDateFormat("MMM yyyy", locale).format(Date(timestamp))
    }
}

fun Int.toFormattedTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}

fun Int.toShortFormattedTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    return when {
        minutes > 0 && seconds > 0 -> "${minutes}m ${seconds}s"
        minutes > 0 -> "${minutes}m"
        else -> "${seconds}s"
    }
}

@Composable
fun rememberFormattedDate(timestamp: Long): String {
    val language = LocalAppLanguage.current
    val locale = when (language) {
        Language.ENGLISH -> Locale.ENGLISH
        Language.UKRAINIAN -> Locale.forLanguageTag("uk")
    }
    val pattern = when (language) {
        Language.ENGLISH -> "MMM d, yyyy · h:mm a"
        Language.UKRAINIAN -> "d MMM yyyy · HH:mm"
    }
    return remember(timestamp, language) {
        SimpleDateFormat(pattern, locale).format(Date(timestamp))
    }
}