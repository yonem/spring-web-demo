package jp.ne.yonem.login;

import com.github.javafaker.Faker;
import jp.ne.yonem.util.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Objects;

import static jp.ne.yonem.util.MessageUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class SignUpControllerTest {

    private final MockMvc mockMvc;
    private final MessageSource messageSource;
    private final Faker faker = new Faker();
    private final int PASSWORD_MAX_SIZE = 16;

    @Autowired
    public SignUpControllerTest(
            MockMvc mockMvc
            , MessageSource messageSource
    ) {
        this.mockMvc = mockMvc;
        this.messageSource = messageSource;
    }

    @Test
    @DisplayName("ユーザ情報登録画面 - 初期表示")
    void test1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL.SIGN_UP))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("ユーザ情報登録画面 - ユーザ登録成功")
    void test2() throws Exception {
        var userName = faker.name().username();
        var email = faker.internet().emailAddress();
        var pwd = faker.lorem().characters((PASSWORD_MAX_SIZE));

        var form = new LinkedMultiValueMap<String, String>();
        form.add("userName", userName);
        form.add("email", email);
        form.add("password", pwd);
        form.add("reEnter", pwd);

        var response = mockMvc.perform(MockMvcRequestBuilders.post(URL.SIGN_UP)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(form).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("msg", getMessage(messageSource, I_101)))
                .andExpect(model().attribute("isError", false))
                .andReturn();

        var actual = (SignUpForm) Objects.requireNonNull(response.getModelAndView()).getModel().get("form");
        assertEquals(userName, actual.userName);
        assertEquals(email, actual.email);
        assertEquals(pwd, actual.password);
        assertEquals(pwd, actual.reEnter);
    }

    @Test
    @DisplayName("ユーザ情報登録画面 - ユーザ重複")
    void test3() throws Exception {
        var userName = faker.name().username();
        var email = faker.internet().emailAddress();
        var pwd = faker.lorem().characters((PASSWORD_MAX_SIZE));

        var form = new LinkedMultiValueMap<String, String>();
        form.add("userName", userName);
        form.add("email", email);
        form.add("password", pwd);
        form.add("reEnter", pwd);

        // ユーザを登録する
        mockMvc.perform(MockMvcRequestBuilders.post(URL.SIGN_UP)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(form).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("msg", getMessage(messageSource, I_101)))
                .andExpect(model().attribute("isError", false));

        // 同じメールアドレスでユーザを登録する
        var response = mockMvc.perform(MockMvcRequestBuilders.post(URL.SIGN_UP)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(form).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("msg", getMessage(messageSource, E_101)))
                .andExpect(model().attribute("isError", true))
                .andReturn();

        var actual = (SignUpForm) Objects.requireNonNull(response.getModelAndView()).getModel().get("form");
        assertEquals(userName, actual.userName);
        assertEquals(email, actual.email);
        assertEquals(pwd, actual.password);
        assertEquals(pwd, actual.reEnter);
    }

    @Test
    @DisplayName("ユーザ情報登録画面 - パスワード再入力不一致")
    void test4() throws Exception {
        var userName = faker.name().username();
        var email = faker.internet().emailAddress();
        var pwd = faker.lorem().characters((PASSWORD_MAX_SIZE));

        var form = new LinkedMultiValueMap<String, String>();
        form.add("userName", userName);
        form.add("email", email);
        form.add("password", pwd);
        form.add("reEnter", pwd.concat("x"));

        var response = mockMvc.perform(MockMvcRequestBuilders.post(URL.SIGN_UP)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(form).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("reEnterError", getMessage(messageSource, E_102)))
                .andExpect(model().attribute("isError", true))
                .andReturn();

        var actual = (SignUpForm) Objects.requireNonNull(response.getModelAndView()).getModel().get("form");
        assertEquals(userName, actual.userName);
        assertEquals(email, actual.email);
        assertEquals(pwd, actual.password);
        assertEquals(pwd.concat("x"), actual.reEnter);
    }
}