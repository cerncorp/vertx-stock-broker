package com.example.starter.watchlist;

import com.example.starter.db.DbResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class PutWatchListFromDatabaseHandler implements Handler<RoutingContext> {

  public static final Logger LOG = LoggerFactory.getLogger(PutWatchListFromDatabaseHandler.class);
  private final Pool db;

  public PutWatchListFromDatabaseHandler(final Pool db) {
    this.db = db;
  }


  @Override
  public void handle(RoutingContext context) {
    var accountId = WatchListRestApi.getAccountId(context);

    var json = context.getBodyAsJson();

    var watchList = json.mapTo(WatchList.class);

    watchList.getAssets().forEach(asset -> {

      final HashMap<String, Object> params = new HashMap<>();
      params.put("account_id", accountId);
      params.put("asset", asset.getSymbol());
      SqlTemplate.forUpdate(db,
          "INSERT INTO broker.watchlist VALUES (#{account_id}, #{asset})")
        .execute(params)
        .onFailure(DbResponse.errorHandler(context, "Failed to insert into watchlist"))
        .onSuccess(result -> {
          if (!context.response().ended()) {
            context.response()
              .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
              .end();
          }
        });


    });


  }
}
