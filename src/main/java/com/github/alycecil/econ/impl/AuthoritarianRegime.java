package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.impl.common.SupportInfraGrowsPopulation;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

//TODO ALERT FACTIONS LUDDIC_*, HEGEMONY, and the League on trade or advance. Quickly causing war.
public class AuthoritarianRegime extends SupportInfraGrowsPopulation {

    public static final String CRIMES_AGAINST_HUMANITY = "Crimes against humanity";

    public AuthoritarianRegime() {
        super(-0.05f,
                new PopulationCommodityDemand(Commodities.HAND_WEAPONS, 0, CRIMES_AGAINST_HUMANITY),
                new PopulationCommodityDemand(Commodities.MARINES, 1, CRIMES_AGAINST_HUMANITY)
        );
    }

    @Override
    protected int getGrowthRate() {
        return -10;
    }

    @Override
    protected String getDescription() {
        return CRIMES_AGAINST_HUMANITY;
    }

    @Override
    protected void applyForIndustry(float effectiveness) {
        super.applyForIndustry(effectiveness);
        market.getStability().modifyFlat(getModId(0), 5*effectiveness, getNameForModifier());
    }

    @Override
    protected void unapplyForIndustry() {
        super.unapplyForIndustry();
        market.getStability().unmodifyFlat(getModId(0));
    }
}
