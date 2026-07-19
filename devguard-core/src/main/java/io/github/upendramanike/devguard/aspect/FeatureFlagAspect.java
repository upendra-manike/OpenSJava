package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.FeatureFlag;
import io.github.upendramanike.devguard.core.FeatureDisabledException;
import io.github.upendramanike.devguard.core.FeatureFlagProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Implements {@link FeatureFlag}: skips (or fails) invocations of disabled features. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 60)
public class FeatureFlagAspect {

    private final FeatureFlagProvider provider;

    public FeatureFlagAspect(FeatureFlagProvider provider) {
        this.provider = provider;
    }

    @Around("@annotation(featureFlag)")
    public Object around(ProceedingJoinPoint pjp, FeatureFlag featureFlag) throws Throwable {
        if (provider.isEnabled(featureFlag.value())) {
            return pjp.proceed();
        }
        if (featureFlag.failIfDisabled()) {
            throw new FeatureDisabledException("Feature disabled: " + featureFlag.value());
        }
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        return Aspects.defaultValue(sig.getReturnType());
    }
}
