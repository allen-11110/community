package com.newcode.community.service;


import com.newcode.community.dao.UserMapper;
import com.newcode.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户id查询数据
     * @param userId
     * @return
     */
    public User findUserById(int userId){
        return userMapper.selectById(userId);
    }
}
