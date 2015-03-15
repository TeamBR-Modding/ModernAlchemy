package com.dyonovan.modernalchemy.client.gui.machines;

import com.dyonovan.modernalchemy.client.gui.GuiBaseConfigurableSlots;
import com.dyonovan.modernalchemy.client.gui.INeiProvider;
import com.dyonovan.modernalchemy.client.gui.StandardPalette;
import com.dyonovan.modernalchemy.common.container.machines.ContainerAdvancedCrafter;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentArrowProgress;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentChangeIconButton;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentRF;
import com.dyonovan.modernalchemy.client.gui.components.GuiComponentToolTip;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.client.rpc.ILevelChanger;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileAdvancedCrafter;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import openmods.gui.component.BaseComponent;
import openmods.gui.component.BaseComposite;
import openmods.gui.component.GuiComponentLabel;
import openmods.gui.component.GuiComponentTab;
import openmods.gui.listener.IMouseDownListener;
import openmods.gui.logic.ValueCopyAction;
import openmods.utils.MiscUtils;
import openmods.utils.render.FakeIcon;

import java.util.ArrayList;

public class GuiAdvancedCrafter extends GuiBaseConfigurableSlots<TileAdvancedCrafter, ContainerAdvancedCrafter, TileAdvancedCrafter.AUTO_SLOTS> implements INeiProvider{

    GuiComponentToolTip rfTip;
    GuiComponentArrowProgress progress;
    GuiComponentChangeIconButton buttonMode;

    public GuiAdvancedCrafter(ContainerAdvancedCrafter container) {
        super(container, 176, 166, "tile.modernalchemy.blockAdvancedCrafter.name");
        //setArrowLocation(113, 33, 24, 15);
    }

    @Override
    protected Iterable<TileAdvancedCrafter.AUTO_SLOTS> getSlots() {
        return ImmutableList.of(TileAdvancedCrafter.AUTO_SLOTS.output);
    }

    @Override
    public void preRender(float mouseX, float mouseY) {
        super.preRender(mouseX, mouseY);
        TileAdvancedCrafter te = getContainer().getOwner();

        rfTip.setToolTip(te.getEnergyToolTip());
        progress.setMaxProgress(te.requiredProcessTime.get());
        progress.setProgress(te.getProgress().getValue());

        switch (te.currentMode.get()) {
            case TileAdvancedCrafter.ENRICH:
                buttonMode.icon = FakeIcon.createSheetIcon(0, 0, 16, 16);
                break;
            case TileAdvancedCrafter.EXTRUDE:
                buttonMode.icon = FakeIcon.createSheetIcon(16, 0, 16, 16);
                break;
            case TileAdvancedCrafter.BEND:
                buttonMode.icon = FakeIcon.createSheetIcon(32, 0, 16, 16);
                break;
            case TileAdvancedCrafter.FURNACE:
                buttonMode.icon = FakeIcon.createSheetIcon(48, 0, 16, 16);
                break;
        }
    }

    @Override
    protected void addCustomizations(BaseComposite root) {
        final TileAdvancedCrafter te = getContainer().getOwner();

        root.addComponent(progress = new GuiComponentArrowProgress(113, 33, 0));
        root.addComponent(rfTip = new GuiComponentToolTip(15, 20, 20, 50));

        GuiComponentRF energyLevel = new GuiComponentRF(15, 20, 20, 50);
        addSyncUpdateListener(ValueCopyAction.create(te.getRFEnergyStorageProvider(), energyLevel.rfBankReciever()));
        root.addComponent(energyLevel);

        IIcon icon = FakeIcon.createSheetIcon(0, 0, 16, 16);
        ResourceLocation resource = new ResourceLocation(Constants.MODID + ":textures/gui/AC_states.png");

        final ILevelChanger rpc = te.createClientRpcProxy(ILevelChanger.class);
        buttonMode = new GuiComponentChangeIconButton(40, 35, 0xFFFFFF, icon, resource);
        buttonMode.setListener(new IMouseDownListener() {
            @Override
            public void componentMouseDown(BaseComponent component, int x, int y, int button) {
                if (te.currentMode.get() == 3)
                    rpc.changeLevel(0);
                else
                    rpc.changeLevel(te.currentMode.get() + 1);
            }
        });
        root.addComponent(buttonMode);
    }

    @Override
    protected GuiComponentTab createTab(TileAdvancedCrafter.AUTO_SLOTS slot) {
        if(tabs == null)
            tabs = new ArrayList<>();

        switch(slot) {
            case output:
                GuiComponentTab outputTab = new  GuiComponentTab(StandardPalette.green.getColor(), new ItemStack(BlockHandler.blockAdvancedCrafter), 100, 100);
                tabs.add(outputTab);
                return outputTab;
            default:
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    protected GuiComponentLabel createLabel(TileAdvancedCrafter.AUTO_SLOTS slot) {
        switch(slot) {
            case output:
                return new GuiComponentLabel(22, 82, "Auto Output");
            default:
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    public String getNeiLabel() {
        return "modernalchemy.advancedCrafter.recipes";
    }
}
