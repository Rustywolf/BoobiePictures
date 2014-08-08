package pictures.boobie.plugin.util;

import org.bukkit.entity.Entity;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

public class PacketUtil {
    
    public static void killEntities(Player player, Entity[] entities) {
        
        int[] ids = new int[entities.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = ((CraftEntity)entities[i]).getHandle().getId();
        }
        
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ids);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
    
    public static void spawnEntities(Player player, Entity[] entities, int entityType) {
        
        PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
        for (Entity entity : entities) {
            Location loc = entity.getLocation();
            PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(((CraftEntity)entity).getHandle(), entityType);
            PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(((CraftEntity)entity).getHandle().getId(), ((CraftEntity)entity).getHandle().getDataWatcher(), true);
            //PacketPlayOutEntityTeleport telePacket = new PacketPlayOutEntityTeleport(((CraftEntity)entity).getHandle().getId(), (loc.getBlockX()-10)*32, loc.getBlockY()*32, (loc.getBlockZ()-20)*32, (byte) ((int) (loc.getYaw() * (256f/360f))), (byte) ((int) (loc.getPitch() * (256f/360f))));
            //PacketPlayOutEntityTeleport telePacket = new PacketPlayOutEntityTeleport(((CraftEntity)entity).getHandle());
            conn.sendPacket(packet);
            conn.sendPacket(metaPacket);
            //conn.sendPacket(telePacket);
        }
    }
    
    public static void hideItemFrames(Player player, ItemFrame[] entities) {
        
        int[] ids = new int[entities.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = ((CraftEntity)entities[i]).getHandle().getId();
        }
        
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ids);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
    
    public static void showItemFrames(Player player, ItemFrame[] entities) {
        
    }
    
}
