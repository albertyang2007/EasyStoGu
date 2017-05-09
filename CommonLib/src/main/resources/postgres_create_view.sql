-- View: "luzao_phaseII"

-- DROP VIEW "luzao_phaseII";

CREATE OR REPLACE VIEW "luzao_phaseII" AS 
 SELECT ind_ma.stockid,
    ind_ma.date,
    ind_ma.close,
    ind_ma.ma19,
    ind_ma.ma43,
    ind_ma.ma86
   FROM ind_ma
  WHERE ind_ma.date = (( SELECT stockprice_latest_date.date
           FROM stockprice_latest_date)) AND ind_ma.close > ind_ma.ma19 AND ind_ma.ma43 > ind_ma.ma19 AND ind_ma.ma86 > ind_ma.ma43
  ORDER BY ind_ma.stockid;

ALTER TABLE "luzao_phaseII"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII" TO public;
GRANT ALL ON TABLE "luzao_phaseII" TO postgres;

-- View: "luzao_phaseIII"

-- DROP VIEW "luzao_phaseIII";

CREATE OR REPLACE VIEW "luzao_phaseIII" AS 
 SELECT ind_ma.stockid,
    ind_ma.date,
    ind_ma.close,
    ind_ma.ma19,
    ind_ma.ma43,
    ind_ma.ma86
   FROM ind_ma
  WHERE ind_ma.date = (( SELECT stockprice_latest_date.date
           FROM stockprice_latest_date)) AND ind_ma.ma43 < ind_ma.ma19 AND ind_ma.ma86 > ind_ma.ma43
  ORDER BY ind_ma.stockid;

ALTER TABLE "luzao_phaseIII"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII" TO public;
GRANT ALL ON TABLE "luzao_phaseIII" TO postgres;


-- View: "luzao_phaseIII_wr_all_ind_same"

-- DROP VIEW "luzao_phaseIII_wr_all_ind_same";

CREATE OR REPLACE VIEW "luzao_phaseIII_wr_all_ind_same" AS 
 SELECT wr_all_ind_same.stockid,
    wr_all_ind_same.date,
    wr_all_ind_same.shoterm,
    wr_all_ind_same.midterm,
    wr_all_ind_same.lonterm
   FROM wr_all_ind_same
  WHERE (( SELECT count(1) AS num
           FROM "luzao_phaseIII"
          WHERE "luzao_phaseIII".stockid = wr_all_ind_same.stockid)) = 1;

ALTER TABLE "luzao_phaseIII_wr_all_ind_same"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII_wr_all_ind_same" TO public;
GRANT ALL ON TABLE "luzao_phaseIII_wr_all_ind_same" TO postgres;
COMMENT ON VIEW "luzao_phaseIII_wr_all_ind_same"
  IS 'Luzao: phaseIII, chigu
WR:short Term==Middle Term == Long Term';


-- View: "luzao_phaseIII_wr_all_ind_same_Details"

-- DROP VIEW "luzao_phaseIII_wr_all_ind_same_Details";

CREATE OR REPLACE VIEW "luzao_phaseIII_wr_all_ind_same_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    "luzao_phaseIII_wr_all_ind_same".date,
    round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) AS pricediff,
    "luzao_phaseIII_wr_all_ind_same".shoterm,
    "luzao_phaseIII_wr_all_ind_same".midterm,
    "luzao_phaseIII_wr_all_ind_same".lonterm
   FROM qian_fuquan_stockprice,
    company_info,
    "luzao_phaseIII_wr_all_ind_same"
  WHERE qian_fuquan_stockprice.date = "luzao_phaseIII_wr_all_ind_same".date AND qian_fuquan_stockprice.stockid = "luzao_phaseIII_wr_all_ind_same".stockid AND company_info.stockid = qian_fuquan_stockprice.stockid
  ORDER BY round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) DESC;

ALTER TABLE "luzao_phaseIII_wr_all_ind_same_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII_wr_all_ind_same_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseIII_wr_all_ind_same_Details" TO postgres;


-- View: "luzao_phaseIII_wr_midTerm_lonTerm_same"

-- DROP VIEW "luzao_phaseIII_wr_midTerm_lonTerm_same";

CREATE OR REPLACE VIEW "luzao_phaseIII_wr_midTerm_lonTerm_same" AS 
 SELECT "wr_midTerm_lonTerm_same".stockid,
    "wr_midTerm_lonTerm_same".date,
    "wr_midTerm_lonTerm_same".shoterm,
    "wr_midTerm_lonTerm_same".midterm,
    "wr_midTerm_lonTerm_same".lonterm
   FROM "wr_midTerm_lonTerm_same"
  WHERE (( SELECT count(1) AS num
           FROM "luzao_phaseIII"
          WHERE "luzao_phaseIII".stockid = "wr_midTerm_lonTerm_same".stockid)) = 1;

ALTER TABLE "luzao_phaseIII_wr_midTerm_lonTerm_same"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII_wr_midTerm_lonTerm_same" TO public;
GRANT ALL ON TABLE "luzao_phaseIII_wr_midTerm_lonTerm_same" TO postgres;


-- View: "luzao_phaseIII_wr_midTerm_lonTerm_same_Details"

-- DROP VIEW "luzao_phaseIII_wr_midTerm_lonTerm_same_Details";

CREATE OR REPLACE VIEW "luzao_phaseIII_wr_midTerm_lonTerm_same_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    "luzao_phaseIII_wr_midTerm_lonTerm_same".date,
    round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) AS pricediff,
    "luzao_phaseIII_wr_midTerm_lonTerm_same".shoterm,
    "luzao_phaseIII_wr_midTerm_lonTerm_same".midterm,
    "luzao_phaseIII_wr_midTerm_lonTerm_same".lonterm
   FROM qian_fuquan_stockprice,
    company_info,
    "luzao_phaseIII_wr_midTerm_lonTerm_same"
  WHERE qian_fuquan_stockprice.date = "luzao_phaseIII_wr_midTerm_lonTerm_same".date AND qian_fuquan_stockprice.stockid = "luzao_phaseIII_wr_midTerm_lonTerm_same".stockid AND company_info.stockid = qian_fuquan_stockprice.stockid
  ORDER BY round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) DESC;

ALTER TABLE "luzao_phaseIII_wr_midTerm_lonTerm_same_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII_wr_midTerm_lonTerm_same_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseIII_wr_midTerm_lonTerm_same_Details" TO postgres;


-- View: "luzao_phaseIII_zijinliu_3_days_top300"

-- DROP VIEW "luzao_phaseIII_zijinliu_3_days_top300";

