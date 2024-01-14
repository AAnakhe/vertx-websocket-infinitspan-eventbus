package org.example.verticle;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MainVerticle extends AbstractVerticle {

    public static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class.getName());

    private final PriceBrokerVerticle priceBrokerVerticle;

    private final ReferenceIDVerticle referenceIDVerticle;

    private final ServerVerticle serverVerticle;

    public MainVerticle(PriceBrokerVerticle priceBrokerVerticle, ReferenceIDVerticle referenceIDVerticle, ServerVerticle serverVerticle) {
        this.priceBrokerVerticle = priceBrokerVerticle;
        this.referenceIDVerticle = referenceIDVerticle;
        this.serverVerticle = serverVerticle;
    }


    @Override
    public Uni<Void> asyncStart() {
        return deployVerticle();
    }

    private Uni<Void> deployVerticle() {

        Uni<String> priceBroker = vertx.deployVerticle(priceBrokerVerticle);
        Uni<String> refID = vertx.deployVerticle(referenceIDVerticle);
        Uni<String> serverVert = vertx.deployVerticle(serverVerticle);

        return Uni.combine().all()
                .unis(priceBroker, refID, serverVert)
                .combinedWith(list -> "successful")
                .onItem().invoke(() -> LOG.info("Deploy Verticles"))
                .onFailure().invoke((e) -> LOG.error("DeployableVerticle", e))
                .replaceWithVoid();
    }
}
