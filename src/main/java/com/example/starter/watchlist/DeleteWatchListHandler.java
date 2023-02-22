package com.example.starter.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

import static com.example.starter.watchlist.WatchListRestApi.getAccountId;

public class DeleteWatchListHandler implements Handler<RoutingContext> {
  public static final Logger LOG = LoggerFactory.getLogger(DeleteWatchListHandler.class);
  private final Map<UUID, WatchList> watchListPerAccountList;

  public DeleteWatchListHandler(Map<UUID, WatchList> watchListPerAccountList) {
    this.watchListPerAccountList = watchListPerAccountList;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = getAccountId(context);
    final WatchList deleted = watchListPerAccountList.remove(UUID.fromString(accountId));

    LOG.debug("Deleted {}, Remaining: {}", deleted, watchListPerAccountList.values());

    context.response()
      .end(deleted.toJsonObject().toBuffer());
  }
}
