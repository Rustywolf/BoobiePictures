package pictures.boobie.plugin.tasks;

import pictures.boobie.plugin.room.RoomData;

public class UpdatePageTask implements Runnable {
    
    private RoomData room;
    
    public UpdatePageTask(RoomData room) {
        this.room = room;
    }
    
    @Override
    public void run() {
        room.getSubredditData().nextPage();
        (new UpdateImageTask(room, room.getSubredditData().getUrls().get(0))).run();
    }
    
}
