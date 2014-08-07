package pictures.boobie.plugin.tasks;

import pictures.boobie.plugin.maps.MapFactory;
import pictures.boobie.plugin.room.RoomData;
import pictures.boobie.plugin.util.ImageUtil;

public class UpdateImageTask implements Runnable {
    
    private RoomData room;
    private String url;
    
    public UpdateImageTask(RoomData room, String url) {
        this.room = room;
        this.url = url;
    }
    
    @Override
    public void run() {
        room.updateImage(MapFactory.createMaps(ImageUtil.getImage(url), room.getRoomCuboid().getWorld(), 512, 512));
        room.setState(RoomData.RoomState.MAIN);
    }
    
}
