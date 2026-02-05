package com.ogui.gui;

import com.ogui.OGUIPlugin;
import com.ogui.condition.Condition;
import com.ogui.items.ItemProvider;
import com.ogui.util.ColorUtil;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class GuiInventoryProvider implements InventoryProvider {

    private final GuiDefinition definition;
    private final OGUIPlugin plugin;
    private final ItemProvider itemProvider;
    private Economy economy = null;


    public GuiInventoryProvider(GuiDefinition definition, OGUIPlugin plugin, ItemProvider itemProvider) {
        this.definition = definition;
        this.plugin = plugin;
        this.itemProvider = itemProvider;
        setupEconomy();
    }

    @Deprecated
    public GuiInventoryProvider(GuiDefinition definition) {
        this.definition = definition;
        this.plugin = (OGUIPlugin) Bukkit.getPluginManager().getPlugin("OGUI");
        this.itemProvider = plugin != null ? plugin.getItemProvider() : null;
        setupEconomy();
    }

    private void setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        for (GuiItem guiItem : definition.getItems().values()) {

            ItemStack stack = getItemStack(guiItem);
            if (stack == null) {
                plugin.getLogger().warning("Failed to create item for slot " + guiItem.getSlot() + " in GUI " + definition.getId());
                continue;
            }

            ItemMeta meta = stack.getItemMeta();
            if (meta != null) {
                if (guiItem.getName() != null && !guiItem.getName().isEmpty()) {
                    meta.setDisplayName(ColorUtil.color(guiItem.getName()));
                }
                List<String> lore = guiItem.getLore();
                if (!lore.isEmpty()) {
                    meta.setLore(lore);
                }
                stack.setItemMeta(meta);
            }

            ClickableItem clickable = ClickableItem.of(stack, event -> {
                handleItemClick(player, guiItem);
            });

            contents.set(guiItem.getSlot(), clickable);
        }
    }


    private ItemStack getItemStack(GuiItem guiItem) {
        if (itemProvider != null) {
            ItemStack stack = itemProvider.getItem(
                    guiItem.getMaterial(),
                    guiItem.getItemType(),
                    guiItem.getCustomModelData()
            );

            if (stack != null) {
                return stack;
            }
        }

        Material material = Material.matchMaterial(guiItem.getMaterial());
        if (material == null) {
            plugin.getLogger().warning("Invalid material: " + guiItem.getMaterial());
            material = Material.STONE;
        }

        ItemStack stack = new ItemStack(material);

        if (guiItem.getCustomModelData() != null) {
            ItemMeta meta = stack.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(guiItem.getCustomModelData());
                stack.setItemMeta(meta);
            }
        }

        return stack;
    }


    private void handleItemClick(Player player, GuiItem guiItem) {
        if (guiItem.hasConditions()) {
            if (!checkAndConsumeConditions(player, guiItem)) {
                return;
            }
        }
        else {
            if (!checkLegacyRequirements(player, guiItem)) {
                return;
            }
        }

        executeCommands(player, guiItem);

        if (guiItem.isCloseOnClick()) {
            player.closeInventory();
        }
    }


    private boolean checkAndConsumeConditions(Player player, GuiItem guiItem) {
        List<Condition> conditions = guiItem.getConditions();

        for (Condition condition : conditions) {
            if (!condition.check(player)) {
                player.sendMessage(condition.getErrorMessage(player));
                player.closeInventory();
                return false;
            }
        }

        for (Condition condition : conditions) {
            if (!condition.take(player)) {
                player.sendMessage(ColorUtil.color("&c✖ Failed to complete transaction!"));
                player.closeInventory();
                return false;
            }
        }

        return true;
    }


    @SuppressWarnings("deprecation")
    private boolean checkLegacyRequirements(Player player, GuiItem guiItem) {
        if (guiItem.hasPrice() && economy != null) {
            double balance = economy.getBalance(player);
            if (balance < guiItem.getPrice()) {
                player.sendMessage(ColorUtil.color("&c✖ You don't have enough money!"));
                player.sendMessage(ColorUtil.color("&7Need: &f$" + String.format("%.2f", guiItem.getPrice())));
                player.sendMessage(ColorUtil.color("&7You have: &f$" + String.format("%.2f", balance)));
                player.closeInventory();
                return false;
            }

            economy.withdrawPlayer(player, guiItem.getPrice());
            player.sendMessage(ColorUtil.color("&a✔ Purchased for &f$" + String.format("%.2f", guiItem.getPrice())));
        }

        if (guiItem.hasRequirement()) {
            if (!player.hasPermission(guiItem.getRequirement())) {
                player.sendMessage(ColorUtil.color("&c✖ You don't have permission to purchase this item!"));
                player.sendMessage(ColorUtil.color("&7Required: &f" + guiItem.getRequirement()));
                player.closeInventory();
                return false;
            }
        }

        return true;
    }


    private void executeCommands(Player player, GuiItem guiItem) {
        for (String command : guiItem.getCommands()) {
            String resolved = command.replace("{player}", player.getName());

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), resolved);
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}