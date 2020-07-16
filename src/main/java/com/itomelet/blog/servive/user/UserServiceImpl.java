package com.itomelet.blog.servive.user;

import com.itomelet.blog.dao.UserRepository;
import com.itomelet.blog.po.User;
import com.itomelet.blog.util.MD5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
    }
}
