package com.ogui.condition;

import org.bukkit.entity.Player;


public interface Condition {
    boolean check(Player player);
    boolean take(Player player);
    String getErrorMessage(Player player);
    ConditionType getType();
}