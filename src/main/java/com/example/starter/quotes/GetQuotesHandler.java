package com.example.starter.quotes;

import com.example.starter.db.DbResponse;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class GetQuotesHandler implements Handler<RoutingContext> {
  public static final Logger LOG = LoggerFactory.getLogger(GetQuotesHandler.class);
  private final Map<String, Quote> cachedQuotes;

  public GetQuotesHandler(Map<String, Quote> cachedQuotes) {
    this.cachedQuotes = cachedQuotes;
  }

  @Override
  public void handle(RoutingContext context) {
    final String assetParam = context.pathParam("asset");
    LOG.debug("Asset parameter: " + assetParam);

    final Optional<Quote> quote = Optional.ofNullable(cachedQuotes.get(assetParam));
    if (quote.isEmpty()) {
      DbResponse.notFound(context, "quote for asset " + assetParam + " does not exist!");
      return;
    }

    final JsonObject response = quote.get().toJsonObject();

    LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
    context.response().end(response.toBuffer());
  }
}
