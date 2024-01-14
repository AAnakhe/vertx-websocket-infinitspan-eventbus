package org.example.controller;


import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.springframework.stereotype.Controller;

import java.util.function.Function;

@Controller
public class PriceDisplayController implements Function<RoutingContext, Uni<Void>> {

    public PriceDisplayController(Router router) {
        router.get("/price-display").respond(this);
    }

    @Override
    public Uni<Void> apply(RoutingContext rc) {
        return rc.response().setStatusCode(200)
                .putHeader("content-type", "text/html")
                .sendFile("static/index.html");
    }

}
