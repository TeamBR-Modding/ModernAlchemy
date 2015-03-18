package com.dyonovan.modernalchemy.client.gui;

import com.dyonovan.modernalchemy.client.ChangelogBuilder;
import com.dyonovan.modernalchemy.lib.Constants;
import com.google.common.collect.Lists;
import net.minecraft.util.StatCollector;
import openmods.gui.ComponentGui;
import openmods.gui.DummyContainer;
import openmods.gui.component.BaseComposite;
import openmods.gui.component.GuiComponentBook;
import openmods.gui.component.GuiComponentLabel;
import openmods.gui.component.page.PageBase;
import openmods.gui.component.page.SectionPage;
import openmods.gui.component.page.TitledPage;
import openmods.infobook.PageBuilder;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiManual extends ComponentGui {

    private GuiComponentBook book;

    public GuiManual() {
        super(new DummyContainer(), 0, 0);
    }

    private static void setupBookmark(GuiComponentLabel label, GuiComponentBook book, int index) {
        label.setListener(book.createBookmarkListener(index));
    }

    @Override
    public void initGui() {
        // Nothing can change this value, otherwise client will crash when player picks item
        // this.mc.thePlayer.openContainer = this.inventorySlots;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    private static int alignToEven(final GuiComponentBook book) {
        int index = book.getNumberOfPages();
        if (index % 2 == 1) {
            book.addPage(PageBase.BLANK_PAGE);
            index++;
        }
        return index;
    }

    private static int tocLine(int index) {
        final int tocStartHeight = 70;
        final int tocLineHeight = 15;
        return tocStartHeight + index * tocLineHeight;
    }

    @Override
    public void handleKeyboardInput() {
        super.handleKeyboardInput();

        if (Keyboard.getEventKeyState()) {
            switch (Keyboard.getEventKey()) {
                case Keyboard.KEY_PRIOR:
                    book.prevPage();
                    break;
                case Keyboard.KEY_NEXT:
                    book.nextPage();
                    break;
                case Keyboard.KEY_HOME:
                    book.firstPage();
                    break;
                case Keyboard.KEY_END:
                    book.lastPage();
                    break;
            }
        }
    }

    @Override
    protected BaseComposite createRoot() {
        book = new GuiComponentBook();
        PageBase contentsPage = new TitledPage("modernalchemy.gui.welcome.title", "modernalchemy.gui.welcome.content");

        GuiComponentLabel lblBlocks = new GuiComponentLabel(27, tocLine(0), "- " + StatCollector.translateToLocal("modernalchemy.gui.blocks"));
        contentsPage.addComponent(lblBlocks);

        GuiComponentLabel lblItems = new GuiComponentLabel(27, tocLine(1), "- " + StatCollector.translateToLocal("modernalchemy.gui.items"));
        contentsPage.addComponent(lblItems);

        GuiComponentLabel lblMisc = new GuiComponentLabel(27, tocLine(2), "- " + StatCollector.translateToLocal("modernalchemy.gui.misc"));
        contentsPage.addComponent(lblMisc);

        GuiComponentLabel lblChangelogs = new GuiComponentLabel(27, tocLine(3), "- " + StatCollector.translateToLocal("modernalchemy.gui.changelogs"));
        contentsPage.addComponent(lblChangelogs);

        book.addPage(PageBase.BLANK_PAGE);
        book.addPage(new StartPage());
        book.addPage(new TitledPage("modernalchemy.gui.credits.title", "modernalchemy.gui.credits.content"));
        book.addPage(contentsPage);

        {
            int blocksIndex = alignToEven(book);
            setupBookmark(lblBlocks, book, blocksIndex);
            book.addPage(PageBase.BLANK_PAGE);
            book.addPage(new SectionPage("modernalchemy.gui.blocks"));

            PageBuilder builder = new PageBuilder();
            builder.includeModId(Constants.MODID);
            builder.createBlockPages();
            builder.insertTocPages(book, 4, 4, 1.5f);
            alignToEven(book);
            builder.insertPages(book);
        }

        {
            int itemsIndex = alignToEven(book);
            setupBookmark(lblItems, book, itemsIndex);
            book.addPage(PageBase.BLANK_PAGE);
            book.addPage(new SectionPage("modernalchemy.gui.items"));

            PageBuilder builder = new PageBuilder();
            builder.includeModId(Constants.MODID);
            builder.createItemPages();
            builder.insertTocPages(book, 4, 4, 1.5f);
            alignToEven(book);
            builder.insertPages(book);
        }

        {
            int miscIndex = alignToEven(book);
            setupBookmark(lblMisc, book, miscIndex);
            book.addPage(PageBase.BLANK_PAGE);
            book.addPage(new SectionPage("modernalchemy.gui.misc"));
            book.addPage(new TitledPage("modernalchemy.gui.teslaPower", "modernalchemy.gui.teslaPowerInfo"));
            book.addPage(new TitledPage("modernalchemy.gui.arcFurnace", "modernalchemy.gui.arcFurnaceInfo"));
        }

        int changelogsIndex = alignToEven(book);
        book.addPage(PageBase.BLANK_PAGE);
        setupBookmark(lblChangelogs, book, changelogsIndex);
        book.addPage(new SectionPage("modernalchemy.gui.changelogs"));

        createChangelogPages(book);

        book.enablePages();

        xSize = book.getWidth();
        ySize = book.getHeight();

        return book;
    }

    private static void createChangelogPages(final GuiComponentBook book) {
        String prevVersion = null;
        int prevIndex = 0;
        List<ChangelogPage> prevPages = Lists.newArrayList();

        final List<ChangelogBuilder.Changelog> changelogs = ChangelogBuilder.readChangeLogs();
        for (int i = 0; i < changelogs.size(); i++) {
            ChangelogBuilder.Changelog changelog = changelogs.get(i);
            final String currentVersion = changelog.version;
            int currentPage = book.getNumberOfPages();

            for (ChangelogPage prevPage : prevPages)
                prevPage.addNextVersionBookmark(book, currentVersion, currentPage);

            prevPages.clear();

            for (ChangelogBuilder.ChangelogSection section : changelog.sections) {
                ChangelogPage page = new ChangelogPage(currentVersion, section.title, section.lines);
                book.addPage(page);
                prevPages.add(page);

                if (i > 0) {
                    page.addPrevVersionBookmark(book, prevVersion, prevIndex);
                }
            }

            alignToEven(book);

            prevVersion = currentVersion;
            prevIndex = currentPage;
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        prepareRenderState();
        GL11.glPushMatrix();
        root.renderOverlay(this.mc, this.guiLeft, this.guiTop, par1 - this.guiLeft, par2 - this.guiTop);
        GL11.glPopMatrix();
        restoreRenderState();
    }
}