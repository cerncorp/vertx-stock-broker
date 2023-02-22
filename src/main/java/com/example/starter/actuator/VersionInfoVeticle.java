package com.example.starter.actuator;

import com.example.starter.config.ConfigLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionInfoVeticle extends AbstractVerticle {

  public static final Logger LOG = LoggerFactory.getLogger(VersionInfoVeticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    ConfigLoader.load(vertx)
      .onFailure(startPromise::fail)
      .onSuccess(configuration -> {
        LOG.info("Current Version Application is {}", configuration.getVersion());
        startPromise.complete();
      });
  }
}
