package net.sf.json.processors;

import com.eplat.db.rowset.DataRowSet;
import net.sf.json.JsonConfig;

/**
 * RowSet 序列化的处理
 * 
 * @author Administrator
 * 
 */
public class JsonRowsetToStringProcessorImpl implements JsonValueProcessor {

	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String[] obj = {};
		try {
			if (value == null)
				return obj;
			DataRowSet[] rowsets = (DataRowSet[]) value;
			obj = new String[rowsets.length];
			for (int i = 0; i < rowsets.length; i++) {
				obj[i] = rowsets[i].toJson();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return obj;
	}

	@Override
	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		DataRowSet rowset = (DataRowSet) value;
		return rowset.toJson();
	}

}
