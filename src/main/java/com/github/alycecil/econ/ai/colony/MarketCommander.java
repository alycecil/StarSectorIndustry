package com.github.alycecil.econ.ai.colony;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.econ.ResourceDepositsCondition;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.econ.impl.ConstructionQueue;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Random;

import static com.fs.starfarer.api.impl.campaign.econ.impl.Farming.AQUA_PLANETS;
import static com.github.alycecil.econ.util.AliceIndustries.*;

public class MarketCommander implements EconomyTickListener {
    private static final int COST = 100;

    MarketAPI market;
    private volatile static Random random = new Random();
    private volatile static Logger logger = Global.getLogger(MarketCommander.class);

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
            logger.warn("Market Null");
            return;
        } else if (market.getFaction() == null
        ) {
            logger.info("Market Unfit, " + market.getName());
            return;
        } else if (market.isPlayerOwned()) {
            if (random.nextInt(10) > 1) { //rarely do for player stuff
                logger.debug("Player market sleeping " + market.getName());
                return;
            }
        }

        if (market.getConstructionQueue() == null) {
            logger.warn("Market unable to build with queue, " + market.getName());
            return;
        }

        boolean building = isBuilding();

        if (!building) {
            logger.debug("Market " + market.getName() + " looking for construction.");

            prepareConstruction();
        } else {
            logger.debug("Market " + market.getName() + " building.");
        }
    }

    protected boolean isBuilding() {
        boolean building = false;
        for (Industry industry : market.getIndustries()) {
            if (industry.isBuilding()) {
                logger.debug("Market " + market.getName() + " building a " + industry.getCurrentName() + ".");

                building = true;
                break;
            }
        }
        return building;
    }

    protected void prepareConstruction() {
        boolean build;

        ConstructionQueue constructionQueue = market.getConstructionQueue();
        if (constructionQueue == null) return;
        List<ConstructionQueue.ConstructionQueueItem> items = constructionQueue.getItems();
        if (items == null) return;
        if (items.isEmpty()) {
            logger.debug("Market " + market.getName() + " queueing new work.");
            build = buildQueue(constructionQueue);
        } else {
            logger.debug("Market " + market.getName() + " already queued work.");

            build = true;
        }

        if (build) {
            if (market.getSize() < random.nextInt(10)) {
                //todo debug
                logger.info("Market " + market.getName() + " rolled not ready.");
                return;
            }

            //todo debug
            logger.info("Market " + market.getName() + " starting building.");

            BaseIndustry.buildNextInQueue(market);
        }
    }


    protected boolean buildQueue(ConstructionQueue constructionQueue) {
        //dear lord let has industry by on a hashtable and not an O(n) list scan.
        //if not well at least only end of month sucks.

        float stability = market.getStability().getModifiedValue();

        boolean cruel = market.getFaction().isIllegal(Commodities.HAND_WEAPONS)
                && !market.getFaction().isIllegal(Commodities.ORGANS);

        if (doStability(constructionQueue, stability, cruel)) return true;

        boolean farmers = market.hasIndustry(Industries.FARMING);
        boolean waterFarmers = market.hasIndustry(Industries.AQUACULTURE);
        boolean miners = market.hasIndustry(Industries.MINING);

        int num = Misc.getNumIndustries(market);
        int max = Misc.getMaxIndustries(market);

        if (addMissingIndustriesPriority(constructionQueue, farmers, waterFarmers, miners, num, max)) return true;


        return doPicker(constructionQueue, stability, cruel, farmers, waterFarmers, miners, num, max);

    }

    private boolean hasConditions(String desiredIndustry) {
        for (MarketConditionAPI mc : market.getConditions()) {
            String commodity = ResourceDepositsCondition.COMMODITY.get(mc.getId());
            if (commodity != null) {
                String industry = ResourceDepositsCondition.INDUSTRY.get(commodity);
                if (desiredIndustry.equals(industry)) return true;
            }
        }
        return false;
    }

    private boolean doStability(ConstructionQueue constructionQueue, float stability, boolean cruel) {
        if (stability < 5) {
            if (!market.hasIndustry(PoliceState)) {
                constructionQueue.addToEnd(PoliceState, COST);
                return true;
            } else if (cruel && !market.hasIndustry(AuthoritarianRegime) && market.getSize() > 4) {
                //need a large enough population for tribalism to falter to the point where otherism starts and survival is not a primary concern.
                constructionQueue.addToEnd(AuthoritarianRegime, COST);
                return true;
            }
        }
        return false;
    }

    private boolean doPicker(ConstructionQueue constructionQueue, float stability, boolean cruel, boolean farmers, boolean waterFarmers, boolean miners, int num, int max) {
        WeightedRandomPicker<String> picker = new WeightedRandomPicker<>(random);
        boolean hasSpace = num >= max;

        switch (market.getSize()) {
            case 10:
                if (hasSpace) {
                    queueIfNotPresent(picker, PopulationWealthy, 10);

                    if (farmers) {
                        queueIfNotPresent(picker, AutomatedFarming);
                    }
                    if (cruel && stability > 6) {
                        queueIfNotPresent(picker, ChopShop);
                    }
                }
            case 9:
                queueIfNotPresent(picker, SpaceElevator);
                queueIfNotPresent(picker, GatesCustomsNavy);
                if (farmers && hasSpace) {
                    queueIfNotPresent(picker, IndustrialFarming);
                }
            case 8:
                if (market.hasIndustry(Industries.HEAVYINDUSTRY)) {
                    queueIfNotPresent(picker, IndustrialDefenseForce);
                } else {
                    queueIfNotPresent(picker, SelfDefenseForce);
                }
                queueIfNotPresent(picker, CivilianInfrastructureLuxury);
                if (farmers && hasSpace) {
                    queueIfNotPresent(picker, StateFarms);
                }
            case 7:

                queueIfNotPresent(picker, CivilianInfrastructureCommon);

                if (hasSpace) {
                    if (farmers || waterFarmers && market.hasIndustry(CivilianInfrastructureLuxury)) {
                        //has a lot of nice things already, even if its shrank one.
                        queueIfNotPresent(picker, PopulationWealthy, 5);
                    }

                    if (!farmers && !waterFarmers && market.hasIndustry(Industries.FUELPROD)) {
                        queueIfNotPresent(picker, BulkFuelProduction);
                    }
                }
            case 6:
                queueIfNotPresent(picker, TransportationInfrastructure);

                if (market.hasIndustry(Industries.ORBITALWORKS)) {
                    queueIfNotPresent(picker, IndustrialDefenseForce);
                }
            case 5:
                if (waterFarmers) {
                    queueIfNotPresent(picker, AquacultureExtensions);
                } else if (!farmers && miners && hasSpace) {
                    queueIfNotPresent(picker, TraceMining);
                }
            case 4:
                if (market.hasIndustry(Industries.HIGHCOMMAND)) {
                    if (cruel) {
                        queueIfNotPresent(picker, PoliceState);
                    }

                    queueIfNotPresent(picker, GatesCustomsNavy);

                    if (hasSpace) {
                        queueIfNotPresent(picker, Industries.FUELPROD);
                    }
                }
                if (hasSpace) {
                    if (miners && (market.hasIndustry(Industries.ORBITALWORKS) || market.hasIndustry(Industries.HEAVYINDUSTRY))) {
                        queueIfNotPresent(picker, Industries.REFINING);
                    }
                }
            default:
                if (farmers) {
                    queueIfNotPresent(picker, RuralFarmCoOp);
                }
                if (hasSpace) {
                    addMissingIndustriesPicker(picker);
                }
        }

        String result = picker.pick();
        if (result == null) {
            return false;
        }
        constructionQueue.addToEnd(result, COST);
        return true;
    }

    private void addMissingIndustriesPicker(WeightedRandomPicker<String> picker) {
        if (hasConditions(Industries.TECHMINING)) {
            queueIfNotPresent(picker, Industries.TECHMINING);
        }
    }

    private boolean addMissingIndustriesPriority(ConstructionQueue constructionQueue, boolean farmers, boolean waterFarmers, boolean miners, int num, int max) {

        if (num >= max) return false;

        if (!farmers && !waterFarmers && hasConditions(Industries.FARMING)) {
            boolean canAquaculture = market.getPlanetEntity() != null &&
                    AQUA_PLANETS.contains(market.getPlanetEntity().getTypeId());

            if (canAquaculture) {
                constructionQueue.addToEnd(Industries.AQUACULTURE, COST);
            } else {
                constructionQueue.addToEnd(Industries.FARMING, COST);
            }

            return true;

        }
        if (!miners && hasConditions(Industries.MINING)) {
            constructionQueue.addToEnd(Industries.MINING, COST);
            return true;
        }
        return false;
    }

    private void queueIfNotPresent(WeightedRandomPicker<String> picker, String id) {
        queueIfNotPresent(picker, id, 100);
    }

    private void queueIfNotPresent(WeightedRandomPicker<String> picker, String id, int weight) {
        if (!market.hasIndustry(id)) {
            logger.debug("Market " + market.getName() + " should build " + id + " next.");

            picker.add(id, weight);
        }
    }
}
