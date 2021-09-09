package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.github.alycecil.econ.IndustryExtension;
import com.github.alycecil.econ.model.FlatCommodityBonus;

public class AquacultureExtension extends IndustryExtension {
    public static final String AQUACULTURE = "Aquaculture";
    public static final String DESC = AQUACULTURE + " improvements";

    public AquacultureExtension() {
        super(
                new FlatCommodityBonus(Commodities.ORGANICS, 1, DESC),
                new FlatCommodityBonus(Commodities.FOOD, 2, DESC),
                new FlatCommodityBonus(Commodities.LOBSTER, 2, DESC)
        );
    }

    @Override
    protected String getIndustryId() {
        return Industries.AQUACULTURE;
    }

    @Override
    protected String getIndustryFriendlyName() {
        return AQUACULTURE;
    }
}
