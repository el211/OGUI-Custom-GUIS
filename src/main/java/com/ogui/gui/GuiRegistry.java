package com.ogui.gui;

import com.ogui.OGUIPlugin;
import com.ogui.condition.Condition;
import com.ogui.condition.ConditionFactory;
import com.ogui.util.ColorUtil;
import java.io.File;
import java.util.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class GuiRegistry {

    private final OGUIPlugin plugin;
    private final Map<String, GuiDefinition> definitions = new LinkedHashMap<>();
    private final ConditionFactory conditionFactory;
    // Add to GuiRegistry class
    private final Map<Integer, String> npcToGuiMap = new HashMap<>();

    // Call this when loading/registering GUIs with NPC bindings
    public void cacheNpcBinding(int npcId, String guiId) {
        npcToGuiMap.put(npcId, guiId);
    }

    public String getGuiByNpc(int npcId) {
        return npcToGuiMap.get(npcId);
    }

    // Call this when unregistering GUIs
    public void clearNpcBindings() {
        npcToGuiMap.clear();
    }
    public GuiRegistry(OGUIPlugin plugin) {
        this.plugin = plugin;
        this.conditionFactory = new ConditionFactory(plugin);
    }

    public void reload() {
        definitions.clear();
        File file = new File(plugin.getDataFolder(), "guis.yml");

        if (!file.exists()) {
            plugin.getLogger().warning("guis.yml not found! Creating default file...");
            plugin.saveResource("guis.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection guisSection = config.getConfigurationSection("guis");

        if (guisSection == null) {
            plugin.getLogger().warning("No guis section found in guis.yml.");
            return;
        }

        for (String id : guisSection.getKeys(false)) {
            try {
                GuiDefinition definition = loadGuiDefinition(id, guisSection.getConfigurationSection(id));
                if (definition != null) {
                    definitions.put(id, definition);

                    if (definition.hasNpcBinding()) {
                        plugin.getLogger().info("GUI '" + id + "' bound to NPC ID: " + definition.getNpcId());
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to load GUI '" + id + "': " + e.getMessage());
                e.printStackTrace();
            }
        }

        plugin.getLogger().info("Loaded " + definitions.size() + " GUI definition(s).");
    }


    private GuiDefinition loadGuiDefinition(String id, ConfigurationSection guiSection) {
        if (guiSection == null) {
            return null;
        }

        String title = guiSection.getString("title", id);
        int rows = Math.min(6, Math.max(1, guiSection.getInt("rows", 1)));
        List<String> guiCommands = guiSection.getStringList("commands");

        Integer npcId = guiSection.isSet("npc_id") ? guiSection.getInt("npc_id") : null;

        Map<Integer, GuiItem> items = new LinkedHashMap<>();
        ConfigurationSection itemsSection = guiSection.getConfigurationSection("items");

        if (itemsSection != null) {
            for (String slotKey : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(slotKey);
                if (itemSection == null) {
                    continue;
                }

                Integer slot = parseSlot(slotKey);
                if (slot == null) {
                    plugin.getLogger().warning("Invalid slot '" + slotKey + "' in GUI " + id);
                    continue;
                }

                GuiItem guiItem = loadGuiItem(id, slot, itemSection);
                if (guiItem != null) {
                    items.put(slot, guiItem);
                }
            }
        }

        return new GuiDefinition(id, title, rows, items, guiCommands, npcId);
    }


    private GuiItem loadGuiItem(String guiId, int slot, ConfigurationSection itemSection) {
        try {
            String material = itemSection.getString("material", "STONE");
            String name = itemSection.getString("name", "");
            List<String> lore = ColorUtil.colorList(itemSection.getStringList("lore"));
            List<String> commands = itemSection.getStringList("commands");
            boolean closeOnClick = itemSection.getBoolean("close", false);

            double price = itemSection.getDouble("price", 0.0);
            String requirement = itemSection.getString("requirement", "");

            String itemType = itemSection.getString("item_type", "vanilla").toLowerCase();
            String itemId = itemSection.getString("item_id");
            Integer customModelData = itemSection.isSet("custom_model_data") ?
                    itemSection.getInt("custom_model_data") : null;

            List<Condition> conditions = parseConditions(itemSection);

            if (!conditions.isEmpty()) {
                plugin.getLogger().info("  Slot " + slot + ": Loaded " + conditions.size() + " condition(s)");
            }
            if (!"vanilla".equals(itemType)) {
                plugin.getLogger().info("  Slot " + slot + ": Using " + itemType + " item: " + itemId);
            }

            return new GuiItem(
                    slot,
                    material,
                    name,
                    lore,
                    commands,
                    closeOnClick,
                    price,
                    requirement,
                    conditions,
                    itemType,
                    itemId,
                    customModelData
            );

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load item at slot " + slot + " in GUI " + guiId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    private List<Condition> parseConditions(ConfigurationSection itemSection) {
        if (!itemSection.isList("conditions")) {
            return Collections.emptyList();
        }

        List<Condition> conditions = new ArrayList<>();
        List<?> conditionsList = itemSection.getList("conditions");

        if (conditionsList == null) {
            return Collections.emptyList();
        }

        for (Object condObj : conditionsList) {
            if (!(condObj instanceof Map)) {
                continue;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> condMap = (Map<String, Object>) condObj;

            YamlConfiguration tempConfig = new YamlConfiguration();
            for (Map.Entry<String, Object> entry : condMap.entrySet()) {
                tempConfig.set(entry.getKey(), entry.getValue());
            }

            try {
                Condition condition = conditionFactory.parseCondition(tempConfig);
                if (condition != null) {
                    conditions.add(condition);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to parse condition: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return conditions;
    }

    public GuiDefinition getGui(String id) {
        return definitions.get(id);
    }

    public Set<String> getGuiIds() {
        return Collections.unmodifiableSet(definitions.keySet());
    }

    private Integer parseSlot(String slotKey) {
        try {
            return Integer.parseInt(slotKey);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}