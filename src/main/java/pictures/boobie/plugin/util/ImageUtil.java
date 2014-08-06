package pictures.boobie.plugin.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.bukkit.inventory.ItemStack;

public class ImageUtil {

    public static ArrayList<ItemStack> loadingMaps = new ArrayList<>();
    public static ArrayList<ItemStack> mainMaps = new ArrayList<>();
    
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    
    public static BufferedImage getImage(String url) {
        if (url == null || url.equals("")) {
            throw new IllegalArgumentException("Cannot pass empty URL!");
        }
        try {
            return (getImage(new URL(url)));
        } catch (Exception e) {
            return null;
        }
    }
    
    public static BufferedImage getImage(URL url) {
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return image;
    }
    
    public static BufferedImage getImage(InputStream stream) {
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return image;
    }
}
