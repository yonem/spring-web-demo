package jp.ne.yonem.login;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersDAO extends JpaRepository<UserInfoEntity, Integer> {

    List<UserInfoEntity> findByEmail(String email);
}
