package com.itomelet.blog.dao;

import com.itomelet.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);

    @Modifying
    @Query("update User set username=?1,nickname=?2 where id=?3")
    void updateName(String username, String nickname, Long id);

    @Modifying
    @Query("update User set password=?1 where id=?2")
    void updatePassword(String newPassword, Long id);
}
