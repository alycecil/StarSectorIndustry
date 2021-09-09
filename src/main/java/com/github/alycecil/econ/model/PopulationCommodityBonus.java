package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;

public class PopulationCommodityBonus extends QuantityCommodityBonus {
    private int reduction;

    public PopulationCommodityBonus(String commodityId, int reduction, String desc) {
        super(commodityId, desc);
        this.reduction = reduction;
    }

    @Override
    int getQuantity(Industry industry) {
        return Math.max(1, industry.getMarket().getSize() - reduction);
    }
}
