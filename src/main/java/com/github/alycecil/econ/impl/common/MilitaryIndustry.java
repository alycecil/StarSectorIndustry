package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BattleAPI;
import com.fs.starfarer.api.campaign.CampaignEventListener;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.listeners.FleetEventListener;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.DebugFlags;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import com.github.alycecil.econ.model.IndustryEffect;

import java.awt.*;
import java.util.Random;

import static com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase.createPatrol;
import static com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase.getPatrolCombatFP;

public abstract class MilitaryIndustry extends SupportInfraGrowsPopulation implements RouteManager.RouteFleetSpawner, FleetEventListener, HasEffectiveness {
    public MilitaryIndustry(float perMarketSize, IndustryEffect... bonuses) {
        super(perMarketSize, bonuses);
    }

    protected float returningPatrolValue = 0f;

    @Override
    protected int getGrowthRate() {
        return 1;
    }

    @Override
    protected void applyForIndustry(float effectiveness) {
        super.applyForIndustry(effectiveness);

        MemoryAPI memory = market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, MemFlags.MARKET_PATROL, getModId(), true, -1);

        if (isFullMilitaryBase()) {
            Misc.setFlagWithReason(memory, MemFlags.MARKET_MILITARY, getModId(), true, -1);
        }

        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).modifyFlat(getModId(), getLight());
        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_MEDIUM_MOD).modifyFlat(getModId(), getMedium());
        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_HEAVY_MOD).modifyFlat(getModId(), getHeavy());
    }

    protected abstract boolean isFullMilitaryBase();

    protected abstract float getHeavy();

    protected abstract float getMedium();

    protected abstract float getLight();

    @Override
    protected void unapplyForIndustry() {
        super.unapplyForIndustry();

        MemoryAPI memory = market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, MemFlags.MARKET_PATROL, getModId(), false, -1);
        Misc.setFlagWithReason(memory, MemFlags.MARKET_MILITARY, getModId(), false, -1);

        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).unmodifyFlat(getModId());
        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_MEDIUM_MOD).unmodifyFlat(getModId());
        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_HEAVY_MOD).unmodifyFlat(getModId());
    }

    public boolean shouldCancelRouteAfterDelayCheck(RouteManager.RouteData route) {
        return false;
    }


    public void reportBattleOccurred(CampaignFleetAPI fleet, CampaignFleetAPI primaryWinner, BattleAPI battle) {

    }

    public void reportFleetDespawnedToListener(CampaignFleetAPI fleet, CampaignEventListener.FleetDespawnReason reason, Object param) {
        if (!isFunctional()) return;

        if (reason == CampaignEventListener.FleetDespawnReason.REACHED_DESTINATION) {
            RouteManager.RouteData route = RouteManager.getInstance().getRoute(getRouteSourceId(), fleet);
            if (route.getCustom() instanceof MilitaryBase.PatrolFleetData) {
                MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) route.getCustom();
                if (custom.spawnFP > 0) {
                    float fraction = fleet.getFleetPoints() / custom.spawnFP;
                    returningPatrolValue += fraction;
                }
            }
        }
    }

    public String getRouteSourceId() {
        return getMarket().getId() + "_" + "military";
    }

    public void reportAboutToBeDespawnedByRouteManager(RouteManager.RouteData route) {
    }

    public boolean shouldRepeat(RouteManager.RouteData route) {
        return false;
    }

    public CampaignFleetAPI spawnFleet(RouteManager.RouteData route) {

        MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) route.getCustom();
        FleetFactory.PatrolType type = custom.type;

        Random random = route.getRandom();

        CampaignFleetAPI fleet = createPatrol(type, market.getFactionId(), route, market, null, random);

        if (fleet == null || fleet.isEmpty()) return null;

        fleet.addEventListener(this);

        market.getContainingLocation().addEntity(fleet);
        fleet.setFacing((float) Math.random() * 360f);
        // this will get overridden by the patrol assignment AI, depending on route-time elapsed etc
        fleet.setLocation(market.getPrimaryEntity().getLocation().x, market.getPrimaryEntity().getLocation().y);

        fleet.addScript(getScript(route, fleet));

        fleet.getMemoryWithoutUpdate().set(MemFlags.FLEET_IGNORES_OTHER_FLEETS, true, 0.3f);

        //market.getContainingLocation().addEntity(fleet);
        //fleet.setLocation(market.getPrimaryEntity().getLocation().x, market.getPrimaryEntity().getLocation().y);

        if (custom.spawnFP <= 0) {
            custom.spawnFP = fleet.getFleetPoints();
        }

        return fleet;
    }

    protected abstract EveryFrameScript getScript(RouteManager.RouteData route, CampaignFleetAPI fleet);


    protected IntervalUtil tracker = new IntervalUtil(Global.getSettings().getFloat("averagePatrolSpawnInterval") * 0.7f,
            Global.getSettings().getFloat("averagePatrolSpawnInterval") * 1.3f);

    @Override
    protected void buildingFinished() {
        super.buildingFinished();

        tracker.forceIntervalElapsed();
    }

    @Override
    protected void upgradeFinished(Industry previous) {
        super.upgradeFinished(previous);

        tracker.forceIntervalElapsed();
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);

        if (Global.getSector().getEconomy().isSimMode()) return;

        if (!isFunctional()) return;

        float days = Global.getSector().getClock().convertToDays(amount);

