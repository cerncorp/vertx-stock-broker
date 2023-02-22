package com.example.starter.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.example.starter.watchlist.WatchListRestApi.getAccountId;

public class GetWatchListHandler implements Handler<RoutingContext> {
  public static final Logger LOG = LoggerFactory.getLogger(GetWatchListHandler.class);
  private final Map<UUID, WatchList> watchListPerAccountList;

  public GetWatchListHandler(Map<UUID, WatchList> watchListPerAccountList) {
    this.watchListPerAccountList = watchListPerAccountList;
  }

  @Override
  public void handle(RoutingContext context) {

    String accountId = getAccountId(context);
    LOG.debug("{} for account {}", context.normalizedPath(), accountId);
    var watchListPerAccount = Optional.ofNullable(watchListPerAccountList.get(UUID.fromString(accountId)));
    if (watchListPerAccount.isEmpty()) {
      context.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(
          new JsonObject()
            .put("message", "watchList for accountId " + accountId + " does not exist!")
            .put("path", context.normalizedPath())
            .toBuffer()
        );
      return;
    }

    context.response()
      .setStatusCode(HttpResponseStatus.OK.code())
      .end(watchListPerAccount.get().toJsonObject().toBuffer());
  }
}
