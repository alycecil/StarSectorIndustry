package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;

public interface IndustryBonus {
    void apply(Industry industry, String modId);

    void unapply(Industry Industry, String modId);
}
