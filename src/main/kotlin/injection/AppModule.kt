package injection

import Routes
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import controller.PageViewController
import service.WebSocketReceiverService

class AppModule : AbstractModule() {

    @Provides
    @Singleton
    fun routes(
        pageViewController: PageViewController,
        webSocketReceiverService: WebSocketReceiverService
    ) = Routes(pageViewController, webSocketReceiverService)
}
