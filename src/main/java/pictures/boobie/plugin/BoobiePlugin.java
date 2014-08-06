package pictures.boobie.plugin;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pictures.boobie.plugin.listeners.BlockListener;
import pictures.boobie.plugin.listeners.EntityListener;
import pictures.boobie.plugin.listeners.PlayerListener;
import pictures.boobie.plugin.maps.MapFactory;
import pictures.boobie.plugin.room.RoomData;
import pictures.boobie.plugin.util.ImageUtil;
import pictures.boobie.plugin.web.SubredditData;

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
    
    public static RoomData room;
    
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
        SubredditData data = SubredditData.browse("boobs");
        
        Location location = new Location(Bukkit.getWorlds().get(0), -1, 70, -10);
        ArrayList<ItemFrame> frames = new ArrayList<>();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Location spawnLoc = location.clone().add(x, -y, 0);
                ItemFrame frame = (ItemFrame)location.getWorld().spawnEntity(spawnLoc, EntityType.ITEM_FRAME);
                frame.setFacingDirection(BlockFace.SOUTH);
                frames.add(frame);
            }
        }
        
        room = new RoomData(this, location, location, new Location(location.getWorld(), 1, 67.5, -7, 180, 0), frames);
        room.setSubredditData(data);
        room.setMainScreen();
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
