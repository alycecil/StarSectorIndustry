package com.github.alycecil.econ;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.github.alycecil.econ.model.IndustryBonus;

import java.util.Arrays;
import java.util.List;

public abstract class IndustryExtension extends BaseIndustry {

    List<IndustryBonus> bonuses;

    public IndustryExtension(IndustryBonus... bonuses) {
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
    }

    private void applyForIndustry() {
        String industryId = getIndustryId();
        if (industryId != null) {
            Industry industry = market.getIndustry(industryId);

            if (industry != null) {
                for (IndustryBonus bonus : bonuses) {
                    bonus.apply(industry, getModId());
                }
            }
        }
    }

    @Override
    public void unapply() {
        super.unapply();

        for (IndustryBonus bonus : bonuses) {
            bonus.unapply(this, getModId());
        }

        unapplyForIndustry();


    }

    private void unapplyForIndustry() {
        String industryId = getIndustryId();
        if (industryId != null) {
            Industry industry = market.getIndustry(industryId);
            if (industry != null) {
                for (IndustryBonus bonus : bonuses) {
                    bonus.unapply(industry, getModId());
                }
            }
        }
    }

    @Override
    public String getUnavailableReason() {
        if (!super.isAvailableToBuild()) return super.getUnavailableReason();
        return "Requires " + getIndustryFriendlyName() + ".";
    }


    @Override
    public boolean isAvailableToBuild() {
        if (!super.isAvailableToBuild()) return false;

        return hasRequiredIndustries();
    }

    protected boolean hasRequiredIndustries() {
        return market.hasIndustry(getIndustryId());
    }

    protected abstract String getIndustryId();

    protected abstract String getIndustryFriendlyName();
}
