CREATE TABLE watchlist
(
  account_id VARCHAR,
  asset VARCHAR,
  FOREIGN KEY (asset) REFERENCES broker.assets(symbol),
  PRIMARY KEY (account_id, asset)
);
