package com.dyonovan.modernalchemy.manual;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.helpers.LogHelper;
import com.dyonovan.modernalchemy.manual.component.*;
import com.dyonovan.modernalchemy.manual.pages.GuiManual;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SideOnly(Side.CLIENT)
public class ManualRegistry {
    /**
     * Our Manual instance
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
        ArrayList<String> files = getFilesForPages();
        for(String f : files) {
            if(buildManualFromFile(f) != null)
                addPage(buildManualFromFile(f));
        }
        visitedPages.clear();
        visitedPages.push(pages.get(ManualLib.MAINPAGE));
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
     * Returns if the manual is on the base page
     * @return True if at base
     */
    public boolean isAtRoot() {
        return visitedPages.size() < 2;
    }
    /**
     * Gets the page that was open on top of the visited stack
     * @return The top {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} in the stack
     */
    public GuiManual getOpenPage() {
        if(visitedPages.empty()) {
            return pages.get(ManualLib.MAINPAGE);
        }
        return visitedPages.get(visitedPages.size() - 1);
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
     * Pushes a new page to the visited stack and opens it
     * @param name Block/Item name from {@link cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier}
     */
    public void visitNewPage(String name) {
        if(pages.containsKey(name)) {
            visitedPages.push(pages.get(name));
            openManual();
        }
    }
    /**
     * Pops the visited page stack
     */
    public void deleteLastPage() {
        try {
            visitedPages.pop();
        } catch(EmptyStackException e) {
            visitedPages.push(pages.get(ManualLib.MAINPAGE));
            LogHelper.warning("Tried to delete last page with no stack");
        }
    }
    /**
     * Opens the manual gui with the current page
     */
    public void openManual() {
        if(visitedPages.empty()) {
            visitedPages.push(pages.get(ManualLib.MAINPAGE));
        }
        Minecraft.getMinecraft().thePlayer.openGui(ModernAlchemy.instance, GuiHandler.MANUAL_GUI_ID, Minecraft.getMinecraft().theWorld, (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ);
    }
    /**
     * Builds the page from the file provided
     * @param input The file with the context
     * @return A built {@link com.dyonovan.modernalchemy.manual.pages.GuiManual}
     */
    public GuiManual buildManualFromFile(String input) {
        //GuiManual page = new GuiManual(input.getName().split(".json")[0]);
        GuiManual page = new GuiManual(input.split(".json")[0]);
        ManualJson json;
        InputStream is = ModernAlchemy.class.getResourceAsStream("/manualPages/" + input);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        json = readJson(bufferedReader);

        page.setTitle(StatCollector.translateToLocal(json.title)); //Set the title
        for (int i = 1; i < json.numPages; i++) //Build the pages
            page.pages.add(new ComponentSet());
        for (ManualComponents component : json.component) { //Add the components to their page
            page.pages.get(component.pageNum - 1).add(buildFromComponent(component));
        }
        return page;
    }
    /**
     * Gets all the files in the manual pages directory ("resources/manualPages")
     * @return An array of {@link java.io.File}s containing our info
     */
    /*public File[] getFilesForPages() {
        File directory = null;
        try {
            directory = new File(URLDecoder.decode(ModernAlchemy.class.getResource("/manualPages").getFile(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LogHelper.severe("Could not find Manual Pages");
        }
        return directory.listFiles();
    }*/

    public ArrayList<String> getFilesForPages() {
        ArrayList<String> files = new ArrayList<String>();
        String path = "manualPages";
        File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if (jarFile.isFile()) {
            try {
                JarFile jar = new JarFile(jarFile);
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path + "/")) files.add(name);

                }
                jar.close();
            } catch (IOException e) {
                LogHelper.severe("Could not find Manual Pages");
            }
        } else {
            URL url = ModernAlchemy.class.getResource("/" + path);
            if (url != null) {
                try {
                    File apps = new File(url.toURI());
                    for (File app : apps.listFiles()) {
                        files.add(app.getName());
                    }
                } catch (URISyntaxException e) {
                    LogHelper.severe("Could not find Manual Pages");
                }
            }
        }
        return files;
    }

    /**
     * Reads the Json into usable information
     * @param br {@link java.io.BufferedReader} that contains the json file
     * @return A {@link com.dyonovan.modernalchemy.manual.ManualJson} object with all the information in the file
     */
    public ManualJson readJson(BufferedReader br) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ManualJson.class, new MJDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(br, ManualJson.class);
    }
    /**
     * Converts the {@link com.dyonovan.modernalchemy.manual.ManualComponents} to {@link com.dyonovan.modernalchemy.manual.component.IComponent}
     * @param component The {@link com.dyonovan.modernalchemy.manual.ManualComponents} to convert (from Json)
     * @return The {@link com.dyonovan.modernalchemy.manual.component.IComponent} of the type definded in the {@link com.dyonovan.modernalchemy.manual.ManualComponents}
     */
    public IComponent buildFromComponent(ManualComponents component) {
        ComponentBase goodComponent;
//Component Types:
//ComponentTextBox - "TEXT_BOX"
//ComponentCraftingRecipe - "CRAFTING"
//ComponentHeader - "HEADER"
//ComponentImage - "IMAGE"
//ComponentItemRender - "ITEM_RENDER"
//ComponentLineBreak - "BREAK"
//ComponentLink - "LINK"
        if(component.type.equalsIgnoreCase("TEXT_BOX"))
            goodComponent = new ComponentTextBox(StatCollector.translateToLocal(component.text));
        else if(component.type.equalsIgnoreCase("CRAFTING"))
            goodComponent = new ComponentCraftingRecipe(ReplicatorUtils.getReturn(component.item));
        else if(component.type.equalsIgnoreCase("HEADER"))
            goodComponent = new ComponentHeader(StatCollector.translateToLocal(component.text));
        else if(component.type.equalsIgnoreCase("IMAGE"))
            goodComponent = new ComponentImage(component.resource);
        else if(component.type.equalsIgnoreCase("ITEM_RENDER"))
            goodComponent = new ComponentItemRender(ReplicatorUtils.getReturn(component.item));
        else if(component.type.equalsIgnoreCase("BREAK"))
            goodComponent = new ComponentLineBreak();
        else if(component.type.equalsIgnoreCase("LINK"))
            goodComponent = new ComponentLink(StatCollector.translateToLocal(component.text), component.destination);
        else
            goodComponent = new ComponentBase();
        goodComponent.setPositionAndSize(component.xPos, component.yPos, component.width, component.height);
        goodComponent.setAlignment(component.alignment);
        if(!component.tooltips.isEmpty())
            for(String tip : component.tooltips)
                goodComponent.addToTip(StatCollector.translateToLocal(tip));
        return goodComponent;
    }
}