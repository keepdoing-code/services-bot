package ru.keepdoing.workflow;

import ru.keepdoing.model.Dialog;
import ru.keepdoing.model.State;
import ru.keepdoing.service.MessageSender;

public class SimpleWorkflow {

    public static void processStep(Dialog dialog, String message) {
        State state = dialog.getState();
        switch (state.getState()) {
            case NEW:
                MessageSender.sendMessage(dialog.getChatId(), "Hello! Please register as service man.");
                state.setState(State.UserState.REGISTRATION);
                break;
            case REGISTRATION:

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
