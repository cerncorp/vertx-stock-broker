package com.example.starter.assets;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.starter.db.DbResponse.errorHandler;


public class GetAssetsFromDatabaseHandler implements Handler<RoutingContext> {
  public static final Logger LOG = LoggerFactory.getLogger(GetAssetsFromDatabaseHandler.class);
  private final Pool db;

  public GetAssetsFromDatabaseHandler(Pool db) {
    this.db = db;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    db.query("SELECT a.symbol FROM broker.assets a")
      .execute()
      .onFailure(errorHandler(routingContext, "Failed to get assets from db !"))
      .onSuccess(result -> {
        var response = new JsonArray();
        result.forEach(row -> {
          response.add(row.getValue("symbol"));
        });
        LOG.info("Path {} responds with {}", routingContext.normalizedPath(), response.encode() );
        routingContext.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .end(response.toBuffer());
      })



    ;
  }

}
