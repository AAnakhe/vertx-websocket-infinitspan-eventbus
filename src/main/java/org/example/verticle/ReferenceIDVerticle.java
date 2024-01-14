package org.example.verticle;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.example.config.ConfigData;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: Anakhe Ajayi
 * @Date: 29 Dec, 2023
 */
@Component
public class ReferenceIDVerticle extends AbstractVerticle {
    private final String id;

    public ReferenceIDVerticle() {
        this.id = String.valueOf(UUID.randomUUID());
    }

    @Override
    public Uni<Void> asyncStart() {
        return Uni.createFrom().item(vertx.eventBus().consumer(ConfigData.REF_ADDRESS, message -> {
            message.reply(new JsonObject()
                    .put("message", "Hello Vertx Mutiny Infinispan Cluster")
                    .put("id", id).encode());
        })).replaceWithVoid();
    }
}
