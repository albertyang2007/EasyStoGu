package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.CompanyInfoVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CompanyInfoTableHelper {
	private static Logger logger = LogHelper.getLogger(CompanyInfoTableHelper.class);
	private static CompanyInfoTableHelper instance = null;
	private static CompanyInfoTableHelper georedInstance = null;
	protected String tableName = "COMPANY_INFO";
	// please modify this SQL in all subClass
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, name, totalguben, liutongagu, updatetime) VALUES (:stockId, :name, :totalguben, :liutongagu, :updatetime)";
	protected String QUERY_BY_STOCKID = "SELECT * FROM " + tableName + " WHERE stockId = :stockId";
	protected String QUERY_ALL = "SELECT * FROM " + tableName;
	protected String QUERY_ALL_STOCKID = "SELECT stockId AS rtn FROM " + tableName;
	protected String DELETE_BY_STOCKID = "DELETE FROM " + tableName + " WHERE stockId = :stockId";

	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private CompanyInfoTableHelper(javax.sql.DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}

	public static CompanyInfoTableHelper getInstance() {
		if (instance == null) {
			instance = new CompanyInfoTableHelper(PostgreSqlDataSourceFactory.createDataSource());
		}
		return instance;
	}

	public static CompanyInfoTableHelper getGeoredInstance() {
		if (georedInstance == null) {
			georedInstance = new CompanyInfoTableHelper(PostgreSqlDataSourceFactory.createGeoredDataSource());
		}
		return georedInstance;
	}

	private static final class CompanyInfoVOMapper implements RowMapper<CompanyInfoVO> {
		public CompanyInfoVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CompanyInfoVO vo = new CompanyInfoVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setName(rs.getString("name"));
			vo.setTotalGuBen(rs.getDouble("totalguben"));
			vo.setLiuTongAGu(rs.getDouble("liutongagu"));
			return vo;
		}
	}

	private static final class StringVOMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("rtn");
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(CompanyInfoVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("name", vo.getName());
			namedParameters.addValue("totalguben", vo.getTotalGuBen());
			namedParameters.addValue("liutongagu", vo.getLiuTongAGu());
			namedParameters.addValue("updatetime", vo.getUpdateTime());

			namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertIfNotExist(CompanyInfoVO vo) {
		if (this.getCompanyInfoByStockId(vo.stockId) == null) {
			this.insert(vo);
		}
	}

	public void updateByDeleteInsert(CompanyInfoVO vo) {
		if (this.getCompanyInfoByStockId(vo.stockId) != null) {
			this.delete(vo.stockId);
			this.insert(vo);
		}
	}

	public void delete(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameterJdbcTemplate.execute(DELETE_BY_STOCKID, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insert(List<CompanyInfoVO> list) throws Exception {
		for (CompanyInfoVO vo : list) {
			this.insert(vo);
		}
	}

	public CompanyInfoVO getCompanyInfoByStockId(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			CompanyInfoVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_STOCKID, namedParameters,
					new CompanyInfoVOMapper());
			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<CompanyInfoVO> getAllCompanyInfo() {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();

			List<CompanyInfoVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL, namedParameters,
					new CompanyInfoVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CompanyInfoVO>();
	}

	public List<String> getAllCompanyStockId() {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();

			List<String> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_STOCKID, namedParameters,
					new StringVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoTableHelper ins = CompanyInfoTableHelper.getInstance();
		List<String> stockIds = ins.getAllCompanyStockId();
		for (String stockId : stockIds) {
			System.out.println(stockId);
		}
		System.out.println("total size=" + stockIds.size());
		try {
			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
