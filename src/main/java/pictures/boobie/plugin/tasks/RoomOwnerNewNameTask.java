package pictures.boobie.plugin.tasks;

import pictures.boobie.plugin.BoobiePlugin;

public class RoomOwnerNewNameTask implements Runnable {

    private String name;
    private int expectedNumber;

    public RoomOwnerNewNameTask(String name, int expectedNumber) {
        this.name = name;
        this.expectedNumber = expectedNumber;
    }

    @Override
    public void run() {
        if (BoobiePlugin.roomOwner.getOwnerCount() != expectedNumber) {
            return;
        }

        BoobiePlugin.roomOwner.setNextName();
    }

}
