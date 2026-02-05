package com.ogui.condition;

import com.ogui.OGUIPlugin;
import com.ogui.condition.impl.*;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConditionFactory {
    private final OGUIPlugin plugin;

    public ConditionFactory(OGUIPlugin plugin) {
        this.plugin = plugin;
    }

    public List<Condition> parseConditions(ConfigurationSection section) {
        List<Condition> conditions = new ArrayList<>();
        if (section == null || !section.isList("conditions")) {
            return conditions;
        }

        List<?> conditionsList = section.getList("conditions");
        if (conditionsList == null) return conditions;

        for (Object obj : conditionsList) {
            if (!(obj instanceof ConfigurationSection)) continue;
            ConfigurationSection conditionSection = (ConfigurationSection) obj;
            Condition condition = parseCondition(conditionSection);
            if (condition != null) conditions.add(condition);
        }
        return conditions;
    }

    public Condition parseCondition(ConfigurationSection section) {
        String type = section.getString("type");
        if (type == null) return null;

        try {
            switch (type.toUpperCase(Locale.ROOT)) {
                case "VAULT_MONEY":
                case "MONEY":
                    return new VaultMoneyCondition(section.getDouble("amount", 0.0));

                case "OREO_CURRENCY":
                case "CURRENCY":
                    return new OreoCurrencyCondition(plugin,
                            section.getString("currency"),
                            section.getDouble("amount", 0.0));

                case "XP_LEVEL":
                case "LEVEL":
                    return new XpLevelCondition(section.getInt("amount", 0));

                case "XP_POINTS":
                case "XP":
                    return new XpPointsCondition(section.getInt("amount", 0));

                case "ITEM":
                    Material mat = Material.matchMaterial(section.getString("material"));
                    if (mat == null) return null;
                    return new ItemCondition(mat, section.getInt("amount", 1));

                case "ITEM_CUSTOM_MODEL":
                    Material mat2 = Material.matchMaterial(section.getString("material"));
                    if (mat2 == null) return null;
                    return new ItemCondition(mat2, section.getInt("amount", 1),
                            section.getInt("custom_model_data"));

                case "ITEMSADDER":
                    return new ItemsAdderCondition(plugin,
                            section.getString("item_id"),
                            section.getInt("amount", 1));

                case "NEXO":
                    return new NexoCondition(plugin,
                            section.getString("item_id"),
                            section.getInt("amount", 1));

                case "PERMISSION":
                    return new PermissionCondition(section.getString("permission"));

                case "WORLDGUARD_REGION":
                    return new WorldGuardRegionCondition(plugin,
                            section.getString("region"),
                            section.getBoolean("require_member", false));

                case "PLACEHOLDER":
                    return new PlaceholderCondition(plugin,
                            section.getString("placeholder"),
                            section.getString("operator", "=="),
                            section.getString("value"),
                            section.getString("error_message"));

                default:
                    plugin.getLogger().warning("Unknown condition type: " + type);
                    return null;
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to parse condition: " + e.getMessage());
            return null;
        }
    }
}