package ru.keepdoing.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dialog {
    private static Map<Long, Dialog> dialogs = new HashMap<>();
    private long chatId;
    private State state;

    private Dialog(long chatId) {
        this.chatId = chatId;
        this.state = new State(State.UserState.NEW);
    }

    public static Dialog getDialog(long chatId) {
        if (!dialogs.containsKey(chatId)) {
            Dialog dialog = new Dialog(chatId);
            dialogs.put(chatId, dialog);
            return dialog;
        }
        return dialogs.get(chatId);
    }

    public long getChatId() {
        return chatId;
    }

    public State getState() {
        return this.state;
    }

    public static boolean isExists(long chatId) {
        return dialogs.containsKey(chatId);
    }

    public static List<Long> getChats() {
        return new ArrayList<>(dialogs.keySet());
    }
}
