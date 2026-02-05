package com.ogui.condition.impl;

import com.ogui.OGUIPlugin;
import com.ogui.condition.Condition;
import com.ogui.condition.ConditionType;
import com.ogui.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WorldGuardRegionCondition implements Condition {
    private final OGUIPlugin plugin;
    private final String regionId;
    private final boolean requireMember;

    public WorldGuardRegionCondition(OGUIPlugin plugin, String regionId, boolean requireMember) {
        this.plugin = plugin;
        this.regionId = regionId;
        this.requireMember = requireMember;
    }

    @Override
    public boolean check(Player player) {
        if (!isAvailable()) return false;

        try {
            Class<?> worldGuardClass = Class.forName("com.sk89q.worldguard.WorldGuard");
            Object worldGuard = worldGuardClass.getMethod("getInstance").invoke(null);
            Object platform = worldGuardClass.getMethod("getPlatform").invoke(worldGuard);
            Object regionContainer = platform.getClass().getMethod("getRegionContainer").invoke(platform);
            Object query = regionContainer.getClass().getMethod("createQuery").invoke(regionContainer);

            Class<?> bukkitAdapterClass = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter");
            Object location = bukkitAdapterClass.getMethod("adapt", org.bukkit.Location.class).invoke(null, player.getLocation());

            Object regionSet = query.getClass().getMethod("getApplicableRegions",
                    Class.forName("com.sk89q.worldedit.util.Location")).invoke(query, location);

            Iterable<?> regions = (Iterable<?>) regionSet;

            for (Object region : regions) {
                String id = (String) region.getClass().getMethod("getId").invoke(region);
                if (id.equalsIgnoreCase(regionId)) {
                    if (requireMember) {
                        Class<?> wgPluginClass = Class.forName("com.sk89q.worldguard.bukkit.WorldGuardPlugin");
                        Object wgPlugin = wgPluginClass.getMethod("inst").invoke(null);
                        Object localPlayer = wgPluginClass.getMethod("wrapPlayer", Player.class).invoke(wgPlugin, player);

                        boolean isMember = (boolean) region.getClass().getMethod("isMember",
                                Class.forName("com.sk89q.worldguard.domains.Association")).invoke(region, localPlayer);
                        boolean isOwner = (boolean) region.getClass().getMethod("isOwner",
                                Class.forName("com.sk89q.worldguard.domains.Association")).invoke(region, localPlayer);

                        return isMember || isOwner;
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            plugin.getLogger().warning("WorldGuard check failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean take(Player player) {
        return check(player);
    }

    @Override
    public String getErrorMessage(Player player) {
        return ColorUtil.color(requireMember ?
                "&cYou must be a member of region: &f" + regionId :
                "&cYou must be in region: &f" + regionId);
    }

    @Override
    public ConditionType getType() {
        return ConditionType.WORLDGUARD_REGION;
    }

    private boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
    }
}