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
  IS 'select the latest date from stockprice';

  
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
  ORDER BY ind_wr.date DESC;

ALTER TABLE wr_daily_compare
  OWNER TO postgres;
GRANT ALL ON TABLE wr_daily_compare TO public;
GRANT ALL ON TABLE wr_daily_compare TO postgres;
