package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;

public abstract class QuantityCommodityBonus implements IndustryBonus {
    protected String commodityId;
    protected String desc;


    public QuantityCommodityBonus(String commodityId, String desc) {
        this.commodityId = commodityId;
        this.desc = desc;
    }

    @Override
    public void apply(Industry industry, String modId) {
        industry.getDemand(commodityId).getQuantity().modifyFlat(modId, getQuantity(industry), desc);
    }

    @Override
    public void unapply(Industry industry, String modId) {
        industry.getDemand(commodityId).getQuantity().unmodifyFlat(modId);
    }

    abstract int getQuantity(Industry industry);
}
