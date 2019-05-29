package org.yeyu.simpletest;

import java.text.MessageFormat;

public enum StsInventoryNodesType {
    ENCLOSURE("Enclosure"),
    CONTROLLER("Controller"),
    DISK("Disk"),
    ETH("Eth"),
    LUN("LUN"),
    STROAGE("Stroage"),
    RAID("Raid"),
    FCOE("FCoE"),
    POWER("Power"),
    FAN("Fan"),
    BBU("BBU");


    public static final String STARTWITH="^{0}[^\\s]*";
    public static final String ENDWITH="[^\\s]*{0}$";
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private StsInventoryNodesType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static String getStartWithExp(StsInventoryNodesType type) {
        return MessageFormat.format(STARTWITH, type);
    }
    public static String getEndWithExp(StsInventoryNodesType type) {
        return MessageFormat.format(ENDWITH, type);
    }
}
