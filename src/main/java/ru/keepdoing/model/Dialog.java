package ru.keepdoing.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dialog {
    private static Map<Long, Dialog> dialogs = new HashMap<>();
    private IState state;
    private long chatId;

    private Dialog(long chatId) {
        this.chatId = chatId;
    }

    public static Dialog createDialog(long chatId) {
        if (!dialogs.containsKey(chatId)) {
            return dialogs.put(chatId, new Dialog(chatId));
        }
        return dialogs.get(chatId);
    }

    public long getChatId() {
        return chatId;
    }

    public IState getState() {
        return this.state;
    }

    public void setState(IState state) {
        this.state = state;
    }

    public static boolean isExists(long chatId) {
        return dialogs.containsKey(chatId);
    }

    public static Dialog getDialog(long chatId) {
        return dialogs.get(chatId);
    }

    public static List<Long> getChats() {
        return new ArrayList<>(dialogs.keySet());
    }
}
