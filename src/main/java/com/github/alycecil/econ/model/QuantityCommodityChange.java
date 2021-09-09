package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;

public abstract class QuantityCommodityChange implements IndustryBonus {
    protected String commodityId;
    protected String desc;

    public QuantityCommodityChange(String commodityId, String desc) {
        this.commodityId = commodityId;
        this.desc = desc;
    }

    @Override
    public void apply(Industry industry, String modId) {
        String desc = this.desc + industry.getNameForModifier();
        getModifier(industry).getQuantity().modifyFlat(modId, getQuantity(industry), desc);
    }

    public abstract MutableCommodityQuantity getModifier(Industry industry);

    @Override
    public void unapply(Industry industry, String modId) {
        getModifier(industry).getQuantity().unmodifyFlat(modId);
    }

    abstract int getQuantity(Industry industry);
}
