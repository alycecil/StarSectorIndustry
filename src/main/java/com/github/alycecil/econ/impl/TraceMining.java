package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.github.alycecil.econ.impl.common.PollutingIndustry;
import com.github.alycecil.econ.model.FlatCommodityBonus;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class TraceMining extends PollutingIndustry {

    public static final String MINING = "Mining";
    public static final String DESC = "Trace Mining";

    public TraceMining() {
        super(
                new FlatCommodityBonus(Commodities.ORGANICS, 1, DESC),
                new FlatCommodityBonus(Commodities.RARE_ORE, 2, DESC),
                new FlatCommodityBonus(Commodities.VOLATILES, 2, DESC),
                new FlatCommodityBonus(Commodities.ORE, 1, DESC),
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 3, DESC)
        );
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        if(incoming == null) return;
        incoming.add(Factions.TRITACHYON, 10f);
    }

    @Override
    protected String getIndustryId() {
        return Industries.MINING;
    }

    @Override
    protected String getIndustryFriendlyName() {
        return MINING;
    }
}
