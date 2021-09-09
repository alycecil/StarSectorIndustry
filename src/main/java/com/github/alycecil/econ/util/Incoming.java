package com.github.alycecil.econ.util;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;

public class Incoming {
    public static void modifyIncoming(MarketAPI market, PopulationComposition incoming, String modId, float value, String desc) {
        if(incoming == null) return;
        MutableStat weight = incoming.getWeight();
        if(weight == null) return;
        weight.modifyFlat(modId, value, desc);
    }
}
