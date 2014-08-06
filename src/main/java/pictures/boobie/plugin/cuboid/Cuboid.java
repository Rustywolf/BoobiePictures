package pictures.boobie.plugin.cuboid;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import pictures.boobie.plugin.exceptions.CrossDimensionalCuboidException;

public class Cuboid {

    protected final Location locOne;
    protected final Location locTwo;

    public Cuboid(Location pointOne, Location pointTwo) {
        if (!pointOne.getWorld().getName().equals(pointTwo.getWorld().getName())) {
            throw new CrossDimensionalCuboidException("Locations must be within the same world!");
        }

        locOne = new Location(pointOne.getWorld(),
                Math.min(pointOne.getBlockX(), pointTwo.getBlockX()),
                Math.min(pointOne.getBlockY(), pointTwo.getBlockY()),
                Math.min(pointOne.getBlockZ(), pointTwo.getBlockZ()));

        locTwo = new Location(pointOne.getWorld(),
                Math.max(pointOne.getBlockX(), pointTwo.getBlockX()),
                Math.max(pointOne.getBlockY(), pointTwo.getBlockY()),
                Math.max(pointOne.getBlockZ(), pointTwo.getBlockZ()));
    }

    public boolean contains(Location point) {
        if (!point.getWorld().equals(locOne.getWorld())) {
            return false;
        }

        return ((point.getX() >= locOne.getBlockX() && point.getX() < locTwo.getBlockX() + 1)
                && (point.getY() >= locOne.getBlockY() && point.getY() < locTwo.getBlockY() + 1)
                && (point.getZ() >= locOne.getBlockZ() && point.getZ() < locTwo.getBlockZ() + 1));
    }

    
    //Convinience method
    public Block[][][] getBlocks() {
        Block[][][] blocks = new Block
                [locTwo.getBlockX()-locOne.getBlockX()+1]
                [locTwo.getBlockY()-locOne.getBlockY()+1]
                [locTwo.getBlockZ()-locOne.getBlockZ()+1];
        
        World world = locOne.getWorld();
        
        for (int x = locOne.getBlockX(); x <= locTwo.getBlockX(); x++) {
            for (int y = locOne.getBlockY(); y <= locTwo.getBlockY(); y++) {
                for (int z = locOne.getBlockZ(); z <= locTwo.getBlockZ(); z++) {
                    blocks[x][y][z] = world.getBlockAt(x, y, z);
                }
            }
        }
        
        return blocks;
    }
    
    public List<Block> getBlockList() {
        List<Block> blocks = new ArrayList<>();
        World world = locOne.getWorld();
        
        for (int x = locOne.getBlockX(); x <= locTwo.getBlockX(); x++) {
            for (int y = locOne.getBlockY(); y <= locTwo.getBlockY(); y++) {
                for (int z = locOne.getBlockZ(); z <= locTwo.getBlockZ(); z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        
        return blocks;
    }
    
    public int getWidth() {
        return locTwo.getBlockX() - locOne.getBlockX() + 1;
    }
    
    public int getHeight() {
        return locTwo.getBlockY() - locOne.getBlockY() + 1;
    }
    
    public int getDepth() {
        return locTwo.getBlockZ() - locOne.getBlockZ() + 1;
    }

    public World getWorld() {
        return locOne.getWorld();
    }
    
}
