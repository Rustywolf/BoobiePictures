package pictures.boobie.plugin.maps;

import java.awt.Image;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import pictures.boobie.plugin.exceptions.IllegalImageSizeException;

public class BoobRenderer extends MapRenderer {
    
    private Image image;
    private ArrayList<String> hasDrawnFor = new ArrayList<>();
    
    public void setImage(Image image) {
        if (image.getWidth(null) != 128 || image.getHeight(null) != 128) {
            throw new IllegalImageSizeException("Image size must be 128x128");
        }
        
        this.image = image;
        hasDrawnFor.clear();
    }
    
    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if (image != null && !hasDrawnFor.contains(player.getName())) {
            canvas.drawImage(0, 0, image);
            hasDrawnFor.add(player.getName());
            player.sendMap(map);
        }
    }
}
