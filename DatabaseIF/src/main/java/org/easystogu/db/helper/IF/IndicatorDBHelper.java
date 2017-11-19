package org.easystogu.db.helper.IF;

import java.util.List;

import org.easystogu.db.vo.table.IndicatorVO;

public interface IndicatorDBHelper {

	public <T extends IndicatorVO> void insert(T vo);

	public <T extends IndicatorVO> void insert(List<T> list);

	public void delete(String stockId);

	public void delete(String stockId, String date);

	public <T extends IndicatorVO> T getSingle(String stockId, String date);

	public <T extends IndicatorVO> List<T> getAll(String stockId);

	public <T extends IndicatorVO> List<T> getByIdAndBetweenDate(String stockId, String StartDate, String endDate);

	public <T extends IndicatorVO> List<T> getByIdAndLatestNDate(String stockId, int day);

}
