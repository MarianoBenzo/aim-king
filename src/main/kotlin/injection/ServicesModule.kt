package injection

import service.*
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

class ServicesModule : AbstractModule() {
    @Provides
    @Singleton
    fun webSocketSenderService() = WebSocketSenderService()

    @Provides
    @Singleton
    fun webSocketReceiverService(
        webSocketSenderService: WebSocketSenderService,
        aimKingService: AimKingService
    ) = WebSocketReceiverService(webSocketSenderService, aimKingService)

    @Provides
    @Singleton
    fun aimKingService(webSocketSenderService: WebSocketSenderService) = AimKingService(webSocketSenderService)
}