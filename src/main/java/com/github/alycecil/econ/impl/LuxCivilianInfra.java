package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.impl.common.SupportInfraGrowsPopulation;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class LuxCivilianInfra extends SupportInfraGrowsPopulation implements MarketImmigrationModifier {

    public static final String DESC = "Civilian Infrastructure (Luxury)";

    public LuxCivilianInfra() {
        super(0.08f,
                new PopulationCommodityDemand(Commodities.LUXURY_GOODS, 0, DESC),
                new PopulationCommodityDemand(Commodities.FOOD, -1, DESC),
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 3, DESC)
        );
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    protected int getGrowthRate() {
        return 3;
    }
}
