package com.iwanvi.ad.controller;

import com.iwanvi.ad.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dongle
 * @desc 消息推送相关外部接口
 * @since 2022/7/11 10:33
 */
@RestController
@RequestMapping("message")
public class MessageController {

    private static final List<String> MESSAGE_LIST = new ArrayList<>();

    @Autowired
    private MessageService messageService;

    @RequestMapping("register")
    public SseEmitter register(String userId, HttpServletResponse response){
        response.setContentType("text/event-stream;charset=utf-8");
        return messageService.register(userId);
    }

    @RequestMapping("close")
    public String close(String userId){
        messageService.close(userId);
        return "OK";
    }

    @RequestMapping(value = "push",produces = {"text/plain;charset=utf-8"})
    @ResponseBody
    public String pushMessage(String message,String userId){
        if (userId == null)
            messageService.pushMessage(message);
        else
            messageService.pushMessage(userId,message);
        MESSAGE_LIST.add(message);
        return "OK";
    }

    @RequestMapping(value = "request-message")
    public String ajaxMessage(){
        if (MESSAGE_LIST.size() == 0){
            return "OK";
        }
        String message = MESSAGE_LIST.get(0);
        MESSAGE_LIST.remove(0);
        return message;
    }
}
