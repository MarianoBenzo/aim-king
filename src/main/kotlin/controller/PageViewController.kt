package controller

import spark.ModelAndView
import spark.Request
import spark.Response
import spark.TemplateEngine
import spark.template.velocity.VelocityTemplateEngine
import kotlin.collections.HashMap

class PageViewController {

    fun index(height: Int, width: Int) =
        { _: Request, _: Response ->
            val model: HashMap<String, Any?> = hashMapOf(
                "title" to "Aim King",
                "height" to height,
                "width" to width
            )
            renderEngine.render(ModelAndView(model, "public/index.vm"))
        }

    companion object {
        private val renderEngine: TemplateEngine
            get() = VelocityTemplateEngine()
    }
}
