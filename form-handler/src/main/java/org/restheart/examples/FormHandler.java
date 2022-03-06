package org.restheart.examples;

import org.restheart.exchange.ServiceRequest;
import org.restheart.exchange.JsonResponse;
import org.restheart.plugins.RegisterPlugin;
import org.restheart.plugins.Service;
import org.restheart.utils.HttpStatus;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormParserFactory;

import static org.restheart.utils.GsonUtils.object;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

@RegisterPlugin(name = "formHandler", description = "handle a form post using FormDataParser")
public class FormHandler implements Service<FormRequest, JsonResponse> {
    @Override
    public void handle(FormRequest req, JsonResponse res) {
        switch(req.getMethod()) {
            case POST ->  {
                var out = object();
                var formData = req.getContent();

                formData.forEach(field -> out.put(field, formData.getFirst(field).getValue()));

                res.setContent(out);
            }
            case OPTIONS -> handleOptions(req);
            default -> res.setStatusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    public Consumer<HttpServerExchange> requestInitializer() {
        return e -> FormRequest.init(e);
    }

    @Override
    public Consumer<HttpServerExchange> responseInitializer() {
        return e -> JsonResponse.init(e);
    }

    @Override
    public Function<HttpServerExchange, FormRequest> request() {
        return e -> FormRequest.of(e);
    }

    @Override
    public Function<HttpServerExchange, JsonResponse> response() {
        return e -> JsonResponse.of(e);
    }
}

class FormRequest extends ServiceRequest<FormData> {

    private FormRequest(HttpServerExchange exchange) {
        super(exchange);
    }

    public static FormRequest init(HttpServerExchange exchange) {
        var ret = new FormRequest(exchange);

        try {
            ret.injectContent();
        } catch (Throwable ieo) {
            ret.setInError(true);
        }

        return ret;
    }

    public static FormRequest of(HttpServerExchange exchange) {
        return of(exchange, FormRequest.class);
    }

    private static FormParserFactory builder = FormParserFactory.builder().build();

    public void injectContent() throws IOException {
        var parser = builder.createParser(getExchange());

        if (parser == null) {
            throw new IOException("Not a form.");
        }

        setContent(parser.parseBlocking());
    }
}