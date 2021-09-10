package com.github.alycecil.econ.util;

import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;

import java.awt.*;

public class AliceCommon {
    public static final Color WARNING = Color.YELLOW.darker();

    public static FleetFactory.PatrolType getPatrolType(RouteManager.RouteData route) {
        FleetFactory.PatrolType type;
        if (route.getCustom() != null && route.getCustom() instanceof MilitaryBase.PatrolFleetData) {
            type = ((MilitaryBase.PatrolFleetData) route.getCustom()).type;
        } else {
            type = FleetFactory.PatrolType.FAST;
        }
        return type;
    }
}
