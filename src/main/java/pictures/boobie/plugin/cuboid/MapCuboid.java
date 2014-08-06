package pictures.boobie.plugin.cuboid;

import org.bukkit.Location;
import pictures.boobie.plugin.exceptions.InvalidMapPointsException;

public class MapCuboid extends Cuboid {

    public MapCuboid(Location pointOne, Location pointTwo) {
        super(pointOne, pointTwo);
        
        //If the provided cuboids aren't "flat"
        if (pointOne.getBlockX() != pointTwo.getBlockX() && pointOne.getBlockZ() != pointTwo.getBlockZ()) {
            throw new InvalidMapPointsException("Provided cuboid must be flat (x1 == x2 || z1 == z2)");
        }
    }
    
    /**
     * Returns points of side that has length > 1 (or X incase of both having legnth 1)
     * @return int[] of either X or Z points
     */
    public int[] getWidthPoints() {
        if (locOne.getBlockZ() - locTwo.getBlockZ() == 0) {
            return new int[]{ locOne.getBlockX(), locTwo.getBlockX() };
        } else {
            return new int[]{ locOne.getBlockZ(), locTwo.getBlockZ() };
        }
    }
    
    @Override
    public int getWidth() {
        int[] points = getWidthPoints();
        return points[1] - points[0] + 1;
    }
}
