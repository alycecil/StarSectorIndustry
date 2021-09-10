package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.combat.MutableStat;

public abstract class QuantityCommodityBonus extends QuantityCommodityChange implements IndustryBonus {


    public QuantityCommodityBonus(String commodityId, String desc) {
        super(commodityId, desc);
    }

    @Override
    public MutableCommodityQuantity getModifier(Industry industry) {
        return industry.getSupply(commodityId);
    }

    @Override
    public void doChange(String modId, float mult, String desc, MutableStat quantityStat, int quantityActual) {
        quantityStat.modifyFlat(modId, Math.round(quantityActual*mult), desc);
    }
}
