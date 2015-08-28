CREATE INDEX index_id_for_stockprice
  ON stockprice
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_stockprice
  ON stockprice
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_id_for_week_stockprice
  ON week_stockprice
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_week_stockprice
  ON week_stockprice
  USING hash
  (date COLLATE pg_catalog."default");      
  
CREATE INDEX index_id_for_ind_macd
  ON ind_macd
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_macd
  ON ind_macd
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_kdj
  ON ind_kdj
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_kdj
  ON ind_kdj
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_boll
  ON ind_boll
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_boll
  ON ind_boll
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_mai1mai2
  ON ind_mai1mai2
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_mai1mai2
  ON ind_mai1mai2
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_shenxian
  ON ind_shenxian
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_shenxian
  ON ind_shenxian
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_xueshi2
  ON ind_xueshi2
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_xueshi2
  ON ind_xueshi2
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_yimengbs
  ON ind_yimengbs
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_yimengbs
  ON ind_yimengbs
  USING hash
  (date COLLATE pg_catalog."default");  

CREATE INDEX index_id_for_ind_zhulijinchu
  ON ind_zhulijinchu
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_zhulijinchu
  ON ind_zhulijinchu
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_week_boll
  ON ind_week_boll
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_week_boll
  ON ind_week_boll
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_week_kdj
  ON ind_week_kdj
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_week_kdj
  ON ind_week_kdj
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_week_macd
  ON ind_week_macd
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_week_macd
  ON ind_week_macd
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_week_mai1mai2
  ON ind_week_mai1mai2
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_week_mai1mai2
  ON ind_week_mai1mai2
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_week_shenxian
  ON ind_week_shenxian
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_week_shenxian
  ON ind_week_shenxian
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_week_xueshi2
  ON ind_week_xueshi2
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_week_xueshi2
  ON ind_week_xueshi2
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_week_yimengbs
  ON ind_week_yimengbs
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_week_yimengbs
  ON ind_week_yimengbs
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_ind_week_zhulijinchu
  ON ind_week_zhulijinchu
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_ind_week_zhulijinchu
  ON ind_week_zhulijinchu
  USING hash
  (date COLLATE pg_catalog."default");  
  
CREATE INDEX index_id_for_zijinliu
  ON zijinliu
  USING hash
  (date COLLATE pg_catalog."default");
  
CREATE INDEX index_date_for_zijinliu
  ON zijinliu
  USING hash
  (date COLLATE pg_catalog."default");  
  
                                  