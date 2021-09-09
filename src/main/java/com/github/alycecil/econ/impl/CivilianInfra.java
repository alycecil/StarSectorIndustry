package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.fs.starfarer.api.util.Pair;
import com.github.alycecil.econ.impl.common.SupportInfrastructure;
import com.github.alycecil.econ.model.PopulationCommodityDemand;
import com.github.alycecil.econ.util.Incoming;

public class CivilianInfra extends SupportInfrastructure implements MarketImmigrationModifier {

    public static final String DESC = "Civilian Infrastructure (Common)";

    public CivilianInfra() {
        super(0.08f,
                new PopulationCommodityDemand(Commodities.FOOD, 1, DESC),
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 3, DESC)
        );
    }

    @Override
    protected String getCommodity() {
        return Commodities.DOMESTIC_GOODS;
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

    public static class IndustrialTunnels extends SupportInfrastructure {
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
}
