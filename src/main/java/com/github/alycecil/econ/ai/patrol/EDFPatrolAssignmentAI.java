package com.github.alycecil.econ.ai.patrol;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetActionTextProvider;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;

import static com.github.alycecil.econ.impl.EmergencyDefenceForce.EDF;

public class EDFPatrolAssignmentAI extends BasePatrolAssignmentAI implements FleetActionTextProvider {
    public EDFPatrolAssignmentAI(CampaignFleetAPI fleet, RouteManager.RouteData route) {
        super(fleet, route);
    }

    @Override
    public String getActionText(CampaignFleetAPI fleet) {
        return EDF + " " + super.getActionText(fleet);
    }

}
