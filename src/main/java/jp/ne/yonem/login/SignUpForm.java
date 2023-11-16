package jp.ne.yonem.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpForm {

    public SignUpForm(String userName, String email, String password, String reEnter) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.reEnter = reEnter;
    }

    @NotBlank
    @Size(max = 40)
    String userName;

    @NotBlank
    @Email
    String email;

    @Size(min = 6, max = 16)
    String password;

    String reEnter;
}
