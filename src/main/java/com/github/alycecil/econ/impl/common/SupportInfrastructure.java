package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.github.alycecil.econ.model.IndustryEffect;

public abstract class SupportInfrastructure extends AliceBaseIndustry implements HasEffectiveness {

    private final float perMarketSize;

    public SupportInfrastructure(float perMarketSize, IndustryEffect... bonuses) {
        super(bonuses);
        this.perMarketSize = perMarketSize;
    }


    @Override
    protected void applyForIndustry(float effectiveness) {
        market.getAccessibilityMod().modifyFlat(id, perMarketSize * market.getSize() * effectiveness, getDescription());
    }

    protected abstract String getDescription();

    @Override
    protected void unapplyForIndustry() {
        market.getAccessibilityMod().unmodifyFlat(id);
    }
}
