package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.github.alycecil.econ.impl.common.FarmingExtension;
import com.github.alycecil.econ.impl.common.HasEffectiveness;
import com.github.alycecil.econ.model.ConditionalFlatCommodityBonus;
import com.github.alycecil.econ.model.PopulationCommodityBonus;
import com.github.alycecil.econ.model.PopulationCommodityDemand;
import com.github.alycecil.econ.util.AliceCommon;

public class ScalingFarmingExtension extends FarmingExtension implements HasEffectiveness {
    public ScalingFarmingExtension() {
        super(
                new PopulationCommodityBonus(Commodities.ORGANICS, 2, DESC),
                new PopulationCommodityBonus(Commodities.FOOD, 2, DESC),
                new PopulationCommodityBonus(AliceCommon.LOBSTER, 6, DESC),
                new ConditionalFlatCommodityBonus(AliceCommon.LOBSTER, Conditions.WATER, 3, DESC),
                new ConditionalFlatCommodityBonus(AliceCommon.LOBSTER, Conditions.WATER_SURFACE, 2, DESC),
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 3, DESC)
        );
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        if (incoming == null) return;
        incoming.add(Factions.LUDDIC_CHURCH, 5f);
        incoming.add(Factions.POOR, 10f);
        incoming.add(Factions.LUDDIC_PATH, 5f);
    }
}
