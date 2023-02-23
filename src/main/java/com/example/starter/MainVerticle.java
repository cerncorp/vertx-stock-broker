package com.example.starter;

import com.example.starter.actuator.VersionInfoVeticle;
import com.example.starter.assets.AssetsRestApi;
import com.example.starter.config.ConfigLoader;
import com.example.starter.db.migration.FlywayMigration;
import com.example.starter.quotes.QuotesRestApi;
import com.example.starter.watchlist.WatchListRestApi;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  public static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) throws Exception {
    System.setProperty(ConfigLoader.SERVER_PORT, "9000");
    var vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> {
      LOG.error("Unhandled: {}", error);
    });

    vertx.deployVerticle(new MainVerticle())
      .onFailure(err -> LOG.error("Failed to deploy: ", err))
      .onSuccess(id -> {
        LOG.info("Deployed {} with id {}", MainVerticle.class.getSimpleName(), id);
        }
      );
//      , ar -> {
//      if (ar.failed()) {
//        LOG.error("Failed to deploy: {}", ar.cause());
//        return;
//      }
//      LOG.info("Deployed MainVerticle! Redeployed!");
//    });
//
//    vertx.setPeriodic(500, id -> {
//      LOG.debug("deploying... updated!");
//    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx
      // Version Verticle
      .deployVerticle(VersionInfoVeticle.class.getName())
      .onFailure(startPromise::fail)
      .onSuccess(id -> {
        LOG.info("Deployed {} with id {}", VersionInfoVeticle.class.getSimpleName(), id);
      })
      // Flyway Migration
      .compose(next -> migrateDatabase())
      .onFailure(startPromise::fail)
      .onSuccess(id -> LOG.info("Migrated db schema to latest version!"))
      // Rest API Verticle
      .compose(next -> deployRestApiVerticle(startPromise));

  }

  private Future<Void> migrateDatabase() {
    return ConfigLoader.load(vertx)
      .compose(config -> {
        return FlywayMigration.migrate(vertx, config.getDbConfig());
      });
  }

  private Future<String> deployRestApiVerticle(Promise<Void> startPromise) {
    vertx.deployVerticle(RestApiVerticle.class.getName(),
        new DeploymentOptions().setInstances(processors()))
      .onFailure(startPromise::fail)
      .onComplete(id -> {
        LOG.info("Deployed {} with id {}", RestApiVerticle.class.getSimpleName(), id);
        startPromise.complete();
        }
      );
    return Future.succeededFuture("Deployed " + RestApiVerticle.class.getSimpleName());
  }

  private int processors() {
    int _processors = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
    LOG.info("Processors: {}", _processors);
    return _processors;
  }

}
