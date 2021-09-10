package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.github.alycecil.econ.model.IndustryEffect;

public abstract class FarmingExtension extends PopulationAwareExtension {

    public static final String FARMING = "Farming";
    public static final String DESC = "Farming improvements";

    public FarmingExtension(IndustryEffect... bonuses) {
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
