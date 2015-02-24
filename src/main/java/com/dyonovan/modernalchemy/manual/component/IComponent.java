package com.dyonovan.modernalchemy.manual.component;

public interface IComponent {
    /**
     * Enum for alignment on page
     */
    enum ALIGNMENT {
        LEFT,
        CENTER,
        RIGHT
    }

    public int xPos = 0;
    public int yPos = 0;

    /**
     * Draw the Component
     * @param x guiLeft
     * @param y guiTop
     * @param mouseX Mouse x location
     * @param mouseY Mouse y location
     */
    public void drawComponent(int x, int y, int mouseX, int mouseY);
}
