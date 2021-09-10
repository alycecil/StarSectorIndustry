package com.github.alycecil.econ.ai;

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

public class StandardPatrolAssignmentAI extends SimplePatrolAssignmentAI implements FleetActionTextProvider {
    public StandardPatrolAssignmentAI(CampaignFleetAPI fleet, RouteManager.RouteData route) {
        super(fleet, route);
    }

    @Override
    public String getActionText(CampaignFleetAPI fleet) {
        return INDUSTRIAL_DEFENSE_FORCE + " " + super.getActionText(fleet);
    }

    @Override
    protected void setupPicker(MilitaryBase.PatrolFleetData custom, FleetFactory.PatrolType type, LocationAPI loc, WeightedRandomPicker<SectorEntityToken> picker) {
        CountingMap<SectorEntityToken> existing = new CountingMap<SectorEntityToken>();
        for (RouteManager.RouteData data : RouteManager.getInstance().getRoutesForSource(route.getSource())) {
            CampaignFleetAPI other = data.getActiveFleet();
            if (other == null) continue;
            FleetAssignmentDataAPI curr = other.getCurrentAssignment();
            if (curr == null || curr.getTarget() == null ||
                    curr.getAssignment() != FleetAssignment.PATROL_SYSTEM) {
                continue;
            }
            existing.add(curr.getTarget());
        }

        List<MarketAPI> markets = Misc.getMarketsInLocation(fleet.getContainingLocation());
        int hostileMax = 0;
        int ourMax = 0;
        for (MarketAPI market : markets) {
            if (market.getFaction().isHostileTo(fleet.getFaction())) {
                hostileMax = Math.max(hostileMax, market.getSize());
            } else if (market.getFaction() == fleet.getFaction()) {
                ourMax = Math.max(ourMax, market.getSize());
            }
        }
        boolean inControl = ourMax > hostileMax;

        for (SectorEntityToken entity : loc.getEntitiesWithTag(Tags.OBJECTIVE)) {
            if (entity.getFaction() != fleet.getFaction()) continue;

            float w = 2f;
            for (int i = 0; i < existing.getCount(entity); i++) w *= 0.1f;

            if (type == FleetFactory.PatrolType.HEAVY) w *= 0.1f;

            picker.add(entity, w);
        }

        // patrol stable locations, will build there
        for (SectorEntityToken entity : loc.getEntitiesWithTag(Tags.STABLE_LOCATION)) {
            float w = 2f;
            for (int i = 0; i < existing.getCount(entity); i++) w *= 0.1f;

            picker.add(entity, w);
        }


        for (SectorEntityToken entity : loc.getJumpPoints()) {
            float w = 2f;
            for (int i = 0; i < existing.getCount(entity); i++) w *= 0.1f;

            if (type == FleetFactory.PatrolType.HEAVY) w *= 0.1f;

            picker.add(entity, w);
        }

        if (inControl) {
            if (loc instanceof StarSystemAPI && custom.type == FleetFactory.PatrolType.HEAVY) {
                StarSystemAPI system = (StarSystemAPI) loc;
                if (system.getHyperspaceAnchor() != null) {
                    float w = 3f;
                    for (int i = 0; i < existing.getCount(system.getHyperspaceAnchor()); i++) w *= 0.1f;
                    picker.add(system.getHyperspaceAnchor(), w);
                }
            }
        }

        for (MarketAPI market : markets) {
            if (market.getFaction().isHostileTo(fleet.getFaction())) continue;

            float w;
            if (market == route.getMarket()) {
                w = 15f;
            } else {
                // defend on-hostile non-military markets; prefer own faction
                //if (!market.hasSubmarket(Submarkets.GENERIC_MILITARY)) {
                if (market.getMemoryWithoutUpdate().getBoolean(MemFlags.MARKET_PATROL)) {
                    if (market.getFaction() != fleet.getFaction()) {
                        w = 0f; // don't patrol near patrolHQ/military markets of another faction
                    } else {
                        w = 4f;
                    }
                }else{
                    //some times just fly to other stuff to see whats up; cause we can.
                   w = 1f;
                }
            }

            for (int i = 0; i < existing.getCount(market.getPrimaryEntity()); i++) w *= 0.1f;
            picker.add(market.getPrimaryEntity(), w);
        }

        if (fleet.getContainingLocation() instanceof StarSystemAPI && type != FleetFactory.PatrolType.HEAVY) {
            StarSystemAPI system = (StarSystemAPI) fleet.getContainingLocation();
            float w = 1f;
            for (int i = 0; i < existing.getCount(system.getCenter()); i++) w *= 0.1f;
            picker.add(system.getCenter(), w);
        }
    }

}
