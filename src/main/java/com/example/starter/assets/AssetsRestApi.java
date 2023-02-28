package com.example.starter.assets;

import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;

import java.util.Arrays;
import java.util.List;

public class AssetsRestApi {

//  public static final Logger LOG = LoggerFactory.getLogger(AssetsRestApi.class);
  public static final String PATH_ASSETS = "/assets";

  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "NFLX", "TSLA", "NEUR");
  public static final String PG_PATH_ASSETS = "/pg/assets";

  public static void attach(Router parent, Pool db) {

    parent.get(PATH_ASSETS).handler(new GetAssetsHandler());
    parent.get(PG_PATH_ASSETS).handler(new GetAssetsFromDatabaseHandler(db));
  }
}
