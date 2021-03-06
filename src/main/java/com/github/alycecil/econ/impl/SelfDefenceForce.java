package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.ai.patrol.SDFPatrolAssignmentAI;
import com.github.alycecil.econ.impl.common.MilitaryIndustry;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class SelfDefenceForce extends MilitaryIndustry {

    public static final String NAVAL_SELF_DEFENSE = "Self Defense Navy";

    public SelfDefenceForce() {
        super(0.01f,
                new PopulationCommodityDemand(Commodities.SHIPS, 2, NAVAL_SELF_DEFENSE),
                new PopulationCommodityDemand(Commodities.FUEL, 2, NAVAL_SELF_DEFENSE),
                new PopulationCommodityDemand(Commodities.MARINES, 4, NAVAL_SELF_DEFENSE),
                new PopulationCommodityDemand(Commodities.HAND_WEAPONS, 4, NAVAL_SELF_DEFENSE),
                new PopulationCommodityDemand(Commodities.CREW, 2, NAVAL_SELF_DEFENSE)
        );
    }

    @Override
    protected boolean isFullMilitaryBase() {
        return false;
    }

    @Override
    protected int getHeavy() {
        return market.getSize() > 8 ? 1 : 0;
    }

    @Override
    protected int getMedium() {
        return market.getSize() > 6 ? 2 : 1;
    }

    @Override
    protected int getLight() {
        return 1;
    }

    @Override
    protected String getDescription() {
        return NAVAL_SELF_DEFENSE;
    }

    @Override
    protected EveryFrameScript getScript(RouteManager.RouteData route, CampaignFleetAPI fleet) {
        return new SDFPatrolAssignmentAI(fleet, route);
    }
}
