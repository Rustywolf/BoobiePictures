package pictures.boobie.plugin.tasks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.exceptions.NoResultsException;
import pictures.boobie.plugin.gui.AnvilGUI.AnvilClickEvent;
import pictures.boobie.plugin.web.SubredditData;

public class AnvilUpdateSubredditTask implements Runnable {

    private Player player;
    private AnvilClickEvent ace;

    public AnvilUpdateSubredditTask(Player player, AnvilClickEvent ace) {
        this.player = player;
        this.ace = ace;
    }

    @Override
    public void run() {
        try {
            BoobiePlugin.room.setSubredditData(SubredditData.browse(ace.getName()));
        } catch (NoResultsException e) {
            player.sendMessage(BoobiePlugin.prefix + "That subreddit does not contain images" + ChatColor.DARK_GRAY + "!");
        }
    }

}
