package com.ogui.condition.impl;

import com.ogui.condition.Condition;
import com.ogui.condition.ConditionType;
import com.ogui.util.ColorUtil;
import org.bukkit.entity.Player;

public class XpPointsCondition implements Condition {
    private final int points;

    public XpPointsCondition(int points) {
        this.points = points;
    }

    @Override
    public boolean check(Player player) {
        return player.getTotalExperience() >= points;
    }

    @Override
    public boolean take(Player player) {
        if (check(player)) {
            player.setTotalExperience(player.getTotalExperience() - points);
            updateExperience(player);
            return true;
        }
        return false;
    }

    @Override
    public String getErrorMessage(Player player) {
        return ColorUtil.color("&cInsufficient XP! Need: &f" + points +
                " &c(You have: &f" + player.getTotalExperience() + "&c)");
    }

    @Override
    public ConditionType getType() {
        return ConditionType.XP_POINTS;
    }

    private void updateExperience(Player player) {
        int totalExp = player.getTotalExperience();
        int level = 0;
        int expForLevel = 0;

        while (expForLevel <= totalExp) {
            int expToNext = getExpToNextLevel(level);
            if (expForLevel + expToNext > totalExp) break;
            expForLevel += expToNext;
            level++;
        }

        player.setLevel(level);
        player.setExp((float) (totalExp - expForLevel) / getExpToNextLevel(level));
    }

    private int getExpToNextLevel(int level) {
        if (level <= 15) return 2 * level + 7;
        else if (level <= 30) return 5 * level - 38;
        else return 9 * level - 158;
    }
}