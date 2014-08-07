package pictures.boobie.plugin.tasks;

import org.bukkit.entity.Player;

public class UpdatePlayerInventoryTask implements Runnable {
    
    private Player player;
    
    public UpdatePlayerInventoryTask(Player player) {
        this.player = player;
    }
    
    @Override
    public void run() {
        player.updateInventory();
    }
    
}
