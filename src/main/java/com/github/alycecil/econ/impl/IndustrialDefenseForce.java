package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.ai.patrol.StandardPatrolAssignmentAI;
import com.github.alycecil.econ.impl.common.MilitaryIndustry;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class IndustrialDefenseForce extends MilitaryIndustry {

    public static final String INDUSTRIAL_DEFENSE_FORCE = "Industrial Defense Force";

    public IndustrialDefenseForce() {
        super(0.01f,
                new PopulationCommodityDemand(Commodities.SHIPS, 2, INDUSTRIAL_DEFENSE_FORCE),
                new PopulationCommodityDemand(Commodities.FUEL, 1, INDUSTRIAL_DEFENSE_FORCE),
                new PopulationCommodityDemand(Commodities.CREW, 2, INDUSTRIAL_DEFENSE_FORCE),
                new PopulationCommodityDemand(Commodities.HAND_WEAPONS, 3, INDUSTRIAL_DEFENSE_FORCE),
                new PopulationCommodityDemand(Commodities.MARINES, 2, INDUSTRIAL_DEFENSE_FORCE)
        );
    }

    @Override
    protected boolean isFullMilitaryBase() {
        return true;
    }

    @Override
    protected int getHeavy() {
        return market.getSize() > 8 ? 1 : 0;
    }

    @Override
    protected int getMedium() {
        return market.getSize() > 5 ? 1 : 0;
    }

    @Override
    protected int getLight() {
        return Math.max(1, market.getSize() / 2);
    }

    @Override
    protected String getDescription() {
        return INDUSTRIAL_DEFENSE_FORCE;
    }

    @Override
    protected EveryFrameScript getScript(RouteManager.RouteData route, CampaignFleetAPI fleet) {
        return new StandardPatrolAssignmentAI(fleet, route);
    }

    @Override
    protected void applyForIndustry(float effectiveness) {
        super.applyForIndustry(effectiveness);
    }

    @Override
    protected void unapplyForIndustry() {
        super.unapplyForIndustry();
    }
}
