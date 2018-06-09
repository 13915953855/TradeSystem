package com.jason.trade.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.jason.trade.entity.UserInfo;
import com.jason.trade.repository.UserRepository;
import com.jason.trade.util.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(@SessionAttribute(WebSecurityConfig.SESSION_KEY) String account, Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "index";
    }

    @GetMapping(value="/index")
    public String index(Model model, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/trade/selfsupport")
    public String selfsupport(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "/trade/selfsupport";
    }
    @GetMapping("/trade/selfsupport/add")
    public String selfsupportadd(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "/trade/selfsupportadd";
    }
    @GetMapping("/trade/inout")
    public String inout(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "/trade/inout";
    }
    @GetMapping("/trade/agent")
    public String agent(Model model, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(WebSecurityConfig.SESSION_KEY);
        model.addAttribute("user", userInfo);
        return "/trade/agent";
    }
    @PostMapping("/login")
    public @ResponseBody Map<String, Object> loginPost(String username, String password, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        UserInfo userInfo = userRepository.findByAccount(username);
        if(userInfo != null && userInfo.getPasswd().equals(password)){
            // 设置session
            session.setAttribute(WebSecurityConfig.SESSION_KEY, userInfo);

            map.put("status", "1");
            map.put("message", "登录成功");
            return map;
        }else{
            map.put("status", "-1");
            map.put("message", "密码错误");
            return map;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 移除session
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        return "redirect:/login";
    }
}
