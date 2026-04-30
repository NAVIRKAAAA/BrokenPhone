package com.brokentelephone.game.data.banner

import com.brokentelephone.game.data.banner.handler.ActiveSessionBannerHandler
import com.brokentelephone.game.domain.banner.BannerController
import com.brokentelephone.game.domain.model.banner.BannerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BannerControllerImpl(
    scope: CoroutineScope,
    private val activeSessionBannerHandler: ActiveSessionBannerHandler,
) : BannerController {

    private val queue = Channel<BannerType>(Channel.UNLIMITED)
    private val _banners = MutableStateFlow<BannerType?>(null)
    private var bannerJob: Job? = null

    init {
        scope.launch {
            for (banner in queue) {
                bannerJob = launch {
                    when (banner) {
                        is BannerType.ActiveSession -> activeSessionBannerHandler.handle(banner)
                            .collect { _banners.value = it }
                    }
                }
                bannerJob?.join()
                _banners.value = null
            }
        }
    }

    override fun observe(): Flow<BannerType?> = _banners.asStateFlow()

    override fun show(banner: BannerType) {
        queue.trySend(banner)
    }

    override fun hide() {
        bannerJob?.cancel()
        _banners.value = null
    }
}
