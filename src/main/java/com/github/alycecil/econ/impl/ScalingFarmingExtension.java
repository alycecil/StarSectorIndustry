package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.model.PopulationCommodityBonus;

public class ScalingFarmingExtension extends FarmingExtension {
    public ScalingFarmingExtension() {
        super(
                new PopulationCommodityBonus(Commodities.ORGANICS, 2, DESC),
                new PopulationCommodityBonus(Commodities.FOOD, 2, DESC),
                new PopulationCommodityBonus(Commodities.LOBSTER, 3, DESC)
        );
    }
}
