CREATE TABLE assets
(
  SYMBOL VARCHAR PRIMARY KEY
);

CREATE TABLE quotes
(
  bid NUMERIC,
  ask NUMERIC,
  last_price NUMERIC,
  volume NUMERIC,
  asset VARCHAR,
  FOREIGN KEY (asset) REFERENCES assets(SYMBOL),
  CONSTRAINT last_price_is_positive CHECK (last_price > 0),
  CONSTRAINT volume_is_positive_or_zero CHECK (volume >= 0)

);
