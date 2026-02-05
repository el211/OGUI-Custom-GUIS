// ========== FILE: condition/impl/ItemCondition.java ==========
package com.ogui.condition.impl;

import com.ogui.condition.Condition;
import com.ogui.condition.ConditionType;
import com.ogui.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCondition implements Condition {
    private final Material material;
    private final int amount;
    private final Integer customModelData;

    public ItemCondition(Material material, int amount) {
        this(material, amount, null);
    }

    public ItemCondition(Material material, int amount, Integer customModelData) {
        this.material = material;
        this.amount = amount;
        this.customModelData = customModelData;
    }

    @Override
    public boolean check(Player player) {
        return countItems(player) >= amount;
    }

    @Override
    public boolean take(Player player) {
        if (!check(player)) return false;
        int remaining = amount;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() != material) continue;
            if (customModelData != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta == null || !meta.hasCustomModelData() ||
                        meta.getCustomModelData() != customModelData) continue;
            }

            int itemAmount = item.getAmount();
            if (itemAmount >= remaining) {
                item.setAmount(itemAmount - remaining);
                return true;
            } else {
                remaining -= itemAmount;
                item.setAmount(0);
            }
        }
        return remaining <= 0;
    }

    @Override
    public String getErrorMessage(Player player) {
        int has = countItems(player);
        String itemName = material.name().toLowerCase().replace("_", " ");
        if (customModelData != null) itemName += " (CMD: " + customModelData + ")";
        return ColorUtil.color("&cInsufficient items! Need: &f" + amount + "x " + itemName +
                " &c(You have: &f" + has + "&c)");
    }

    @Override
    public ConditionType getType() {
        return customModelData != null ? ConditionType.ITEM_CUSTOM_MODEL : ConditionType.ITEM;
    }

    private int countItems(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() != material) continue;
            if (customModelData != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta == null || !meta.hasCustomModelData() ||
                        meta.getCustomModelData() != customModelData) continue;
            }
            count += item.getAmount();
        }
        return count;
    }
}

