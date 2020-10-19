package testframework.app;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:config.properties"})
public interface PropertiesHolder extends Config {

    String baseUrl();

    String token();
}
