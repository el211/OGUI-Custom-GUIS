package com.ogui.listener;

import com.ogui.OGUIPlugin;
import com.ogui.gui.GuiDefinition;
import fr.elias.npcs.events.NPCInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class NPCInteractListener implements Listener {

    private final OGUIPlugin plugin;

    public NPCInteractListener(OGUIPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNPCInteract(NPCInteractEvent event) {
        Player player = event.getPlayer();
        int npcId = event.getNpcId();

        for (String guiId : plugin.getGuiRegistry().getGuiIds()) {
            GuiDefinition definition = plugin.getGuiRegistry().getGui(guiId);

            if (definition == null) {
                continue;
            }

            if (definition.hasNpcBinding() && definition.isNpcBound(npcId)) {
                event.setCancelled(true);

                try {
                    definition.createInventory(plugin.getInventoryManager(), plugin).open(player);

                    Map<String, String> replacements = new HashMap<>();
                    replacements.put("gui", guiId);
                    replacements.put("id", String.valueOf(npcId));

                    plugin.getMessageManager().send(player, "npc.gui_opened", replacements);

                    Map<String, String> consoleReplacements = new HashMap<>();
                    consoleReplacements.put("player", player.getName());
                    consoleReplacements.put("gui", guiId);
                    consoleReplacements.put("id", String.valueOf(npcId));
                    plugin.getLogger().info(plugin.getMessageManager().getMessage("npc.interaction_logged", consoleReplacements));

                } catch (Exception e) {
                    plugin.getLogger().severe("Failed to open GUI '" + guiId + "' for NPC " + npcId + ": " + e.getMessage());
                    e.printStackTrace();
                }

                return;
            }
        }
    }
}