package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;

public interface IndustryEffect {
    void apply(Industry industry, String modId, float effectiveness);

    void unapply(Industry Industry, String modId);
}
