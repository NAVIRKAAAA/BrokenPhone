package com.brokentelephone.game.data.banner

import com.brokentelephone.game.domain.banner.BannerManager
import com.brokentelephone.game.domain.model.banner.BannerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class BannerManagerImpl(scope: CoroutineScope) : BannerManager {

    private val incoming = Channel<BannerType>(Channel.UNLIMITED)

    private val _banners = MutableSharedFlow<BannerType>()
    override val banners: Flow<BannerType> = _banners

    init {
        scope.launch {
            var isFirst = true
            for (banner in incoming) {
                if (!isFirst) delay(DELAY_BETWEEN_BANNERS_MS)
                _banners.emit(banner)
                isFirst = false
            }
        }
    }

    override fun show(banner: BannerType) {
        incoming.trySend(banner)
    }

    private companion object {
        const val DELAY_BETWEEN_BANNERS_MS = 500L
    }
}
