package com.github.lilacbud.astonproject5.app.screen;

import com.github.lilacbud.astonproject5.app.App;
import com.github.lilacbud.astonproject5.ui.InputMenu;
import com.github.lilacbud.astonproject5.ui.InputMenuItem;
import com.github.lilacbud.astonproject5.ui.UIMenu;
import com.github.lilacbud.astonproject5.ui.UIScreen;

final class EntityField {
    private final String field;
    private final String fieldTitle;

    public EntityField(String field, String fieldTitle) {
        this.field = field;
        this.fieldTitle = fieldTitle;
    }

    public String getField() {
        return field;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }
}

public class ManualStrategyScreen implements UIScreen {
    private static EntityField[] fields = new EntityField[]{
        new EntityField("name", "Название"),
        new EntityField("yearOfRelease", "Год выхода"),
        new EntityField("hourLength", "Продолжительность"),
    };

    private UIMenu menu;

    private int currentFieldIndex = 0;

    public ManualStrategyScreen() {
        goToField(currentFieldIndex);

        var app = App.getInstance();

        // TODO: задать стратегию
        // var strategy = new ManualInputStrategy();
        app.useFillStrategy(null);

        try {
            app.fillData();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UIMenu getMenu() {
        return menu;
    }

    private UIScreen onInput(EntityField field, String text) {
        switch (field.getField()) {
            case "name" -> {
                // NOTE: Эмитируем ошибку валидации
                if (text.length() < 1) {
                    System.out.println("Название должно быть длиннее!!!");
                    return this;
                }

                // TODO: set field
            }

            case "yearOfRelease" -> {
                // TODO: set field
            }

            case "hourLength" -> {
                // TODO: set field
            }

            default -> {
                return this;
            }
        }

        if (currentFieldIndex < fields.length - 1) {
            goToField(currentFieldIndex + 1);
            return this;
        }

        // NOTE: Добавлено последнее поле, сохранить в колекцию
        var app = App.getInstance();
        app.addData(null);
        goToField(0);
        // Запросить еще
        return new AddMoreEntitiesScreen(this);
    }

    private void goToField(int fieldIndex) {
        var field = fields[fieldIndex];

        menu = new InputMenu(
            "%s:".formatted(field.getFieldTitle()),
            new InputMenuItem((str) -> onInput(field, str))
        );

        currentFieldIndex = fieldIndex;
    }
}
