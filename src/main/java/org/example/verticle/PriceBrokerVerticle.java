package org.example.verticle;

import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.example.config.ConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;


/**
 * @Author: Anakhe Ajayi
 * @Date: 29 Dec, 2023
 */
@Component
public class PriceBrokerVerticle extends AbstractVerticle  {

    public static final Logger LOGGER = LoggerFactory.getLogger(PriceBrokerVerticle.class.getName());

    public void start() {
        vertx.setPeriodic(2000, timerId -> {
            JsonObject prices = new JsonObject()
                    .put("Apple", generateRandomPrice())
                    .put("HP", generateRandomPrice())
                    .put("Dell", generateRandomPrice());

            vertx.eventBus().publish(ConfigData.PRICE_ADDRESS, prices);
//            LOGGER.info("Price published to EventBus {}", prices);
        });
    }

    private double generateRandomPrice() {
        return ThreadLocalRandom.current().nextDouble() * 100;
    }
}
