package com.newcode.community.service;


import com.newcode.community.dao.LoginTicketMapper;
import com.newcode.community.dao.UserMapper;
import com.newcode.community.entity.LoginTicket;
import com.newcode.community.entity.User;
import com.newcode.community.utils.CommunityConstant;
import com.newcode.community.utils.CommunityUtil;
import com.newcode.community.utils.Mail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Mail mail;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${community.path.domain}")
    private String domain;


    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 根据用户id查询数据
     *
     * @param userId
     * @return
     */
    public User findUserById(int userId) {
        return userMapper.selectById(userId);
    }


    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数不可为空！");
        }

        //验证账号不可为空
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不可为空!");
            return map;
        }
        //验证密码不可为空
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不可为空!");
            return map;
        }
        //验证邮箱不可为空
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("emailMsg", "邮箱不可为空!");
            return map;
        }

        //账号是否存在
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "账号已存在!");
            return map;
        }
        //邮箱是否存在
        User u1 = userMapper.selectByEmeil(user.getEmail());
        if (u1 != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }

        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setType(0);
        user.setStatus(0);
        user.setCreateTime(new Date());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));

        userMapper.insertUser(user);

        //发送邮箱
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);

        mail.sendMail(user.getEmail(), "激活账号", content);


        return map;
    }


    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            if (user.getStatus() == 1) {
                return ACTIVATION_REPECT;
            } else if (user.getActivationCode().equals(code)) {
                userMapper.updateStatus(userId, 1);
                return ACTIVATION_SUCCESS;
            } else {
                return ACTIVATION_FAILURE;
            }
        }
        return ACTIVATION_FAILURE;
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        //验证表单数据是否为空
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不可为空!");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不可为空!");
            return map;
        }
        //验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "账号不存在!");
            return map;
        }
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "账号未激活!");
            return map;
        }

        //验证密码是否正确
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        //生成凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * expiredSeconds));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }
}
