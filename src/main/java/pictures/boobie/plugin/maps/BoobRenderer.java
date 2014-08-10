package pictures.boobie.plugin.maps;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import pictures.boobie.plugin.exceptions.IllegalImageSizeException;

public class BoobRenderer extends MapRenderer {

    private Image image;
    private static HashMap<Integer, ArrayList<String>> hasDrawnFor = new HashMap<>();

    public void setImage(Image image) {
        if (image.getWidth(null) != 128 || image.getHeight(null) != 128) {
            throw new IllegalImageSizeException("Image size must be 128x128");
        }

        this.image = image;
        hasDrawnFor.put(image.hashCode(), new ArrayList<String>());
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if (image != null) {
            ArrayList<String> drawList = hasDrawnFor.get(image.hashCode());
            if (!drawList.contains(player.getName()))  {
                for (int i = 0; i < canvas.getCursors().size(); i++) {
                    canvas.getCursors().removeCursor(canvas.getCursors().getCursor(i));
                }
                
                canvas.drawImage(0, 0, image);
                drawList.add(player.getName());
                player.sendMap(map);
            }
        }
    }
    
    public static void removePlayerFromRenders(Player player) {
        String playerName = player.getName();
        for (ArrayList<String> list : hasDrawnFor.values()) {
            list.remove(playerName);
        }
    }
}
