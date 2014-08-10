package pictures.boobie.plugin.listeners;

import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.gui.AnvilGUI;
import pictures.boobie.plugin.gui.AnvilGUI.AnvilSlot;
import pictures.boobie.plugin.items.CustomItems;
import pictures.boobie.plugin.maps.BoobRenderer;
import pictures.boobie.plugin.particles.Particles;
import pictures.boobie.plugin.room.RoomData;
import pictures.boobie.plugin.tasks.SubredditAnvilClickHandler;

public class PlayerListener implements Listener {

    private BoobiePlugin plugin;

    private final Pattern roomNumberSplit = Pattern.compile("#");

    public PlayerListener(BoobiePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        RoomData room = BoobiePlugin.roomManager.getRoom(player);

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null) {
                if (CustomItems.compareItems(item, CustomItems.YOUR_STICK)) {
                    try {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            Particles.FIREWORKS_SPARK.sendToPlayer(p, player.getLocation().add(player.getLocation().getDirection().setY(0).normalize()).add(0, 0.5, 0), .25f, .25f, .25f, 0, 20);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CustomItems.compareItems(item, CustomItems.NEXT_BUTTON)) {
                    try {
                        if (room == null) {
                            player.sendMessage(BoobiePlugin.prefix + "You do not own the room" + ChatColor.DARK_GRAY + ".");
                            return;
                        }
                        
                        String ret = room.getNextImage(player.getName());
                        if (!ret.equals("")) {
                            player.sendMessage(BoobiePlugin.prefix + ret);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CustomItems.compareItems(item, CustomItems.PREV_BUTTON)) {
                    try {
                        if (room == null) {
                            player.sendMessage(BoobiePlugin.prefix + "You do not own the room" + ChatColor.DARK_GRAY + ".");
                            return;
                        }
                        
                        
                        String ret = room.getPrevImage(player.getName());
                        if (!ret.equals("")) {
                            player.sendMessage(BoobiePlugin.prefix + ret);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (item.getType() == Material.COMPASS) {
                    player.openInventory(BoobiePlugin.roomManager.getInventory());
                } else if (CustomItems.compareItems(item, CustomItems.FORCE_PASS_ROD)) {
                    try {
                        BoobiePlugin.roomManager.getRoomViewing(player).setOwnerName(player.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CustomItems.compareItems(item, CustomItems.CHANGE_SUB)) {
                    if (room == null) {
                        player.sendMessage(BoobiePlugin.prefix + "You do not own the room" + ChatColor.DARK_GRAY + ".");
                        return;
                    }
                        
                    if (!room.getOwnerName().equals(player.getName())) {
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
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame) {
            event.setCancelled(true);
            return;
        }
        
        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }
        
        Player player = event.getPlayer();
        Player clicked = (Player) event.getRightClicked();
        ItemStack item = event.getPlayer().getItemInHand();
        
        if (item == null) {
            return;
        }
        
        RoomData room = BoobiePlugin.roomManager.getRoom(player);
        
        if (room == null) {
            return;
        }
        
        if (CustomItems.compareItems(item, CustomItems.PASS_ROD) || CustomItems.compareItems(item, CustomItems.FORCE_PASS_ROD)) {
            try {
                room.setOwnerName(clicked.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Inventory inv = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();

        if (inv == null || inv.getTitle() == null) {
            return;
        }
        
        if (inv.getTitle().equals(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Rooms")) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item != null) {
                if (item.getType() == Material.STAINED_GLASS || item.getType() == Material.STAINED_CLAY) {
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        String displayName = item.getItemMeta().getDisplayName();
                        try {
                            int roomID = Integer.valueOf(roomNumberSplit.split(ChatColor.stripColor(displayName))[1]) - 1;
                            BoobiePlugin.roomManager.switchRoom(player, roomID);
                            player.sendMessage(BoobiePlugin.prefix + ChatColor.GOLD + "You have been moved to " + displayName);
                        } catch (Exception e) {
                            player.sendMessage(BoobiePlugin.prefix + ChatColor.RED + "There was an error moving you. Please contact an administrator.");
                        }

                        player.closeInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) {
            player.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName());
        } else {
            player.setDisplayName(ChatColor.GOLD + player.getName());
        }

        RoomData room = BoobiePlugin.roomManager.assignToRoom(player);
        player.teleport(room.getRoomSpawn());

        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setItem(0, CustomItems.PREV_BUTTON);
        inv.setItem(1, CustomItems.CHANGE_SUB);
        inv.setItem(2, CustomItems.NEXT_BUTTON);
        inv.setItem(3, CustomItems.NAVIGATION_COMPASS);
        inv.setItem(8, CustomItems.YOUR_STICK);

        if (player.isOp()) {
            inv.setItem(6, CustomItems.FORCE_PASS_ROD);
        }

        event.setJoinMessage("");
        Bukkit.broadcastMessage(BoobiePlugin.prefixJoin + player.getDisplayName() + ChatColor.YELLOW + " has joined" + ChatColor.DARK_GRAY + "!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        Bukkit.broadcastMessage(BoobiePlugin.prefixLeave + event.getPlayer().getDisplayName() + ChatColor.YELLOW + " has left" + ChatColor.DARK_GRAY + "!");

        BoobiePlugin.roomManager.removePlayerFromRooms(event.getPlayer());
        BoobRenderer.removePlayerFromRenders(event.getPlayer());
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
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}
