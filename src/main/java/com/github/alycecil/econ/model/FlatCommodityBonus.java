package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;

public class FlatCommodityBonus extends QuantityCommodityBonus {
    private int quantity;

    public FlatCommodityBonus(String commodityId, int quantity, String desc) {
        super(commodityId, desc);
        this.quantity = quantity;
    }

    @Override
    int getQuantity(Industry industry) {
        return quantity;
    }
}
