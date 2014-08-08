package pictures.boobie.plugin.room;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.tasks.UpdateImageTask;
import pictures.boobie.plugin.tasks.UpdatePageTask;
import pictures.boobie.plugin.util.ImageUtil;
import pictures.boobie.plugin.web.SubredditData;

public class RoomData {

    private final BoobiePlugin plugin;

    private ArrayList<ItemFrame> frames;
    private SubredditData data = null;
    private int urlCount = 0;

    private RoomState state = RoomState.NONE;
    private Location roomSpawn;

    private String ownerName = "";
    private ArrayList<String> viewers = new ArrayList<>();

    public RoomData(BoobiePlugin plugin, Location roomSpawn, ArrayList<ItemFrame> frames) {
        this.plugin = plugin;
        //mapCuboid = new MapCuboid(roomPointOne, roomPointTwo);
        this.roomSpawn = roomSpawn;
        this.frames = frames;
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
        Bukkit.broadcastMessage(BoobiePlugin.prefix + "The subreddit has been set to " + ChatColor.BOLD + data.getSearchString());
    }

    public SubredditData getSubredditData() {
        return this.data;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String setRandomOwner() {
        if (this.viewers.size() > 0) {
            setOwnerName(this.viewers.get((int) Math.floor(((float) this.viewers.size()) * Math.random())));
        } else {
            setOwnerName("");
        }
        
        return getOwnerName();
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

    public World getWorld() {
        return this.roomSpawn.getWorld();
    }

    public List<ItemFrame> getItemFrames() {
        return (List<ItemFrame>)this.frames.clone();
    }
    
    public void addViewer(String viewerName) {
        if (!viewers.contains(viewerName)) {
            viewers.add(viewerName);
        }
    }

    public boolean removeViewer(String viewerName) {
        return viewers.remove(viewerName);
    }

    public List<String> getViewers() {
        return viewers;
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
            urlCount = 0;
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
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new UpdatePageTask(this));
        } else {
            String url = data.getUrls().get(urlCount);
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new UpdateImageTask(this, url));
        }
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
