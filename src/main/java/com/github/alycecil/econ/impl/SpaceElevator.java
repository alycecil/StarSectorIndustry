package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.impl.common.SupportInfraGrowsPopulation;

public class SpaceElevator extends SupportInfraGrowsPopulation implements MarketImmigrationModifier {

    public static final int DEMAND = 4;

    public SpaceElevator() {
        super(0.5f);
    }

    @Override
    protected String getCommodity() {
        return Commodities.HEAVY_MACHINERY;
    }

    @Override
    public int getDemand() {
        return DEMAND + market.getSize();
    }

    @Override
    public String getDescription() {
        return "Space Elevator";
    }

    @Override
    protected int getGrowthRate() {
        return 10;
    }
}
