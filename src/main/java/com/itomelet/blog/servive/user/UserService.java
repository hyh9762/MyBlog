package com.itomelet.blog.servive.user;

import com.itomelet.blog.po.User;

public interface UserService {
    User checkUser(String username, String password);

    Boolean updateName(User user, String username, String nickname);

    Boolean updatePassword(User user, String originalPassword, String newPassword);
}
