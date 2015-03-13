package com.dyonovan.teambrcore.nei;

import codechicken.nei.api.IConfigureNEI;
import com.dyonovan.teambrcore.TeamBRCore;
import com.dyonovan.teambrcore.lib.Constants;

public class NEIAddonConfig implements IConfigureNEI{
    @Override
    public void loadConfig() {
        TeamBRCore.nei = new NEICallback();
    }

    @Override
    public String getName() {
        return "Beyond Reality Core";
    }

    @Override
    public String getVersion() {
        return Constants.VERSION;
    }
}
