package jp.ne.yonem.login;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@Transactional
class SignUpServiceTest {

    private final SignUpService sut;
    private final UsersDAO users;
    private final Faker faker = new Faker();

    @Autowired
    public SignUpServiceTest(
            SignUpService service
            , UsersDAO users
    ) {
        this.sut = service;
        this.users = users;
    }

    @AfterEach
    public void tearDown() {

        try {
            users.deleteAll();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("ユーザ情報登録")
    void test1() {
        assertEquals(0, users.findAll().size());
        var user = new UserInfoEntity(faker.name().username(), faker.internet().emailAddress(), faker.lorem().characters(6, 16));
        sut.createUser(user);
        assertEquals(1, users.findAll().size());
    }

    @Test
    @DisplayName("同じメールアドレスで登録した場合の例外発生")
    void test2() {
        var email = faker.internet().emailAddress();
        var user = new UserInfoEntity(faker.name().username(), email, faker.lorem().characters(6, 16));
        sut.createUser(user);

        var sameEmailUser = new UserInfoEntity(faker.name().username(), email, faker.lorem().characters(6, 16));

        assertThrows(DataIntegrityViolationException.class, () -> {
            sut.createUser(sameEmailUser);
        });
    }
}