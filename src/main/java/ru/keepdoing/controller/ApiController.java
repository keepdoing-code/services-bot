package ru.keepdoing.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.keepdoing.model.Dialog;
import ru.keepdoing.service.MessageSender;

@RestController
public class ApiController {

    @RequestMapping("broadcast")
    public String broadcastMessage(@RequestParam("msg") String message) {
        for (Long chatId : Dialog.getChats()) {
            MessageSender.sendMessage(chatId, message);
        }
        return message;
    }
}
