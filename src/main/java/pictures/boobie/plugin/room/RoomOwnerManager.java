package pictures.boobie.plugin.room;

import pictures.boobie.plugin.tasks.RoomOwnerNewNameTask;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.items.CustomItems;
import pictures.boobie.plugin.tasks.UpdatePlayerInventoryTask;

public class RoomOwnerManager {
    
    private final BoobiePlugin plugin;
    
    private String name = "";
    private ArrayList<String> hasOwned = new ArrayList<>();
    private ArrayList<String> toOwn =  new ArrayList<>();
    
    private int ownerCount = 0;
    
    public RoomOwnerManager(BoobiePlugin plugin) {
        this.plugin = plugin;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerCount() {
        return ownerCount;
    }
    
    public void setNextName() {
        String oldName = getName();
        
        String newName = "";
        Player newPlayer = null;
        do {
            if (toOwn.isEmpty()) {
                if(!resetLists()) {
                    setName("");
                    return;
                }
            }
            
            newName = toOwn.remove(0);
        } while ((newPlayer = Bukkit.getPlayerExact(newName)) == null);
        
        hasOwned.add(newName);
        setName(newName);
        
        if (!oldName.equals(newName)) {
            Player oldPlayer = Bukkit.getPlayerExact(oldName);
            if (oldPlayer != null) {
                oldPlayer.sendMessage(BoobiePlugin.prefix + "Your ownership has expired" + ChatColor.DARK_GRAY + "!");
                oldPlayer.getInventory().remove(Material.BLAZE_ROD);
                Bukkit.getScheduler().runTaskLater(plugin, new UpdatePlayerInventoryTask(oldPlayer), 5);
            }

            newPlayer.sendMessage(BoobiePlugin.prefix + "You have been granted ownership of the room for 5 minutes" + ChatColor.DARK_GRAY + "!");
            newPlayer.getInventory().setItem(7, CustomItems.PASS_ROD);
            
            Bukkit.getScheduler().runTaskLater(plugin, new UpdatePlayerInventoryTask(newPlayer), 5);
            
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.getName().equals(newName)) {
                    p.sendMessage(BoobiePlugin.prefix + newPlayer.getDisplayName() + ChatColor.YELLOW + " is now the owner" + ChatColor.DARK_GRAY + "!");
                }
            }
        }
        
        ownerCount++;
        Bukkit.getScheduler().runTaskLater(plugin, new RoomOwnerNewNameTask(newName, ownerCount), 5*60*20);
    }
    
    public boolean resetLists() {
        if (Bukkit.getOnlinePlayers().length <= 0) {
            return false;
        }
        
        hasOwned.clear();
        toOwn.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            toOwn.add(p.getName());
        }
        
        return true;
    }
    
}
