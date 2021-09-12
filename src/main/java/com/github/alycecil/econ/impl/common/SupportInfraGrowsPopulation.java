package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.github.alycecil.econ.model.IndustryEffect;
import com.github.alycecil.econ.util.AliceCommon;
import com.github.alycecil.econ.util.Incoming;

public abstract class SupportInfraGrowsPopulation extends SupportInfrastructure {


    public SupportInfraGrowsPopulation(float perMarketSize, IndustryEffect... bonuses) {
        super(perMarketSize, bonuses);
    }

    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        Incoming.modifyIncoming(market, incoming, getModId(), getGrowthRate() * getEffectiveness(), getDescription());
    }

    protected abstract int getGrowthRate();

    @Override
    protected void addPostSupplySection(TooltipMakerAPI tooltip, boolean hasSupply, IndustryTooltipMode mode) {
        super.addPostSupplySection(tooltip, hasSupply, mode);

        float value = getGrowthRate() * getEffectiveness();
        if (value > 0.01f) {
            tooltip.addPara("Increasing growth rate by %s.", 10f, Misc.getHighlightColor(), String.valueOf(value));
        } else if (value < -0.01f) {
            tooltip.addPara("Decreasing growth rate by %s.", 10f, AliceCommon.WARNING, Misc.getNegativeHighlightColor(), String.valueOf(value));
        }
    }
}
