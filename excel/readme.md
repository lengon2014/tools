- 问题：封装读取excel到javabean的通用操作。 excel里面的数据是一行对应一个javabean
- 解决方式：使用的方法是POI的excel操作(当前支持excel2007和2010之后的版本，也就是xls和xlsx文件)和java的反射机制

- 依赖的jar的pom文件

```
<dependency>
	<groupId>org.apache.poi</groupId>
	<artifactId>poi-ooxml</artifactId>
	<version>3.9</version>
</dependency>
```

- 解决步骤
1. 使用poi去读取excel文件。

```java
Workbook workbook = null;
try {
	workbook = new XSSFWorkbook(file.getInputStream());
} catch (Exception ex) {
	workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
}

代码中查看读取行和列的操作...
```

2. 根据预配的每列的字符，使用javabean class的反射获得对应的列应该是什么类型

```java
String key = methodKeys.get(i);
// 首字母大写。
key = key.substring(0, 1).toUpperCase() + key.substring(1);
String getKey = "get" + key;
String setKey = "set" + key;
Method get;
try {
	get = clz.getMethod(getKey);
	Class<?> type = get.getReturnType();
} catch (Exception e) {
	throw new ServerException("fail to format set method , maybe type format mismatch for " + key);
}
```

3. 根据2得到的类型获取excel的对应的列的值

```
Object args = null;
if (type.equals(Integer.class) || type.equals(int.class)) {
	double value = cell.getNumericCellValue();
	args = (int) value;
} else if (type.equals(Long.class) || type.equals(long.class)) {
	double value = cell.getNumericCellValue();
	args = (long) value;
}
其他类型看代码...
```

4. 使用反射的方法实现对应的javabean的赋值

```
try {
	clz.getMethod(setMethod, type).invoke(bean, args);
} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
	| NoSuchMethodException | SecurityException e) {
	e.printStackTrace();
}
```