package pictures.boobie.plugin.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.exceptions.NoResultsException;
import pictures.boobie.plugin.gui.AnvilGUI.AnvilClickEvent;
import pictures.boobie.plugin.room.RoomData;
import pictures.boobie.plugin.web.SubredditData;

public class AnvilUpdateSubredditTask implements Runnable {

    private BoobiePlugin plugin;
    private Player player;
    private AnvilClickEvent ace;

    public AnvilUpdateSubredditTask(BoobiePlugin plugin, Player player, AnvilClickEvent ace) {
        this.plugin = plugin;
        this.player = player;
        this.ace = ace;
    }

    @Override
    public void run() {
        try {
            RoomData room = BoobiePlugin.roomManager.getRoom(player);
            if (room != null) {
                room.setUrlCount(0);
                SubredditData data = SubredditData.browse(ace.getName());
                if (data != null) {
                    room.setSubredditData(data);
                } else {
                    Bukkit.getScheduler().runTask(plugin, new Runnable() { 
                        @Override 
                        public void run() { 
                            player.sendMessage(BoobiePlugin.prefix + ChatColor.RED + "The \"" + ChatColor.BOLD + ace.getName() + ChatColor.RED + "\" subreddit does not exist" + ChatColor.DARK_GRAY + ".");
                            player.sendMessage(BoobiePlugin.prefix + ChatColor.RED + "Make sure to check the spelling" + ChatColor.DARK_GRAY + ".");
                            player.sendMessage(BoobiePlugin.prefix + ChatColor.RED + "(The subreddit must contain images)" + ChatColor.DARK_GRAY + ".");
                        } 
                    });
                }
            }
        } catch (NoResultsException e) {
            player.sendMessage(BoobiePlugin.prefix + ChatColor.RED + "That subreddit does not contain images" + ChatColor.DARK_GRAY + "!");
        }
    }

}
