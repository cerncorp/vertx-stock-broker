package com.example.starter.assets;

import com.example.starter.AbstractRestApiTest;
import com.example.starter.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.uritemplate.UriTemplate;
import io.vertx.uritemplate.Variables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.starter.quotes.QuotesRestApi.PATH_QUOTES_ASSET;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
class QuotesRestApiTest extends AbstractRestApiTest {
  public static final Logger LOG = LoggerFactory.getLogger(QuotesRestApiTest.class);

  @Test
  void returns_quote_for_asset(Vertx vertx, VertxTestContext testContext) {
    WebClient client = webClient(vertx);

    client.get(
      UriTemplate.of(PATH_QUOTES_ASSET)
        .expandToString(Variables.variables().set("asset", "AMZN"))
        )
      .send()
      .onComplete(testContext.succeeding(response -> {
        JsonObject json = response.bodyAsJsonObject();
        LOG.info("JSON response: {}", json);
        assertEquals("{\"symbol\":\"AMZN\"}", json.getJsonObject("asset").encode());
        assertEquals(200, response.statusCode());
        testContext.completeNow();
      }));
  }

  @Test
  void returns_quote_for_unknown_asset(Vertx vertx, VertxTestContext testContext) {
    WebClient client = webClient(vertx);

    LOG.debug(UriTemplate.of(PATH_QUOTES_ASSET)
      .expandToString(Variables.variables().set("asset", "UNKNOWN")));

    client.get(
        UriTemplate.of(PATH_QUOTES_ASSET)
          .expandToString(Variables.variables().set("asset", "UNKNOWN"))
      )
      .send()
      .onComplete(testContext.succeeding(response -> {
        JsonObject json = response.bodyAsJsonObject();
        LOG.info("JSON response: {}", json);
        assertEquals(404, response.statusCode());
        assertEquals("quote for asset UNKNOWN does not exist!", json.getString("message"));
        testContext.completeNow();
      }));
  }

  private static WebClient webClient(Vertx vertx) {
    return WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
  }
}
