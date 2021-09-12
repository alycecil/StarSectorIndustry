package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.github.alycecil.econ.impl.common.AliceBaseIndustry;
import com.github.alycecil.econ.impl.common.HasEffectiveness;
import com.github.alycecil.econ.model.FlatCommodityBonus;
import com.github.alycecil.econ.model.PopulationCommodityBonus;
import com.github.alycecil.econ.model.PopulationCommodityDemand;
import com.github.alycecil.econ.util.AliceCommon;

public class StandAloneFarmingExtension extends AliceBaseIndustry implements HasEffectiveness, MarketImmigrationModifier {
    public static final String DESC = "Farming improvements";

    public StandAloneFarmingExtension() {
        super(
                new PopulationCommodityBonus(Commodities.ORGANICS, 3, DESC),
                new PopulationCommodityBonus(Commodities.FOOD, 3, DESC),
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 3, DESC)
        );
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        if(incoming == null) return;
        incoming.add(Factions.TRITACHYON, 5f);
        incoming.add(Factions.LUDDIC_CHURCH, 5f);
        incoming.add(Factions.LUDDIC_PATH, 4f);
        incoming.add(Factions.PERSEAN, 1f);
    }

    @Override
    protected void applyForIndustry(float effectiveness) {
    }

    @Override
    protected void unapplyForIndustry() {
    }
}
