package io.github.upendramanike.devguard.core;

import java.util.Map;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

/** Evaluates SpEL key/condition expressions against a {@link MethodInvocationContext}. */
public class SpelKeyResolver {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    public ParameterNameDiscoverer parameterNames() {
        return discoverer;
    }

    /**
     * Resolves the given SpEL expression to a String key. When the expression is blank, a digest of
     * the argument values is returned so callers always get a usable key.
     */
    public String key(String expression, MethodInvocationContext context) {
        if (!StringUtils.hasText(expression)) {
            return context.argumentDigest();
        }
        Object value = evaluate(expression, context, Object.class);
        return value == null ? "null" : value.toString();
    }

    /** Evaluates a boolean SpEL condition; a blank expression is treated as {@code true}. */
    public boolean condition(String expression, MethodInvocationContext context) {
        if (!StringUtils.hasText(expression)) {
            return true;
        }
        Boolean result = evaluate(expression, context, Boolean.class);
        return result != null && result;
    }

    private <T> T evaluate(String expression, MethodInvocationContext context, Class<T> type) {
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        for (Map.Entry<String, Object> entry : context.argumentMap().entrySet()) {
            evaluationContext.setVariable(entry.getKey(), entry.getValue());
        }
        Expression parsed = parser.parseExpression(expression);
        return parsed.getValue(evaluationContext, type);
    }
}
