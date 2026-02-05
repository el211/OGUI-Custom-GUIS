package com.ogui.gui;

import com.ogui.condition.Condition;

import java.util.Collections;
import java.util.List;

/**
 * Represents an item in a GUI with enhanced features:
 * - Multiple condition types (OreoEssentials currency, XP, items, permissions, etc.)
 * - Custom item support (ItemsAdder, Nexo)
 * - Custom model data support
 * - Backwards compatible with legacy price/requirement system
 */
public class GuiItem {

    private final int slot;
    private final String material;
    private final String name;
    private final List<String> lore;
    private final List<String> commands;
    private final boolean closeOnClick;

    private final double price;
    private final String requirement;

    // Enhanced fields
    private final List<Condition> conditions;
    private final String itemType;
    private final String itemId;
    private final Integer customModelData;


    @Deprecated
    public GuiItem(int slot, String material, String name, List<String> lore, List<String> commands,
                   boolean closeOnClick, double price, String requirement) {
        this(slot, material, name, lore, commands, closeOnClick, price, requirement,
                Collections.emptyList(), "vanilla", null, null);
    }

    /**
     * @param slot Inventory slot (0-53)
     * @param material Material name or fallback material
     * @param name Display name of the item
     * @param lore Lore lines (already colored)
     * @param commands Commands to execute on click
     * @param closeOnClick Whether to close GUI after click
     * @param price Legacy Vault economy price (0 = no price)
     * @param requirement Legacy permission requirement (null/empty = no requirement)
     * @param conditions List of conditions to check (new system)
     * @param itemType Type of item: "vanilla", "itemsadder", or "nexo"
     * @param itemId Item ID for ItemsAdder/Nexo items
     * @param customModelData Custom model data for vanilla items
     */
    public GuiItem(int slot, String material, String name, List<String> lore, List<String> commands,
                   boolean closeOnClick, double price, String requirement, List<Condition> conditions,
                   String itemType, String itemId, Integer customModelData) {
        this.slot = slot;
        this.material = material;
        this.name = name;
        this.lore = lore == null ? Collections.emptyList() : lore;
        this.commands = commands == null ? Collections.emptyList() : commands;
        this.closeOnClick = closeOnClick;
        this.price = price;
        this.requirement = requirement;
        this.conditions = conditions == null ? Collections.emptyList() : conditions;
        this.itemType = itemType == null ? "vanilla" : itemType;
        this.itemId = itemId;
        this.customModelData = customModelData;
    }


    public int getSlot() {
        return slot;
    }

    public String getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return Collections.unmodifiableList(lore);
    }

    public List<String> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public boolean isCloseOnClick() {
        return closeOnClick;
    }


    @Deprecated
    public double getPrice() {
        return price;
    }

    @Deprecated
    public String getRequirement() {
        return requirement;
    }

    @Deprecated
    public boolean hasPrice() {
        return price > 0;
    }


    @Deprecated
    public boolean hasRequirement() {
        return requirement != null && !requirement.isEmpty();
    }


    public List<Condition> getConditions() {
        return Collections.unmodifiableList(conditions);
    }


    public boolean hasConditions() {
        return !conditions.isEmpty();
    }


    public String getItemType() {
        return itemType;
    }


    public String getItemId() {
        return itemId;
    }


    public Integer getCustomModelData() {
        return customModelData;
    }


    public boolean isCustomItem() {
        return "itemsadder".equalsIgnoreCase(itemType) || "nexo".equalsIgnoreCase(itemType);
    }


    public boolean isVanillaItem() {
        return "vanilla".equalsIgnoreCase(itemType);
    }

    @Override
    public String toString() {
        return "GuiItem{" +
                "slot=" + slot +
                ", material='" + material + '\'' +
                ", itemType='" + itemType + '\'' +
                ", conditions=" + conditions.size() +
                '}';
    }
}