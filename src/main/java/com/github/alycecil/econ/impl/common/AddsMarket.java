package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.github.alycecil.econ.model.IndustryEffect;

import java.awt.*;

import static com.fs.starfarer.api.impl.campaign.econ.impl.TradeCenter.BASE_BONUS;
import static com.fs.starfarer.api.impl.campaign.econ.impl.TradeCenter.STABILITY_PELANTY;

public abstract class AddsMarket extends SupportInfraGrowsPopulation {
    protected transient SubmarketAPI saved = null;

    protected boolean addedMarket = false;

    public AddsMarket(float perMarketSize, IndustryEffect... bonuses) {
        super(perMarketSize, bonuses);
    }

    protected abstract String getSubmarket();

    protected abstract String getMarketDescription();


    public void apply() {
        super.apply();

        apply(true);

        addMarket();

        if (!isFunctional()) {
            unapply();
        }
    }

    protected void addMarket() {
        if (isFunctional() && market.isPlayerOwned()) {
            SubmarketAPI open = market.getSubmarket(getSubmarket());
            if (open == null) {
                addedMarket = true;
                if (saved != null) {
                    market.addSubmarket(saved);
                } else {
                    market.addSubmarket(getSubmarket());
                    SubmarketAPI sub = market.getSubmarket(getSubmarket());
                    sub.setFaction(getMarketAddedFaction());
                    Global.getSector().getEconomy().forceStockpileUpdate(market);
                }

            }
        } else if (market.isPlayerOwned()) {
            if (addedMarket) {
                market.removeSubmarket(getSubmarket());
                addedMarket = false;
            }
        }

        //modifyStabilityWithBaseMod();
        market.getStability().modifyFlat(getModId(), -STABILITY_PELANTY, getNameForModifier());

        market.getIncomeMult().modifyPercent(getModId(0), BASE_BONUS, getNameForModifier());
    }

    protected FactionAPI getMarketAddedFaction() {
        return market.getFaction();
    }

    @Override
    public void unapply() {
        super.unapply();

        removeMarket();
    }

    protected void removeMarket() {
        if (market.isPlayerOwned() && addedMarket) {
            SubmarketAPI open = market.getSubmarket(getSubmarket());
            saved = open;
            market.removeSubmarket(getSubmarket());
            addedMarket = false;
        }

        market.getStability().unmodifyFlat(getModId());

        market.getIncomeMult().unmodifyPercent(getModId(0));
    }


    protected void addStabilityPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        super.addStabilityPostDemandSection(tooltip, hasDemand, mode);

        Color h = Misc.getHighlightColor();
        float opad = 10f;

        float a = BASE_BONUS;
        String aStr = "+" + (int) Math.round(a * 1f) + "%";
        tooltip.addPara("Colony income: %s", opad, h, aStr);

        h = Misc.getNegativeHighlightColor();
        tooltip.addPara("Base stability penalty: %s", opad, h, "" + -(int) STABILITY_PELANTY);
    }

    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        super.addPostDemandSection(tooltip, hasDemand, mode);
        if (mode != IndustryTooltipMode.NORMAL || isFunctional()) {
            addStabilityPostDemandSection(tooltip, hasDemand, mode);
        }
    }

    @Override
    protected void addRightAfterDescriptionSection(TooltipMakerAPI tooltip, IndustryTooltipMode mode) {
        super.addRightAfterDescriptionSection(tooltip, mode);

        if (market.isPlayerOwned() && addedMarket || currTooltipMode == IndustryTooltipMode.ADD_INDUSTRY) {
            FactionAPI marketAddedFaction = getMarketAddedFaction();
            String factionName = marketAddedFaction != null ? marketAddedFaction.getDisplayNameWithArticle() : "Independents";

            tooltip.addPara("%s owns the \'%s\' that the colony's owner is able to trade with.", 10f, Misc.getHighlightColor(),
                    factionName,
                    getMarketDescription()
            );
        }
    }
}
