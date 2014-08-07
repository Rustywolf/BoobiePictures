package pictures.boobie.plugin.tasks;

import pictures.boobie.plugin.BoobiePlugin;

public class RoomOwnerNewNameTask implements Runnable {

    private String name;

    public RoomOwnerNewNameTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        if (!BoobiePlugin.roomOwner.getName().equals(this.name)) {
            return;
        }

        BoobiePlugin.roomOwner.setNextName();
    }

}
