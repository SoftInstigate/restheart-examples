import org.restheart.exchange.JsonRequest
import org.restheart.exchange.JsonResponse
import org.restheart.plugins.JsonService
import com.google.gson.JsonObject
import org.restheart.plugins.RegisterPlugin

@RegisterPlugin(name = "kotlinGreeterService",
        description = "A simple service written in Kotlin",
        defaultURI = "/greetings")
class GreeterService : JsonService {
    override fun handle(request: JsonRequest?, response: JsonResponse?) {
        var greetings = JsonObject()
        greetings.addProperty("msg", "Hello World")

        response?.content = greetings;
    }
}