package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.impl.common.SupportInfraGrowsPopulation;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class CivilianInfra extends SupportInfraGrowsPopulation implements MarketImmigrationModifier {

    public static final String DESC = "Civilian Infrastructure (Common)";

    public CivilianInfra() {
        super(0.08f,
                new PopulationCommodityDemand(Commodities.DOMESTIC_GOODS, 0, DESC),
                new PopulationCommodityDemand(Commodities.FOOD, 1, DESC),
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
