package com.dyonovan.modernalchemy.manual.component;

public interface IComponent {
    /**
     * Enum for alignment on pages
     */
    enum ALIGNMENT {
        LEFT,
        CENTER,
        RIGHT
    }

    /**
     * Draw the Component
     * @param x guiLeft
     * @param y guiTop
     * @param mouseX Mouse x location
     * @param mouseY Mouse y location
     */
    public void drawComponent(int x, int y, int mouseX, int mouseY);
}