CREATE OR REPLACE VIEW "luzao_phaseIII_zijinliu_3_days_top300" AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT "luzao_phaseIII_zijinliu_top300".stockid,
                    "luzao_phaseIII_zijinliu_top300".date
                   FROM stockprice_latest_3_date,
                    "luzao_phaseIII_zijinliu_top300"
                  WHERE stockprice_latest_3_date.date = "luzao_phaseIII_zijinliu_top300".date
                  ORDER BY "luzao_phaseIII_zijinliu_top300".stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count = 3;

ALTER TABLE "luzao_phaseIII_zijinliu_3_days_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_3_days_top300" TO public;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_3_days_top300" TO postgres;
COMMENT ON VIEW "luzao_phaseIII_zijinliu_3_days_top300"
  IS '鲁兆持股，连续3天以上资金流入前300名';

  
  -- View: "luzao_phaseIII_zijinliu_3_days_top300_Details"

-- DROP VIEW "luzao_phaseIII_zijinliu_3_days_top300_Details";

CREATE OR REPLACE VIEW "luzao_phaseIII_zijinliu_3_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM "luzao_phaseIII_zijinliu_3_days_top300",
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = "luzao_phaseIII_zijinliu_3_days_top300".stockid;

ALTER TABLE "luzao_phaseIII_zijinliu_3_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_3_days_top300_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_3_days_top300_Details" TO postgres;
COMMENT ON VIEW "luzao_phaseIII_zijinliu_3_days_top300_Details"
  IS '鲁兆持股，连续3天以上资金流入前300名';

  
  -- View: "luzao_phaseIII_zijinliu_3_of_5_days_top300"

-- DROP VIEW "luzao_phaseIII_zijinliu_3_of_5_days_top300";

CREATE OR REPLACE VIEW "luzao_phaseIII_zijinliu_3_of_5_days_top300" AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT "luzao_phaseIII_zijinliu_top300".stockid,
                    "luzao_phaseIII_zijinliu_top300".date
                   FROM stockprice_latest_5_date,
                    "luzao_phaseIII_zijinliu_top300"
                  WHERE stockprice_latest_5_date.date = "luzao_phaseIII_zijinliu_top300".date
                  ORDER BY "luzao_phaseIII_zijinliu_top300".stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count >= 3;

ALTER TABLE "luzao_phaseIII_zijinliu_3_of_5_days_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_3_of_5_days_top300" TO public;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_3_of_5_days_top300" TO postgres;
COMMENT ON VIEW "luzao_phaseIII_zijinliu_3_of_5_days_top300"
  IS '鲁兆持股, 5天中有3天资金排名前300';

  
  -- View: "luzao_phaseIII_zijinliu_3_of_5_days_top300_Details"

-- DROP VIEW "luzao_phaseIII_zijinliu_3_of_5_days_top300_Details";

CREATE OR REPLACE VIEW "luzao_phaseIII_zijinliu_3_of_5_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM "luzao_phaseIII_zijinliu_3_of_5_days_top300",
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = "luzao_phaseIII_zijinliu_3_of_5_days_top300".stockid;

ALTER TABLE "luzao_phaseIII_zijinliu_3_of_5_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_3_of_5_days_top300_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_3_of_5_days_top300_Details" TO postgres;
COMMENT ON VIEW "luzao_phaseIII_zijinliu_3_of_5_days_top300_Details"
  IS '鲁兆持股，5天中有3天以上资金排名前300';

  
  -- View: "luzao_phaseIII_zijinliu_top300"

-- DROP VIEW "luzao_phaseIII_zijinliu_top300";

CREATE OR REPLACE VIEW "luzao_phaseIII_zijinliu_top300" AS 
 SELECT ind_ma.stockid,
    ind_ma.date,
    zijinliu.rate
   FROM ind_ma,
    zijinliu
  WHERE ind_ma.date = zijinliu.date AND ind_ma.stockid = zijinliu.stockid AND zijinliu.rate <= 300 AND zijinliu.majornetin > 0::numeric AND ind_ma.close > ind_ma.ma19 AND ind_ma.ma43 < ind_ma.ma19 AND ind_ma.ma86 > ind_ma.ma43
  ORDER BY zijinliu.rate;

ALTER TABLE "luzao_phaseIII_zijinliu_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_top300" TO public;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_top300" TO postgres;
COMMENT ON VIEW "luzao_phaseIII_zijinliu_top300"
  IS '鲁兆持股，资金流入排名前300名';

  
  -- View: "luzao_phaseIII_zijinliu_top300_Details"

-- DROP VIEW "luzao_phaseIII_zijinliu_top300_Details";

CREATE OR REPLACE VIEW "luzao_phaseIII_zijinliu_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    "luzao_phaseIII_zijinliu_top300".date,
    "luzao_phaseIII_zijinliu_top300".rate
   FROM "luzao_phaseIII_zijinliu_top300",
    company_info
  WHERE company_info.stockid = "luzao_phaseIII_zijinliu_top300".stockid;

ALTER TABLE "luzao_phaseIII_zijinliu_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_top300_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseIII_zijinliu_top300_Details" TO postgres;


-- View: "luzao_phaseII_wr_all_ind_same"

-- DROP VIEW "luzao_phaseII_wr_all_ind_same";

CREATE OR REPLACE VIEW "luzao_phaseII_wr_all_ind_same" AS 
 SELECT wr_all_ind_same.stockid,
    wr_all_ind_same.date,
    wr_all_ind_same.shoterm,
    wr_all_ind_same.midterm,
    wr_all_ind_same.lonterm
   FROM wr_all_ind_same
  WHERE (( SELECT count(1) AS num
           FROM "luzao_phaseII"
          WHERE "luzao_phaseII".stockid = wr_all_ind_same.stockid)) = 1;

ALTER TABLE "luzao_phaseII_wr_all_ind_same"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_wr_all_ind_same" TO public;
GRANT ALL ON TABLE "luzao_phaseII_wr_all_ind_same" TO postgres;
COMMENT ON VIEW "luzao_phaseII_wr_all_ind_same"
  IS 'Luzao: phaseII, jiancang
WR: short Term == Middle Term == Long Term';


-- View: "luzao_phaseII_wr_all_ind_same_Details"

-- DROP VIEW "luzao_phaseII_wr_all_ind_same_Details";

