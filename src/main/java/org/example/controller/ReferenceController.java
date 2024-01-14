package org.example.controller;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;


import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.example.config.VertxHolderConfig;
import org.springframework.stereotype.Controller;

import java.util.function.Function;
import java.util.logging.Logger;

/**
 * @Author: Anakhe Ajayi
 * @Date: 29 Dec, 2023
 */
@Controller
public class ReferenceController implements Function<RoutingContext, Uni<Void>> {

    private final Vertx vertx;

    public ReferenceController(Router router) {
        this.vertx =  VertxHolderConfig.getInstance().getVertx();
        router.get("/api/ref") .respond(this);
    }


    @Override
    public Uni<Void> apply(RoutingContext rc) {
        return vertx.eventBus().request("refAddress", "")
                .flatMap(message -> {
                    String data = (String) message.body();
                    return rc.response().setStatusCode(200)
                            .putHeader("content-type", "application/json")
                            .end(new JsonObject(data).encode());
                });
    }
}