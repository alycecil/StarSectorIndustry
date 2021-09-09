package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.github.alycecil.econ.IndustryExtension;
import com.github.alycecil.econ.model.IndustryBonus;

public abstract class FarmingExtension extends IndustryExtension {

    public static final String FARMING = "Farming";
    public static final String DESC = "Farming improvements";

    public FarmingExtension(IndustryBonus... bonuses) {
        super(bonuses);
    }

    @Override
    protected String getIndustryId() {
        return Industries.FARMING;
    }

    @Override
    protected String getIndustryFriendlyName() {
        return FARMING;
    }
}
