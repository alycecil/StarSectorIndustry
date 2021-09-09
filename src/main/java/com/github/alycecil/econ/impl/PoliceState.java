package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.impl.common.SupportInfraGrowsPopulation;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

//TODO ALERT FACTIONS LUDDIC_*, HEGEMONY, and the League on trade or advance. Quickly causing war.
public class PoliceState extends SupportInfraGrowsPopulation {

    public static final String CRIMES_AGAINST_HUMANITY = "Crimes against humanity.";

    public PoliceState() {
        super(-0.05f, new PopulationCommodityDemand(Commodities.MARINES, 3, CRIMES_AGAINST_HUMANITY));
    }

    @Override
    protected int getGrowthRate() {
        return -3;
    }

    @Override
    public int getDemand() {
        return market.getSize()-2;
    }

    @Override
    protected String getDescription() {
        return CRIMES_AGAINST_HUMANITY;
    }

    @Override
    protected String getCommodity() {
        return Commodities.HAND_WEAPONS;
    }

    @Override
    protected void applyForIndustry() {
        super.applyForIndustry();
        market.getStability().modifyFlat(getModId(0), 3, getNameForModifier());
    }

    @Override
    protected void unapplyForIndustry() {
        super.unapplyForIndustry();
        market.getStability().unmodifyFlat(getModId(0));
    }


}