package ru.keepdoing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.keepdoing.model.Dialog;

@Component
public class DialogProcessor {
    private static Logger LOGGER = LoggerFactory.getLogger(DialogProcessor.class);

    public void processUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        LOGGER.info("Message received");

        if (!Dialog.isExists(chatId) || "/start".equals(messageText)) {
            LOGGER.info("New user. Dialog created.");
            Dialog.createDialog(chatId);
            MessageSender.sendMessage(chatId, "Hello\nchat id = " + chatId + " \nuser - " + update.getMessage().getChat().getUserName());
            return;
        }

        MessageSender.sendMessage(chatId, "Echo: " + messageText);
    }


    public void processCallback(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long msgId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        Dialog dialog = Dialog.getDialog(chatId);

        if (dialog == null) {
            Dialog.createDialog(chatId);
        }

        this.processCallbackQuery(callbackData);
        EditMessageText editMessageText = this.getEditedMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = this.getEditedKeyboard();
        MessageSender.sendMessage(chatId, msgId, editMessageText, inlineKeyboardMarkup);
    }


    public boolean processCallbackQuery(String callbackData) {
        return true;
    }

    public EditMessageText getEditedMessage() {
        return new EditMessageText();
    }

    public InlineKeyboardMarkup getEditedKeyboard() {
        return new InlineKeyboardMarkup();
    }
}
