package com.example.starter;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {

  public static final Logger LOG = LoggerFactory.getLogger(FuturePromiseExample.class);

  @Test
  void promise_success(Vertx vertx, VertxTestContext context) throws Exception {
    final Promise<String> promise = Promise.promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Success, id: {}", id);

//      Future<String> result = promise.future();
//      LOG.debug("future: {}", result.result());
      context.completeNow();
    });
    LOG.debug("End");

  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext context) throws Exception {
    final Promise<String> promise = Promise.promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!"));
      LOG.debug("Failed");
      context.completeNow();
    });
    LOG.debug("End");
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext context) throws Exception {
    final Promise<String> promise = Promise.promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Success, id: {}. Timer done.", id);
    });

    final Future<String> future = promise.future();
    future.onSuccess(result -> {
      LOG.debug("End: result {}", result);
      context.completeNow();
    })
      .onFailure(context::failNow);

  }

  @Test
  void future_failure(Vertx vertx, VertxTestContext context) throws Exception {
    final Promise<String> promise = Promise.promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!"));
      LOG.debug("Failed, id: {}. Timer done.", id);
    });

    final Future<String> future = promise.future();
    future.onSuccess(result -> {
        LOG.debug("End: result {}", result);
        context.completeNow();
      })
      .onFailure(result -> {
        LOG.debug("End: result {}", result);
        context.completeNow();
      });
  }

  @Test
  void future_coordination(Vertx vertx, VertxTestContext context) throws Exception {
    vertx.createHttpServer()
      .requestHandler(request -> {
        LOG.debug("request: {}", request);
      })
      .listen(10_000)
      .compose(server -> {
        LOG.debug("Another Task");
        return Future.succeededFuture(server);
      })
      .compose(server -> {
        LOG.debug("Even more");
        return Future.succeededFuture(server);
      })
      .onFailure(context::failNow)
      .onSuccess(server -> {
        LOG.debug("Server started on port: {}", server.actualPort());
        context.completeNow();
      });
  }


  @Test
  void future_composition(Vertx vertx, VertxTestContext context) throws Exception {
    Promise one = Promise.<String>promise();
    Promise two = Promise.<String>promise();
    Promise three = Promise.<String>promise();

    Future futureOne = one.future();
    Future futureTwo = two.future();
    Future futureThree = three.future();

    CompositeFuture.all(futureOne, futureTwo, futureThree)
      .onSuccess(result -> {
        LOG.debug("result: " + result);
        context.completeNow();
      })
      .onFailure(context::failNow);

    // Complete Future
    vertx.setTimer(500, id -> {
      one.complete();
      two.complete();
      three.complete();
//      three.fail("Three failed");
    });

  }
}
