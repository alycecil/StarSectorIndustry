package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;

public abstract class QuantityCommodityDemand extends QuantityCommodityChange {


    public QuantityCommodityDemand(String commodityId, String desc) {
        super(commodityId, desc);
    }

    @Override
    public MutableCommodityQuantity getModifier(Industry industry) {
        return industry.getDemand(commodityId);
    }
}
