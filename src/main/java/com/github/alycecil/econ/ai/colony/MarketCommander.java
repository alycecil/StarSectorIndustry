package com.github.alycecil.econ.ai.colony;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.econ.impl.ConstructionQueue;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Industries;

import java.util.List;

import static com.github.alycecil.econ.util.AliceIndustries.*;

public class MarketCommander implements EconomyTickListener {
    MarketAPI market;

    public MarketCommander(MarketAPI market) {
        this.market = market;
    }

    @Override
    public void reportEconomyTick(int iterIndex) {
        reportEconomyMonthEnd();
    }

    @Override
    public void reportEconomyMonthEnd() {
        if (market == null) {
            Global.getLogger(this.getClass()).warn("Market Null");
            return;
        } else if (market.isPlayerOwned()
                || market.getFaction() == null
        ) {
            Global.getLogger(this.getClass()).info("Market Unfit, " + market.getName());
            return;
        }

        if (market.getConstructionQueue() == null) {
            Global.getLogger(this.getClass()).warn("Market unable to build with queue, " + market.getName());
            return;
        }

        boolean building = isBuilding();

        if (!building) {
            //todo debug
            Global.getLogger(this.getClass()).info("Market " + market.getName() + " looking for construction.");

            prepareConstruction();
        } else {
            //todo debug
            Global.getLogger(this.getClass()).info("Market " + market.getName() + " building.");
        }
    }

    protected boolean isBuilding() {
        boolean building = false;
        for (Industry industry : market.getIndustries()) {
            if (industry.isBuilding()) {
                //todo debug
                Global.getLogger(this.getClass()).info("Market " + market.getName() + " building a " + industry.getCurrentName() + ".");

                building = true;
                break;
            }
        }
        return building;
    }

    protected void prepareConstruction() {
        boolean build = false;

        ConstructionQueue constructionQueue = market.getConstructionQueue();
        if (constructionQueue == null) return;
        List<ConstructionQueue.ConstructionQueueItem> items = constructionQueue.getItems();
        if (items == null) return;
        if (items.isEmpty()) {
            //todo debug
            Global.getLogger(this.getClass()).info("Market " + market.getName() + " queueing new work.");
            build = buildQueue(constructionQueue);
        } else {
            Global.getLogger(this.getClass()).info("Market " + market.getName() + " already queued work.");

            build = true;
        }

        if (build) {
            //todo debug
            Global.getLogger(this.getClass()).info("Market " + market.getName() + " starting building.");

            BaseIndustry.buildNextInQueue(market);
        }
    }

    //TODO remove tech-debt.
    boolean did; //dont overqueue, lazy and bad way should be better "market plans" loop but this is POC land and is just a simple "ai"

    protected synchronized /*jic see TODO*/ boolean buildQueue(ConstructionQueue constructionQueue) {
        did = false;
        //dear lord let has industry by on a hashtable and not an O(n) list scan.
        //if not well at least only end of month sucks.

        float modifiedValue = market.getStability().getModifiedValue();

        boolean cruel = market.getFaction().isIllegal(Commodities.HAND_WEAPONS)
                && !market.getFaction().isIllegal(Commodities.ORGANS);

        if (modifiedValue < 5) {
            if (!market.hasIndustry(PoliceState)) {
                constructionQueue.addToEnd(PoliceState, 1000);
            } else if (cruel && !market.hasIndustry(AuthoritarianRegime) && modifiedValue < 3 && market.getSize() > 4) {
                //need a large enough population for tribalism to falter to the point where otherism starts and survival is not a primary concern.
                constructionQueue.addToEnd(AuthoritarianRegime, 1000);
            }
        }

        boolean farmers = market.hasIndustry(Industries.FARMING);
        boolean waterFarmers = market.hasIndustry(Industries.AQUACULTURE);

        //TODO Make Picker.
        switch (market.getSize()) {
            case 10:
                queueIfNotPresent(constructionQueue, PopulationWealthy);
                if (farmers) {
                    queueIfNotPresent(constructionQueue, AutomatedFarming);
                }
                if (cruel && modifiedValue > 6) {
                    queueIfNotPresent(constructionQueue, ChopShop);
                }
            case 9:
                queueIfNotPresent(constructionQueue, SpaceElevator);
                queueIfNotPresent(constructionQueue, GatesCustomsNavy);
                if (farmers) {
                    queueIfNotPresent(constructionQueue, IndustrialFarming);
                }
            case 8:
                if (market.hasIndustry(Industries.HEAVYINDUSTRY)) {
                    queueIfNotPresent(constructionQueue, IndustrialDefenseForce);
                }else{
                    queueIfNotPresent(constructionQueue, SelfDefenseForce);
                }
                queueIfNotPresent(constructionQueue, CivilianInfrastructureLuxury);
                if (farmers) {
                    queueIfNotPresent(constructionQueue, StateFarms);
                }
            case 7:
                queueIfNotPresent(constructionQueue, CivilianInfrastructureCommon);

                if (!farmers && !waterFarmers && market.hasIndustry(Industries.FUELPROD)) {
                    queueIfNotPresent(constructionQueue, BulkFuelProduction);
                }
            case 6:
                queueIfNotPresent(constructionQueue, TransportationInfrastructure);

                if (market.hasIndustry(Industries.ORBITALWORKS)) {
                    queueIfNotPresent(constructionQueue, IndustrialDefenseForce);
                }
            case 5:
                if (waterFarmers) {
                    queueIfNotPresent(constructionQueue, AquacultureExtensions);
                } else if (!farmers && market.hasIndustry(Industries.MINING)) {
                    queueIfNotPresent(constructionQueue, TraceMining);
                }
            case 4:
                if (market.hasIndustry(Industries.HIGHCOMMAND)) {
                    if (cruel) {
                        queueIfNotPresent(constructionQueue, PoliceState);
                    } else {
                        queueIfNotPresent(constructionQueue, GatesCustomsNavy);
                    }
                    queueIfNotPresent(constructionQueue, Industries.FUELPROD);
                }
            default:
                if (farmers) {
                    queueIfNotPresent(constructionQueue, RuralFarmCoOp);
                }

        }
        return did;
    }

    private void queueIfNotPresent(ConstructionQueue constructionQueue, String id) {
        if (!did && !market.hasIndustry(id)) {
            //todo debug
            Global.getLogger(this.getClass()).info("Market " + market.getName() + " should build " + id + " next.");

            constructionQueue.addToEnd(id, 1000);
            did = true;
        }
    }
}
