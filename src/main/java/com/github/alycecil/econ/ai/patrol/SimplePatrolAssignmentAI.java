package com.github.alycecil.econ.ai.patrol;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetActionTextProvider;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.impl.campaign.fleets.PatrolAssignmentAIV4;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.util.WeightedRandomPicker;

import java.util.Random;

public abstract class SimplePatrolAssignmentAI extends PatrolAssignmentAIV4 implements FleetActionTextProvider {
    public SimplePatrolAssignmentAI(CampaignFleetAPI fleet, RouteManager.RouteData route) {
        super(fleet, route);
    }

    public SectorEntityToken pickEntityToGuard() {
        Random random = route.getRandom(1);

        MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) route.getCustom();
        FleetFactory.PatrolType type = custom.type;

        LocationAPI loc = fleet.getContainingLocation();
        if (loc == null) return null;

        WeightedRandomPicker<SectorEntityToken> picker = new WeightedRandomPicker<SectorEntityToken>(random);

        setupPicker(custom, type, loc, picker);

        SectorEntityToken target = picker.pick();
        if (target == null) {
            target = route.getMarket().getPrimaryEntity();
        }

        return target;
    }

    protected abstract void setupPicker(MilitaryBase.PatrolFleetData custom, FleetFactory.PatrolType type, LocationAPI loc, WeightedRandomPicker<SectorEntityToken> picker);
}
