package jp.ne.yonem.login;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("ユーザ情報登録")
    void test1() {
        assertEquals(0, users.findAll().size());
        var pwd = faker.lorem().characters(6, 16);
        var user = new UserInfoEntity(faker.name().username(), faker.internet().emailAddress(), pwd);
        sut.createUser(user);

        var actual = users.findAll().getFirst();
        assertEquals(1, users.findAll().size());
        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getName(), actual.getName());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
        assertNotEquals(pwd, actual.getPassword()); // 変換後のパスワードで登録されていること
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