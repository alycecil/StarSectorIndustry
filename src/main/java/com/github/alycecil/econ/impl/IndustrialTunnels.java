package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.util.Pair;
import com.github.alycecil.econ.impl.common.SupportInfrastructure;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class IndustrialTunnels extends SupportInfrastructure {
    private static final String DESC = "Transportation Infrastructure";

    public IndustrialTunnels() {
        super(0.2f,
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 2, DESC),
                new PopulationCommodityDemand(Commodities.FUEL, 5, DESC),
                new PopulationCommodityDemand(Commodities.DOMESTIC_GOODS, 2, DESC)
        );
    }

    public static final int DEMAND = 2;

    @Override
    public int getDemand() {
        return DEMAND + market.getSize();
    }

    @Override
    protected String getDescription() {
        return DESC;
    }

    @Override
    protected String getCommodity() {
        return Commodities.SUPPLIES;
    }

    protected float getEffectiveness() {
        float effectiveness = 1f;
        Pair<String, Integer> deficit = getMaxDeficit(getCommodity(), Commodities.HEAVY_MACHINERY, Commodities.FUEL, Commodities.DOMESTIC_GOODS);
        if (deficit != null && deficit.two != null && deficit.two >= 1) {
            int demand = getDemand();
            effectiveness = (float) (demand - deficit.two) / demand;
        }
        return effectiveness;
    }
}