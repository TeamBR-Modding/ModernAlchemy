package com.dyonovan.modernalchemy.client.manual;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.helpers.LogHelper;
import com.dyonovan.modernalchemy.client.manual.component.*;
import com.dyonovan.modernalchemy.client.manual.pages.GuiManual;
import com.dyonovan.modernalchemy.client.manual.util.AbstractComponent;
import com.dyonovan.modernalchemy.client.manual.util.AbstractManualPage;
import com.dyonovan.modernalchemy.client.manual.util.ManualPageDeserializer;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import com.dyonovan.modernalchemy.util.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

@SideOnly(Side.CLIENT)
public class ManualRegistry {

    public static ManualRegistry instance = new ManualRegistry();

    /**
     * The list of {@link com.dyonovan.modernalchemy.client.manual.pages.GuiManual} that have been opened, the stack
     */
    public static Stack<GuiManual> visitedPages;
    /**
     * All registered {@link com.dyonovan.modernalchemy.client.manual.pages.GuiManual}, built from files on init
     */
    public static HashMap<String, GuiManual> pages;
    /**
     * Creates our registry
     */
    public ManualRegistry() {
        pages = new HashMap<>();
        visitedPages = new Stack<>();
        init();
    }
    /**
     * Fills the pages registry with all {@link com.dyonovan.modernalchemy.client.manual.pages.GuiManual} from files
     */
    public void init() {
        pages.clear();
        ArrayList<String> files = FileUtils.getJarDirList(ModernAlchemy.class.getResource("/manualPages"));
        for(String f : files) {
            String[] string = f.split("manualPages/");
            if(string.length > 1 && buildManualFromFile(string[1]) != null) {
                addPage(buildManualFromFile(string[1]));
            }
        }
        visitedPages.clear();
        visitedPages.push(pages.get(ManualLib.MAINPAGE));
    }
    /**
     * Adds a {@link com.dyonovan.modernalchemy.client.manual.pages.GuiManual} to the registered pages
     * @param page The built {@link com.dyonovan.modernalchemy.client.manual.pages.GuiManual} to add
     */
    public void addPage(GuiManual page) {
        pages.put(page.getID(), page);
    }
    /**
     * Get the {@link com.dyonovan.modernalchemy.client.manual.pages.GuiManual} in the registry
     * @param id The string representing our page
     * @return The {@link com.dyonovan.modernalchemy.client.manual.pages.GuiManual}, null if not found
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
     * @return The top {@link com.dyonovan.modernalchemy.client.manual.pages.GuiManual} in the stack
     */
    public GuiManual getOpenPage() {
        if(visitedPages.empty()) {
            return pages.get(ManualLib.MAINPAGE);
        }
        return visitedPages.get(visitedPages.size() - 1);
    }
    /**
     * Pushes a new page onto the visited stack
     * @param page The {@link com.dyonovan.modernalchemy.client.manual.pages.GuiManual} to add (must be registered)
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
     * @param input The filename as a {@link java.lang.String}
     * @return A built {@link com.dyonovan.modernalchemy.client.manual.pages.GuiManual}
     */
    public GuiManual buildManualFromFile(String input) {
        GuiManual page = new GuiManual(input.split(".json")[0]);
        InputStream is = ModernAlchemy.class.getResourceAsStream("/manualPages/" + input);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        AbstractManualPage json = readJson(bufferedReader);

        page.setTitle(StatCollector.translateToLocal(json.title)); //Set the title
        for (int i = 1; i < json.numPages; i++) //Build the pages
            page.pages.add(new ComponentSet());
        for (AbstractComponent component : json.component) { //Add the components to their page
            page.pages.get(component.pageNum - 1).add(buildFromComponent(component));
        }
        return page;
    }

    /**
     * Gets all the files in the manual pages directory ("resources/manualPages")
     * @return An array of {@link java.lang.String}s containing our info
     *//*
    @SuppressWarnings("ConstantConditions")
    public ArrayList<String> getFilesForPages() {
        ArrayList<String> files = new ArrayList<>();
        String path = "manualPages";
        URL url = ModernAlchemy.class.getResource("/" + path);

        if (url.toString().substring(0,3).equalsIgnoreCase("jar")) {
            try {
                String[] urlString = url.toString().split("!");
                JarURLConnection jarCon = (JarURLConnection)new URL(urlString[0] + "!/").openConnection();
                Enumeration<JarEntry> entries = jarCon.getJarFile().entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(path)) {
                        if (!(entryName.replaceAll("manualPages/", "").equals("")))
                            files.add(entryName.replaceAll("manualPages/", ""));
                    }
                }
            } catch (IOException e) {
                LogHelper.severe("Could not find Manual Pages");
            }
        } else {
            try {
                File apps = new File(url.toURI());
                for (File app : apps.listFiles()) {
                    files.add(app.getName());
                }
            } catch (URISyntaxException e) {
                LogHelper.severe("Could not find Manual Pages");
            }

        }
        return files;
    }*/

    /**
     * Reads the Json into usable information
     * @param br {@link java.io.BufferedReader} that contains the json file
     * @return A {@link com.dyonovan.modernalchemy.client.manual.util.AbstractManualPage} object with all the information in the file
     */
    public AbstractManualPage readJson(BufferedReader br) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AbstractManualPage.class, new ManualPageDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(br, AbstractManualPage.class);
    }
    /**
     * Converts the {@link com.dyonovan.modernalchemy.client.manual.util.AbstractComponent} to {@link com.dyonovan.modernalchemy.client.manual.component.IComponent}
     * @param component The {@link com.dyonovan.modernalchemy.client.manual.util.AbstractComponent} to convert (from Json)
     * @return The {@link com.dyonovan.modernalchemy.client.manual.component.IComponent} of the type definded in the {@link com.dyonovan.modernalchemy.client.manual.util.AbstractComponent}
     */
    public IComponent buildFromComponent(AbstractComponent component) {
        ComponentBase goodComponent;

        //Component Types:
        //ComponentTextBox        - "TEXT_BOX"
        //ComponentHeader         - "HEADER"
        //ComponentImage          - "IMAGE"
        //ComponentItemRender     - "ITEM_RENDER"
        //ComponentLineBreak      - "BREAK"
        //ComponentLink           - "LINK"

        if(component.type.equalsIgnoreCase("TEXT_BOX"))
            goodComponent = new ComponentTextBox(StatCollector.translateToLocal(component.text));
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