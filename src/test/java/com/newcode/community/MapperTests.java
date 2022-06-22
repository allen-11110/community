package com.newcode.community;

import com.newcode.community.dao.DiscussPostMapper;
import com.newcode.community.dao.UserMapper;
import com.newcode.community.entity.DiscussPost;
import com.newcode.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Test
    public void testSelectByid(){
        System.out.println(userMapper.selectById(101));
    }

    @Test
    public void testSelectByUsername(){
        System.out.println(userMapper.selectByName("liubei"));
    }

    @Test
    public void testSelectByEmail(){
        System.out.println(userMapper.selectByEmeil("nowcoder131@sina.com"));
    }

    @Test
    public void testInsertUser(){
        User user=new User();
        user.setUsername("test");
        user.setPassword("123");
        user.setCreateTime(new Date());
        user.setEmail("999@.com");
        int i = userMapper.insertUser(user);
        System.out.println(i);
        System.out.println(user.getId());

    }

    @Test
    public void updateById(){
        userMapper.updateStatus(150,1);
        userMapper.updatePassword(150,"2");
        userMapper.updateHeaderUrl(150,"http://7777");
    }

    @Test
    public void selectDiscussPosts(){
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 5);
        System.out.println(list);
    }

    @Test
    public void selectDiscussPostRows(){
        int rows=discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }
}
