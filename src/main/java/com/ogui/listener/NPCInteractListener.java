package com.ogui.listener;

import com.ogui.OGUIPlugin;
import com.ogui.gui.GuiDefinition;
import fr.elias.npcs.events.NPCInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


public class NPCInteractListener implements Listener {

    private final OGUIPlugin plugin;

    public NPCInteractListener(OGUIPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNPCInteract(NPCInteractEvent event) {
        Player player = event.getPlayer();
        int npcId = event.getNpcId();

        for (String guiId : plugin.getGuiRegistry().getGuiIds()) {
            GuiDefinition definition = plugin.getGuiRegistry().getGui(guiId);
            if (definition == null) continue;

            if (definition.getNpcId() != null && definition.getNpcId().intValue() == npcId) {
                definition.createInventory(plugin.getInventoryManager(), plugin).open(player);

                event.setCancelled(true);

                plugin.getLogger().info("Opened GUI '" + guiId + "' for player " +
                        player.getName() + " via NPC " + npcId);
                return;
            }
        }
    }
}