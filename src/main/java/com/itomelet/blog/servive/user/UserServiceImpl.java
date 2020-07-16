package com.itomelet.blog.servive.user;

import com.itomelet.blog.dao.UserRepository;
import com.itomelet.blog.po.User;
import com.itomelet.blog.util.MD5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
    }

    @Transactional
    @Override
    public Boolean updateName(User user, String username, String nickname) {
        try {
            userRepository.updateName(username, nickname, user.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    @Override
    public Boolean updatePassword(User user, String originalPassword, String newPassword) {
        //转换密码
        newPassword = MD5Utils.code(newPassword);
        //校验密码
        if (checkUser(user.getUsername(), originalPassword) == null) {
            return false;
        }
        userRepository.updatePassword(newPassword, user.getId());
        return true;

    }
}
