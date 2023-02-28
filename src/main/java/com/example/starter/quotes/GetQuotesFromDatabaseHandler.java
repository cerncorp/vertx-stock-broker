package com.example.starter.quotes;

import com.example.starter.db.DbResponse;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

import static com.example.starter.db.DbResponse.errorHandler;

public class GetQuotesFromDatabaseHandler implements Handler<RoutingContext> {

  private final Pool db;
  public static final Logger LOG = LoggerFactory.getLogger(GetQuotesFromDatabaseHandler.class);

  public GetQuotesFromDatabaseHandler(Pool db) {
    this.db = db;
  }

  @Override
  public void handle(RoutingContext context) {

    final String assetParam = context.pathParam("asset");

    LOG.debug("Asset parameter: {}", assetParam);

    SqlTemplate.forQuery(db,
      "SELECT q.asset, q.bid, q.ask, q.last_price, q.volume from broker.quotes q WHERE q.asset=#{asset}")
      .mapTo(QuoteEntity.class) // yeah
      .execute(Collections.singletonMap("asset", assetParam))
      .onFailure(errorHandler(context, "Failed to get quotes for assets "+assetParam+" from db !"))
      .onSuccess(quotes -> {
        if (!quotes.iterator().hasNext()) {
          DbResponse.notFound(context, "quote for asset " + assetParam + " does not exist!");
          return;
        }
        final JsonObject response = quotes.iterator().next().toJsonObject();
        LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
        context.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .end(response.toBuffer());
      });




  }

}
