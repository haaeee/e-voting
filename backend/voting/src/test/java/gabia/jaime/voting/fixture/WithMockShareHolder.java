package gabia.jaime.voting.fixture;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomShareHolderSecurityContextFactory.class)
public @interface WithMockShareHolder {

    String username() default "shareholder@email.com";

    String name() default "shareholder@email.com";
}

