package com.decimalprices;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DecimalPricesTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DecimalPrices.class);
		RuneLite.main(args);
	}
}