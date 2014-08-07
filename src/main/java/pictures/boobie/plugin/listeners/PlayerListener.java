package pictures.boobie.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.gui.AnvilGUI;
import pictures.boobie.plugin.gui.AnvilGUI.AnvilSlot;
import pictures.boobie.plugin.items.CustomItems;
import pictures.boobie.plugin.particles.Particles;
import pictures.boobie.plugin.tasks.SubredditAnvilClickHandler;

public class PlayerListener implements Listener {

    private BoobiePlugin plugin;

    public PlayerListener(BoobiePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null) {
                if (CustomItems.compareItems(item, CustomItems.YOUR_STICK)) {
                    try {
                        Particles.FIREWORKS_SPARK.sendToPlayer(player, player.getLocation().add(player.getLocation().getDirection().setY(0).normalize()).add(0, 0.5, 0), .25f, .25f, .25f, 0, 20);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CustomItems.compareItems(item, CustomItems.NEXT_BUTTON)) {
                    try {
                        String ret = BoobiePlugin.room.getNextImage(player.getName());
                        if (!ret.equals("")) {
                            player.sendMessage(BoobiePlugin.prefix + ret);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CustomItems.compareItems(item, CustomItems.PREV_BUTTON)) {
                    try {
                        String ret = BoobiePlugin.room.getPrevImage(player.getName());
                        if (!ret.equals("")) {
                            player.sendMessage(BoobiePlugin.prefix + ret);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CustomItems.compareItems(item, CustomItems.PASS_ROD) || CustomItems.compareItems(item, CustomItems.FORCE_PASS_ROD)) {
                    try {
                        BoobiePlugin.roomOwner.setNextName();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }  else if (CustomItems.compareItems(item, CustomItems.CHANGE_SUB)) {
                    if (!BoobiePlugin.roomOwner.getName().equals(player.getName())) {
                        player.sendMessage(BoobiePlugin.prefix + "You do not control the room" + ChatColor.DARK_GRAY + ".");
                        return;
                    }
                    
                    AnvilGUI gui = new AnvilGUI(player, plugin, new SubredditAnvilClickHandler(player, plugin));

                    gui.setSlot(AnvilSlot.INPUT_LEFT, CustomItems.createNew(new ItemStack(Material.PAPER)).withName("Enter Subreddit Name").getItemStack());
                    gui.open();
                }
            }
        }/* else {
         player.getInventory().addItem(CustomItems.YOUR_STICK);
         player.getInventory().addItem(CustomItems.NEXT_BUTTON);
         player.getInventory().addItem(CustomItems.PREV_BUTTON);
         }*/

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (player.isOp()) {
            player.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName());
        } else {
            player.setDisplayName(ChatColor.GOLD + player.getName());
        }
        
        player.teleport(BoobiePlugin.room.getRoomSpawn());

        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setItem(0, CustomItems.PREV_BUTTON);
        inv.setItem(1, CustomItems.CHANGE_SUB);
        inv.setItem(2, CustomItems.NEXT_BUTTON);
        inv.setItem(8, CustomItems.YOUR_STICK);
        
        if (player.isOp()) {
            inv.setItem(6, CustomItems.FORCE_PASS_ROD);
        }
        
        event.setJoinMessage("");
        Bukkit.broadcastMessage(BoobiePlugin.prefixJoin + player.getDisplayName() + ChatColor.YELLOW + " has joined" + ChatColor.DARK_GRAY + "!");
        
        if (BoobiePlugin.roomOwner.getName().equals("")) {
            BoobiePlugin.roomOwner.setNextName();
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        Bukkit.broadcastMessage(BoobiePlugin.prefixLeave + event.getPlayer().getDisplayName() + ChatColor.YELLOW + " has left" + ChatColor.DARK_GRAY + "!");
        
        if (BoobiePlugin.roomOwner.getName().equals(event.getPlayer().getName())) {
            BoobiePlugin.roomOwner.setNextName();
        }
    }
    
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.setFormat(ChatColor.translateAlternateColorCodes('&', "%s &e>> &r%s"));
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}
