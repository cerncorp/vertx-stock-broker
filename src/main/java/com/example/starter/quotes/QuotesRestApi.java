package com.example.starter.quotes;

import com.example.starter.assets.Asset;
import com.example.starter.assets.AssetsRestApi;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.uritemplate.UriTemplate;
import io.vertx.uritemplate.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


public class QuotesRestApi {

  public static final Logger LOG = LoggerFactory.getLogger(QuotesRestApi.class);
  public static final String PATH_QUOTES_ASSET = "/quotes/{asset}";

  public static void attach(Router parent) {
    final Map<String, Quote> cachedQuotes = new HashMap<String, Quote>();
    AssetsRestApi.ASSETS.forEach(symbol -> {
      cachedQuotes.put(symbol, initRandomQuote(symbol));
    });

    parent.get("/quotes/:asset")
      .handler(new GetQuotesHandler(cachedQuotes));
  }

  private static String getAssetPath() {
    return UriTemplate.of(PATH_QUOTES_ASSET)
      .expandToString(Variables.variables()
        .set("asset", ":asset"));
  }

  private static Quote initRandomQuote(String assetParam) {
    return Quote.builder()
      .asset(new Asset(assetParam))
      .volume(randomValue())
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100  ));
  }
}
