package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.github.alycecil.econ.impl.common.PopulationAwareExtension;
import com.github.alycecil.econ.model.FlatCommodityBonus;
import com.github.alycecil.econ.util.AliceCommon;

public class AquacultureExtension extends PopulationAwareExtension {
    public static final String AQUACULTURE = "Aquaculture";
    public static final String DESC = AQUACULTURE + " improvements";

    public AquacultureExtension() {
        super(
                new FlatCommodityBonus(Commodities.ORGANICS, 1, DESC),
                new FlatCommodityBonus(Commodities.FOOD, 2, DESC),
                new FlatCommodityBonus(Commodities.LOBSTER, 1, DESC),
                new FlatCommodityBonus(AliceCommon.LOBSTER, 2, DESC)
        );
    }

    @Override
    protected String getIndustryId() {
        return Industries.AQUACULTURE;
    }

    @Override
    protected String getIndustryFriendlyName() {
        return AQUACULTURE;
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        if(incoming == null) return;
        incoming.add(Factions.LUDDIC_CHURCH, 5f);
        incoming.add(Factions.LUDDIC_PATH, 1f);
        incoming.add(Factions.POOR, 7f);
        incoming.add(Factions.PERSEAN, 7f);
    }
}
