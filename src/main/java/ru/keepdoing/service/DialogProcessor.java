package ru.keepdoing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.keepdoing.model.Dialog;
import ru.keepdoing.workflow.SimpleWorkflow;

@Component
public class DialogProcessor {
    private static Logger LOGGER = LoggerFactory.getLogger(DialogProcessor.class);

    //TODO need to change different workflows on relevant actions
    //TODO for example - when calling settings - change to settings workflow
    //TODO mark condition for change workflows

    public void processUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        LOGGER.info("Message received");

        if (!Dialog.isExists(chatId) || ("/start".equals(messageText) && !Dialog.isExists(chatId))) {
            processNewDialog(chatId);
        } else {
            processMessage(chatId, messageText);
        }

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

    private void processNewDialog(long chatId) {
        LOGGER.info("New user. Dialog created.");
        Dialog dialog = Dialog.createDialog(chatId);
        SimpleWorkflow.processStep(dialog, "");
    }

    private void processMessage(long chatId, String message) {
        SimpleWorkflow.processStep(Dialog.getDialog(chatId), message);
    }

    private boolean processCallbackQuery(String callbackData) {
        return true;
    }


    private EditMessageText getEditedMessage() {
        return new EditMessageText();
    }

    private InlineKeyboardMarkup getEditedKeyboard() {
        return new InlineKeyboardMarkup();
    }
}
