package com.example.starter.assets;

import com.example.starter.AbstractRestApiTest;
import com.example.starter.MainVerticle;
import com.example.starter.watchlist.WatchList;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
class WatchListRestApiTest extends AbstractRestApiTest {
  public static final Logger LOG = LoggerFactory.getLogger(WatchListRestApiTest.class);

//
//  @Test
//  void returns_watchList_for_account(Vertx vertx, VertxTestContext testContext) {
//    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
//
//    var accountId = randomUUID().toString();
//    client.get("/account/watchList/" + accountId)
//      .send()
//      .onComplete(testContext.succeeding(response -> {
//        JsonObject json = response.bodyAsJsonObject();
//        LOG.info("JSON response: {}", json);
//        assertEquals(404, response.statusCode());
//        testContext.completeNow();
//      }));
//  }

  private static JsonObject getBody() {
    return new WatchList(Arrays.asList(new Asset("AMZN"), new Asset("TSLA"))).toJsonObject();
  }

  @Test
  void adds_watchList_for_account(Vertx vertx, VertxTestContext testContext) {
    WebClient client = webClient(vertx);

    var accountId = randomUUID().toString();
    client.put("/account/watchList/" + accountId)
      .sendJsonObject(getBody())
      .onComplete(testContext.succeeding(response -> {
        JsonObject json = response.bodyAsJsonObject();
        LOG.info("JSON response: {}", json);
        assertEquals(200, response.statusCode());
        assertEquals("{\"assets\":[{\"symbol\":\"AMZN\"},{\"symbol\":\"TSLA\"}]}", json.encode());
      }))
      .compose(next -> {
        client.get("/account/watchList/" + accountId)
          .send()
          .onComplete(testContext.succeeding(response -> {
            var json = response.bodyAsJsonObject();
            LOG.info("Response GET: " + json);
            assertEquals("{\"assets\":[{\"symbol\":\"AMZN\"},{\"symbol\":\"TSLA\"}]}", json.encode());
            assertEquals(200, response.statusCode());
            testContext.completeNow();
          }));
        return Future.succeededFuture();
      });
  }

  private static WebClient webClient(Vertx vertx) {
    return WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
  }

  @Test
  void adds_and_deletes_watchList_for_account(Vertx vertx, VertxTestContext testContext) {
    WebClient client = webClient(vertx);

    var accountId = randomUUID().toString();
    client.put("/account/watchList/" + accountId)
      .sendJsonObject(getBody())
      .onComplete(testContext.succeeding(response -> {
        JsonObject json = response.bodyAsJsonObject();
        LOG.info("JSON response: {}", json);
        assertEquals(200, response.statusCode());
        assertEquals("{\"assets\":[{\"symbol\":\"AMZN\"},{\"symbol\":\"TSLA\"}]}", json.encode());
      }))
      .compose(next -> {
        client.delete("/account/watchList/" + accountId)
          .send()
          .onComplete(testContext.succeeding(response -> {
            var json = response.bodyAsJsonObject();
            LOG.info("Response DELETE: " + json);
            assertEquals("{\"assets\":[{\"symbol\":\"AMZN\"},{\"symbol\":\"TSLA\"}]}", json.encode());
            assertEquals(200, response.statusCode());
            testContext.completeNow();
          }));
        return Future.succeededFuture();
      });
  }

}
