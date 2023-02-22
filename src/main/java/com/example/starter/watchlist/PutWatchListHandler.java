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

public class PutWatchListHandler implements Handler<RoutingContext> {
  public static final Logger LOG = LoggerFactory.getLogger(PutWatchListHandler.class);
  private final Map<UUID, WatchList> watchListPerAccountList;

  public PutWatchListHandler(Map<UUID, WatchList> watchListPerAccountList) {
    this.watchListPerAccountList = watchListPerAccountList;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = getAccountId(context);

    var json = context.getBodyAsJson();
    var watchList = json.mapTo(WatchList.class);

    var putWatchList = watchListPerAccountList.put(UUID.fromString(accountId), watchList);
    if (putWatchList == null) {
      LOG.debug("Put {} to watchListPerAccountList", json);
    }

    context.response()
      .end(json.toBuffer());
  }
}
