- 问题：主要解决的问题是spring boot里面用mybatis的注解，没有批量插入的功能
- 解决方式：使用InsertProvider，自定义batch的sql语句， 最终的格式是：

> INSERT INTO User (id, name) VALUES (null, #{list[0].name}), (null, #{list[1].name})[,(null, #{list[i].name})] 

- 解决步骤：
1. 批量插入的统一的代理，这块在网上找到的资料基本都不是通用的，因此在这边借用反射写了一个通用的BatchDaoProvider.java

```
		//要求list里面的每个都是一样的，否则将会出现异常
		List<?> list = (List<?>) map.get("list");
		Class<?> cls = list.get(0).getClass();
		
		//要求表名称和list里面的class名称是一样的
		String tableName = cls.getName();
		tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
		tableName = tableName.substring(0, 1).toLowerCase() + tableName.substring(1);

		//每个有get的字段都会插入到数据库中，包含为null的情况
		Method[] methods = cls.getMethods();
		List<String> keys = new ArrayList<>();
		for (Method method : methods) {
			if (method.getName().startsWith("get") && !method.getName().equals("getClass")) {
				String name = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
				keys.add(name);
			}
		}
		insertKeysAll(list, keys, tableName);
```

2. 注解的dao里面使用代理

```
	@InsertProvider(type = BatchDAOProvider.class, method = "insertAll")
	void insertAll(@Param("list") List<User> users);
```

3. 调用和其他的一样，只要传入user即可。


- 假如出现了表名或者是插入的表中的数据需要自己指定， 那么可以将前面的tableName和keys换成从dao里面传入

```
		//要求list里面的每个都是一样的，否则将会出现异常
		List<?> list = (List<?>) map.get("list");
		Class<?> cls = list.get(0).getClass();
	
		String tableName = map.get("table");
		List<String> keys = map.get("keys");
		insertKeysAll(list, keys, tableName);
```

