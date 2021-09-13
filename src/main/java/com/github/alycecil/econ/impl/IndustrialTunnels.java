package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.impl.common.SupportInfrastructure;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class IndustrialTunnels extends SupportInfrastructure {
    private static final String DESC = "Transportation Infrastructure";

    public IndustrialTunnels() {
        super(0.2f,
                new PopulationCommodityDemand(Commodities.SUPPLIES, 0, DESC),
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 2, DESC),
                new PopulationCommodityDemand(Commodities.FUEL, 5, DESC),
                new PopulationCommodityDemand(Commodities.DOMESTIC_GOODS, 2, DESC)
        );
    }

    @Override
    protected String getDescription() {
        return DESC;
    }
}