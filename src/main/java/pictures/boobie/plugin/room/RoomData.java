package pictures.boobie.plugin.room;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.cuboid.Cuboid;
import pictures.boobie.plugin.maps.MapFactory;
import pictures.boobie.plugin.tasks.UpdateImageTask;
import pictures.boobie.plugin.util.ImageUtil;
import pictures.boobie.plugin.web.SubredditData;

public class RoomData {
    
    private final BoobiePlugin plugin;
    private final Cuboid roomCuboid;
    
    private ArrayList<ItemFrame> frames;
    private SubredditData data = null;
    private int urlCount = 0;
    
    private RoomState state = RoomState.NONE;
    private Location roomSpawn;
    
    public RoomData(BoobiePlugin plugin, Location roomPointOne, Location roomPointTwo, Location roomSpawn, ArrayList<ItemFrame> frames) {
        this.plugin = plugin;
        roomCuboid = new Cuboid(roomPointOne, roomPointTwo);
        //mapCuboid = new MapCuboid(roomPointOne, roomPointTwo);
        this.roomSpawn = roomSpawn;
        this.frames = frames;
    }

    public Cuboid getRoomCuboid() {
        return roomCuboid;
    }
    
    public void setMainScreen() {
        if (state != RoomState.MAIN) {
            state = RoomState.MAIN;
            updateImage(ImageUtil.mainMaps);
        }
    }
    
    public void setLoadingScreen() {
        if (state != RoomState.LOADING) {
            state = RoomState.LOADING;
            updateImage(ImageUtil.loadingMaps);
        }
    }
    
    public void setSubredditData(SubredditData data) {
        this.data = data;
        this.getNextImage(this.getOwnerName());
    }
    
    public SubredditData getSubredditData() {
        return this.data;
    }

    public String getOwnerName() {
        return BoobiePlugin.roomOwner.getName();
    }

    public void setOwnerName(String ownerName) {
        BoobiePlugin.roomOwner.setName(ownerName);
    }

    public Location getRoomSpawn() {
        return roomSpawn;
    }

    public void setRoomSpawn(Location roomSpawn) {
        this.roomSpawn = roomSpawn;
    }

    public RoomState getState() {
        return state;
    }

    public void setState(RoomState state) {
        this.state = state;
    }
    
    public String getPrevImage(String playerName) {
        if (!playerName.equals(this.getOwnerName())) {
            return "You do not control the room" + ChatColor.DARK_GRAY + ".";
        }
        
        if (data == null) {
            return "No subreddit has been selected.";
        }
        
        setLoadingScreen();
        
        urlCount--;
        if (urlCount < 0) {
            urlCount = data.getUrls().size()-1;
        }

        String url = data.getUrls().get(urlCount);
        
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new UpdateImageTask(this, url));
        
        return "";
    }
    
    public String getNextImage(String playerName) {
        if (!playerName.equals(this.getOwnerName())) {
            return "You do not control the room" + ChatColor.DARK_GRAY + ".";
        }
        
        if (data == null) {
            return "No subreddit has been selected.";
        }
        
        setLoadingScreen();
        
        urlCount++;
        if (urlCount >= data.getUrls().size()) {
            urlCount = 0;
        }

        String url = data.getUrls().get(urlCount);
          
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new UpdateImageTask(this, url));
        
        return "";
    }
    
    public void updateImage(ArrayList<ItemStack> maps) {
        for (int i = 0; i < frames.size(); i++) {
            ItemStack map = maps.get(i);
            if (map == null) {
                break;
            }
            
            frames.get(i).setItem(map);
        }
    }
    
    public static enum RoomState {
        NONE, MAIN, LOADING, IMAGE;
    }
    
}
