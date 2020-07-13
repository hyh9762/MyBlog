package com.itomelet.blog.servive.user;

import com.itomelet.blog.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
