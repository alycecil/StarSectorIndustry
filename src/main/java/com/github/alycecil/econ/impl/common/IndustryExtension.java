package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.github.alycecil.econ.model.IndustryEffect;

public abstract class IndustryExtension extends AliceBaseIndustry {

    public IndustryExtension(IndustryEffect... bonuses) {
        super(bonuses);
    }

    @Override
    protected void applyForIndustry(float effectiveness) {
        String industryId = getIndustryId();
        if (industryId != null) {
            Industry industry = market.getIndustry(industryId);

            if (industry != null) {
                for (IndustryEffect bonus : bonuses) {
                    bonus.apply(industry, getModId(), effectiveness);
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
                for (IndustryEffect bonus : bonuses) {
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
