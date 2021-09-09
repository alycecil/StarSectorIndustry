package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.github.alycecil.econ.model.IndustryBonus;

public abstract class IndustryExtension extends AliceBaseIndustry {

    public IndustryExtension(IndustryBonus... bonuses) {
        super(bonuses);
    }

    @Override
    protected void applyForIndustry() {
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
    protected void unapplyForIndustry() {
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
