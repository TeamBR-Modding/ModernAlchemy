package com.dyonovan.modernalchemy.util;

public class ReplicatorValues {

    public String modName;
    public String itemName;
    public int reqTicks;
    public int qtyReturn;

    public ReplicatorValues(String modName, String itemName, int reqTicks, int qtyReturn) {
        this.modName = modName;
        this.itemName = itemName;
        this.reqTicks = reqTicks;
        this.qtyReturn = qtyReturn;
    }
}
