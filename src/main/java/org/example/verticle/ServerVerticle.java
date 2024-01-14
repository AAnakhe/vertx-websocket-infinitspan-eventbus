package org.example.verticle;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.tracing.TracingPolicy;
import io.vertx.mutiny.ext.web.Router;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ServerVerticle extends AbstractVerticle {

    public static final Logger LOGGER = Logger.getLogger(ServerVerticle.class.getName());

    private final Router router;
    private String host = "127.0.0.1";
    private int port = 8081;

    public ServerVerticle(Router router) {
        this.router = router;
    }

    @Override
    public Uni<Void> asyncStart() {
        return startHttpServer();
    }

    private Uni<Void> startHttpServer() {

        HttpServerOptions httpServerOptions = new HttpServerOptions()
                .setHost(host).setPort(port)
                .setTracingPolicy(TracingPolicy.ALWAYS);

        return vertx.createHttpServer(httpServerOptions)
                .requestHandler(router)
                .listen(port, host)
                .onItem()
                .invoke(server -> LOGGER.info("Server Running on http://localhost:" + server.actualPort())).replaceWithVoid();
    }
}
