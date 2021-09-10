package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.github.alycecil.econ.impl.common.FarmingExtension;
import com.github.alycecil.econ.model.FlatCommodityBonus;
import com.github.alycecil.econ.util.AliceCommon;

public class FlatFarmingExtension extends FarmingExtension {
    public FlatFarmingExtension() {
        super(
                new FlatCommodityBonus(Commodities.ORGANICS, 2, DESC),
                new FlatCommodityBonus(Commodities.FOOD, 3, DESC),
                new FlatCommodityBonus(Commodities.LOBSTER, 1, DESC),
                new FlatCommodityBonus(AliceCommon.LOBSTER, 1, DESC)
        );
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        if(incoming == null) return;
        incoming.add(Factions.LUDDIC_CHURCH, 10f);
        incoming.add(Factions.LUDDIC_PATH, 9f);
        incoming.add(Factions.PERSEAN, 1f);
    }
}