CREATE OR REPLACE VIEW "luzao_phaseII_wr_all_ind_same_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    "luzao_phaseII_wr_all_ind_same".date,
    round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) AS pricediff,
    "luzao_phaseII_wr_all_ind_same".shoterm,
    "luzao_phaseII_wr_all_ind_same".midterm,
    "luzao_phaseII_wr_all_ind_same".lonterm
   FROM qian_fuquan_stockprice,
    company_info,
    "luzao_phaseII_wr_all_ind_same"
  WHERE qian_fuquan_stockprice.date = "luzao_phaseII_wr_all_ind_same".date AND qian_fuquan_stockprice.stockid = "luzao_phaseII_wr_all_ind_same".stockid AND company_info.stockid = qian_fuquan_stockprice.stockid
  ORDER BY round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) DESC;

ALTER TABLE "luzao_phaseII_wr_all_ind_same_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_wr_all_ind_same_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseII_wr_all_ind_same_Details" TO postgres;


-- View: "luzao_phaseII_wr_midTerm_lonTerm_same"

-- DROP VIEW "luzao_phaseII_wr_midTerm_lonTerm_same";

CREATE OR REPLACE VIEW "luzao_phaseII_wr_midTerm_lonTerm_same" AS 
 SELECT "wr_midTerm_lonTerm_same".stockid,
    "wr_midTerm_lonTerm_same".date,
    "wr_midTerm_lonTerm_same".shoterm,
    "wr_midTerm_lonTerm_same".midterm,
    "wr_midTerm_lonTerm_same".lonterm
   FROM "wr_midTerm_lonTerm_same"
  WHERE (( SELECT count(1) AS num
           FROM "luzao_phaseII"
          WHERE "luzao_phaseII".stockid = "wr_midTerm_lonTerm_same".stockid)) = 1;

ALTER TABLE "luzao_phaseII_wr_midTerm_lonTerm_same"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_wr_midTerm_lonTerm_same" TO public;
GRANT ALL ON TABLE "luzao_phaseII_wr_midTerm_lonTerm_same" TO postgres;


-- View: "luzao_phaseII_wr_midTerm_lonTerm_same_Details"

-- DROP VIEW "luzao_phaseII_wr_midTerm_lonTerm_same_Details";

CREATE OR REPLACE VIEW "luzao_phaseII_wr_midTerm_lonTerm_same_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    "luzao_phaseII_wr_midTerm_lonTerm_same".date,
    round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) AS pricediff,
    "luzao_phaseII_wr_midTerm_lonTerm_same".shoterm,
    "luzao_phaseII_wr_midTerm_lonTerm_same".midterm,
    "luzao_phaseII_wr_midTerm_lonTerm_same".lonterm
   FROM qian_fuquan_stockprice,
    company_info,
    "luzao_phaseII_wr_midTerm_lonTerm_same"
  WHERE qian_fuquan_stockprice.date = "luzao_phaseII_wr_midTerm_lonTerm_same".date AND qian_fuquan_stockprice.stockid = "luzao_phaseII_wr_midTerm_lonTerm_same".stockid AND company_info.stockid = qian_fuquan_stockprice.stockid
  ORDER BY round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) DESC;

ALTER TABLE "luzao_phaseII_wr_midTerm_lonTerm_same_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_wr_midTerm_lonTerm_same_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseII_wr_midTerm_lonTerm_same_Details" TO postgres;


-- View: "luzao_phaseII_wr_shoTerm_midTerm_same"

-- DROP VIEW "luzao_phaseII_wr_shoTerm_midTerm_same";

CREATE OR REPLACE VIEW "luzao_phaseII_wr_shoTerm_midTerm_same" AS 
 SELECT "wr_shoTerm_midTerm_same".stockid,
    "wr_shoTerm_midTerm_same".date,
    "wr_shoTerm_midTerm_same".shoterm,
    "wr_shoTerm_midTerm_same".midterm,
    "wr_shoTerm_midTerm_same".lonterm
   FROM "wr_shoTerm_midTerm_same"
  WHERE (( SELECT count(1) AS num
           FROM "luzao_phaseII"
          WHERE "luzao_phaseII".stockid = "wr_shoTerm_midTerm_same".stockid)) = 1;

ALTER TABLE "luzao_phaseII_wr_shoTerm_midTerm_same"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_wr_shoTerm_midTerm_same" TO public;
GRANT ALL ON TABLE "luzao_phaseII_wr_shoTerm_midTerm_same" TO postgres;


-- View: "luzao_phaseII_wr_shoTerm_midTerm_same_Details"

-- DROP VIEW "luzao_phaseII_wr_shoTerm_midTerm_same_Details";

CREATE OR REPLACE VIEW "luzao_phaseII_wr_shoTerm_midTerm_same_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    "luzao_phaseII_wr_shoTerm_midTerm_same".date,
    round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) AS pricediff,
    "luzao_phaseII_wr_shoTerm_midTerm_same".shoterm,
    "luzao_phaseII_wr_shoTerm_midTerm_same".midterm,
    "luzao_phaseII_wr_shoTerm_midTerm_same".lonterm
   FROM qian_fuquan_stockprice,
    company_info,
    "luzao_phaseII_wr_shoTerm_midTerm_same"
  WHERE qian_fuquan_stockprice.date = "luzao_phaseII_wr_shoTerm_midTerm_same".date AND qian_fuquan_stockprice.stockid = "luzao_phaseII_wr_shoTerm_midTerm_same".stockid AND company_info.stockid = qian_fuquan_stockprice.stockid
  ORDER BY round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) DESC;

ALTER TABLE "luzao_phaseII_wr_shoTerm_midTerm_same_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_wr_shoTerm_midTerm_same_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseII_wr_shoTerm_midTerm_same_Details" TO postgres;


-- View: "luzao_phaseII_zijinliu_3_days_top300"

-- DROP VIEW "luzao_phaseII_zijinliu_3_days_top300";

CREATE OR REPLACE VIEW "luzao_phaseII_zijinliu_3_days_top300" AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT "luzao_phaseII_zijinliu_top300".stockid,
                    "luzao_phaseII_zijinliu_top300".date
                   FROM stockprice_latest_3_date,
                    "luzao_phaseII_zijinliu_top300"
                  WHERE stockprice_latest_3_date.date = "luzao_phaseII_zijinliu_top300".date
                  ORDER BY "luzao_phaseII_zijinliu_top300".stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count = 3;

ALTER TABLE "luzao_phaseII_zijinliu_3_days_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_3_days_top300" TO public;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_3_days_top300" TO postgres;
COMMENT ON VIEW "luzao_phaseII_zijinliu_3_days_top300"
  IS '鲁兆建仓，连续3天以上资金流入前300名';

  
  -- View: "luzao_phaseII_zijinliu_3_days_top300_Details"

-- DROP VIEW "luzao_phaseII_zijinliu_3_days_top300_Details";

