package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.github.alycecil.econ.model.IndustryBonus;

import java.util.Arrays;
import java.util.List;

public abstract class AliceBaseIndustry extends BaseIndustry {
    List<IndustryBonus> bonuses;

    public AliceBaseIndustry(IndustryBonus... bonuses) {
        this.bonuses = Arrays.asList(bonuses);
    }

    @Override
    public void apply() {
        if (isFunctional() && bonuses != null) {
            applyForIndustry();

            for (IndustryBonus bonus : bonuses) {
                bonus.apply(this, getModId());
            }
        }
        if (this instanceof MarketImmigrationModifier) {
            market.addTransientImmigrationModifier((MarketImmigrationModifier) this);
        }
    }

    protected abstract void applyForIndustry();

    @Override
    public void unapply() {
        super.unapply();

        for (IndustryBonus bonus : bonuses) {
            bonus.unapply(this, getModId());
        }

        unapplyForIndustry();


    }

    protected abstract void unapplyForIndustry();
}
