package com.github.alycecil.econ.ai.patrol;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetActionTextProvider;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.util.WeightedRandomPicker;

import static com.github.alycecil.econ.impl.SelfDefenceForce.NAVAL_SELF_DEFENSE;

public class SDFPatrolAssignmentAI extends SimplePatrolAssignmentAI implements FleetActionTextProvider {
    public SDFPatrolAssignmentAI(CampaignFleetAPI fleet, RouteManager.RouteData route) {
        super(fleet, route);
    }

    @Override
    public String getActionText(CampaignFleetAPI fleet) {
        return NAVAL_SELF_DEFENSE + " " + super.getActionText(fleet);
    }

    @Override
    protected void setupPicker(MilitaryBase.PatrolFleetData custom, FleetFactory.PatrolType type, LocationAPI loc, WeightedRandomPicker<SectorEntityToken> picker) {
        // patrol stable locations, will build there
        int i = 1;
        for (SectorEntityToken entity : loc.getEntitiesWithTag(Tags.STABLE_LOCATION)) {
            float w = 2f;

            if (type == FleetFactory.PatrolType.HEAVY) w *= 0.1f;

            picker.add(entity, w);
            i++;
        }
        for (SectorEntityToken entity : loc.getJumpPoints()) {
            float w = 1.5f;

            if (type == FleetFactory.PatrolType.HEAVY) w *= 0.1f;

            picker.add(entity, w);
            i++;
        }
        picker.add(route.getMarket().getPrimaryEntity(), i*2f);
    }
}
