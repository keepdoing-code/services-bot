package ru.keepdoing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.keepdoing.model.Dialog;
import ru.keepdoing.workflow.SimpleWorkflow;

@Component
public class DialogProcessor {
    private static Logger LOGGER = LoggerFactory.getLogger(DialogProcessor.class);

    public void processUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        LOGGER.info("Received message: {}", messageText);

        if (!Dialog.isExists(chatId)) {
            LOGGER.info("New user. Dialog created.");
            Dialog.getDialog(chatId);
        }

        if (messageText.startsWith("/")) {
            LOGGER.info("Command message process: {}", messageText);
            SimpleWorkflow.processCommands(chatId, messageText);
        } else {
            LOGGER.info("Simple message process: {}", messageText);
            SimpleWorkflow.processStep(chatId, messageText);
        }

    }

    public void processCallbackUpdate(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long msgId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        Dialog.getDialog(chatId);
        LOGGER.info("Received callback message: {}", callbackData);
        SimpleWorkflow.processCallbackStep(chatId, callbackData, msgId);
    }
}
