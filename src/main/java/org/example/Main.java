package org.example;

import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.cluster.infinispan.InfinispanClusterManager;
import io.vertx.mutiny.core.Vertx;
import org.example.config.ApplicationConfig;
import org.example.config.VertxHolderConfig;
import org.example.verticle.MainVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: Anakhe Ajayi
 * @Date: 29 Dec, 2023
 */
@SpringBootApplication
public class Main {

    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class.getName());
    public static void main(String[] args) {
        ClusterManager clusterManager = new InfinispanClusterManager();
        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
        Vertx.clusteredVertx(options).flatMap(vertx -> {

                    VertxHolderConfig.getInstance().setVertx(vertx);
                    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
                    MainVerticle deployableVerticle = applicationContext.getBean(MainVerticle.class);
                    return vertx.deployVerticle(deployableVerticle);

                }).subscribe()
                .with(id -> LOGGER.info("Vertx Instance Id: {}", id),
                        throwable -> LOGGER.error("Main", throwable));
    }
}