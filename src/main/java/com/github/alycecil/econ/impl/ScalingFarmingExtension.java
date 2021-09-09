package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.github.alycecil.econ.FarmingExtension;
import com.github.alycecil.econ.model.PopulationCommodityBonus;

public class ScalingFarmingExtension extends FarmingExtension {
    public ScalingFarmingExtension() {
        super(
                new PopulationCommodityBonus(Commodities.ORGANICS, 2, DESC),
                new PopulationCommodityBonus(Commodities.FOOD, 2, DESC),
                new PopulationCommodityBonus(Commodities.LOBSTER, 3, DESC)
        );
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        incoming.add(Factions.LUDDIC_CHURCH, 5f);
        incoming.add(Factions.POOR, 10f);
        incoming.add(Factions.LUDDIC_PATH, 5f);
    }
}