CREATE OR REPLACE VIEW "luzao_phaseII_zijinliu_3_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM "luzao_phaseII_zijinliu_3_days_top300",
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = "luzao_phaseII_zijinliu_3_days_top300".stockid;

ALTER TABLE "luzao_phaseII_zijinliu_3_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_3_days_top300_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_3_days_top300_Details" TO postgres;
COMMENT ON VIEW "luzao_phaseII_zijinliu_3_days_top300_Details"
  IS '鲁兆建仓，连续3天以上资金流入前300名';

  
  -- View: "luzao_phaseII_zijinliu_3_of_5_days_top300"

-- DROP VIEW "luzao_phaseII_zijinliu_3_of_5_days_top300";

CREATE OR REPLACE VIEW "luzao_phaseII_zijinliu_3_of_5_days_top300" AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT "luzao_phaseII_zijinliu_top300".stockid,
                    "luzao_phaseII_zijinliu_top300".date
                   FROM stockprice_latest_5_date,
                    "luzao_phaseII_zijinliu_top300"
                  WHERE stockprice_latest_5_date.date = "luzao_phaseII_zijinliu_top300".date
                  ORDER BY "luzao_phaseII_zijinliu_top300".stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count >= 3;

ALTER TABLE "luzao_phaseII_zijinliu_3_of_5_days_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_3_of_5_days_top300" TO public;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_3_of_5_days_top300" TO postgres;
COMMENT ON VIEW "luzao_phaseII_zijinliu_3_of_5_days_top300"
  IS '鲁兆建仓，5天有3天以上资金排名前300';

  
  -- View: "luzao_phaseII_zijinliu_3_of_5_days_top300_Details"

-- DROP VIEW "luzao_phaseII_zijinliu_3_of_5_days_top300_Details";

CREATE OR REPLACE VIEW "luzao_phaseII_zijinliu_3_of_5_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM "luzao_phaseII_zijinliu_3_of_5_days_top300",
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = "luzao_phaseII_zijinliu_3_of_5_days_top300".stockid;

ALTER TABLE "luzao_phaseII_zijinliu_3_of_5_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_3_of_5_days_top300_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_3_of_5_days_top300_Details" TO postgres;
COMMENT ON VIEW "luzao_phaseII_zijinliu_3_of_5_days_top300_Details"
  IS '鲁兆建仓，5天有3天资金排名前300';

  
  -- View: "luzao_phaseII_zijinliu_top300"

-- DROP VIEW "luzao_phaseII_zijinliu_top300";

CREATE OR REPLACE VIEW "luzao_phaseII_zijinliu_top300" AS 
 SELECT ind_ma.stockid,
    ind_ma.date,
    zijinliu.rate
   FROM ind_ma,
    zijinliu
  WHERE ind_ma.date = zijinliu.date AND ind_ma.stockid = zijinliu.stockid AND zijinliu.rate <= 300 AND zijinliu.majornetin > 0::numeric AND ind_ma.close > ind_ma.ma19 AND ind_ma.ma43 > ind_ma.ma19 AND ind_ma.ma86 > ind_ma.ma43
  ORDER BY zijinliu.rate;

ALTER TABLE "luzao_phaseII_zijinliu_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_top300" TO public;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_top300" TO postgres;
COMMENT ON VIEW "luzao_phaseII_zijinliu_top300"
  IS '鲁兆建仓，资金流入排名前300名';

  
  -- View: "luzao_phaseII_zijinliu_top300_Details"

-- DROP VIEW "luzao_phaseII_zijinliu_top300_Details";

CREATE OR REPLACE VIEW "luzao_phaseII_zijinliu_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    "luzao_phaseII_zijinliu_top300".date,
    "luzao_phaseII_zijinliu_top300".rate
   FROM "luzao_phaseII_zijinliu_top300",
    company_info
  WHERE company_info.stockid = "luzao_phaseII_zijinliu_top300".stockid;

ALTER TABLE "luzao_phaseII_zijinliu_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_top300_Details" TO public;
GRANT ALL ON TABLE "luzao_phaseII_zijinliu_top300_Details" TO postgres;


-- View: stockprice_all_date

-- DROP VIEW stockprice_all_date;

CREATE OR REPLACE VIEW stockprice_all_date AS 
 SELECT DISTINCT stockprice.date
   FROM stockprice
  ORDER BY stockprice.date DESC;

ALTER TABLE stockprice_all_date
  OWNER TO postgres;
GRANT ALL ON TABLE stockprice_all_date TO public;
GRANT ALL ON TABLE stockprice_all_date TO postgres;
COMMENT ON VIEW stockprice_all_date
  IS '所有交易日期';

  
  -- View: stockprice_latest_10_date

-- DROP VIEW stockprice_latest_10_date;

CREATE OR REPLACE VIEW stockprice_latest_10_date AS 
 SELECT DISTINCT stockprice.date
   FROM stockprice
  ORDER BY stockprice.date DESC
 LIMIT 10;

ALTER TABLE stockprice_latest_10_date
  OWNER TO postgres;
GRANT ALL ON TABLE stockprice_latest_10_date TO public;
GRANT ALL ON TABLE stockprice_latest_10_date TO postgres;
COMMENT ON VIEW stockprice_latest_10_date
  IS '最近10个交易日期';

  
  -- View: stockprice_latest_3_date

-- DROP VIEW stockprice_latest_3_date;

CREATE OR REPLACE VIEW stockprice_latest_3_date AS 
 SELECT DISTINCT stockprice.date
   FROM stockprice
  ORDER BY stockprice.date DESC
 LIMIT 3;

ALTER TABLE stockprice_latest_3_date
  OWNER TO postgres;
GRANT ALL ON TABLE stockprice_latest_3_date TO public;
GRANT ALL ON TABLE stockprice_latest_3_date TO postgres;
COMMENT ON VIEW stockprice_latest_3_date
  IS '最近3个交易日期';

  
  -- View: stockprice_latest_5_date

-- DROP VIEW stockprice_latest_5_date;

CREATE OR REPLACE VIEW stockprice_latest_5_date AS 
 SELECT DISTINCT stockprice.date
   FROM stockprice
  ORDER BY stockprice.date DESC
 LIMIT 5;

ALTER TABLE stockprice_latest_5_date
  OWNER TO postgres;
GRANT ALL ON TABLE stockprice_latest_5_date TO public;
GRANT ALL ON TABLE stockprice_latest_5_date TO postgres;
COMMENT ON VIEW stockprice_latest_5_date
  IS '最近5个交易日期';

  
  -- View: stockprice_latest_date

-- DROP VIEW stockprice_latest_date;

