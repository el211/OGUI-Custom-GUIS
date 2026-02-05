package com.ogui.condition.impl;

import com.ogui.condition.Condition;
import com.ogui.condition.ConditionType;
import com.ogui.util.ColorUtil;
import org.bukkit.entity.Player;

public class PermissionCondition implements Condition {
    private final String permission;

    public PermissionCondition(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean check(Player player) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean take(Player player) {
        return check(player);
    }

    @Override
    public String getErrorMessage(Player player) {
        return ColorUtil.color("&cYou don't have permission: &f" + permission);
    }

    @Override
    public ConditionType getType() {
        return ConditionType.PERMISSION;
    }
}