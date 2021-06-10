package controller

import spark.ModelAndView
import spark.Request
import spark.Response
import spark.TemplateEngine
import spark.template.velocity.VelocityTemplateEngine
import kotlin.collections.HashMap

class PageViewController {

    fun index() =
        { _: Request, _: Response ->
            val model: HashMap<String, Any?> = hashMapOf(
                "title" to "Aim King"
            )
            render("index.vm", model)
        }

    companion object {
        private val renderEngine: TemplateEngine
            get() = VelocityTemplateEngine()

        private fun render(templateName: String, model: Map<String, Any?> = emptyMap()) =
            renderEngine.render(ModelAndView(model, "public/$templateName"))
    }
}
