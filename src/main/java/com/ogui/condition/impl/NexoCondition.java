package com.ogui.condition.impl;

import com.ogui.OGUIPlugin;
import com.ogui.condition.Condition;
import com.ogui.condition.ConditionType;
import com.ogui.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NexoCondition implements Condition {
    private final OGUIPlugin plugin;
    private final String itemId;
    private final int amount;

    public NexoCondition(OGUIPlugin plugin, String itemId, int amount) {
        this.plugin = plugin;
        this.itemId = itemId;
        this.amount = amount;
    }

    @Override
    public boolean check(Player player) {
        if (!isAvailable()) return false;
        return countItems(player) >= amount;
    }

    @Override
    public boolean take(Player player) {
        if (!check(player)) return false;
        int remaining = amount;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;

            try {
                Class<?> nexoItemsClass = Class.forName("com.nexomc.nexo.api.NexoItems");
                String id = (String) nexoItemsClass.getMethod("idFromItem", ItemStack.class).invoke(null, item);

                if (id == null || !id.equals(itemId)) continue;

                int itemAmount = item.getAmount();
                if (itemAmount >= remaining) {
                    item.setAmount(itemAmount - remaining);
                    return true;
                } else {
                    remaining -= itemAmount;
                    item.setAmount(0);
                }
            } catch (Exception e) {
            }
        }
        return remaining <= 0;
    }

    @Override
    public String getErrorMessage(Player player) {
        return ColorUtil.color("&cInsufficient items! Need: &f" + amount + "x " + itemId +
                " &c(You have: &f" + countItems(player) + "&c)");
    }

    @Override
    public ConditionType getType() {
        return ConditionType.NEXO;
    }

    private boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("Nexo") != null;
    }

    private int countItems(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;

            try {
                Class<?> nexoItemsClass = Class.forName("com.nexomc.nexo.api.NexoItems");
                String id = (String) nexoItemsClass.getMethod("idFromItem", ItemStack.class).invoke(null, item);

                if (id != null && id.equals(itemId)) {
                    count += item.getAmount();
                }
            } catch (Exception e) {

            }
        }
        return count;
    }
}