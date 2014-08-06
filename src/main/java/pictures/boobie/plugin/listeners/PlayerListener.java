package pictures.boobie.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.gui.AnvilGUI;
import pictures.boobie.plugin.gui.AnvilGUI.AnvilClickEvent;
import pictures.boobie.plugin.gui.AnvilGUI.AnvilSlot;
import pictures.boobie.plugin.items.CustomItems;
import pictures.boobie.plugin.particles.Particles;
import pictures.boobie.plugin.web.SubredditData;

public class PlayerListener implements Listener {

    private BoobiePlugin plugin;

    public PlayerListener(BoobiePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null) {
                if (CustomItems.compareItems(item, CustomItems.YOUR_STICK)) {
                    try {
                        Particles.FIREWORKS_SPARK.sendToPlayer(event.getPlayer(), event.getPlayer().getLocation().add(event.getPlayer().getLocation().getDirection().setY(0).normalize()).add(0, 0.5, 0), .25f, .25f, .25f, 0, 20);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CustomItems.compareItems(item, CustomItems.NEXT_BUTTON)) {
                    try {
                        BoobiePlugin.room.getNextImage(event.getPlayer().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CustomItems.compareItems(item, CustomItems.PREV_BUTTON)) {
                    try {
                        BoobiePlugin.room.getPrevImage(event.getPlayer().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CustomItems.compareItems(item, CustomItems.PREV_BUTTON)) {
                    try {
                        BoobiePlugin.room.getPrevImage(event.getPlayer().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (CustomItems.compareItems(item, CustomItems.CHANGE_SUB)) {
                    AnvilGUI gui = new AnvilGUI(event.getPlayer(), plugin, (AnvilClickEvent ace) -> {

                        if (ace.getSlot() != AnvilSlot.OUTPUT) {
                            return;
                        }

                        if (ace.getName() != null && !ace.getName().equals("") && !ace.getName().equals("Enter Subreddit Name")) {
                            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> BoobiePlugin.room.setSubredditData(SubredditData.browse(ace.getName())));
                            ace.setWillClose(true);
                            ace.setWillDestroy(true);
                        }
                    });

                    gui.setSlot(AnvilSlot.INPUT_LEFT, CustomItems.createNew(new ItemStack(Material.PAPER)).withName("Enter Subreddit Name").getItemStack());
                    gui.open();
                }
            }
        }/* else {
         event.getPlayer().getInventory().addItem(CustomItems.YOUR_STICK);
         event.getPlayer().getInventory().addItem(CustomItems.NEXT_BUTTON);
         event.getPlayer().getInventory().addItem(CustomItems.PREV_BUTTON);
         }*/

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(BoobiePlugin.room.getRoomSpawn());

        PlayerInventory inv = event.getPlayer().getInventory();
        inv.clear();
        inv.setItem(0, CustomItems.PREV_BUTTON);
        inv.setItem(1, CustomItems.CHANGE_SUB);
        inv.setItem(2, CustomItems.NEXT_BUTTON);
        inv.setItem(8, CustomItems.YOUR_STICK);

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

}
