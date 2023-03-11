package com.decimalprices;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DecimalPricesUnitTest {

  @Test
  public void testConversion() {
    // generic tests
    assertEquals(DecimalPricesUtil.transformDecimalPrice("1.2k"), String.valueOf(1_200));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("1.2m"), String.valueOf(1_200_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("1.2b"), String.valueOf(1_200_000_000));

    assertEquals(DecimalPricesUtil.transformDecimalPrice("32m"), String.valueOf(32_000_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.0m"), String.valueOf(32_000_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.1m"), String.valueOf(32_100_000));
    // previously failing due to floating point imprecision
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.2m"), String.valueOf(32_200_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.3m"), String.valueOf(32_300_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.4m"), String.valueOf(32_400_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.5m"), String.valueOf(32_500_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.6m"), String.valueOf(32_600_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.7m"), String.valueOf(32_700_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.8m"), String.valueOf(32_800_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.9m"), String.valueOf(32_900_000));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("32.333333m"), String.valueOf(32_333_333));
    // testing max num
    assertEquals(DecimalPricesUtil.transformDecimalPrice("2.147483647b"), String.valueOf(2_147_483_647));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("2.147483648b"), String.valueOf(2_147_483_647));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("9b"), String.valueOf(2_147_483_647));
    // testing small numbers
    assertEquals(DecimalPricesUtil.transformDecimalPrice("0k"), String.valueOf(0));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("0m"), String.valueOf(0));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("0b"), String.valueOf(0));
    assertEquals(DecimalPricesUtil.transformDecimalPrice("0.000001k"), String.valueOf(0));

    assertEquals(DecimalPricesUtil.transformDecimalPrice("1.234567890b"), String.valueOf(1_234_567_890));
  }

}
