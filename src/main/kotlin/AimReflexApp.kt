import com.google.inject.Guice
import com.google.inject.Injector
import injection.AppModule
import injection.ControllerModule
import service.GameWebSocketService
import spark.Spark.*
import kotlin.system.exitProcess

private class AimReflexApp(private val injector: Injector) {

    fun configure() = apply {
        port(System.getenv("PORT")?.toInt() ?: 9000)
        staticFiles.externalLocation("src/main/resources/public")
        //staticFiles.location("public")
        webSocket("/ws/game", GameWebSocketService::class.java)
        injector.getInstance(Routes::class.java).register()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                val injector = Guice.createInjector(
                    ControllerModule(),
                    AppModule()
                )
                AimReflexApp(injector).configure()
            } catch (e: Exception) {
                println("Error starting the application")
                exitProcess(1)
            }
        }
    }
}
