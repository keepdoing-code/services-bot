package ru.keepdoing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MessageSender {
    private static Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);
    private static AbsSender sender;

    public static void setSender(AbsSender absSender) {
        sender = absSender;
    }

    public static void sendMessage(long chatId, long messageId, EditMessageText editText, InlineKeyboardMarkup inlineKeyboard) {
        editText
                .setReplyMarkup(inlineKeyboard)
                .setChatId(chatId)
                .setMessageId((int) messageId);
        try {
            sender.execute(editText);
        } catch (TelegramApiException e) {
            LOGGER.error("Send edited message error - {}", e.getMessage());
        }
    }

    public static void sendMessage(long chatId, String message, InlineKeyboardMarkup inlineKeyboard) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(message)
                .setReplyMarkup(inlineKeyboard);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Send msg with Inline keyboard error - {}", e.getMessage());
        }
    }


    public static void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(message);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Send message error - {}", e.getMessage());
        }
    }
}