CREATE OR REPLACE VIEW stockprice_latest_date AS 
 SELECT stockprice.date
   FROM stockprice
  ORDER BY stockprice.date DESC
 LIMIT 1;

ALTER TABLE stockprice_latest_date
  OWNER TO postgres;
GRANT ALL ON TABLE stockprice_latest_date TO public;
GRANT ALL ON TABLE stockprice_latest_date TO postgres;
COMMENT ON VIEW stockprice_latest_date
  IS '最新交易日期';

  
  -- View: wr_all_ind_same

-- DROP VIEW wr_all_ind_same;

CREATE OR REPLACE VIEW wr_all_ind_same AS 
 SELECT ind_wr.stockid,
    ind_wr.date,
    ind_wr.shoterm,
    ind_wr.midterm,
    ind_wr.lonterm
   FROM ind_wr
  WHERE ind_wr.date = (( SELECT stockprice.date
           FROM stockprice
          ORDER BY stockprice.date DESC
         LIMIT 1)) AND ind_wr.shoterm = ind_wr.midterm AND ind_wr.midterm = ind_wr.lonterm
  ORDER BY ind_wr.stockid;

ALTER TABLE wr_all_ind_same
  OWNER TO postgres;
GRANT ALL ON TABLE wr_all_ind_same TO public;
GRANT ALL ON TABLE wr_all_ind_same TO postgres;
COMMENT ON VIEW wr_all_ind_same
  IS 'Short Term == Middle Term == Long Term';

  
  -- View: "wr_all_ind_same_Details"

-- DROP VIEW "wr_all_ind_same_Details";

CREATE OR REPLACE VIEW "wr_all_ind_same_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    wr_all_ind_same.date,
    round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) AS pricediff,
    wr_all_ind_same.shoterm,
    wr_all_ind_same.midterm,
    wr_all_ind_same.lonterm
   FROM qian_fuquan_stockprice,
    company_info,
    wr_all_ind_same
  WHERE qian_fuquan_stockprice.date = wr_all_ind_same.date AND qian_fuquan_stockprice.stockid = wr_all_ind_same.stockid AND company_info.stockid = qian_fuquan_stockprice.stockid
  ORDER BY round(100::numeric * (qian_fuquan_stockprice.close - qian_fuquan_stockprice.lastclose) / qian_fuquan_stockprice.lastclose, 2) DESC;

ALTER TABLE "wr_all_ind_same_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "wr_all_ind_same_Details" TO public;
GRANT ALL ON TABLE "wr_all_ind_same_Details" TO postgres;


-- View: wr_daily_compare

-- DROP VIEW wr_daily_compare;

CREATE OR REPLACE VIEW wr_daily_compare AS 
 SELECT ind_wr.stockid,
    ind_wr.date,
    ind_wr.lonterm,
    ind_wr.shoterm,
    ind_wr.midterm,
    ind_wr.shoterm - lead(ind_wr.shoterm) OVER (ORDER BY ind_wr.date DESC) AS shoterm_diff,
    ind_wr.midterm - lead(ind_wr.midterm) OVER (ORDER BY ind_wr.date DESC) AS midterm_diff,
    ind_wr.lonterm - lead(ind_wr.lonterm) OVER (ORDER BY ind_wr.date DESC) AS lonterm_diff
   FROM ind_wr
  ORDER BY ind_wr.date;

ALTER TABLE wr_daily_compare
  OWNER TO postgres;
GRANT ALL ON TABLE wr_daily_compare TO public;
GRANT ALL ON TABLE wr_daily_compare TO postgres;


-- View: "wr_midTerm_lonTerm_same"

-- DROP VIEW "wr_midTerm_lonTerm_same";

CREATE OR REPLACE VIEW "wr_midTerm_lonTerm_same" AS 
 SELECT ind_wr.stockid,
    ind_wr.date,
    ind_wr.shoterm,
    ind_wr.midterm,
    ind_wr.lonterm
   FROM ind_wr
  WHERE ind_wr.date = (( SELECT stockprice.date
           FROM stockprice
          ORDER BY stockprice.date DESC
         LIMIT 1)) AND ind_wr.midterm = ind_wr.lonterm
  ORDER BY ind_wr.stockid;

ALTER TABLE "wr_midTerm_lonTerm_same"
  OWNER TO postgres;
GRANT ALL ON TABLE "wr_midTerm_lonTerm_same" TO public;
GRANT ALL ON TABLE "wr_midTerm_lonTerm_same" TO postgres;


-- View: "wr_shoTerm_midTerm_same"

-- DROP VIEW "wr_shoTerm_midTerm_same";

CREATE OR REPLACE VIEW "wr_shoTerm_midTerm_same" AS 
 SELECT ind_wr.stockid,
    ind_wr.date,
    ind_wr.shoterm,
    ind_wr.midterm,
    ind_wr.lonterm
   FROM ind_wr
  WHERE ind_wr.date = (( SELECT stockprice.date
           FROM stockprice
          ORDER BY stockprice.date DESC
         LIMIT 1)) AND ind_wr.shoterm = ind_wr.midterm
  ORDER BY ind_wr.stockid;

ALTER TABLE "wr_shoTerm_midTerm_same"
  OWNER TO postgres;
GRANT ALL ON TABLE "wr_shoTerm_midTerm_same" TO public;
GRANT ALL ON TABLE "wr_shoTerm_midTerm_same" TO postgres;


-- View: zijinliu_3_days_top300

-- DROP VIEW zijinliu_3_days_top300;

CREATE OR REPLACE VIEW zijinliu_3_days_top300 AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT zijinliu.stockid,
                    zijinliu.date
                   FROM stockprice_latest_3_date,
                    zijinliu
                  WHERE stockprice_latest_3_date.date = zijinliu.date AND zijinliu.rate <= 300
                  ORDER BY zijinliu.stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count = 3;

ALTER TABLE zijinliu_3_days_top300
  OWNER TO postgres;
GRANT ALL ON TABLE zijinliu_3_days_top300 TO public;
GRANT ALL ON TABLE zijinliu_3_days_top300 TO postgres;
COMMENT ON VIEW zijinliu_3_days_top300
  IS '连续3天资金排名前300';

  
  -- View: "zijinliu_3_days_top300_Details"

-- DROP VIEW "zijinliu_3_days_top300_Details";

CREATE OR REPLACE VIEW "zijinliu_3_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM zijinliu_3_days_top300,
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = zijinliu_3_days_top300.stockid;

