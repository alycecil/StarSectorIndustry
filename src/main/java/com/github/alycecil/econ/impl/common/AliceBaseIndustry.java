package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.util.Pair;
import com.github.alycecil.econ.model.IndustryDemand;
import com.github.alycecil.econ.model.IndustryEffect;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class AliceBaseIndustry extends BaseIndustry {
    List<IndustryEffect> bonuses;

    public AliceBaseIndustry(IndustryEffect... bonuses) {
        this.bonuses = Arrays.asList(bonuses);
    }

    @Override
    public void apply() {
        if (isFunctional() && bonuses != null) {
            float effectiveness = 1f;
            if (this instanceof HasEffectiveness) {
                effectiveness = ((HasEffectiveness) this).getEffectiveness();
            }

            applyForIndustry(effectiveness);


            for (IndustryEffect bonus : bonuses) {
                bonus.apply(this, getModId(), effectiveness);
            }
        }
        if (this instanceof MarketImmigrationModifier) {
            market.addTransientImmigrationModifier((MarketImmigrationModifier) this);
        }
    }

    protected abstract void applyForIndustry(float effectiveness);

    @Override
    public void unapply() {
        super.unapply();

        for (IndustryEffect bonus : bonuses) {
            bonus.unapply(this, getModId());
        }

        unapplyForIndustry();


    }

    protected abstract void unapplyForIndustry();

    public List<String> getDemanded() {
        List<String> list = new LinkedList<>();
        for (IndustryEffect bonus : bonuses) {
            if (bonus instanceof IndustryDemand) {
                list.add(
                        ((IndustryDemand) bonus).getCommodityId()
                );
            }
        }
        return list;
    }

    public float getEffectiveness() {
        List<String> list = getDemanded();
        float effectiveness = 1f;
        if (list == null || list.isEmpty()) return effectiveness;

        String[] type = new String[list.size()];
        Pair<String, Integer> deficit = getMaxDeficit(list.toArray(type));

        if (deficit != null && deficit.two != null && deficit.two >= 1) {
            int size = market.getSize();
            if (deficit.two >= size) {
                effectiveness = 10;
            } else {
                effectiveness = (float) (size - deficit.two) / size;
            }
        }
        return effectiveness;
    }
}
