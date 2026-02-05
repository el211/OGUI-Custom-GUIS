package com.ogui.condition.impl;

import com.ogui.condition.Condition;
import com.ogui.condition.ConditionType;
import com.ogui.util.ColorUtil;
import org.bukkit.entity.Player;

public class XpLevelCondition implements Condition {
    private final int levels;

    public XpLevelCondition(int levels) {
        this.levels = levels;
    }

    @Override
    public boolean check(Player player) {
        return player.getLevel() >= levels;
    }

    @Override
    public boolean take(Player player) {
        if (check(player)) {
            player.setLevel(player.getLevel() - levels);
            return true;
        }
        return false;
    }

    @Override
    public String getErrorMessage(Player player) {
        return ColorUtil.color("&cInsufficient XP levels! Need: &f" + levels +
                " &c(You have: &f" + player.getLevel() + "&c)");
    }

    @Override
    public ConditionType getType() {
        return ConditionType.XP_LEVEL;
    }
}