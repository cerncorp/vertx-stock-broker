package com.example.starter.watchlist;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WatchListRestApi {


  public static final Logger LOG = LoggerFactory.getLogger(WatchListRestApi.class);
//  public static final String PATH_WATCH_LIST = "/account/watchList";
public static final String PATH_WATCH_LIST_ACCOUNT_ID = "/account/watchList/:accountId";
  public static final String PG_PATH_WATCH_LIST_ACCOUNT_ID = "/pg/account/watchList/:accountId";

  public static void attach(final Router parent, Pool db) {
    final Map<UUID, WatchList> watchListPerAccountList = new HashMap<UUID, WatchList>();

    // GET
    parent.get(PATH_WATCH_LIST_ACCOUNT_ID).handler(new GetWatchListHandler(watchListPerAccountList));

    // PUT
    parent.put(PATH_WATCH_LIST_ACCOUNT_ID).handler(new PutWatchListHandler(watchListPerAccountList));

    // DELETE
    parent.delete(PATH_WATCH_LIST_ACCOUNT_ID).handler(new DeleteWatchListHandler(watchListPerAccountList));

    // PG GET
    parent.get(PG_PATH_WATCH_LIST_ACCOUNT_ID).handler(new GetWatchListFromDatabaseHandler(db));

    // PG PUT
    parent.put(PG_PATH_WATCH_LIST_ACCOUNT_ID).handler(new PutWatchListFromDatabaseHandler(db));
//    .failureHandler(frc -> {
//      Throwable failure = frc.failure();
////      if (failure instanceof PostNotFoundException) {
////        frc.response().setStatusCode(404).end();
////      }
//      frc.response().setStatusCode(500).setStatusMessage("Server internal error:" + failure.getMessage()).end();
//    });

    parent.delete(PG_PATH_WATCH_LIST_ACCOUNT_ID).handler(new DeleteWatchListFromDatabaseHandler(db));

  }

  public static String getAccountId(RoutingContext context) {
    var accountId = context.pathParam("accountId");
    LOG.debug("{} for account {} ", context.normalizedPath(), accountId);
    return accountId;
  }
}
