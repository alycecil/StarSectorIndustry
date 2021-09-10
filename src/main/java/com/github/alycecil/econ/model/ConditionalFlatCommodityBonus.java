package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;

public class ConditionalFlatCommodityBonus extends FlatCommodityBonus {
    private final String conditionalId;

    public ConditionalFlatCommodityBonus(String commodityId, String conditionalId, int quantity, String desc) {
        super(commodityId, quantity, desc);
        this.conditionalId = conditionalId;
    }

    @Override
    int getQuantity(Industry industry) {
        if (industry.getMarket().hasCondition(conditionalId)) {
            return super.getQuantity(industry);
        }
        return 0;
    }
}
