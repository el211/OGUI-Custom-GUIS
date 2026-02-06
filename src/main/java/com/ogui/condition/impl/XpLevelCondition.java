package com.ogui.condition.impl;

import com.ogui.OGUIPlugin;
import com.ogui.condition.Condition;
import com.ogui.condition.ConditionType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class XpLevelCondition implements Condition {
    private final OGUIPlugin plugin;
    private final int levels;

    public XpLevelCondition(OGUIPlugin plugin, int levels) {
        this.plugin = plugin;
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
        Map<String, String> replacements = new HashMap<>();
        replacements.put("amount", String.valueOf(levels));
        replacements.put("current", String.valueOf(player.getLevel()));

        return plugin.getMessageManager().getMessage("conditions.xp_level.insufficient", player, replacements);
    }

    @Override
    public ConditionType getType() {
        return ConditionType.XP_LEVEL;
    }
}