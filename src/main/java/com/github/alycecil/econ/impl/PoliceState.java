package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.impl.common.SupportInfraGrowsPopulation;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

//TODO ALERT FACTIONS LUDDIC_*, HEGEMONY, and the League on trade or advance. Quickly causing war.
public class PoliceState extends SupportInfraGrowsPopulation {

    public static final String POLICE_STATE = "Police state";

    public PoliceState() {
        super(-0.05f,
                new PopulationCommodityDemand(Commodities.HAND_WEAPONS, 2, POLICE_STATE),
                new PopulationCommodityDemand(Commodities.MARINES, 3, POLICE_STATE)
        );
    }

    @Override
    protected int getGrowthRate() {
        return -3;
    }

    @Override
    protected String getDescription() {
        return POLICE_STATE;
    }

    @Override
    protected void applyForIndustry(float effectiveness) {
        super.applyForIndustry(effectiveness);
        market.getStability().modifyFlat(getModId(0), 3*effectiveness, getNameForModifier());
    }

    @Override
    protected void unapplyForIndustry() {
        super.unapplyForIndustry();
        market.getStability().unmodifyFlat(getModId(0));
    }
}