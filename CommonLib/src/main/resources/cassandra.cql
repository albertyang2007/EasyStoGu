#Keyspace
CREATE KEYSPACE IF NOT EXISTS EasyStoGu WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

#ind_macd table
CREATE TABLE IF NOT EXISTS EasyStoGu.ind_macd
(
  stockid text,
  date text,
  dif double,
  dea double,
  macd double,
  PRIMARY KEY (stockid, date)
);

#ind_kdj table
CREATE TABLE IF NOT EXISTS EasyStoGu.ind_kdj
(
  stockid text,
  date text,
  k double,
  d double,
  j double,
  rsv double,
  PRIMARY KEY (stockid, date)
);

#ind_boll table
CREATE TABLE IF NOT EXISTS EasyStoGu.ind_boll
(
  stockid text,
  date text,
  mb double,
  up double,
  dn double,
  PRIMARY KEY (stockid, date)
);

#ind_shenxian table
CREATE TABLE IF NOT EXISTS EasyStoGu.ind_shenxian
(
  stockid text,
  date text,
  h1 double,
  h2 double,
  h3 double,
  PRIMARY KEY (stockid, date)
);

#ind_qsdd table
CREATE TABLE IF NOT EXISTS EasyStoGu.ind_qsdd
(
  stockid text,
  date text,
  lonterm double,
  shoterm double,
  midterm double,
  PRIMARY KEY (stockid, date)
);

#ind_wr table
CREATE TABLE IF NOT EXISTS EasyStoGu.ind_wr
(
  stockid text,
  date text,
  lonterm double,
  shoterm double,
  midterm double,
  PRIMARY KEY (stockid, date)
);
