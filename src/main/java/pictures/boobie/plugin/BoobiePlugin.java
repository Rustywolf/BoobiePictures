package pictures.boobie.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pictures.boobie.plugin.listeners.BlockListener;
import pictures.boobie.plugin.listeners.EntityListener;
import pictures.boobie.plugin.listeners.PlayerListener;
import pictures.boobie.plugin.maps.MapFactory;
import pictures.boobie.plugin.room.RoomManager;
import pictures.boobie.plugin.util.ImageUtil;

public class BoobiePlugin extends JavaPlugin {
    
    /**
     * Listeners
     */
    
    private BlockListener blockListener;
    private EntityListener entityListener;
    private PlayerListener playerListener;
    
    /**
     * Other
     */
    
    public static String prefix = ChatColor.translateAlternateColorCodes('&', "&3&lSBP &b>> &e");
    public static String prefixJoin = ChatColor.translateAlternateColorCodes('&', "&2&lSBP &a>> &e");
    public static String prefixLeave = ChatColor.translateAlternateColorCodes('&', "&4&lSBP &c>> &e");
    public static RoomManager roomManager;
    
    @Override
    public void onEnable() {
        
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof ItemFrame) {
                    entity.remove();
                }
            }
        }
        
        ImageUtil.loadingMaps = MapFactory.createMaps(ImageUtil.getImage(BoobiePlugin.class.getResourceAsStream("/loading.png")), Bukkit.getWorlds().get(0), 512, 512);
        ImageUtil.mainMaps = MapFactory.createMaps(ImageUtil.getImage(BoobiePlugin.class.getResourceAsStream("/main.png")), Bukkit.getWorlds().get(0), 512, 512);
        
        this.setupListeners();
        
        roomManager = new RoomManager(this, Bukkit.getWorlds().get(0));
    }
    
    @Override
    public void onDisable() {
        
    }
    
    public void setupListeners() {
        //Listeners
        blockListener = new BlockListener();
        entityListener = new EntityListener();
        playerListener = new PlayerListener(this);
        
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(blockListener, this);
        pm.registerEvents(entityListener, this);
        pm.registerEvents(playerListener, this);
    }
    
}
