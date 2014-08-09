package pictures.boobie.plugin.room;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R3.CraftChunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pictures.boobie.plugin.BoobiePlugin;
import pictures.boobie.plugin.items.CustomItems;
import pictures.boobie.plugin.web.SubredditData;

public class RoomManager {

    private final BoobiePlugin plugin;
    private final ArrayList<RoomData> rooms;
    private final ArrayList<Chunk> roomChunks;

    //Hardcoded since im lazy as fuck
    private final Location pictureOrigin;
    private final Location spawnOrigin;
    private final int buildingWidth = 256;
    private final int buildingDepth = 256;
    
    private final Inventory inventory;

    public RoomManager(BoobiePlugin plugin, World world) {
        rooms = new ArrayList<>();
        roomChunks = new ArrayList<>();

        this.plugin = plugin;
        this.pictureOrigin = new Location(world, -5, 251, 1);
        this.spawnOrigin = new Location(world, -3, 248, 4, 180, 0);
        
        SubredditData data = SubredditData.browse("boobs");
        
        for (int x = 0; x < 6; x++) {
            for (int z = 0; z < 5; z++) {
                RoomData roomData = createRoom(x, z);
                roomData.setSubredditData(data);
                roomData.setMainScreen();
                rooms.add(roomData);
                roomData.getRoomSpawn().getChunk().load();
                roomChunks.add(roomData.getRoomSpawn().getChunk());
            }
        }
        
        inventory = Bukkit.createInventory(null, 6*9, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Rooms");
        updateInventory();
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() { 
            
            @Override 
            public void run() {
                BoobiePlugin.roomManager.updateInventory();
            } 
        }, 5*20, 5*20);
    }

    private RoomData createRoom(int xOffset, int zOffset) {
        Location pictureOffset = pictureOrigin.clone().add(-xOffset * buildingWidth, 0, -zOffset * buildingDepth);

        ArrayList<ItemFrame> frames = new ArrayList<>();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Location spawnLoc = pictureOffset.clone().add(x, -y, 0);
                ItemFrame frame = (ItemFrame) pictureOffset.getWorld().spawnEntity(spawnLoc, EntityType.ITEM_FRAME);
                frame.setFacingDirection(BlockFace.SOUTH);
                frames.add(frame);
            }
        }
        return new RoomData(plugin, spawnOrigin.clone().add(-xOffset * buildingWidth, 0, -zOffset * buildingDepth), frames);
    }
    
    private void updateInventory() {
        int n = 0;
        for (int i = 0; i < 6*9; i++) {
            switch (i % 9) {
                case 0:
                case 1:
                case 7:
                case 8:
                    inventory.setItem(i, new ItemStack(Material.GLASS));
                    break;
                    
                default:
                    RoomData room = this.getRoom(n);
                    String subreddit = (room.getSubredditData() == null) ? "" : ChatColor.DARK_AQUA + "Subreddit: " + ChatColor.ITALIC + room.getSubredditData().getSearchString();
                    String owner = (room.getOwnerName().equals("")) ? "" : ChatColor.AQUA + "Owner: " + ChatColor.ITALIC + room.getOwnerName();
                    String viewerCount = ChatColor.GOLD + "Viewers: " + ChatColor.ITALIC + room.getViewers().size();
                    
                    inventory.setItem(i, CustomItems.createNew(new ItemStack((room.getViewers().isEmpty()) ? Material.STAINED_GLASS : Material.STAINED_CLAY, 1, (short) 3))
                            .withName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Room #" + (n+1))
                            .withLore("", subreddit, owner, viewerCount)
                            .getItemStack());
                    n++;
            }
        }
    }
    
    public Inventory getInventory() {
        return this.inventory;
    }
    
    public void handleChunkEvent(ChunkUnloadEvent event) {
        if (roomChunks.contains(event.getChunk())) {
            event.setCancelled(true);
        }
    }
    
    public RoomData assignToRoom(Player player) {
        RoomData room = null;
        for (RoomData roomData : rooms) {
            if (roomData.getViewers().isEmpty()) {
                roomData.addViewer(player.getName());
                roomData.setOwnerName(player.getName());
                room = roomData;
                break;
            }
        }
        
        if (room == null) {
            room = rooms.get((int) Math.floor(((float) rooms.size()) * Math.random()));
            room.addViewer(player.getName());
        }
        
        return room;
    }
    
    public void switchRoom(Player player, int roomID) {
        removePlayerFromRooms(player);
        RoomData room = addPlayerToRoom(player, roomID);
        ((CraftChunk)room.getRoomSpawn().getChunk()).getHandle().initLighting();
    }
    
    public RoomData addPlayerToRoom(Player player, int roomID) {
        RoomData room = rooms.get(roomID);
        room.addViewer(player.getName());
        
        if (room.getOwnerName().equals("")) {
            room.setOwnerName(player.getName());
        }
        
        player.teleport(room.getRoomSpawn());
        
        return room;
    }
    
    public void removePlayerFromRooms(Player player) {
        for (RoomData room : rooms) {
            if (room.getViewers().contains(player.getName())) {
                room.removeViewer(player.getName());
                
                if (room.getOwnerName().equals(player.getName())) {
                    room.setRandomOwner();
                }
                return;
            }
        }
    }
    
    public void setNewOwner(Player oldOwner) {
        for (RoomData room : rooms) {
            if (room.getOwnerName().equals(oldOwner.getName())) {
                return;
            }
        }
    }

    public RoomData getRoom(Player owner) {
        for (RoomData room : rooms) {
            if (room.getOwnerName().equals(owner.getName())) {
                return room;
            }
        }
        
        return null;
    }
    
    public RoomData getRoom(int roomID) {
        return rooms.get(roomID);
    }
    
    public RoomData getRoomViewing(Player viewer) {
        for (RoomData room : rooms) {
            if (room.getViewers().contains(viewer.getName())) {
                return room;
            }
        }
        
        return null;
    }
    
}
