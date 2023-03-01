package com.example.starter.watchlist;

import com.example.starter.db.DbResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class DeleteWatchListFromDatabaseHandler implements Handler<RoutingContext> {
  private final Pool db;
  public static final Logger LOG = LoggerFactory.getLogger(DeleteWatchListFromDatabaseHandler.class);

  public DeleteWatchListFromDatabaseHandler(final Pool db) {
    this.db = db;
  }

  @Override
  public void handle(RoutingContext context) {

    var accountId = WatchListRestApi.getAccountId(context);

    SqlTemplate.forUpdate(db,
      "DELETE FROM broker.watchlist WHERE account_id = #{account_id}")
      .execute(Collections.singletonMap("account_id", accountId))
      .onFailure(DbResponse.errorHandler(context, "Failed to delete watchlist for account_id: " + accountId))
      .onSuccess(result -> {
        LOG.debug("Delete {} rows for accountId: {}", result.rowCount(), accountId);
        context.response()
          .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
          .end();
      });
  }
}
