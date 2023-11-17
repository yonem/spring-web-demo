package jp.ne.yonem.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersDAO dao;

    @Autowired
    public CustomUserDetailsService(
            UsersDAO dao
    ) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = dao.findByEmail(email).getFirst();
        if (Objects.isNull(user)) throw new UsernameNotFoundException(null);

        return User.withUsername(user.getName())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}