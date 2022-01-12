import org.restheart.exchange.JsonRequest
import org.restheart.exchange.JsonResponse
import org.restheart.plugins.JsonService
import com.google.gson.JsonObject
import org.restheart.plugins.RegisterPlugin

@RegisterPlugin(name = "kotlinGreeterService", description = "just another Hello World in Kotlin")
class GreeterService : JsonService {
    override fun handle(request: JsonRequest, response: JsonResponse) {
        if (request.isGet) {
            var greetings = JsonObject()
            greetings.addProperty("msg", "Hello World")
            response.content = greetings;
        } else {
            response.statusCode = 400;
        }
    }
}