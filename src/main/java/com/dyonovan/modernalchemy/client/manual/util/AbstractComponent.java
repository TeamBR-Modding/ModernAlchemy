package com.dyonovan.modernalchemy.client.manual.util;

import com.dyonovan.modernalchemy.client.manual.component.IComponent;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class AbstractComponent {

    public int xPos, yPos, width, height, pageNum;
    public String text, destination, type, item;
    public IComponent.ALIGNMENT alignment;
    public ResourceLocation resource;
    public ArrayList<String> tooltips;

    public AbstractComponent(String type, int xPos, int yPos, int width, int height, IComponent.ALIGNMENT align, int pageNum, String text, String destination,
                             String item, ResourceLocation resource, ArrayList<String> tooltips) {

        this.type = type;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.alignment = align;
        this.pageNum = pageNum;
        this.text = text;
        this.destination = destination;
        this.item = item;
        this.resource = resource;
        this.tooltips = tooltips;
    }
}
