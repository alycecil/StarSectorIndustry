package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.github.alycecil.econ.model.IndustryEffect;

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
    protected void addPostUpkeepSection(TooltipMakerAPI tooltip, IndustryTooltipMode mode) {
        tooltip.addPara("Population Breakdown:\n"+market.getPopulation().toString(), 10f);
    }
}
