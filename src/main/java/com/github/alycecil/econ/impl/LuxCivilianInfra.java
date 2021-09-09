package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.github.alycecil.econ.impl.common.SupportInfrastructure;
import com.github.alycecil.econ.model.PopulationCommodityDemand;
import com.github.alycecil.econ.util.Incoming;

public class LuxCivilianInfra extends SupportInfrastructure implements MarketImmigrationModifier {

    public static final String DESC = "Civilian Infrastructure (Luxury)";

    public LuxCivilianInfra() {
        super(0.08f,
                new PopulationCommodityDemand(Commodities.FOOD, -1, DESC),
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 3, DESC)
        );
    }

    @Override
    protected String getCommodity() {
        return Commodities.LUXURY_GOODS;
    }

    @Override
    public int getDemand() {
        return market.getSize();
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        Incoming.modifyIncoming(market, incoming, getModId(), 3 * getEffectiveness(), getDescription());
    }
}
