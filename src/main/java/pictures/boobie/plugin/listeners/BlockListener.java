package pictures.boobie.plugin.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.room.RoomManager;

public class BlockListener implements Listener {
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        RoomManager manager = BoobiePlugin.roomManager;
        if (manager != null) {
            BoobiePlugin.roomManager.handleChunkEvent(event);
        }
    }
    
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        RoomManager manager = BoobiePlugin.roomManager;
        if (manager != null) {
            BoobiePlugin.roomManager.handleChunkEntities(event);
        }
    }
    
}
