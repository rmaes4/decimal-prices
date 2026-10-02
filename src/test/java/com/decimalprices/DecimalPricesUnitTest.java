package com.decimalprices;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DecimalPricesUnitTest {

  private final static DecimalPricesInputMode B = DecimalPricesInputMode.MAX_BILLIONS;
  private final static DecimalPricesInputMode T = DecimalPricesInputMode.MAX_TRILLIONS;

  private void assertPrice(String input, DecimalPricesInputMode mode, long expected) {
    assertEquals(String.valueOf(expected), DecimalPricesUtil.transformDecimalPrice(input, mode));
  }

  @Test
  public void testBasicConversions() {
    assertPrice("1.2k", B, 1_200);
    assertPrice("1.2m", B, 1_200_000);
    assertPrice("1.2b", B, 1_200_000_000);
    assertPrice("1.3t", T, 1_300_000_000_000L);

    assertPrice("1.234567890b", B, 1_234_567_890);
    assertPrice("1.234567890t", T, 1_234_567_890_000L);

    assertPrice("1.5M", B, 1_500_000);
    assertPrice("2T", T, 2_000_000_000_000L);

    assertPrice("2.k", B, 2_000);
  }

  @Test
  public void testFloatingPointPrecision() {
    // previously failing due to floating point imprecision
    assertPrice("32m", B, 32_000_000);
    assertPrice("32.0m", B, 32_000_000);
    assertPrice("32.1m", B, 32_100_000);
    assertPrice("32.2m", B, 32_200_000);
    assertPrice("32.3m", B, 32_300_000);
    assertPrice("32.4m", B, 32_400_000);
    assertPrice("32.5m", B, 32_500_000);
    assertPrice("32.6m", B, 32_600_000);
    assertPrice("32.7m", B, 32_700_000);
    assertPrice("32.8m", B, 32_800_000);
    assertPrice("32.9m", B, 32_900_000);
    assertPrice("32.333333m", B, 32_333_333);
  }

  @Test
  public void testMaxLimitsClamping() {
    assertPrice("2.147483647b", B, 2_147_483_647);
    assertPrice("2.147483648b", B, 2_147_483_647);
    assertPrice("9b", B, 2_147_483_647);

    assertPrice("2.149631130647t", T, 2_149_631_130_647L);
    assertPrice("2.149631130648t", T, 2_149_631_130_647L);
    assertPrice("3t", T, 2_149_631_130_647L);
  }

  @Test
  public void testSmallNumbersAndZeroes() {
    assertPrice("0k", B, 0);
    assertPrice("0m", B, 0);
    assertPrice("0b", B, 0);
    assertPrice("0t", T, 0);
    assertPrice("0.000001k", B, 0);
  }
}