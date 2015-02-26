package com.dyonovan.modernalchemy.manual.page;

import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.manual.component.ComponentBase;

public class BlockPage extends BasePage {
    public BlockPage(String pageId, BlockBase block) {
        super(pageId);
        setTitle(block.getLocalizedName());
        if(block.getManualComponents() != null)
            for(ComponentBase component : block.getManualComponents())
                addComponent(component);
    }
}
