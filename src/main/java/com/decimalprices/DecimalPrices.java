package com.decimalprices;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.VarClientIntChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Decimal Prices"
)
public class DecimalPrices extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private KeyManager keyManager;

	@Inject
	private DecimalPricesKeyListener inputListener;

	@Override
	protected void startUp() throws Exception
	{
		keyManager.registerKeyListener(inputListener);
		log.info("Decimal prices started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		keyManager.unregisterKeyListener(inputListener);
		log.info("Decimal prices stopped!");
	}

}
