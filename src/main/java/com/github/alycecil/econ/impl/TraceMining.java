package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.github.alycecil.econ.PollutingIndustry;
import com.github.alycecil.econ.model.FlatCommodityBonus;

public class TraceMining extends PollutingIndustry {

    public static final String MINING = "Mining";
    public static final String DESC = "Trace Mining";

    public TraceMining() {
        super(
                new FlatCommodityBonus(Commodities.ORGANICS, 1, DESC),
                new FlatCommodityBonus(Commodities.RARE_ORE, 2, DESC),
                new FlatCommodityBonus(Commodities.VOLATILES, 2, DESC),
                new FlatCommodityBonus(Commodities.ORE, 1, DESC)
        );
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
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
