package com.example.Order.Config;

import com.example.Order.Customer.CustomerContext;
import io.github.resilience4j.core.ContextPropagator;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
public class JwtContextPropagator implements ContextPropagator<String> {
    @Override
    public Supplier<Optional<String>> retrieve() {
        return ()->Optional.ofNullable(CustomerContext.getJwtToken());
    }

    @Override
    public Consumer<Optional<String>> copy() {
        return (context)->context.ifPresent(CustomerContext::setJwtToken);
    }

    @Override
    public Consumer<Optional<String>> clear() {
        return (context)->context.ifPresent(CustomerContext::clearJwt);
    }
}
