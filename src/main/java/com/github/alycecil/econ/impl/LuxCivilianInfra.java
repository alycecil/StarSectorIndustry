package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.github.alycecil.econ.impl.common.SupportInfraGrowsPopulation;
import com.github.alycecil.econ.impl.common.SupportInfrastructure;
import com.github.alycecil.econ.model.PopulationCommodityDemand;
import com.github.alycecil.econ.util.Incoming;

import java.awt.*;

import static com.fs.starfarer.api.impl.campaign.econ.impl.TradeCenter.BASE_BONUS;
import static com.fs.starfarer.api.impl.campaign.econ.impl.TradeCenter.STABILITY_PELANTY;

public class LuxCivilianInfra extends SupportInfraGrowsPopulation implements MarketImmigrationModifier {

    public static final String DESC = "Civilian Infrastructure (Luxury)";

    public LuxCivilianInfra() {
        super(0.08f,
                new PopulationCommodityDemand(Commodities.LUXURY_GOODS, 0, DESC),
                new PopulationCommodityDemand(Commodities.FOOD, -1, DESC),
                new PopulationCommodityDemand(Commodities.HEAVY_MACHINERY, 3, DESC)
        );
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    protected int getGrowthRate() {
        return 3;
    }
}
