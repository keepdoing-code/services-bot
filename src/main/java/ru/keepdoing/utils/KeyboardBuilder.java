package ru.keepdoing.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardBuilder {
    private List<InlineKeyboardButton> column = new ArrayList<>();
    private List<List<InlineKeyboardButton>> rows = new ArrayList<>();

    public KeyboardBuilder addColumn(String name, String id){
        column.add(new InlineKeyboardButton().setText(name).setCallbackData(id));
        return this;
    }

    public KeyboardBuilder finalizeRow(){
        rows.add(column);
        column = new ArrayList<>();
        return this;
    }

    public InlineKeyboardMarkup endRowAndBuild(){
        rows.add(column);
        return new InlineKeyboardMarkup().setKeyboard(rows);
    }

}
