package jp.ne.yonem.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignUpService {

    private final PasswordEncoder encoder;
    private final UsersDAO dao;

    @Autowired
    public SignUpService(
            PasswordEncoder encoder
            , UsersDAO dao
    ) {
        this.encoder = encoder;
        this.dao = dao;
    }

    @Transactional
    public void createUser(UserInfoEntity userInfo) {
        var password = encoder.encode(userInfo.getPassword());
        userInfo.setPassword(password);
        dao.save(userInfo);
    }
}
