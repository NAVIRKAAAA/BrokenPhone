package com.broken.phone.welcome

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.features.welcome.WelcomeScreen
import org.junit.Rule
import org.junit.Test

/*

    ./gradlew :app:recordPaparazziDebug
    ./gradlew :app:verifyPaparazziDebug

 */
class WelcomeScreenTest {
    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        maxPercentDifference = 0.0,
        deviceConfig = DeviceConfig.PIXEL_8
    )

    @Test
    fun alert() {
        paparazziRule.snapshot {
            BrokenTelephoneTheme() {
                WelcomeScreen(
                    onContinueAsGuest = {},
                    onGetStarted = {},
                    onSignIn = {},
                )
            }
        }
    }
}