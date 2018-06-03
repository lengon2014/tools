package batch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zcnest.common.utils.LogUtils;

/**
 * ͨ�õ���������Ĵ�����
 * @author lengon
 *
 */
public class BatchDAOProvider {

	private String insertKeysAll(List<?> list, List<String> keys, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO  ").append(tableName).append("(");
		for (int i = 0; i < keys.size(); i++) {
			sb.append("`").append(keys.get(i)).append("`");
			if (i < keys.size() - 1) {
				sb.append(",");
			}
		}
		sb.append(") values ");

		for (int i = 0; i < list.size(); i++) {
			sb.append("(");

			for (int j = 0; j < keys.size(); j++) {
				sb.append(" #{list[").append(i).append("].").append(keys.get(j)).append("}");
				if (j < keys.size() - 1) {
					sb.append(",");
				}
			}
			sb.append(")");
			if (i < list.size() - 1) {
				sb.append(",");
			}
		}

		LogUtils.info(sb.toString());
		return sb.toString();
	}

	public String insertAll(Map<?, ?> map) {
		List<?> list = (List<?>) map.get("list");
		Class<?> cls = list.get(0).getClass();

		String tableName = cls.getName();
		tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
		tableName = tableName.substring(0, 1).toLowerCase() + tableName.substring(1);

		Method[] methods = cls.getMethods();
		List<String> keys = new ArrayList<>();
		for (Method method : methods) {
			if (method.getName().startsWith("get") && !method.getName().equals("getClass")) {
				String name = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
				keys.add(name);
			}
		}
		return insertKeysAll(list, keys, tableName);
	}
	
	public String insertAllByTable(Map<?, ?> map) {
		List<?> list = (List<?>) map.get("list");
		String tableName =String.valueOf(map.get("table"));
		List<String> keys = (List<String>) map.get("keys");
		return insertKeysAll(list, keys, tableName);
	}
}