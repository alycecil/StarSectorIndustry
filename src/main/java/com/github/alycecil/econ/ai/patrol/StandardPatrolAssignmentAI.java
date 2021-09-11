package com.github.alycecil.econ.ai.patrol;

import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.ai.FleetAssignmentDataAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.util.CountingMap;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;

import java.util.List;

import static com.github.alycecil.econ.impl.IndustrialDefenseForce.INDUSTRIAL_DEFENSE_FORCE;

public class StandardPatrolAssignmentAI extends BasePatrolAssignmentAI implements FleetActionTextProvider {
    public StandardPatrolAssignmentAI(CampaignFleetAPI fleet, RouteManager.RouteData route) {
        super(fleet, route);
    }

    @Override
    public String getActionText(CampaignFleetAPI fleet) {
        return INDUSTRIAL_DEFENSE_FORCE + " " + super.getActionText(fleet);
    }

}
