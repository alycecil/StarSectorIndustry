package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.impl.common.SupportInfraGrowsPopulation;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class SpaceElevator extends SupportInfraGrowsPopulation implements MarketImmigrationModifier {

    public static final int DEMAND = 4;
    public static final String SPACE_ELEVATOR = "Space Elevator";

    public SpaceElevator() {
        super(0.5f,
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, -DEMAND, SPACE_ELEVATOR),
                new PopulationCommodityDemand(Commodities.MARINES, DEMAND, SPACE_ELEVATOR)
        );
    }

    @Override
    public String getDescription() {
        return SPACE_ELEVATOR;
    }

    @Override
    protected int getGrowthRate() {
        return 10;
    }
}
