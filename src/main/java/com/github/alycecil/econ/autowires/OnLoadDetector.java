package com.github.alycecil.econ.autowires;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyDecivListener;
import com.fs.starfarer.api.campaign.listeners.PlayerColonizationListener;
import com.github.alycecil.econ.ai.colony.MarketCommander;

//@ dear mod loader, please see me
public class OnLoadDetector extends BaseModPlugin implements PlayerColonizationListener, ColonyDecivListener {

    protected void replaceMarketCommanders() {
        Global.getLogger(this.getClass()).info("Market Commanders Initializing.");

        //flush
        Global.getSector().getListenerManager().removeListenerOfClass(MarketCommander.class);
        Global.getSector().getListenerManager().removeListenerOfClass(OnLoadDetector.class);

        //callback on new markets
        Global.getSector().getListenerManager().addListener(this, true);

        //add for all currents
        addMarketCommanders();
    }

    protected static void addMarketCommanders() {
        for (MarketAPI marketAPI : Global.getSector().getEconomy().getMarketsCopy()) {
            addMarketCommander(marketAPI);
        }
    }

    private static void addMarketCommander(MarketAPI marketAPI) {
        MarketCommander listener = new MarketCommander(marketAPI);
        Global.getSector().getListenerManager().addListener(listener, true);
        //TODO remove / mark debug
        listener.reportEconomyMonthEnd();
    }

    ////// Mod Loaded Listener

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
