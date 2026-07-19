package io.github.upendramanike.devguard.annotations;

/** Strategies for masking sensitive values. */
public enum MaskType {

    /** Keep the first two characters of the local part: {@code up****@gmail.com}. */
    EMAIL,

    /** Keep the leading and trailing digits: {@code 98******12}. */
    PHONE,

    /** Reveal only the last 4 characters: {@code ************3456}. */
    CREDIT_CARD,

    /** Reveal only the last 4 characters. */
    LAST_FOUR,

    /** Replace the entire value with a fixed placeholder. */
    FULL
}
