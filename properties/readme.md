- 问题：spring boot 自定义配置项
- 解决方式：spring boot使用默认的application.properties或者是自定义的properties文件来配置配置项。

###解决方式1：使用默认的application.properties里面定义，使用的时候，借助component和value的形式来实现

1. 配置application.properties, 增加一行 自定义的配置项 websocket.port=21000

```javascript
spring.application.name=oms
server.port=18000

websocket.port=21000
```

2. 对应的java bean增加component和value的注解

```java
@Configuration
@Component
public class WsConfig {

	@Value("${websocket.port}")
	private int port;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
```

3. 在main里面启动并使用

```
@SpringBootApplication
@PropertySource(value = {"application.properties"})
public class StartOMSApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(StartOMSApplication.class, args);
		WsConfig conf = context.getBean(WsConfig.class);
		System.out.println(conf.getPort());
	}
}
```

如果是其他spring的mvc中可以直接用autowired 就可以直接使用了
> @Autowired  
> private WsConfig config;

### 解决方式2：使用自定义的properties文件， 例如在resorce目录下（系统配置项文件夹）创建一个 websokcet.properties 文件

1. 创建文件websoket.properties

```
websocket.port=21000
```

2. 使用PropertySource来定义对应的javabean

```
@Configuration
@ConfigurationProperties(prefix = "websocket")
@PropertySource(value = { "classpath:websokcet.properties" })
public class  WsConfig {
	private int port;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
```

3. 启动方式增加引入websokcet.properties

> @PropertySource(value = { "application.properties","websokcet.properties" })

4. 使用方式同方式1， 直接用@Autowired 或者是用Appcontext获得bean来get

