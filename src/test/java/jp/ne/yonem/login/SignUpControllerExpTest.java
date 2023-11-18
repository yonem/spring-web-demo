package jp.ne.yonem.login;

import com.github.javafaker.Faker;
import jp.ne.yonem.util.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Objects;

import static jp.ne.yonem.util.MessageUtil.E_000;
import static jp.ne.yonem.util.MessageUtil.getMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class SignUpControllerExpTest {

    private final MockMvc mockMvc;
    private final MessageSource messageSource;
    private final Faker faker = new Faker();

    @MockBean
    private SignUpService mock;

    @Autowired
    public SignUpControllerExpTest(
            MockMvc mockMvc
            , MessageSource messageSource
    ) {
        this.mockMvc = mockMvc;
        this.messageSource = messageSource;
    }

    @Test
    @DisplayName("ユーザ情報登録画面 - 予期せぬエラー")
    void test1() throws Exception {
        var userName = faker.name().username();
        var email = faker.internet().emailAddress();
        var pwd = faker.lorem().characters((16));

        var form = new LinkedMultiValueMap<String, String>();
        form.add("userName", userName);
        form.add("email", email);
        form.add("password", pwd);
        form.add("reEnter", pwd);

        doAnswer(invocation -> {
            throw new Exception();
        }).when(mock).createUser(any());

        var response = mockMvc.perform(MockMvcRequestBuilders.post(URL.SIGN_UP)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(form).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("msg", getMessage(messageSource, E_000)))
                .andExpect(model().attribute("isError", true))
                .andReturn();

        var actual = (SignUpForm) Objects.requireNonNull(response.getModelAndView()).getModel().get("form");
        assertEquals(userName, actual.userName);
        assertEquals(email, actual.email);
        assertEquals(pwd, actual.password);
        assertEquals(pwd, actual.reEnter);
    }
}