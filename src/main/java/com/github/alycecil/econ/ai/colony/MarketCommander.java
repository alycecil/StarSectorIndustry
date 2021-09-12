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
import com.fs.starfarer.api.loading.IndustrySpecAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import com.github.alycecil.econ.util.IndEvo_ids;
import com.github.alycecil.econ.util.Mods;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Random;

import static com.fs.starfarer.api.impl.campaign.econ.impl.Farming.AQUA_PLANETS;
import static com.github.alycecil.econ.util.AliceIndustries.*;

public class MarketCommander implements EconomyTickListener {

    MarketAPI market;
    private volatile static Random random = new Random();
    private volatile static Logger logger = Global.getLogger(MarketCommander.class);

    public MarketCommander(MarketAPI market) {
        this.market = market;
    }

    @Override
    public void reportEconomyTick(int iterIndex) {
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
            if (random.nextInt(10) > 5) { //rarely do for player stuff
                logger.debug("Player market sleeping " + market.getName());
                return;
            }
        } else if (market.getSize() < random.nextInt(Mods.isIndustrialEvo() ? 20 : 50)) {
            //todo debug
            logger.debug("Market " + market.getName() + " rolled not ready.");
            return;
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
            boolean blackListed = Industries.POPULATION.equals(industry.getId()) || PopulationWealthy.equals(industry.getId());
            if (!blackListed && industry.isBuilding()) {
                logger.info("Market " + market.getName() + " building a " + industry.getCurrentName() + ".");

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
                && !market.getFaction().isIllegal(Commodities.ORGANS)
                && !market.isPlayerOwned();

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
                constructionQueue.addToEnd(PoliceState, (int) Global.getSettings().getIndustrySpec(PoliceState).getCost());
                return true;
            } else if (cruel && !market.hasIndustry(AuthoritarianRegime) && market.getSize() > 4) {
                //need a large enough population for tribalism to falter to the point where otherism starts and survival is not a primary concern.
                constructionQueue.addToEnd(AuthoritarianRegime, (int) Global.getSettings().getIndustrySpec(AuthoritarianRegime).getCost());
                return true;
            }
        }
        return false;
    }

    private boolean doPicker(ConstructionQueue constructionQueue, float stability, boolean cruel, boolean farmers, boolean waterFarmers, boolean miners, int num, int max) {
        WeightedRandomPicker<String> picker = new WeightedRandomPicker<>(random);
        boolean hasSpace = num < max;
        boolean notMilitary = !market.hasIndustry(Industries.HIGHCOMMAND) &&
                !market.hasIndustry(Industries.PATROLHQ) &&
                !market.hasIndustry(Industries.MILITARYBASE);

        boolean heavyIndustry = market.hasIndustry(Industries.ORBITALWORKS) || market.hasIndustry(Industries.HEAVYINDUSTRY);

        switch (market.getSize()) {
            case 9:
                if (hasSpace) {
                    queueIfNotPresent(picker, PopulationWealthy, 10);

                    if (farmers) {
                        queueIfNotPresent(picker, AutomatedFarming);
                    }
                    if (cruel && stability > 6) {
                        queueIfNotPresent(picker, ChopShop);
                    }
                }
            case 8:
                queueIfNotPresent(picker, SpaceElevator);
                queueIfNotPresent(picker, GatesCustomsNavy);
                if (farmers && hasSpace) {
                    queueIfNotPresent(picker, IndustrialFarming);
                }
            case 7:
                if (market.hasIndustry(Industries.HEAVYINDUSTRY)) {
                    queueIfNotPresent(picker, IndustrialDefenseForce);
                } else {
                    queueIfNotPresent(picker, SelfDefenseForce);
                }
                queueIfNotPresent(picker, CivilianInfrastructureLuxury);
                if (farmers && hasSpace) {
                    queueIfNotPresent(picker, StateFarms);
                }
            case 6:

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
            case 5:
                queueIfNotPresent(picker, TransportationInfrastructure);

                if (market.hasIndustry(Industries.ORBITALWORKS)) {
                    queueIfNotPresent(picker, IndustrialDefenseForce);
                }
            case 4:
                if (waterFarmers) {
                    queueIfNotPresent(picker, AquacultureExtensions);
                } else if (!farmers && miners && hasSpace) {
                    queueIfNotPresent(picker, TraceMining);
                }
            case 3:
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
                    if (miners && heavyIndustry) {
                        queueIfNotPresent(picker, Industries.REFINING);
                    }
                }
            default:
                allColoniesPicker(picker, farmers, cruel, hasSpace, notMilitary, heavyIndustry);
        }

        String result = picker.pick();
        return addToQueue(constructionQueue, result);
    }

    //RECURSIVE
    private boolean addToQueue(ConstructionQueue constructionQueue, String result) {
        if (result == null) {
            return false;
        }
        IndustrySpecAPI industrySpec = Global.getSettings().getIndustrySpec(result);
        if (industrySpec == null) {
            return false;
        }

        String downgrade = industrySpec.getDowngrade();
        if (downgrade != null && !market.hasIndustry(downgrade)) {
            if (!addToQueue(constructionQueue, downgrade)) {
                return false;
            }
        }

        int cost = (int) industrySpec.getCost();
        if (market.isPlayerOwned()) {
            cost /= 100f;
        }

        constructionQueue.addToEnd(result, cost);
        return true;
    }

    private void allColoniesPicker(WeightedRandomPicker<String> picker, boolean farmers, boolean cruel, boolean hasSpace, boolean notMilitary, boolean heavyIndustry) {
        if (farmers) {
            queueIfNotPresent(picker, RuralFarmCoOp);
        } else {
            queueIfNotPresent(picker, FarmingSimple);
        }
        if (hasSpace) {
            addMissingIndustriesPicker(picker);
        }

        if (notMilitary
        ) {
            queueIfNotPresent(picker, EmergencyDefenseForce);
        } else {
            if (!heavyIndustry) {
                queueIfNotPresent(picker, PopulationWealthy, 1);
            }
            queueIfNotPresent(picker, Industries.PATROLHQ, 10);
            if(hasSpace) {
                queueIfNotPresent(picker, Industries.MILITARYBASE, 10);
                queueIfNotPresent(picker, Industries.HIGHCOMMAND, 5);
            }
        }
        if (hasSpace) {
            //vanilla
            if (!heavyIndustry) {
                queueIfNotPresent(picker, Industries.HEAVYINDUSTRY, 10);
            }

            if (Mods.isIndustrialEvo()) {
                if (cruel) {
                    queueIfNotPresent(picker, IndEvo_ids.PIRATEHAVEN);
                }
                queueIfNotPresent(picker, IndEvo_ids.REQCENTER, 1);
                queueIfNotPresent(picker, IndEvo_ids.SENATE, 4);
                queueIfNotPresent(picker, IndEvo_ids.PORT, 1);
                queueIfNotPresent(picker, IndEvo_ids.SUPCOM, 1);

                if (!market.isPlayerOwned()) {
                    queueIfNotPresent(picker, IndEvo_ids.EMBASSY, 4);
                }

                if (heavyIndustry) {
                    queueIfNotPresent(picker, IndEvo_ids.COMFORGE, 2);
                    queueIfNotPresent(picker, IndEvo_ids.ADINFRA, 2);
                    queueIfNotPresent(picker, IndEvo_ids.ADASSEM, 2);
                    queueIfNotPresent(picker, IndEvo_ids.ADMANUF, 2);
                    queueIfNotPresent(picker, IndEvo_ids.HULLFORGE, 1);
                    queueIfNotPresent(picker, IndEvo_ids.REPAIRDOCKS, 1);
                    queueIfNotPresent(picker, IndEvo_ids.DECONSTRUCTOR, 1);
                }

                if (notMilitary) {
                    queueIfNotPresent(picker, IndEvo_ids.SCRAPYARD, 10);
                    queueIfNotPresent(picker, IndEvo_ids.ENGHUB, 2);
                    queueIfNotPresent(picker, IndEvo_ids.REQCENTER, 2);
                } else {
                    queueIfNotPresent(picker, IndEvo_ids.ACADEMY, 50);
                    queueIfNotPresent(picker, IndEvo_ids.COMARRAY, 10);
                    queueIfNotPresent(picker, IndEvo_ids.INTARRAY, 1);
                }
            }
        }
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
                constructionQueue.addToEnd(Industries.AQUACULTURE,
                        (int) Global.getSettings().getIndustrySpec(Industries.AQUACULTURE).getCost()
                );
            } else {
                constructionQueue.addToEnd(Industries.FARMING, (int) Global.getSettings().getIndustrySpec(Industries.FARMING).getCost());
            }

            return true;

        }
        if (!miners && hasConditions(Industries.MINING)) {
            constructionQueue.addToEnd(
                    Industries.MINING,
                    (int) Global.getSettings().getIndustrySpec(Industries.MINING).getCost()
            );
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
