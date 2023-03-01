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
import java.util.Map;
import java.util.stream.Collectors;

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

//    watchList.getAssets().forEach(asset -> {

    var parameterBatch = watchList.getAssets().stream()
      .map(asset -> {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("account_id", accountId);
        parameters.put("asset", asset.getSymbol());
        return parameters;
      }).collect(Collectors.toList());

    // Only adding is possible -> Entries for watch list are never removed
    SqlTemplate.forUpdate(db,
        "INSERT INTO broker.watchlist VALUES (#{account_id}, #{asset})"
       + " ON CONFLICT (account_id, asset) DO NOTHING")
//      .execute(params) // execute each
      .executeBatch(parameterBatch)
      .onFailure(DbResponse.errorHandler(context, "Failed to insert into watchlist"))
      .onSuccess(result -> {
//        if (!context.response().ended()) { // execute each
          context.response()
            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
            .end();
//        }
      });

//    });

  }
}
