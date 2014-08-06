package pictures.boobie.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

public class EntityListener implements Listener {
    
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        event.getEntity().remove();
    }
}
