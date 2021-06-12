import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import controller.PageViewController
import service.AimKingService
import service.WebSocketReceiverService
import spark.Request
import spark.Response
import spark.Spark.*

class Routes(
    private val pageViewController: PageViewController,
    private val webSocketReceiverService: WebSocketReceiverService,
    private val aimKingService: AimKingService
) {
    fun register() {
        webSocket("/ws/game", webSocketReceiverService)

        path("/api") {
            getJson("/ranking") { _, _ -> aimKingService.getTop10() }
            getJson("/players-online") { _, _ -> aimKingService.playersOnline.values.map { playerOnline -> playerOnline.name } }
        }

        get("/", pageViewController.index(aimKingService.height, aimKingService.width))
    }

    private fun getJson(path: String, route: (Request, Response) -> Any) {
        get(path, "application/json") { request: Request, response: Response ->
            route(request, response)
                .also { response.type("application/json; charset=utf-8") }
                .let { jacksonObjectMapper().writeValueAsString(it) }
        }
    }
}
