package ru.keepdoing.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.keepdoing.model.Dialog;
import ru.keepdoing.model.State;
import ru.keepdoing.service.MessageSender;
import ru.keepdoing.utils.KeyboardBuilder;

public class SimpleWorkflow {
    private static Logger LOGGER = LoggerFactory.getLogger(SimpleWorkflow.class);

    public static void processStep(long chatId, String message) {
        State state = Dialog.getDialog(chatId).getState();
        LOGGER.info("Ordinary step. Dialog state: {}", state.getState());
        switch (state.getState()) {
            case NEW:
                LOGGER.info("User entered name: {}", message);
                state.setState(State.UserState.REGISTRATION);
                InlineKeyboardMarkup locationKbd = new KeyboardBuilder()
                        .addColumn("N", "North")
                        .finalizeRow()
                        .addColumn("E", "East")
                        .addColumn("+", "Center")
                        .addColumn("W", "West")
                        .finalizeRow()
                        .addColumn("S", "South")
                        .finalizeRow()
                        .build();
                MessageSender.sendMessage(chatId, "Choose you location:", locationKbd);
                break;
            case BUSY:
                break;
            case FREE:
                MessageSender.sendMessage(chatId, "Echo: " + message);
                break;
            case BLOCKED:
                break;
            default:
        }
    }

    public static void processCallbackStep(long chatId, String callbackData, long sourceMessageId) {
        State state = Dialog.getDialog(chatId).getState();
        LOGGER.info("Callback. Dialog state: {}", state.getState());
        switch (state.getState()) {
            case REGISTRATION:
                state.setState(State.UserState.FREE);
                state.setLocation(callbackData);
                LOGGER.info("Setting location: {}", callbackData);
                MessageSender.sendMessage(chatId, sourceMessageId, new EditMessageText().setText("Location set to " + callbackData), new InlineKeyboardMarkup());
                break;
            case FREE:
                LOGGER.info("Action: {}", callbackData);
                MessageSender.sendMessage(chatId, sourceMessageId, new EditMessageText().setText("You choose: " + callbackData), new InlineKeyboardMarkup());
                break;
            default:
        }
    }

    public static void processCommands(long chatId, String message) {
        LOGGER.info("Command: {}", message);
        switch (message) {
            case "/start":
                MessageSender.sendMessage(chatId, "Hello! Tell me your name?");
                break;
            case "/actions":
                InlineKeyboardMarkup actionsKbd = new KeyboardBuilder()
                        .addJustRow("Settings", "settings")
                        .addJustRow("My task", "tasks")
                        .build();
                MessageSender.sendMessage(chatId, "Choose action:", actionsKbd);
                break;
            default:
                MessageSender.sendMessage(chatId, "Unknown command. Try again.");
        }
    }
}
