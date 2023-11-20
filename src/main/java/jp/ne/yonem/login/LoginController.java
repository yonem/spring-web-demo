package jp.ne.yonem.login;

import jp.ne.yonem.util.MessageUtil;
import jp.ne.yonem.util.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログインController
 */
@Controller
public class LoginController {

    private final MessageSource messageSource;

    @Autowired
    public LoginController(
            MessageSource messageSource
    ) {
        this.messageSource = messageSource;
    }

    @GetMapping(value = URL.NON)
    String initBlank() {
        return URL.LOGIN;
    }

    @GetMapping(value = URL.LOGIN)
    String init() {
        return URL.LOGIN;
    }

    @GetMapping(value = URL.LOGIN, params = URL.PARAM_ERROR)
    String doSave(Model model) {
        String msg = MessageUtil.getMessage(messageSource, MessageUtil.E_001);
        model.addAttribute("msg", msg);
        model.addAttribute("isError", true);
        return URL.LOGIN;
    }

    @GetMapping(value = URL.LOGIN, params = URL.PARAM_LOGOUT)
    String logout(Model model) {
        String msg = MessageUtil.getMessage(messageSource, MessageUtil.I_001);
        model.addAttribute("msg", msg);
        model.addAttribute("isError", false);
        return URL.LOGIN;
    }
}
