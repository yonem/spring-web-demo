package jp.ne.yonem.login;

import jakarta.validation.Valid;
import jp.ne.yonem.util.MessageUtil;
import jp.ne.yonem.util.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * ユーザ情報登録Controller
 */
@Controller
@Slf4j
public class SignUpController {

    private final SignUpService service;
    private final MessageSource messageSource;

    @Autowired
    public SignUpController(
            SignUpService service
            , MessageSource messageSource
    ) {
        this.service = service;
        this.messageSource = messageSource;
    }

    @GetMapping(URL.SIGN_UP)
    String init(Model model) {
        model.addAttribute("form", new SignUpForm(null, null, null, null));
        return URL.SIGN_UP;
    }

    @PostMapping(URL.SIGN_UP)
    String doSave(Model model, @Valid SignUpForm form, BindingResult bindingResult) {
        String msg = null;
        var isError = false;

        try {
            if (bindingResult.hasErrors()) {
                var errors = bindingResult.getFieldErrors();

                for (var error : errors) {
                    model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
                }
                isError = true;

            } else {

                if (!form.password.equals(form.reEnter)) {
                    model.addAttribute("reEnterError", MessageUtil.getMessage(messageSource, MessageUtil.E_102));
                    isError = true;

                } else {
                    service.createUser(new UserInfoEntity(form.userName, form.email, form.password));
                    msg = MessageUtil.getMessage(messageSource, MessageUtil.I_101);
                    log.info(msg);
                }
            }

        } catch (DataIntegrityViolationException e) {
            msg = MessageUtil.getMessage(messageSource, MessageUtil.E_101);
            log.error(msg, e);
            isError = true;

        } catch (Exception e) {
            msg = MessageUtil.getMessage(messageSource, MessageUtil.E_000);
            log.error(msg, e);
            isError = true;
        }
        model.addAttribute("form", form);
        model.addAttribute("msg", msg);
        model.addAttribute("isError", isError);
        return URL.SIGN_UP;
    }
}
