package com.dyonovan.modernalchemy.manual;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class ManualComponents {

    public int xPos, yPos, width, height, pageNum;
    public String text, destination, type, item;
    public ResourceLocation resource;
    public ArrayList<String> tooltips;

    public ManualComponents(String type, int xPos, int yPos, int width, int height, int pageNum, String text, String destination,
                            String item, ResourceLocation resource, ArrayList<String> tooltips) {

        this.type = type;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.pageNum = pageNum;
        this.text = text;
        this.destination = destination;
        this.item = item;
        this.resource = resource;
        this.tooltips = tooltips;
    }
}
