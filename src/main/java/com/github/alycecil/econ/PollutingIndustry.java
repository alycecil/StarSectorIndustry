package com.github.alycecil.econ;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.github.alycecil.econ.model.IndustryBonus;

import java.awt.*;


public abstract class PollutingIndustry extends PopulationAwareExtension {
    public static String POLLUTION_ID = Conditions.POLLUTION;
    public static float DAYS_BEFORE_POLLUTION = 90f;
    public static float DAYS_BEFORE_POLLUTION_PERMANENT = 360f;

    public static final Color WARNING = Color.YELLOW.darker();

    public PollutingIndustry(IndustryBonus... bonuses) {
        super(bonuses);
    }

    protected boolean permaPollution = false;
    protected boolean addedPollution = false;
    protected float daysWithPolluter = 0f;


    @Override
    public void advance(float amount) {
        super.advance(amount);

        float days = Global.getSector().getClock().convertToDays(amount);
        daysWithPolluter += days;

        updatePollutionStatus(true);
    }

    @Override
    public void apply() {
        super.apply();
        updatePollutionStatus(true);
    }

    @Override
    public void unapply() {
        super.unapply();
        updatePollutionStatus(false);
    }

    protected void updatePollutionStatus(boolean add) {
        if (!market.hasCondition(Conditions.HABITABLE)) return;

        if (add) {
            if (!addedPollution && daysWithPolluter >= DAYS_BEFORE_POLLUTION) {
                if (market.hasCondition(POLLUTION_ID)) {
                    permaPollution = true;
                } else {
                    market.addCondition(POLLUTION_ID);
                    addedPollution = true;
                }
            }
            if (addedPollution && !permaPollution) {
                if (daysWithPolluter > DAYS_BEFORE_POLLUTION_PERMANENT) {
                    permaPollution = true;
                }
            }
        } else if (addedPollution && !permaPollution) {
            market.removeCondition(POLLUTION_ID);
            addedPollution = false;
        }
    }

    @Override
    protected void addPostSupplySection(TooltipMakerAPI tooltip, boolean hasSupply, IndustryTooltipMode mode) {
        super.addPostSupplySection(tooltip, hasSupply, mode);
        if (addedPollution && permaPollution) {
            tooltip.addPara("The pollution caused by this industry is %s.", 10f, WARNING, Misc.getHighlightColor(), "PERMANENT");
        } else if (addedPollution || market.hasCondition(POLLUTION_ID)) {
            String daysLeft = String.valueOf(Math.max(0, DAYS_BEFORE_POLLUTION_PERMANENT - daysWithPolluter));
            tooltip.addPara("There are %s days left before the pollution becomes permanent, from this source.", 10f, WARNING, Misc.getHighlightColor(), daysLeft);
        } else if (!market.hasCondition(POLLUTION_ID)) {
            String daysLeft = String.valueOf(Math.max(0, DAYS_BEFORE_POLLUTION - daysWithPolluter));
            tooltip.addPara("There are %s days left before the pollution becomes a colony issue, from this source.", 10f, WARNING, Misc.getHighlightColor(), daysLeft);
        }

    }
}
