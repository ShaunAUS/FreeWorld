package server.test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("test")
public class AppProperties {

    public static final String ACCESS_ID = "sId";
    public static final String LOGIN_SERVICE = "logInService";
    public static final String AUTH_TOKEN_NAME = "X-AUTH-TOKEN";  // Header = Authorization key값 , token 은  value
    ;
    private String apiVersion;

    private Long accessHoldTime = 15L;

    private Long accessHoldTimeMillis = 10000L;

    private byte[] memberAuthKey;

    public  String SESSION_ID = "sId";
}
