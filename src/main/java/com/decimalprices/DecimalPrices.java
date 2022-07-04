package com.decimalprices;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
  name = "Decimal Prices"
)
public class DecimalPrices extends Plugin {
  @Inject
  private Client client;

  @Inject
  private KeyManager keyManager;

  @Inject
  private DecimalPricesKeyListener inputListener;

  @Override
  protected void startUp() throws Exception {
    keyManager.registerKeyListener(inputListener);
    log.info("Decimal prices started!");
  }

  @Override
  protected void shutDown() throws Exception {
    keyManager.unregisterKeyListener(inputListener);
    log.info("Decimal prices stopped!");
  }

}
