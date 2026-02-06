package com.ogui.items;

import com.ogui.OGUIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class DefaultItemProvider implements ItemProvider {

    private final OGUIPlugin plugin;

    public DefaultItemProvider(OGUIPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public ItemStack getItem(String material, String itemType, Integer customModelData) {
        if (itemType == null || itemType.equalsIgnoreCase("vanilla")) {
            return getVanillaItem(material, customModelData);
        } else if (itemType.equalsIgnoreCase("itemsadder")) {
            return getItemsAdderItem(material);
        } else if (itemType.equalsIgnoreCase("nexo")) {
            return getNexoItem(material);
        }

        plugin.getLogger().warning("Unknown item type: " + itemType + ", falling back to vanilla");
        return getVanillaItem(material, customModelData);
    }

    @Override
    public boolean isAvailable(String itemType) {
        if (itemType == null || itemType.equalsIgnoreCase("vanilla")) {
            return true;
        } else if (itemType.equalsIgnoreCase("itemsadder")) {
            return Bukkit.getPluginManager().getPlugin("ItemsAdder") != null;
        } else if (itemType.equalsIgnoreCase("nexo")) {
            return Bukkit.getPluginManager().getPlugin("Nexo") != null;
        }
        return false;
    }


    private ItemStack getVanillaItem(String materialName, Integer customModelData) {
        Material mat = Material.matchMaterial(materialName);
        if (mat == null) {
            plugin.getLogger().warning("Invalid material: " + materialName);
            return new ItemStack(Material.STONE);
        }

        ItemStack item = new ItemStack(mat);

        if (customModelData != null && customModelData > 0) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(customModelData);
                item.setItemMeta(meta);
            }
        }

        return item;
    }


    private ItemStack getItemsAdderItem(String itemId) {
        if (!isAvailable("itemsadder")) {
            plugin.getLogger().warning("ItemsAdder is not available!");
            return new ItemStack(Material.STONE);
        }

        try {
            Class<?> customStackClass = Class.forName("dev.lone.itemsadder.api.CustomStack");
            Object customStack = customStackClass.getMethod("getInstance", String.class).invoke(null, itemId);

            if (customStack == null) {
                plugin.getLogger().warning("ItemsAdder item not found: " + itemId);
                return new ItemStack(Material.STONE);
            }

            return (ItemStack) customStackClass.getMethod("getItemStack").invoke(customStack);

        } catch (Exception e) {
            plugin.getLogger().warning("Failed to get ItemsAdder item: " + itemId + " - " + e.getMessage());
            return new ItemStack(Material.STONE);
        }
    }


    private ItemStack getNexoItem(String itemId) {
        if (!isAvailable("nexo")) {
            plugin.getLogger().warning("Nexo is not available!");
            return new ItemStack(Material.STONE);
        }

        try {
            Class<?> nexoItemsClass = Class.forName("com.nexomc.nexo.api.NexoItems");
            Object itemBuilder = nexoItemsClass.getMethod("itemFromId", String.class).invoke(null, itemId);

            if (itemBuilder == null) {
                plugin.getLogger().warning("Nexo item not found: " + itemId);
                return new ItemStack(Material.STONE);
            }

            return (ItemStack) itemBuilder.getClass().getMethod("build").invoke(itemBuilder);

        } catch (Exception e) {
            plugin.getLogger().warning("Failed to get Nexo item: " + itemId + " - " + e.getMessage());
            return new ItemStack(Material.STONE);
        }
    }
}