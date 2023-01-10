package gabia.jaime.voting.global.config;

import com.p6spy.engine.spy.P6SpyOptions;
import gabia.jaime.voting.global.p6spy.CustomP6spySqlFormat;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class P6spyConfig {
    @PostConstruct
    public void setMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(CustomP6spySqlFormat.class.getName());
    }
}
