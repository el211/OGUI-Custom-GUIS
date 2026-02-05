package com.ogui.condition.impl;

import com.ogui.OGUIPlugin;
import com.ogui.condition.Condition;
import com.ogui.condition.ConditionType;
import com.ogui.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderCondition implements Condition {
    private final OGUIPlugin plugin;
    private final String placeholder;
    private final String operator;
    private final String value;
    private final String errorMessage;

    public PlaceholderCondition(OGUIPlugin plugin, String placeholder, String operator,
                                String value, String errorMessage) {
        this.plugin = plugin;
        this.placeholder = placeholder;
        this.operator = operator;
        this.value = value;
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean check(Player player) {
        if (!isAvailable()) return false;

        try {
            Class<?> papiClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            String parsed = (String) papiClass.getMethod("setPlaceholders", Player.class, String.class)
                    .invoke(null, player, placeholder);

            return compare(parsed, operator, value);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean take(Player player) {
        return check(player);
    }

    @Override
    public String getErrorMessage(Player player) {
        if (errorMessage != null && !errorMessage.isEmpty()) {
            String parsed = errorMessage;
            if (isAvailable()) {
                try {
                    Class<?> papiClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
                    parsed = (String) papiClass.getMethod("setPlaceholders", Player.class, String.class)
                            .invoke(null, player, parsed);
                } catch (Exception e) {
                    // Use unparsed message
                }
            }
            return ColorUtil.color(parsed);
        }
        return ColorUtil.color("&cCondition not met: &f" + placeholder + " " + operator + " " + value);
    }

    @Override
    public ConditionType getType() {
        return ConditionType.PLACEHOLDER;
    }

    private boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    private boolean compare(String actual, String operator, String expected) {
        try {
            switch (operator.toLowerCase()) {
                case "==":
                case "equals":
                    return actual.equals(expected);
                case "!=":
                case "not_equals":
                    return !actual.equals(expected);
                case "contains":
                    return actual.contains(expected);
                case ">":
                    return Double.parseDouble(actual) > Double.parseDouble(expected);
                case "<":
                    return Double.parseDouble(actual) < Double.parseDouble(expected);
                case ">=":
                    return Double.parseDouble(actual) >= Double.parseDouble(expected);
                case "<=":
                    return Double.parseDouble(actual) <= Double.parseDouble(expected);
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}