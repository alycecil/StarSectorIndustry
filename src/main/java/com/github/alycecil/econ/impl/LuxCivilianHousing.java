package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.fs.starfarer.api.util.Pair;
import com.github.alycecil.econ.impl.common.AddsMarket;
import com.github.alycecil.econ.impl.common.HasEffectiveness;
import com.github.alycecil.econ.model.PopulationCommodityDemand;
import com.github.alycecil.econ.util.AliceCommon;

public class LuxCivilianHousing extends AddsMarket implements MarketImmigrationModifier, HasEffectiveness {

    public static final String DESC = "Population (Wealthy)";

    public LuxCivilianHousing() {
        super(0.08f,
                new PopulationCommodityDemand(Commodities.FOOD, -3, DESC),
                new PopulationCommodityDemand(Commodities.LUXURY_GOODS, 0, DESC),
                new PopulationCommodityDemand(AliceCommon.LOBSTER, 0, DESC),
                new PopulationCommodityDemand(Commodities.DRUGS, 0, DESC),
                new PopulationCommodityDemand(Commodities.DOMESTIC_GOODS, 0, DESC),
                new PopulationCommodityDemand(Commodities.SUPPLIES, 0, DESC),
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 0, DESC),
                new PopulationCommodityDemand(Commodities.ORGANS, 4, DESC),
                new PopulationCommodityDemand(Commodities.CREW, 4, DESC),
                new PopulationCommodityDemand(Commodities.HAND_WEAPONS, 5, DESC),
                new PopulationCommodityDemand(Commodities.SHIPS, 5, DESC),
                new PopulationCommodityDemand(Commodities.MARINES, 6, DESC)
        );
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        super.modifyIncoming(market, incoming);

        if (incoming == null) return;
        incoming.add(Factions.TRITACHYON, 20f);
        incoming.add(Factions.HEGEMONY, 5f);
        incoming.add(Factions.PERSEAN, 5f);

        incoming.set(Factions.LUDDIC_PATH, 0f);
        incoming.set(Factions.LUDDIC_CHURCH, 0f);
    }

    @Override
    protected String getSubmarket() {
        return Submarkets.SUBMARKET_OPEN;
    }

    @Override
    protected String getMarketDescription() {
        return "Open Market";
    }

    @Override
    protected int getGrowthRate() {
        return 4;
    }

    @Override
    protected void applyForIndustry(float effectiveness) {
        super.applyForIndustry(effectiveness);

        Pair<String, Integer> deficit = getMaxDeficit(Commodities.FOOD, AliceCommon.LOBSTER);
        if (deficit.two <= 0) {
            market.getStability().modifyFlat(getModId(0), 1, "Wealthy food demands met");
        } else {
            market.getStability().unmodifyFlat(getModId(0));
            market.getStability().modifyFlat(getModId(0), -1, "Wealthy food demands not met");
        }

        deficit = getMaxDeficit(Commodities.LUXURY_GOODS);
        if (deficit.two <= 0) {
            market.getStability().modifyFlat(getModId(1), 1, "Wealthy luxury demands met");
        } else {
            market.getStability().unmodifyFlat(getModId(1));
            market.getStability().modifyFlat(getModId(1), -1, "Wealthy luxury demands not met");
        }

        deficit = getMaxDeficit(
                Commodities.HEAVY_MACHINERY,
                Commodities.DOMESTIC_GOODS,
                Commodities.SUPPLIES
        );
        if (deficit.two <= 0) {
            market.getStability().modifyFlat(getModId(2), 1, "Wealthy goods demands met");
        } else {
            market.getStability().unmodifyFlat(getModId(2));
            market.getStability().modifyFlat(getModId(2), -1, "Wealthy goods demands not met");
        }

        deficit = getMaxDeficit(
                Commodities.MARINES,
                Commodities.SHIPS,
                Commodities.HAND_WEAPONS,
                Commodities.CREW,
                Commodities.ORGANS
        );
        if (deficit.two <= 0) {
            market.getStability().modifyFlat(getModId(3), 3, "Wealthy citizens sated.");
        } else {
            market.getStability().unmodifyFlat(getModId(3));
        }

    }

    @Override
    protected void unapplyForIndustry() {
        super.unapplyForIndustry();

        market.getStability().unmodifyFlat(getModId(0));
        market.getStability().unmodifyFlat(getModId(1));
        market.getStability().unmodifyFlat(getModId(2));
        market.getStability().unmodifyFlat(getModId(3));
    }
}
