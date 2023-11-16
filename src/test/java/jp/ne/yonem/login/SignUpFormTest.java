package jp.ne.yonem.login;

import com.github.javafaker.Faker;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignUpFormTest {

    private final int HOST_SIZE = 64;
    private final int DOMAIN_SIZE = 63;
    private final int NAME_SIZE = 40;
    private final int PASSWORD_MIN_SIZE = 6;
    private final int PASSWORD_MAX_SIZE = 16;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final Faker faker = new Faker();

    @Test
    @DisplayName("正常パターン")
    public void test1() {
        var username = faker.lorem().characters(NAME_SIZE);
        var email = faker.lorem().characters(HOST_SIZE).concat("@").concat(faker.lorem().characters(DOMAIN_SIZE));
        var pwd = faker.lorem().characters((PASSWORD_MIN_SIZE - 1)).concat("9");
        var form = new SignUpForm(username, email, pwd, pwd);
        doAssert(form, 0);

        pwd = faker.lorem().characters((PASSWORD_MAX_SIZE - 1)).toUpperCase().concat("9");
        form = new SignUpForm(username, email, pwd, pwd);
        doAssert(form, 0);
    }

    @Test
    @DisplayName("未入力")
    public void test2() {
        var form = new SignUpForm("", "", "", "");
        doAssert(form, 3);
    }

    @Test
    @DisplayName("桁数オーバー")
    public void test3() {
        var username = faker.lorem().characters((NAME_SIZE + 1));
        var email = faker.lorem().characters(HOST_SIZE).concat("@").concat(faker.lorem().characters(DOMAIN_SIZE));
        var pwd = faker.lorem().characters((PASSWORD_MAX_SIZE + 1));
        var form = new SignUpForm(username, email, pwd, pwd);
        doAssert(form, 2);
    }

    @Test
    @DisplayName("メールアドレス不正")
    public void test4() {
        var username = faker.lorem().characters(NAME_SIZE);
        var pwd = faker.lorem().characters((PASSWORD_MIN_SIZE - 1)).concat("9");

        // ドメイン桁数オーバー
        var email = faker.lorem().characters(HOST_SIZE).concat("@").concat(faker.lorem().characters((DOMAIN_SIZE + 1)));
        var form = new SignUpForm(username, email, pwd, pwd);
        doAssert(form, 1);

        // ホスト桁数オーバー
        email = faker.lorem().characters((HOST_SIZE + 1)).concat("@").concat(faker.lorem().characters(DOMAIN_SIZE));
        form = new SignUpForm(username, email, pwd, pwd);
        doAssert(form, 1);

        // フォーマットエラー（＠マークなし）
        email = faker.lorem().characters(HOST_SIZE).concat(faker.lorem().characters(DOMAIN_SIZE));
        form = new SignUpForm(username, email, pwd, pwd);
        doAssert(form, 1);
    }

    private void doAssert(SignUpForm form, int i) {
        var violations = validator.validate(form);

        for (var violation : violations) {
            var descriptor = violation.getConstraintDescriptor();
            var field = violation.getPropertyPath().toString();
            var annotation = descriptor.getAnnotation();
            System.out.println(annotation);

            switch (field) {
                case "userName":
                    if (annotation instanceof NotBlank) assertTrue(true);
                    if (annotation instanceof Size) assertTrue(true);
                    break;
                case "password":
                    if (annotation instanceof Size) assertTrue(true);
                    break;
                case "email":
                    if (annotation instanceof Size) assertTrue(true);
                    if (annotation instanceof NotBlank) assertTrue(true);
                    if (annotation instanceof Email) assertTrue(true);
                    break;
            }
        }
        assertEquals(i, violations.size());
    }
}