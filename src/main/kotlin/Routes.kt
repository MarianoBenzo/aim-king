import controller.PageViewController
import service.WebSocketReceiverService
import spark.Spark.*

class Routes(
    private val pageViewController: PageViewController,
    private val webSocketReceiverService: WebSocketReceiverService
) {
    fun register() {
        webSocket("/ws/game", webSocketReceiverService)
        get("/", pageViewController.index())
    }
}