ALTER TABLE "zijinliu_3_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "zijinliu_3_days_top300_Details" TO public;
GRANT ALL ON TABLE "zijinliu_3_days_top300_Details" TO postgres;
COMMENT ON VIEW "zijinliu_3_days_top300_Details"
  IS '连续3天资金排名前300';

  
  -- View: zijinliu_3_of_5_days_top300

-- DROP VIEW zijinliu_3_of_5_days_top300;

CREATE OR REPLACE VIEW zijinliu_3_of_5_days_top300 AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT zijinliu.stockid,
                    zijinliu.date
                   FROM stockprice_latest_5_date,
                    zijinliu
                  WHERE stockprice_latest_5_date.date = zijinliu.date AND zijinliu.rate <= 300
                  ORDER BY zijinliu.stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count >= 3;

ALTER TABLE zijinliu_3_of_5_days_top300
  OWNER TO postgres;
GRANT ALL ON TABLE zijinliu_3_of_5_days_top300 TO public;
GRANT ALL ON TABLE zijinliu_3_of_5_days_top300 TO postgres;
COMMENT ON VIEW zijinliu_3_of_5_days_top300
  IS '5天有3天以上资金流入前300名';

  
  -- View: "zijinliu_3_of_5_days_top300_Details"

-- DROP VIEW "zijinliu_3_of_5_days_top300_Details";

CREATE OR REPLACE VIEW "zijinliu_3_of_5_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM zijinliu_3_of_5_days_top300,
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = zijinliu_3_of_5_days_top300.stockid;

ALTER TABLE "zijinliu_3_of_5_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "zijinliu_3_of_5_days_top300_Details" TO public;
GRANT ALL ON TABLE "zijinliu_3_of_5_days_top300_Details" TO postgres;

-- add cixin views--
-- View: "cixin_luzao_phaseIII_zijinliu_3_days_top300"

-- DROP VIEW "cixin_luzao_phaseIII_zijinliu_3_days_top300";

CREATE OR REPLACE VIEW "cixin_luzao_phaseIII_zijinliu_3_days_top300" AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT "luzao_phaseIII_zijinliu_top300".stockid,
                    "luzao_phaseIII_zijinliu_top300".date
                   FROM stockprice_latest_3_date,
                    "luzao_phaseIII_zijinliu_top300"
                  WHERE stockprice_latest_3_date.date = "luzao_phaseIII_zijinliu_top300".date
                  ORDER BY "luzao_phaseIII_zijinliu_top300".stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count = 3 AND (86 * 2) >= (( SELECT count(*) AS count
           FROM stockprice
          WHERE stockprice.stockid = rtn2.stockid));

ALTER TABLE "cixin_luzao_phaseIII_zijinliu_3_days_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_3_days_top300" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_3_days_top300" TO postgres;

-- View: "cixin_luzao_phaseIII_zijinliu_3_days_top300_Details"

-- DROP VIEW "cixin_luzao_phaseIII_zijinliu_3_days_top300_Details";

CREATE OR REPLACE VIEW "cixin_luzao_phaseIII_zijinliu_3_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM "cixin_luzao_phaseIII_zijinliu_3_days_top300",
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = "cixin_luzao_phaseIII_zijinliu_3_days_top300".stockid;

ALTER TABLE "cixin_luzao_phaseIII_zijinliu_3_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_3_days_top300_Details" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_3_days_top300_Details" TO postgres;

-- View: "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300"

-- DROP VIEW "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300";

CREATE OR REPLACE VIEW "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300" AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT "luzao_phaseIII_zijinliu_top300".stockid,
                    "luzao_phaseIII_zijinliu_top300".date
                   FROM stockprice_latest_5_date,
                    "luzao_phaseIII_zijinliu_top300"
                  WHERE stockprice_latest_5_date.date = "luzao_phaseIII_zijinliu_top300".date
                  ORDER BY "luzao_phaseIII_zijinliu_top300".stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count >= 3 AND (86 * 2) >= (( SELECT count(*) AS count
           FROM stockprice
          WHERE stockprice.stockid = rtn2.stockid));

ALTER TABLE "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300" TO postgres;

-- View: "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300_Details"

-- DROP VIEW "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300_Details";

CREATE OR REPLACE VIEW "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300",
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300".stockid;

ALTER TABLE "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300_Details" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_3_of_5_days_top300_Details" TO postgres;

-- View: "cixin_luzao_phaseIII_zijinliu_top300"

-- DROP VIEW "cixin_luzao_phaseIII_zijinliu_top300";

CREATE OR REPLACE VIEW "cixin_luzao_phaseIII_zijinliu_top300" AS 
 SELECT ind_ma.stockid,
    ind_ma.date,
    zijinliu.rate
   FROM ind_ma,
    zijinliu
  WHERE ind_ma.date = zijinliu.date AND ind_ma.stockid = zijinliu.stockid AND zijinliu.rate <= 300 AND zijinliu.majornetin > 0::numeric AND ind_ma.close > ind_ma.ma19 AND ind_ma.ma43 < ind_ma.ma19 AND ind_ma.ma86 > ind_ma.ma43 AND (86 * 2) >= (( SELECT count(*) AS count
           FROM stockprice
          WHERE stockprice.stockid = ind_ma.stockid))
  ORDER BY zijinliu.rate;

ALTER TABLE "cixin_luzao_phaseIII_zijinliu_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_top300" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_top300" TO postgres;


-- View: "cixin_luzao_phaseIII_zijinliu_top300_Details"

-- DROP VIEW "cixin_luzao_phaseIII_zijinliu_top300_Details";

CREATE OR REPLACE VIEW "cixin_luzao_phaseIII_zijinliu_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    "cixin_luzao_phaseIII_zijinliu_top300".date,
    "cixin_luzao_phaseIII_zijinliu_top300".rate
   FROM "cixin_luzao_phaseIII_zijinliu_top300",
    company_info
  WHERE company_info.stockid = "cixin_luzao_phaseIII_zijinliu_top300".stockid;

ALTER TABLE "cixin_luzao_phaseIII_zijinliu_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_top300_Details" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseIII_zijinliu_top300_Details" TO postgres;

-- View: "cixin_luzao_phaseII_ddx_2_of_5_days_bigger_05"

-- DROP VIEW "cixin_luzao_phaseII_ddx_2_of_5_days_bigger_05";

CREATE OR REPLACE VIEW "cixin_luzao_phaseII_ddx_2_of_5_days_bigger_05" AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT "luzao_phaseII_ddx_bigger_05".stockid,
                    "luzao_phaseII_ddx_bigger_05".date
                   FROM stockprice_latest_5_date,
                    "luzao_phaseII_ddx_bigger_05"
                  WHERE stockprice_latest_5_date.date = "luzao_phaseII_ddx_bigger_05".date
                  ORDER BY "luzao_phaseII_ddx_bigger_05".stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count >= 2 AND (86 * 2) >= (( SELECT count(*) AS count
           FROM stockprice
          WHERE stockprice.stockid = rtn2.stockid));

