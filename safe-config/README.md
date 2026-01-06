## safe-config

Fail-fast configuration binding and secret-safe logging for Java applications.

### Quick start

Add the dependency (once published to Maven Central):

```xml
<dependency>
  <groupId>io.github.upendramanike</groupId>
  <artifactId>safe-config</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Define a typed configuration class:

```java
import io.github.upendramanike.safeconfig.ConfigProperty;
import io.github.upendramanike.safeconfig.Required;
import io.github.upendramanike.safeconfig.Secret;

public final class AppConfig {
  @ConfigProperty("APP_PORT")
  @Required
  int port;

  @ConfigProperty("APP_API_KEY")
  @Secret
  String apiKey;
}
```

Bind from environment variables or system properties:

```java
import io.github.upendramanike.safeconfig.ConfigBinder;
import io.github.upendramanike.safeconfig.EnvironmentConfigSource;
import io.github.upendramanike.safeconfig.SecretMasking;

ConfigBinder binder = new ConfigBinder(new EnvironmentConfigSource());
AppConfig config = binder.bind(AppConfig.class);

// Safe to log – secrets are masked
log.info("Loaded config: {}", SecretMasking.safeToString(config));
```

If a required property is missing or cannot be converted to the target type,
`ConfigBindingException` is thrown with a descriptive message.


