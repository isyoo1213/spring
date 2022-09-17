package user.userservice;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@TestConfiguration
public class JasyptConfigTest {

    private StringEncryptor encryptor;

    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JasyptConfig.class);
        encryptor = context.getBean(StringEncryptor.class);
    }

    @Test
    void 환경변수_설정여부_확인 () {
        // when
        String encryptionKey = System.getenv(JasyptConfig.ENV_JASYPT_KEY);

        // then
        assertThat(encryptionKey).isNotNull();
        assertThat(encryptionKey).isNotEmpty();
    }

    @Test
    void 암호화_테스트 () {
        // given
        String source = "암호화 테스트";

        // when
        String encrypted = encryptor.encrypt(source);
        String decrypt = encryptor.decrypt(encrypted);

        System.out.println(encrypted);

        // then
        assertThat(encrypted).isNotNull();
        assertThat(encrypted).isNotEmpty();
        assertThat(decrypt).isEqualTo(source);
    }

}