//		float stability = market.getPrevStability();
//		float spawnRate = 1f + (stability - 5) * 0.2f;
//		if (spawnRate < 0.5f) spawnRate = 0.5f;

        float spawnRate = 1f;
        float rateMult = market.getStats().getDynamic().getStat(Stats.COMBAT_FLEET_SPAWN_RATE_MULT).getModifiedValue();
        spawnRate *= rateMult;

        if (Global.getSector().isInNewGameAdvance()) {
            spawnRate *= 3f;
        }

        float extraTime = 0f;
        if (returningPatrolValue > 0) {
            // apply "returned patrols" to spawn rate, at a maximum rate of 1 interval per day
            float interval = tracker.getIntervalDuration();
            extraTime = interval * days;
            returningPatrolValue -= days;
            if (returningPatrolValue < 0) returningPatrolValue = 0;
        }
        tracker.advance(days * spawnRate + extraTime);

        //DebugFlags.FAST_PATROL_SPAWN = true;
        if (DebugFlags.FAST_PATROL_SPAWN) {
            tracker.advance(days * spawnRate * 100f);
        }

        if (tracker.intervalElapsed()) {
            advanceFleetSpawn();
        }
    }

    protected void advanceFleetSpawn() {
        String sid = getRouteSourceId();

        int light = getCount(FleetFactory.PatrolType.FAST);
        int medium = getCount(FleetFactory.PatrolType.COMBAT);
        int heavy = getCount(FleetFactory.PatrolType.HEAVY);

        int maxLight = getMaxPatrols(FleetFactory.PatrolType.FAST);
        int maxMedium = getMaxPatrols(FleetFactory.PatrolType.COMBAT);
        int maxHeavy = getMaxPatrols(FleetFactory.PatrolType.HEAVY);

        WeightedRandomPicker<FleetFactory.PatrolType> picker = new WeightedRandomPicker<FleetFactory.PatrolType>();
        picker.add(FleetFactory.PatrolType.HEAVY, maxHeavy - heavy);
        picker.add(FleetFactory.PatrolType.COMBAT, maxMedium - medium);
        picker.add(FleetFactory.PatrolType.FAST, maxLight - light);

        if (picker.isEmpty()) return;

        RouteManager.RouteData route = createRouteStub(sid, picker);
        createRouteSegments(route);
    }

    protected void createRouteSegments(RouteManager.RouteData route) {
        float patrolDays = 35f + (float) Math.random() * 10f;
        route.addSegment(new RouteManager.RouteSegment(patrolDays, market.getPrimaryEntity()));
    }

    protected RouteManager.RouteData createRouteStub(String sid, WeightedRandomPicker<FleetFactory.PatrolType> picker) {
        FleetFactory.PatrolType type = picker.pick();
        MilitaryBase.PatrolFleetData custom = new MilitaryBase.PatrolFleetData(type);

        RouteManager.OptionalFleetData extra = new RouteManager.OptionalFleetData(market);
        extra.fleetType = type.getFleetType();

        RouteManager.RouteData route = RouteManager.getInstance().addRoute(sid, market, Misc.genRandomSeed(), extra, this, custom);
        extra.strength = (float) getPatrolCombatFP(type, route.getRandom());
        extra.strength = Misc.getAdjustedStrength(extra.strength, market);
        return route;
    }

    public int getCount(FleetFactory.PatrolType... types) {
        int count = 0;
        for (RouteManager.RouteData data : RouteManager.getInstance().getRoutesForSource(getRouteSourceId())) {
            if (data.getCustom() instanceof MilitaryBase.PatrolFleetData) {
                MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) data.getCustom();
                for (FleetFactory.PatrolType type : types) {
                    if (type == custom.type) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }

    public int getMaxPatrols(FleetFactory.PatrolType type) {
        if (type == FleetFactory.PatrolType.FAST) {
            return (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).computeEffective(0);
        }
        if (type == FleetFactory.PatrolType.COMBAT) {
            return (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_MEDIUM_MOD).computeEffective(0);
        }
        if (type == FleetFactory.PatrolType.HEAVY) {
            return (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_HEAVY_MOD).computeEffective(0);
        }
        return 0;
    }

    @Override
    protected void addPostSupplySection(TooltipMakerAPI tooltip, boolean hasSupply, IndustryTooltipMode mode) {
        super.addPostSupplySection(tooltip, hasSupply, mode);


        int maxLight = getMaxPatrols(FleetFactory.PatrolType.FAST);
        int maxMedium = getMaxPatrols(FleetFactory.PatrolType.COMBAT);
        int maxHeavy = getMaxPatrols(FleetFactory.PatrolType.HEAVY);

        tooltip.addPara("This military installation provides %s of %s total light, %s of %s total medium and %s of %s total heavy fleets primarily for the purpose of %s.", 10f, Misc.getHighlightColor(),
                String.valueOf(getLight()),  String.valueOf(maxLight),
                String.valueOf(getMedium()), String.valueOf(maxMedium),
                String.valueOf(getHeavy()),  String.valueOf(maxHeavy),
                getDescription()
        );

        final String note = "Note that these ships fall under the Chain of Command of the existing military structure, utilizes shared Command, Control and Communication and integrates logistics and support fleets. As such they may be ordered for primary and related ops.";
        tooltip.addPara(note, Color.darkGray, 10f);
    }
}
