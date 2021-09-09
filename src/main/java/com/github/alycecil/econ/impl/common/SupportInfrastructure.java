package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import com.github.alycecil.econ.model.IndustryBonus;

public abstract class SupportInfrastructure extends AliceBaseIndustry {

    private final float perMarketSize;

    public SupportInfrastructure(float perMarketSize, IndustryBonus... bonuses) {
        super(bonuses);
        this.perMarketSize = perMarketSize;
    }


    @Override
    protected void applyForIndustry() {
        demand(getCommodity(), getDemand());
        float mult = getEffectiveness();

        market.getAccessibilityMod().modifyFlat(id, perMarketSize * market.getSize() * mult, getDescription());
    }

    @Override
    protected void unapplyForIndustry() {
        market.getAccessibilityMod().unmodifyFlat(id);
    }

    protected float getEffectiveness() {
        float effectiveness = 1f;
        Pair<String, Integer> deficit = getMaxDeficit(getCommodity());
        if (deficit != null && deficit.two != null && deficit.two >= 1) {
            int demand = getDemand();
            effectiveness = (float) (demand - deficit.two) / demand;
        }
        return effectiveness;
    }

    @Override
    protected void addPostSupplySection(TooltipMakerAPI tooltip, boolean hasSupply, IndustryTooltipMode mode) {
        super.addPostSupplySection(tooltip, hasSupply, mode);
        String pct = "" + (int) (getEffectiveness() * 100f) + "%";
        tooltip.addPara("Currently operating at %s effective load.", 10f, Misc.getHighlightColor(), pct);
    }


    public abstract int getDemand();

    protected abstract String getDescription();

    protected abstract String getCommodity();
}
