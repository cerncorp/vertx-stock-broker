package com.example.starter.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class WatchListRestApi {


  public static final Logger LOG = LoggerFactory.getLogger(WatchListRestApi.class);
//  public static final String PATH_WATCH_LIST = "/account/watchList";
  public static final String PATH_WATCH_LIST_ACCOUNT_ID = "/account/watchList/:accountId";

  public static void attach(final Router parent) {
    final Map<UUID, WatchList> watchListPerAccountList = new HashMap<UUID, WatchList>();

    // GET
    parent.get(PATH_WATCH_LIST_ACCOUNT_ID).handler(new GetWatchListHandler(watchListPerAccountList));

    // PUT
    parent.put(PATH_WATCH_LIST_ACCOUNT_ID).handler(new PutWatchListHandler(watchListPerAccountList));

    // DELETE
    parent.delete(PATH_WATCH_LIST_ACCOUNT_ID).handler(new DeleteWatchListHandler(watchListPerAccountList));
  }

  public static String getAccountId(RoutingContext context) {
    var accountId = context.pathParam("accountId");
    LOG.debug("{} for account {} ", context.normalizedPath(), accountId);
    return accountId;
  }
}
