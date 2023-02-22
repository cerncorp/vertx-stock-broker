package com.example.starter.assets;

import com.example.starter.AbstractRestApiTest;
import com.example.starter.MainVerticle;
import com.example.starter.config.ConfigLoader;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.uritemplate.UriTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.starter.assets.AssetsRestApi.PATH_ASSETS;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
class AssetsRestApiTest extends AbstractRestApiTest {
  public static final Logger LOG = LoggerFactory.getLogger(AssetsRestApiTest.class);

  @Test
  void returns_all_assets(Vertx vertx, VertxTestContext testContext) {
    WebClient client = webClient(vertx);

    client.get(PATH_ASSETS)
      .send()
      .onComplete(testContext.succeeding(response -> {
        JsonArray json = response.bodyAsJsonArray();
        LOG.info("JSON response: {}", json);
        assertEquals("[{\"symbol\":\"AAPL\"},{\"symbol\":\"AMZN\"},{\"symbol\":\"NFLX\"},{\"symbol\":\"TSLA\"},{\"symbol\":\"NEUR\"}]", json.encode());
        assertEquals(200, response.statusCode());
        assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
        assertEquals("my-value", response.getHeader("my-header"));
        testContext.completeNow();
      }));


  }

  private static WebClient webClient(Vertx vertx) {
    return WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
  }
}
