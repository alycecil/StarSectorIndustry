package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.model.FlatCommodityBonus;

public class FlatFarmingExtension extends FarmingExtension {
    public FlatFarmingExtension() {
        super(
                new FlatCommodityBonus(Commodities.ORGANICS, 2, DESC),
                new FlatCommodityBonus(Commodities.FOOD, 2, DESC),
                new FlatCommodityBonus(Commodities.LOBSTER, 1, DESC)
        );
    }
}
