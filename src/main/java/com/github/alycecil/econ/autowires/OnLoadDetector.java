package com.github.alycecil.econ.autowires;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyDecivListener;
import com.fs.starfarer.api.campaign.listeners.ListenerManagerAPI;
import com.fs.starfarer.api.campaign.listeners.PlayerColonizationListener;
import com.github.alycecil.econ.ai.colony.MarketCommander;
import com.github.alycecil.econ.util.Mods;

import java.util.LinkedList;
import java.util.List;

//@ dear mod loader, please see me
public class OnLoadDetector extends BaseModPlugin implements PlayerColonizationListener, ColonyDecivListener {
    static final List<MarketCommander> commanders = new LinkedList<>();

    protected void replaceMarketCommanders() {
        synchronized (commanders) {
            Global.getLogger(this.getClass()).info("Market Commanders Initializing.");

            //flush
            ListenerManagerAPI listenerManager = Global.getSector().getListenerManager();
            for (MarketCommander commander : commanders) {
                listenerManager.removeListener(commander);
            }
            commanders.clear();
            listenerManager.removeListenerOfClass(MarketCommander.class);

            listenerManager.removeListener(this);
            listenerManager.removeListenerOfClass(OnLoadDetector.class);

            //callback on new markets
            listenerManager.addListener(this, true);

        }
            //add for all currents
            addMarketCommanders();

    }

    protected static void addMarketCommanders() {
        for (MarketAPI marketAPI : Global.getSector().getEconomy().getMarketsCopy()) {
            addMarketCommander(marketAPI);
        }
    }

    private static void addMarketCommander(MarketAPI marketAPI) {
        synchronized (commanders) {
            MarketCommander listener = new MarketCommander(marketAPI);
            Global.getSector().getListenerManager().addListener(listener, true);
            commanders.add(listener);

            //TODO remove / check if mark debug
            listener.reportEconomyMonthEnd();
        }
    }



    ////// Mod Loaded Listener


    @Override
    public void onApplicationLoad() throws Exception {
        Mods.indEvo = Global.getSettings().getModManager().isModEnabled("IndEvo");
        Mods.nexelin = Global.getSettings().getModManager().isModEnabled("nexerelin");

        Global.getLogger(this.getClass()).info("IndEvo detected : "+Mods.isIndustrialEvo());
        Global.getLogger(this.getClass()).info("Nexerelin detected: "+Mods.isNexelin());
        super.onApplicationLoad();
    }

    @Override
    public void onGameLoad(boolean newGame) {
        super.onGameLoad(newGame);
        Global.getLogger(this.getClass()).info("Game Loaded");
        replaceMarketCommanders();
    }

    @Override
    public void onNewGameAfterEconomyLoad() {
        Global.getLogger(this.getClass()).info("New Game Started");
        replaceMarketCommanders();
    }

    ////// Game Event Listeners

    @Override
    public void reportPlayerColonizedPlanet(PlanetAPI planet) {
        if (planet == null || planet.getMarket() == null) return;

        Global.getLogger(this.getClass()).info("New Colony "+ planet.getFullName());
        addMarketCommander(planet.getMarket());
    }

    @Override
    public void reportColonyDecivilized(MarketAPI market, boolean fullyDestroyed) {
        Global.getLogger(this.getClass()).info("Lost Colony "+ market.getName());
        replaceMarketCommanders();
    }

    @Override
    public void reportPlayerAbandonedColony(MarketAPI colony) {
        //noop
    }

    @Override
    public void reportColonyAboutToBeDecivilized(MarketAPI market, boolean fullyDestroyed) {
        //noop
    }
}
