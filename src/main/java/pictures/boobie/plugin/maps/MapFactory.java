package pictures.boobie.plugin.maps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import pictures.boobie.plugin.hashmap.LimitedHashMap;
import pictures.boobie.plugin.util.ImageUtil;

public class MapFactory {

    private static LimitedHashMap<Integer, Short> mapCache = new LimitedHashMap<>(200);
    
    public static ItemStack createMap(BufferedImage image, World world) {
        ItemStack map = new ItemStack(Material.MAP);

        if (mapCache.containsKey(image.hashCode())) {
            map.setDurability(mapCache.get(image.hashCode()));
        }
        
        image = image.getSubimage(0, 0, 128, 128);

        MapView view = Bukkit.createMap(world);
        for (MapRenderer mr : view.getRenderers()) {
            view.removeRenderer(mr);
        }

        BoobRenderer br = new BoobRenderer();
        br.setImage(image);
        view.addRenderer(br);

        map.setDurability(view.getId());

        mapCache.put(image.hashCode(), view.getId());
        
        return map;
    }

    public static ArrayList<ItemStack> createMaps(BufferedImage image, World world, int maxWidth, int maxHeight) {
        if (maxWidth % 128 != 0 || maxHeight % 128 != 0) {
            throw new IllegalArgumentException("maxWidth and maxHeight must be divisible by 128");
        }
        
        ArrayList<ItemStack> maps = new ArrayList<>();

        if (image.getWidth() > maxWidth || image.getHeight() > maxHeight) {
            if (image.getWidth() > image.getHeight()) {
                image = ImageUtil.toBufferedImage(image.getScaledInstance(maxWidth, (int)Math.floor((float)image.getHeight() * ((float)maxWidth / (float)image.getWidth())), Image.SCALE_SMOOTH));
            } else {
                image = ImageUtil.toBufferedImage(image.getScaledInstance((int)Math.floor((float)image.getWidth() * ((float)maxHeight / (float)image.getHeight())), maxHeight, Image.SCALE_SMOOTH));
            }
        }
        
        BufferedImage border = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = border.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, maxWidth, maxHeight);
        graphics.drawImage(image, (maxWidth-image.getWidth())/2, (maxHeight-image.getHeight())/2, null);
        graphics.dispose();
        
        for (int x = 0; x < maxWidth/128; x++) {
            for (int y = 0; y < maxHeight/128; y++) {
                maps.add(createMap(border.getSubimage(x*128, y*128, 128, 128), world));
            }
        }
        
        return maps;
    }

}
