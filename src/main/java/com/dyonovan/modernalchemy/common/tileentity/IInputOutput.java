package com.dyonovan.modernalchemy.common.tileentity;

import com.dyonovan.modernalchemy.helpers.GuiHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

public interface IInputOutput {
    /**
     * Enum for different mode for directions
     */
    enum MODE {
        NONE("com.teambr.mode.none"),
        OUTPUT("com.teambr.mode.output"),
        INPUT("com.teambr.mode.input"),
        BOTH("com.teambr.mode.both");

        private String name;

        private MODE(String unLocalizedName) {
            name = unLocalizedName;
        }

        public String getColoredLocalization() {
            switch(this) {
                case NONE :
                    return GuiHelper.GuiColor.GRAY + getLocalizedName();
                case OUTPUT :
                    return GuiHelper.GuiColor.BLUE + getLocalizedName();
                case INPUT :
                    return GuiHelper.GuiColor.GREEN + getLocalizedName();
                case BOTH :
                    return GuiHelper.GuiColor.ORANGE + getLocalizedName();
                default  :
                    return GuiHelper.GuiColor.RED + "Could not evaluate mode";
            }
        }

        public String getLocalizedName() {
            return StatCollector.translateToLocal(name);
        }

        public MODE next() {
            return this.ordinal() < MODE.values().length - 1 ? MODE.values()[this.ordinal() + 1] : NONE;
        }
    }

    /**
     * Get what mode is set for a {@link ForgeDirection}
     * @param dir What direction the side faces
     * @return {@link MODE} for directions
     */
    public MODE getModeForDirection(ForgeDirection dir);

    /**
     * Set the mode for the direction
     * @param dir {@link ForgeDirection} to set
     * @param mode {@link MODE} to set
     */
    public void setMode(ForgeDirection dir, MODE mode);

    /**
     * Test if the current state allows for exporting to direction
     * @param dir {@link ForgeDirection} to check
     * @return true if possible
     */
    public boolean canExport(ForgeDirection dir);

    /**
     * Test if allowed to import from direction
     * @param dir {@link ForgeDirection} direction to check
     * @return true if valid
     */
    public boolean canImport(ForgeDirection dir);
}
