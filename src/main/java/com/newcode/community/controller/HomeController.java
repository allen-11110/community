package com.newcode.community.controller;


import com.newcode.community.entity.DiscussPost;
import com.newcode.community.entity.Page;
import com.newcode.community.entity.User;
import com.newcode.community.service.DiscussPostService;
import com.newcode.community.service.LikeService;
import com.newcode.community.service.UserService;
import com.newcode.community.utils.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){

        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list
                = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());//查询数据
        List<Map<String, Object>> discussPosts=new ArrayList<>();
        if(list.size()>0){
            for (DiscussPost post : list) {
                Map<String, Object> map=new HashMap<>();
                User user = userService.findUserById(post.getUserId());
                //点赞数量
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount",likeCount);
                map.put("post",post);
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
}
