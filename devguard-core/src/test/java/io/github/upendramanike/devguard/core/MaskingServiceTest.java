package io.github.upendramanike.devguard.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.upendramanike.devguard.annotations.Mask;
import io.github.upendramanike.devguard.annotations.MaskType;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MaskingServiceTest {

    private final MaskingService service = new MaskingService();

    @Test
    void masksEmail() {
        assertThat(service.mask("upendra@gmail.com", MaskType.EMAIL, '*')).isEqualTo("up*****@gmail.com");
    }

    @Test
    void masksPhone() {
        assertThat(service.mask("9876543210", MaskType.PHONE, '*')).isEqualTo("98******10");
    }

    @Test
    void masksLastFour() {
        assertThat(service.mask("1234567890123456", MaskType.CREDIT_CARD, '*'))
                .isEqualTo("************3456");
    }

    @Test
    void masksFull() {
        assertThat(service.mask("secret", MaskType.FULL, '*')).isEqualTo("******");
    }

    @Test
    void masksAnnotatedFields() {
        Map<String, Object> masked = service.maskFields(new User("upendra@gmail.com", "9876543210", "Upendra"));
        assertThat(masked.get("email")).isEqualTo("up*****@gmail.com");
        assertThat(masked.get("phone")).isEqualTo("98******10");
        assertThat(masked.get("name")).isEqualTo("Upendra");
    }

    static class User {
        @Mask(type = MaskType.EMAIL)
        String email;

        @Mask(type = MaskType.PHONE)
        String phone;

        String name;

        User(String email, String phone, String name) {
            this.email = email;
            this.phone = phone;
            this.name = name;
        }
    }
}
