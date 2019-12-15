package ru.keepdoing.workflow;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.keepdoing.model.Dialog;
import ru.keepdoing.model.State;
import ru.keepdoing.service.MessageSender;
import ru.keepdoing.utils.KeyboardBuilder;

public class SimpleWorkflow {

    public static void processStep(Dialog dialog, String message) {
        State state = dialog.getState();
        switch (state.getState()) {
            case NEW:
                state.setState(State.UserState.REGISTRATION);
                MessageSender.sendMessage(dialog.getChatId(), "Hello! Please register as service man.");
                MessageSender.sendMessage(dialog.getChatId(), "Tell me where you are(location)? ");
                InlineKeyboardMarkup inlineKeyboardMarkup = new KeyboardBuilder()
                        .addColumn("N", "1")
                        .addColumn("E", "2")
                        .finalizeRow()
                        .addColumn("W", "3")
                        .addColumn("S", "4")
                        .endRowAndBuild();
                MessageSender.sendMessage(dialog.getChatId(),"", inlineKeyboardMarkup);
                break;
            case REGISTRATION:
                state.setLocation(message);
                break;
            case BUSY:
                break;
            case FREE:
                break;
            case BLOCKED:
                break;
            default:
        }
    }
}
