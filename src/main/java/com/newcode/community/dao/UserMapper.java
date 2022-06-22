package com.newcode.community.dao;


import com.newcode.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmeil(String emeil);

    int insertUser(User user);

    int updateStatus(@Param("id")int id, @Param("status") int status);

    int updateHeaderUrl(@Param("id")int id, @Param("headerUrl")String headerUrl);

    int updatePassword(@Param("id")int id,@Param("password") String password);

}
