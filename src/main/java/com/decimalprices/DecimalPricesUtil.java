package com.decimalprices;

import java.math.BigDecimal;

public class DecimalPricesUtil {

  private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1_000);
  private static final BigDecimal ONE_MILLION = BigDecimal.valueOf(1_000_000);
  private static final BigDecimal ONE_BILLION = BigDecimal.valueOf(1_000_000_000);
  private static final BigDecimal ONE_TRILLION = BigDecimal.valueOf(1_000_000_000_000L);

  private DecimalPricesUtil() {
  }

  public static String transformDecimalPrice(String decimalPrice, DecimalPricesInputMode inputMode) {
    int priceStringLen = decimalPrice.length();
    // get the unit from the end of string, k (thousands), m (millions), b (billions) or t (trillions)
    char unit = decimalPrice.charAt(priceStringLen - 1);
    // get the number xx.xx without the unit and parse as a BigDecimal (for precision)
    BigDecimal amount = new BigDecimal(decimalPrice.substring(0, priceStringLen - 1));
    // multiply the number and the unit
    BigDecimal product = calculateProduct(unit, amount);
    // bound result to maximum allowable price
    if (inputMode != null && product.compareTo(inputMode.getMaxPrice()) > 0) {
      product = inputMode.getMaxPrice();
    }
    return product.toBigInteger().toString();
  }

  private static BigDecimal calculateProduct(char unit, BigDecimal amount) {
    BigDecimal product;
    switch (unit) {
      case 'k':
      case 'K':
        product = amount.multiply(ONE_THOUSAND);
        break;
      case 'm':
      case 'M':
        product = amount.multiply(ONE_MILLION);
        break;
      case 'b':
      case 'B':
        product = amount.multiply(ONE_BILLION);
        break;
      case 't':
      case 'T':
        product = amount.multiply(ONE_TRILLION);
        break;
      default:
        product = BigDecimal.ZERO;
        break;
    }
    return product;
  }

}