ALTER TABLE "cixin_luzao_phaseII_ddx_2_of_5_days_bigger_05"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseII_ddx_2_of_5_days_bigger_05" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseII_ddx_2_of_5_days_bigger_05" TO postgres;

-- View: "cixin_luzao_phaseII_ddx_bigger_05"

-- DROP VIEW "cixin_luzao_phaseII_ddx_bigger_05";

CREATE OR REPLACE VIEW "cixin_luzao_phaseII_ddx_bigger_05" AS 
 SELECT ind_ma.stockid,
    ind_ma.date,
    ind_ddx.ddx
   FROM ind_ma,
    ind_ddx
  WHERE ind_ma.date = ind_ddx.date AND ind_ma.stockid = ind_ddx.stockid AND ind_ddx.ddx >= 0.5 AND ind_ma.close > ind_ma.ma19 AND ind_ma.ma43 > ind_ma.ma19 AND ind_ma.ma86 > ind_ma.ma43 AND (86 * 2) >= (( SELECT count(*) AS count
           FROM stockprice
          WHERE stockprice.stockid = ind_ma.stockid))
  ORDER BY ind_ddx.ddx DESC;

ALTER TABLE "cixin_luzao_phaseII_ddx_bigger_05"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseII_ddx_bigger_05" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseII_ddx_bigger_05" TO postgres;

-- View: "cixin_luzao_phaseII_ddx_bigger_05_Details"

-- DROP VIEW "cixin_luzao_phaseII_ddx_bigger_05_Details";

CREATE OR REPLACE VIEW "cixin_luzao_phaseII_ddx_bigger_05_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    "cixin_luzao_phaseII_ddx_bigger_05".date,
    "cixin_luzao_phaseII_ddx_bigger_05".ddx
   FROM "cixin_luzao_phaseII_ddx_bigger_05",
    company_info
  WHERE company_info.stockid = "cixin_luzao_phaseII_ddx_bigger_05".stockid;

ALTER TABLE "cixin_luzao_phaseII_ddx_bigger_05_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseII_ddx_bigger_05_Details" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseII_ddx_bigger_05_Details" TO postgres;

-- View: "cixin_luzao_phaseII_zijinliu_3_days_top300"

-- DROP VIEW "cixin_luzao_phaseII_zijinliu_3_days_top300";

CREATE OR REPLACE VIEW "cixin_luzao_phaseII_zijinliu_3_days_top300" AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT "luzao_phaseII_zijinliu_top300".stockid,
                    "luzao_phaseII_zijinliu_top300".date
                   FROM stockprice_latest_3_date,
                    "luzao_phaseII_zijinliu_top300"
                  WHERE stockprice_latest_3_date.date = "luzao_phaseII_zijinliu_top300".date
                  ORDER BY "luzao_phaseII_zijinliu_top300".stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count = 3 AND (86 * 2) >= (( SELECT count(*) AS count
           FROM stockprice
          WHERE stockprice.stockid = rtn2.stockid));

ALTER TABLE "cixin_luzao_phaseII_zijinliu_3_days_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_3_days_top300" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_3_days_top300" TO postgres;

-- View: "cixin_luzao_phaseII_zijinliu_3_days_top300_Details"

-- DROP VIEW "cixin_luzao_phaseII_zijinliu_3_days_top300_Details";

CREATE OR REPLACE VIEW "cixin_luzao_phaseII_zijinliu_3_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM "cixin_luzao_phaseII_zijinliu_3_days_top300",
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = "cixin_luzao_phaseII_zijinliu_3_days_top300".stockid;

ALTER TABLE "cixin_luzao_phaseII_zijinliu_3_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_3_days_top300_Details" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_3_days_top300_Details" TO postgres;

-- View: "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300"

-- DROP VIEW "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300";

CREATE OR REPLACE VIEW "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300" AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT "luzao_phaseII_zijinliu_top300".stockid,
                    "luzao_phaseII_zijinliu_top300".date
                   FROM stockprice_latest_5_date,
                    "luzao_phaseII_zijinliu_top300"
                  WHERE stockprice_latest_5_date.date = "luzao_phaseII_zijinliu_top300".date
                  ORDER BY "luzao_phaseII_zijinliu_top300".stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count >= 3 AND (86 * 2) >= (( SELECT count(*) AS count
           FROM stockprice
          WHERE stockprice.stockid = rtn2.stockid));

ALTER TABLE "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300" TO postgres;

-- View: "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300_Details"

-- DROP VIEW "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300_Details";

CREATE OR REPLACE VIEW "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300",
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300".stockid;

ALTER TABLE "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300_Details" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_3_of_5_days_top300_Details" TO postgres;

-- View: "cixin_luzao_phaseII_zijinliu_top300"

-- DROP VIEW "cixin_luzao_phaseII_zijinliu_top300";

CREATE OR REPLACE VIEW "cixin_luzao_phaseII_zijinliu_top300" AS 
 SELECT ind_ma.stockid,
    ind_ma.date,
    zijinliu.rate
   FROM ind_ma,
    zijinliu
  WHERE ind_ma.date = zijinliu.date AND ind_ma.stockid = zijinliu.stockid AND zijinliu.rate <= 300 AND zijinliu.majornetin > 0::numeric AND ind_ma.close > ind_ma.ma19 AND ind_ma.ma43 > ind_ma.ma19 AND ind_ma.ma86 > ind_ma.ma43 AND (86 * 2) >= (( SELECT count(*) AS count
           FROM stockprice
          WHERE stockprice.stockid = ind_ma.stockid))
  ORDER BY zijinliu.rate;

ALTER TABLE "cixin_luzao_phaseII_zijinliu_top300"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_top300" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_top300" TO postgres;


-- View: "cixin_luzao_phaseII_zijinliu_top300_Details"

-- DROP VIEW "cixin_luzao_phaseII_zijinliu_top300_Details";

CREATE OR REPLACE VIEW "cixin_luzao_phaseII_zijinliu_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    "cixin_luzao_phaseII_zijinliu_top300".date,
    "cixin_luzao_phaseII_zijinliu_top300".rate
   FROM "cixin_luzao_phaseII_zijinliu_top300",
    company_info
  WHERE company_info.stockid = "cixin_luzao_phaseII_zijinliu_top300".stockid;

