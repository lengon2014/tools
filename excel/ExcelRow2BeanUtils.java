package com.zcnest.wms.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.zcnest.common.utils.ErrorCode;
import com.zcnest.common.utils.ServerException;

public class ExcelRow2BeanUtils {

	/**
	 * 每行的转为java bean， 根据反射实现
	 */
	public <T> T getBeanFromExcelRow(Row row, List<String> methodKeys, Class<T> clz) throws ServerException {
		T bean = null;
		try {
			bean = clz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < methodKeys.size(); i++) {
			String key = methodKeys.get(i);
			// 首字母大写。
			key = key.substring(0, 1).toUpperCase() + key.substring(1);
			String getKey = "get" + key;
			String setKey = "set" + key;
			Method get;
			try {
				get = clz.getMethod(getKey);
				Class<?> type = get.getReturnType();
				Cell cell = row.getCell(i);
				setByMethod(type, cell, setKey, bean, clz);
			} catch (Exception e) {
				throw new ServerException(ErrorCode.HTTP_PARA_WRONG,
						"fail to format set method , maybe type format mismatch for " + key);
			}

		}
		return bean;
	}

	/**
	 * 适配每个单元格里面的类型和对应的javabean里面的类型。 
	 */
	private <T> void setByMethod(Class<?> type, Cell cell, String setMethod, T bean, Class<T> clz)
			throws ServerException {
		Object args = null;
		if (type.equals(Integer.class) || type.equals(int.class)) {
			double value = cell.getNumericCellValue();
			args = (int) value;
		} else if (type.equals(Long.class) || type.equals(long.class)) {
			double value = cell.getNumericCellValue();
			args = (long) value;
		} else if (type.equals(Double.class) || type.equals(double.class)) {
			args = cell.getNumericCellValue();
		} else if (type.equals(Float.class) || type.equals(float.class)) {
			double value = cell.getNumericCellValue();
			args = (float) value;
		} else if (type.equals(Short.class) || type.equals(short.class)) {
			double value = cell.getNumericCellValue();
			args = (short) value;
		} else if (type.equals(String.class)) {
			args = cell.getStringCellValue();
		} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
			args = cell.getBooleanCellValue();
		}

		if (null != args) {
			try {
				clz.getMethod(setMethod, type).invoke(bean, args);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		} else {
			throw new ServerException(ErrorCode.HTTP_PARA_WRONG,
					"fail to format set method , maybe type format mismatch for " + setMethod);
		}
	}
}
