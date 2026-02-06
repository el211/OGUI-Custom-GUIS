package com.ogui.gui;

import com.ogui.OGUIPlugin;
import com.ogui.util.ColorUtil;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GuiDefinition {

    private final String id;
    private final String title;
    private final int rows;
    private final Map<Integer, GuiItem> items;
    private final List<String> commands;
    private final Integer npcId;

    @Deprecated
    public GuiDefinition(String id, String title, int rows, Map<Integer, GuiItem> items, List<String> commands) {
        this(id, title, rows, items, commands, null);
    }

    public GuiDefinition(String id, String title, int rows, Map<Integer, GuiItem> items, List<String> commands, Integer npcId) {
        this.id = id;
        this.title = title;
        this.rows = rows;
        this.items = items;
        this.commands = commands != null ? commands : Collections.emptyList();
        this.npcId = npcId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public Map<Integer, GuiItem> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public List<String> getCommands() {
        return Collections.unmodifiableList(commands);
    }


    public Integer getNpcId() {
        return npcId;
    }

    public boolean hasNpcBinding() {
        return npcId != null;
    }

    public SmartInventory createInventory(InventoryManager manager, OGUIPlugin plugin) {
        return SmartInventory.builder()
                .id("ogui:" + id)
                .provider(new GuiInventoryProvider(this, plugin, plugin.getItemProvider()))
                .size(rows, 9)
                .title(ColorUtil.color(title))
                .manager(manager)
                .build();
    }
}