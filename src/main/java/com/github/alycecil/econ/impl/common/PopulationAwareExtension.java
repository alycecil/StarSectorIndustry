package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.github.alycecil.econ.model.IndustryBonus;

public abstract class PopulationAwareExtension extends IndustryExtension implements MarketImmigrationModifier {
    public PopulationAwareExtension(IndustryBonus... bonuses) {
        super(bonuses);
    }

    @Override
    public void apply() {
        super.apply();
        market.addTransientImmigrationModifier(this);
    }

    @Override
    public void unapply() {
        super.unapply();
        market.removeTransientImmigrationModifier(this);
    }

    @Override
    protected void addPostUpkeepSection(TooltipMakerAPI tooltip, IndustryTooltipMode mode) {
        tooltip.addPara("Population Breakdown: <br>"+market.getPopulation().toString(), 10f);
    }
}
