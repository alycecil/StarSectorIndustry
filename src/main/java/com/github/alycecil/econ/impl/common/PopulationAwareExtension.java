package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.github.alycecil.econ.model.IndustryEffect;

import java.awt.*;

public abstract class PopulationAwareExtension extends IndustryExtension implements MarketImmigrationModifier {
    public PopulationAwareExtension(IndustryEffect... bonuses) {
        super(bonuses);
    }

    @Override
    public void apply() {
        super.apply();
    }

    @Override
    public void unapply() {
        super.unapply();
    }

    @Override
    protected void addPostSupplySection(TooltipMakerAPI tooltip, boolean hasSupply, IndustryTooltipMode mode) {
        super.addPostSupplySection(tooltip, hasSupply, mode);

        if (Global.getSettings().getBoolean("alice_market_census")) {
            tooltip.addPara("Census:\n" + market.getPopulation().toString(), Color.lightGray, 10f);
        }
    }
}
