package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.ai.patrol.EDFPatrolAssignmentAI;
import com.github.alycecil.econ.impl.common.MilitaryIndustry;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class EmergencyDefenceForce extends MilitaryIndustry {

    public static final String EDF = "Emergency Defense Force";

    public EmergencyDefenceForce() {
        super(0.01f,
                new PopulationCommodityDemand(Commodities.SHIPS, 3, EDF),
                new PopulationCommodityDemand(Commodities.FUEL, 2, EDF),
                new PopulationCommodityDemand(Commodities.CREW, 3, EDF)
        );
    }

    @Override
    protected boolean isFullMilitaryBase() {
        return false;
    }

    @Override
    protected int getHeavy() {
        return 0;
    }

    @Override
    protected int getMedium() {
        return 0;
    }

    @Override
    protected int getLight() {
        int min = Math.min(market.getSize() - 5, 3);
        return Math.max(1, min);
    }

    @Override
    protected String getDescription() {
        return EDF;
    }

    @Override
    protected EveryFrameScript getScript(RouteManager.RouteData route, CampaignFleetAPI fleet) {
        return new EDFPatrolAssignmentAI(fleet, route);
    }
}
