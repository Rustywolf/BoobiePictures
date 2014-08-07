package pictures.boobie.plugin.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.gui.AnvilGUI;
import pictures.boobie.plugin.gui.AnvilGUI.AnvilClickEventHandler;

public class SubredditAnvilClickHandler implements AnvilClickEventHandler {

    private Player player;
    private BoobiePlugin plugin;
    
    public SubredditAnvilClickHandler(Player player, BoobiePlugin plugin) {
        this.player = player;
        this.plugin = plugin;
    }
    
    @Override
    public void onAnvilClick(AnvilGUI.AnvilClickEvent ace) {
        if (ace.getSlot() != AnvilGUI.AnvilSlot.OUTPUT) {
            return;
        }

        if (ace.getName() != null && !ace.getName().equals("") && !ace.getName().equals("Enter Subreddit Name")) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new AnvilUpdateSubredditTask(player, ace));
            ace.setWillClose(true);
            ace.setWillDestroy(true);
        }
    }

}
