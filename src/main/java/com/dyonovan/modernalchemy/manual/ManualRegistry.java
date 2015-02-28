package com.dyonovan.modernalchemy.manual;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.helpers.LogHelper;
import com.dyonovan.modernalchemy.manual.pages.GuiManual;
import com.google.gson.Gson;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.URLDecoder;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

public class ManualRegistry {
    /**
     * Out Manual instance
     */
    public static ManualRegistry instance = new ManualRegistry();

    /**
     * The list of {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} that have been opened, the stack
     */
    public static Stack<GuiManual> visitedPages;

    /**
     * All registered {@link com.dyonovan.modernalchemy.manual.pages.GuiManual}, built from files on preInit
     */
    public static HashMap<String, GuiManual> pages;

    /**
     * Creates our registry
     */
    public ManualRegistry() {
        pages = new HashMap<String, GuiManual>();
        visitedPages = new Stack<GuiManual>();
        init();
    }

    /**
     * Fills the pages registry with all {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} from files
     */
    public void init() {
        File[] files = getFilesForPages();
        for(File f : files) {
            if(buildManualFromFile(f.getName()) != null)
                pages.put(f.getName().split(".")[0], buildManualFromFile(f.getName()));
        }
    }

    /**
     * Adds a {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} to the registered pages
     * @param page The built {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} to add
     */
    public void addPage(GuiManual page) {
        pages.put(page.getID(), page);
    }

    /**
     * Get the {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} in the registry
     * @param id The string representing our page
     * @return The {@link com.dyonovan.modernalchemy.manual.pages.GuiManual}, null if not found
     */
    public GuiManual getPage(String id) {
        return pages.get(id);
    }

    /**
     * Gets the page that was open on top of the visited stack
     * @return The top {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} in the stack
     */
    public GuiManual getOpenPage() {
        return !visitedPages.isEmpty() ? visitedPages.get(visitedPages.size() - 1) : new GuiManual(ManualLib.MAINPAGE);
    }

    /**
     * Pushes a new page onto the visited stack
     * @param page The {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} to add (must be registered)
     */
    public void visitNewPage(GuiManual page) {
        if(pages.containsKey(page.getID())) {
            visitedPages.push(page);
            openManual();
        }
        else
            LogHelper.warning("Could not load page: " + page.getID());
    }

    /**
     * Get the page below the current one
     * @return The last {@link com.dyonovan.modernalchemy.manual.pages.GuiManual}, landing page if not found
     */
    public GuiManual getLastPage() {
        return visitedPages.size() > 2 ? visitedPages.get(visitedPages.size() - 2) : new GuiManual(ManualLib.MAINPAGE);
    }

    /**
     * Pops the visited page stack
     */
    public void deleteLastPage() {
        try {
            visitedPages.pop();
        } catch(EmptyStackException e) {
            visitedPages.push(new GuiManual(ManualLib.MAINPAGE));
            LogHelper.warning("Tried to delete last page with no stack");
        }
    }

    /**
     * Opens the manual gui with the current page
     */
    @SideOnly(Side.CLIENT)
    public void openManual() {
        visitedPages.clear();
        if(visitedPages.empty())
            visitedPages.push(buildManualFromFile(ManualLib.MAINPAGE + ".json"));
        Minecraft.getMinecraft().thePlayer.openGui(ModernAlchemy.instance, GuiHandler.MANUAL_GUI_ID, Minecraft.getMinecraft().theWorld, (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ);
    }

    /**
     * Builds the page from the file provided
     * @param fileName The name of the file with the context
     * @return A built {@link com.dyonovan.modernalchemy.manual.pages.GuiManual}
     */
    public GuiManual buildManualFromFile(String fileName) {
        GuiManual page = new GuiManual(fileName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(ModernAlchemy.class.getResource("/manualPages").getFile() + File.separator + fileName));
        } catch (FileNotFoundException e) {
            LogHelper.severe("Could not find file: " + fileName + " at " + ModernAlchemy.class.getResource("/manualPages").getFile() + File.separator + fileName);
            return null;
        }
        Gson file = new Gson();

        String title = file.fromJson("title", String.class);
        page.setTitle(title);

        return page;
    }

    /**
     * Gets all the files in the manual pages directory ("resources/manualPages")
     * @return An array of {@link java.io.File}s containing our info
     */
    public File[] getFilesForPages() {
        File directory = null;
        try {
            directory = new File(URLDecoder.decode(ModernAlchemy.class.getResource("/manualPages").getFile(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LogHelper.severe("Could not find Manual Pages");
        }
        return directory.listFiles();
    }

}
