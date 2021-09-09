package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;

public class SpaceElevator extends BaseIndustry  implements MarketImmigrationModifier {

    public static final int DEMAND = 5;

    @Override
    public void apply() {
        demand(Commodities.HEAVY_MACHINERY, DEMAND);

        float mult = getEffectiveness();


        if (isFunctional()) {
            market.getAccessibilityMod().modifyFlat(id, 0.5f * mult, "Space Elevator");
        } else if (isDisrupted()) {
            market.getAccessibilityMod().modifyFlat(id, 0.1f * mult, "Disrupted Space Elevator");
        }

        market.addTransientImmigrationModifier(this);
    }

    private float getEffectiveness() {
        float mult = 1f;
        Pair<String, Integer> deficit = getMaxDeficit(Commodities.HEAVY_MACHINERY);
        if (deficit != null && deficit.two != null) {
            mult = (DEMAND - deficit.two) / DEMAND;
        }
        return mult;
    }

    @Override
    public void unapply() {
        super.unapply();
        market.removeTransientImmigrationModifier(this);
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        float mult = getEffectiveness();
        incoming.getWeight().modifyFlat(getModId(), 10 * mult, "Space Elevator");
    }

    @Override
    protected void addPostSupplySection(TooltipMakerAPI tooltip, boolean hasSupply, IndustryTooltipMode mode) {
        super.addPostSupplySection(tooltip, hasSupply, mode);
        String pct = "" + (int)(getEffectiveness() * 100f) + "%";
        tooltip.addPara("Currently operating at %s effective load.", 10f, Misc.getHighlightColor(), pct);
    }
}
