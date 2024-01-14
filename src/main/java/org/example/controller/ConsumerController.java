package org.example.controller;


import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;

import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.example.config.ConfigData;
import org.example.config.VertxHolderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.function.Function;

/**
 * @Author: Anakhe Ajayi
 * @Date: 29 Dec, 2023
 */
@Controller
public class ConsumerController implements Function<RoutingContext, Uni<Void>> {

    public static final Logger LOGGER = LoggerFactory.getLogger(ConsumerController.class.getName());
    private final Vertx vertx;

    @Autowired
    public ConsumerController(Router router) {
        this.vertx =  VertxHolderConfig.getInstance().getVertx();
        router.get("/prices").respond(this);
    }

    @Override
    public Uni<Void> apply(RoutingContext rc) {
        return  rc.request().toWebSocket()
                .flatMap(serverWebSocket -> {

                    vertx.eventBus().consumer(ConfigData.PRICE_ADDRESS, message -> {
                        JsonObject prices = (JsonObject) message.body();
                        serverWebSocket.writeTextMessage(prices.encode())
                                .onItem().invoke(() -> LOGGER.info("Message sent successfully"))
                                .onFailure().invoke(throwable -> LOGGER.error("Failed to send message: {}", throwable.getMessage()))
                                .subscribe().with(
                                        ignored -> {},
                                        throwable -> LOGGER.error("Subscription failed: {}", throwable.getMessage())
                                );
                    });

                    return Uni.createFrom().nullItem();
                });
    }
}
