package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.github.alycecil.econ.impl.common.PollutingIndustry;
import com.github.alycecil.econ.model.PopulationCommodityBonus;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class FuelProd extends PollutingIndustry {

    public static final String NAME = "Fuel Production";
    public static final String DESC = "Bulk";

    public FuelProd() {
        super(
                new PopulationCommodityBonus(Commodities.FUEL, -2, DESC),
                new PopulationCommodityDemand(Commodities.VOLATILES, 2, DESC),
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 3, DESC)
        );
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        incoming.add(Factions.TRITACHYON, 15f);
    }

    @Override
    protected String getIndustryId() {
        return Industries.FUELPROD;
    }

    @Override
    protected String getIndustryFriendlyName() {
        return NAME;
    }
}
