package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.github.alycecil.econ.impl.common.AddsMarket;
import com.github.alycecil.econ.model.PopulationCommodityBonus;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

//TODO ALERT FACTIONS LUDDIC_*, HEGEMONY, and the League on trade or advance. Slowly causing war.
public class ChopShop extends AddsMarket {

    public static final String CRIMES_AGAINST_HUMANITY = "Organ Harvesting";

    public ChopShop() {
        super(-0.05f,
                new PopulationCommodityDemand(Commodities.HAND_WEAPONS, 2, CRIMES_AGAINST_HUMANITY),
                new PopulationCommodityDemand(Commodities.CREW, 3, CRIMES_AGAINST_HUMANITY),
                new PopulationCommodityBonus(Commodities.ORGANS, 1, CRIMES_AGAINST_HUMANITY)
        );
    }

    @Override
    protected int getGrowthRate() {
        return -5;
    }

    @Override
    protected String getDescription() {
        return CRIMES_AGAINST_HUMANITY;
    }

    @Override
    protected void applyForIndustry(float effectiveness) {
        super.applyForIndustry(effectiveness);
        market.getStability().modifyFlat(getModId(0), -2, CRIMES_AGAINST_HUMANITY);
    }

    @Override
    protected void unapplyForIndustry() {
        super.unapplyForIndustry();
        market.getStability().unmodifyFlat(getModId(0));
    }


    @Override
    protected String getSubmarket() {
        return Submarkets.SUBMARKET_BLACK;
    }

    @Override
    protected FactionAPI getMarketAddedFaction() {
        return Global.getSector().getFaction(Factions.INDEPENDENT);
    }

    @Override
    protected String getMarketDescription() {
        return "Black Market";
    }
}