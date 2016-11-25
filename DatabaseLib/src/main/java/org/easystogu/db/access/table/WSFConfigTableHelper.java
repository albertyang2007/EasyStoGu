package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.WSFConfigVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class WSFConfigTableHelper {
	private static Logger logger = LogHelper.getLogger(WSFConfigTableHelper.class);
	private static WSFConfigTableHelper instance = null;
	protected DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
	protected String tableName = "WSFCONFIG";
	// please modify this SQL in all subClass
	protected String QUERY_BY_NAME = "SELECT * FROM " + tableName + " WHERE name = :name";

	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public static WSFConfigTableHelper getInstance() {
		if (instance == null) {
			instance = new WSFConfigTableHelper();
		}
		return instance;
	}

	protected WSFConfigTableHelper() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	private static final class ConfigVOMapper implements RowMapper<WSFConfigVO> {
		public WSFConfigVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			WSFConfigVO vo = new WSFConfigVO();
			vo.setName(rs.getString("name"));
			vo.setValue(rs.getString("value"));
			vo.setType(rs.getString("type"));
			vo.setDesc(rs.getString("desc"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public String getValue(String name, String defaultValue) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("name", name);

			WSFConfigVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_NAME, namedParameters,
					new ConfigVOMapper());

			return vo.getValue();
		} catch (EmptyResultDataAccessException ee) {
			return defaultValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	public String getValue(String name) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("name", name);

			WSFConfigVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_NAME, namedParameters,
					new ConfigVOMapper());

			return vo.getValue();
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WSFConfigTableHelper ins = new WSFConfigTableHelper();
		try {

			System.out.println(ins.getValue("zone", "home"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
