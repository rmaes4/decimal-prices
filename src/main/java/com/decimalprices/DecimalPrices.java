package com.decimalprices;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(name = "Decimal Prices")
public class DecimalPrices extends Plugin {

  @Inject
  private KeyManager keyManager;

  @Inject
  private DecimalPricesKeyListener inputListener;

  @Override
  protected void startUp() {
    keyManager.registerKeyListener(inputListener);
    log.info("Decimal prices started!");
  }

  @Override
  protected void shutDown() {
    keyManager.unregisterKeyListener(inputListener);
    log.info("Decimal prices stopped!");
  }

}
