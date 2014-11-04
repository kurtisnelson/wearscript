package com.dappervision.wearscript.events;

public class ControlEvent {
    public static final String TAP = "TAP";
    public static final String SWIPE_LEFT = "SWIPE_LEFT";
    public static final String SWIPE_RIGHT = "SWIPE_RIGHT";
    public static final String SWIPE_DOWN = "SWIPE_DOWN";
    public static final String INIT = "INIT";

    String command;

    public ControlEvent(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