ALTER TABLE "cixin_luzao_phaseII_zijinliu_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_top300_Details" TO public;
GRANT ALL ON TABLE "cixin_luzao_phaseII_zijinliu_top300_Details" TO postgres;

-- View: cixin_zijinliu_3_days_top300

-- DROP VIEW cixin_zijinliu_3_days_top300;

CREATE OR REPLACE VIEW cixin_zijinliu_3_days_top300 AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT zijinliu.stockid,
                    zijinliu.date
                   FROM stockprice_latest_3_date,
                    zijinliu
                  WHERE stockprice_latest_3_date.date = zijinliu.date AND zijinliu.rate <= 300
                  ORDER BY zijinliu.stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count = 3 AND (86 * 2) >= (( SELECT count(*) AS count
           FROM stockprice
          WHERE stockprice.stockid = rtn2.stockid));

ALTER TABLE cixin_zijinliu_3_days_top300
  OWNER TO postgres;
GRANT ALL ON TABLE cixin_zijinliu_3_days_top300 TO public;
GRANT ALL ON TABLE cixin_zijinliu_3_days_top300 TO postgres;

-- View: "cixin_zijinliu_3_days_top300_Details"

-- DROP VIEW "cixin_zijinliu_3_days_top300_Details";

CREATE OR REPLACE VIEW "cixin_zijinliu_3_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM cixin_zijinliu_3_days_top300,
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = cixin_zijinliu_3_days_top300.stockid;

ALTER TABLE "cixin_zijinliu_3_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_zijinliu_3_days_top300_Details" TO public;
GRANT ALL ON TABLE "cixin_zijinliu_3_days_top300_Details" TO postgres;

-- View: cixin_zijinliu_3_of_5_days_top300

-- DROP VIEW cixin_zijinliu_3_of_5_days_top300;

CREATE OR REPLACE VIEW cixin_zijinliu_3_of_5_days_top300 AS 
 SELECT rtn2.stockid
   FROM ( SELECT rtn.stockid,
            count(*) AS count
           FROM ( SELECT zijinliu.stockid,
                    zijinliu.date
                   FROM stockprice_latest_5_date,
                    zijinliu
                  WHERE stockprice_latest_5_date.date = zijinliu.date AND zijinliu.rate <= 300
                  ORDER BY zijinliu.stockid) rtn
          GROUP BY rtn.stockid) rtn2
  WHERE rtn2.count >= 3 AND (86 * 2) >= (( SELECT count(*) AS count
           FROM stockprice
          WHERE stockprice.stockid = rtn2.stockid));

ALTER TABLE cixin_zijinliu_3_of_5_days_top300
  OWNER TO postgres;
GRANT ALL ON TABLE cixin_zijinliu_3_of_5_days_top300 TO public;
GRANT ALL ON TABLE cixin_zijinliu_3_of_5_days_top300 TO postgres;

-- View: "cixin_zijinliu_3_of_5_days_top300_Details"

-- DROP VIEW "cixin_zijinliu_3_of_5_days_top300_Details";

CREATE OR REPLACE VIEW "cixin_zijinliu_3_of_5_days_top300_Details" AS 
 SELECT company_info.name,
    company_info.stockid,
    stockprice_latest_date.date
   FROM cixin_zijinliu_3_of_5_days_top300,
    company_info,
    stockprice_latest_date
  WHERE company_info.stockid = cixin_zijinliu_3_of_5_days_top300.stockid;

ALTER TABLE "cixin_zijinliu_3_of_5_days_top300_Details"
  OWNER TO postgres;
GRANT ALL ON TABLE "cixin_zijinliu_3_of_5_days_top300_Details" TO public;
GRANT ALL ON TABLE "cixin_zijinliu_3_of_5_days_top300_Details" TO postgres;

-- View: "OneYuan_Stock_Statistics"

-- DROP VIEW "OneYuan_Stock_Statistics";

CREATE OR REPLACE VIEW "OneYuan_Stock_Statistics" AS 
 SELECT v1.date,
    count(*) AS count
   FROM ( SELECT stockprice.stockid,
            stockprice.date,
            stockprice.open,
            stockprice.high,
            stockprice.low,
            stockprice.close,
            stockprice.volume,
            stockprice.lastclose
           FROM stockprice
          WHERE stockprice.close < 2::numeric) v1
  GROUP BY v1.date
  ORDER BY v1.date DESC;

ALTER TABLE "OneYuan_Stock_Statistics"
  OWNER TO postgres;
GRANT ALL ON TABLE "OneYuan_Stock_Statistics" TO public;
GRANT ALL ON TABLE "OneYuan_Stock_Statistics" TO postgres;
COMMENT ON VIEW "OneYuan_Stock_Statistics"
  IS '一元股统计';

-- DROP VIEW "FiveYuan_Stock_Statistics";

CREATE OR REPLACE VIEW "FiveYuan_Stock_Statistics" AS 
 SELECT v1.date,
    count(*) AS count
   FROM ( SELECT stockprice.stockid,
            stockprice.date,
            stockprice.open,
            stockprice.high,
            stockprice.low,
            stockprice.close,
            stockprice.volume,
            stockprice.lastclose
           FROM stockprice
          WHERE stockprice.close < 5::numeric) v1
  GROUP BY v1.date
  ORDER BY v1.date DESC;

ALTER TABLE "FiveYuan_Stock_Statistics"
  OWNER TO postgres;
GRANT ALL ON TABLE "FiveYuan_Stock_Statistics" TO public;
GRANT ALL ON TABLE "FiveYuan_Stock_Statistics" TO postgres;
COMMENT ON VIEW "FiveYuan_Stock_Statistics"
  IS '5元股统计';
  
-- View: "TenYuan_Stock_Statistics"

-- DROP VIEW "TenYuan_Stock_Statistics";

CREATE OR REPLACE VIEW "TenYuan_Stock_Statistics" AS 
 SELECT v1.date,
    count(*) AS count
   FROM ( SELECT stockprice.stockid,
            stockprice.date,
            stockprice.open,
            stockprice.high,
            stockprice.low,
            stockprice.close,
            stockprice.volume,
            stockprice.lastclose
           FROM stockprice
          WHERE stockprice.close < 10::numeric) v1
  GROUP BY v1.date
  ORDER BY v1.date DESC;

ALTER TABLE "TenYuan_Stock_Statistics"
  OWNER TO postgres;
GRANT ALL ON TABLE "TenYuan_Stock_Statistics" TO public;
GRANT ALL ON TABLE "TenYuan_Stock_Statistics" TO postgres;
COMMENT ON VIEW "TenYuan_Stock_Statistics"
  IS '10元股统计';

