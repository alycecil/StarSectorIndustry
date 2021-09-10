package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.combat.MutableStat;

public abstract class QuantityCommodityChange implements IndustryEffect, HasCommodity {
    protected String commodityId;
    protected String desc;

    public QuantityCommodityChange(String commodityId, String desc) {
        this.commodityId = commodityId;
        this.desc = desc;
    }

    @Override
    public void apply(Industry industry, String modId, float mult) {
        if (industry == null) return;
        String desc = this.desc +" for "+ industry.getNameForModifier();
        MutableCommodityQuantity modifier = getModifier(industry);
        if (modifier == null) return;
        MutableStat quantityStat = modifier.getQuantity();
        if (quantityStat == null) return;
        int quantityActual = getQuantity(industry);
        doChange(modId, mult, desc, quantityStat, quantityActual);
    }

    protected abstract void doChange(String modId, float mult, String desc, MutableStat quantityStat, int quantityActual);

    public abstract MutableCommodityQuantity getModifier(Industry industry);

    @Override
    public void unapply(Industry industry, String modId) {
        getModifier(industry).getQuantity().unmodifyFlat(modId);
    }

    abstract int getQuantity(Industry industry);

    public String getCommodityId() {
        return commodityId;
    }
}
