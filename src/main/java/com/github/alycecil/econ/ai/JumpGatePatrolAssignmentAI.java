package com.github.alycecil.econ.ai;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetActionTextProvider;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.util.WeightedRandomPicker;

import static com.github.alycecil.econ.impl.JumpDefense.JUMP_DEFENSE_FORCE;

public class JumpGatePatrolAssignmentAI extends SimplePatrolAssignmentAI implements FleetActionTextProvider {
    public JumpGatePatrolAssignmentAI(CampaignFleetAPI fleet, RouteManager.RouteData route) {
        super(fleet, route);
    }

    @Override
    protected void setupPicker(MilitaryBase.PatrolFleetData custom, FleetFactory.PatrolType type, LocationAPI loc, WeightedRandomPicker<SectorEntityToken> picker) {
        for (SectorEntityToken entity : loc.getEntitiesWithTag(Tags.STABLE_LOCATION)) {
            float w = 2f;

            if (type == FleetFactory.PatrolType.HEAVY) w *= 0.1f;

            picker.add(entity, w);
        }

        for (SectorEntityToken entity : loc.getJumpPoints()) {
            float w = 5f;

            if (type == FleetFactory.PatrolType.HEAVY) w *= 0.1f;

            picker.add(entity, w);
        }
        picker.add(route.getMarket().getPrimaryEntity(), 1f);
    }

    @Override
    public String getActionText(CampaignFleetAPI fleet) {
        return JUMP_DEFENSE_FORCE + " " + super.getActionText(fleet);
    }
}
