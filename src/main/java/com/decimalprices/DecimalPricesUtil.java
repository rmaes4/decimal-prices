package com.decimalprices;

import java.math.BigDecimal;

public class DecimalPricesUtil {

  private static final BigDecimal ONE_THOUSAND = new BigDecimal(1_000);
  private static final BigDecimal ONE_MILLION = new BigDecimal(1_000_000);
  private static final BigDecimal ONE_BILLION = new BigDecimal(1_000_000_000);
  private static final BigDecimal MAX = new BigDecimal(2_147_483_647);

  public static String transformDecimalPrice(String decimalPrice) {
    int priceStringLen = decimalPrice.length();
    // get the unit from the end of string, k (thousands), m (millions) or b (billions)
    char unit = decimalPrice.charAt(priceStringLen - 1);
    // get the number xx.xx without the unit and parse as a BigDecimal (for precision)
    BigDecimal amount = new BigDecimal(decimalPrice.substring(0, priceStringLen - 1));
    // multiply the number and the unit
    BigDecimal product;
    switch (unit) {
      case 'k':
        product = amount.multiply(ONE_THOUSAND);
        break;
      case 'm':
        product = amount.multiply(ONE_MILLION);
        break;
      case 'b':
        product = amount.multiply(ONE_BILLION);
        break;
      default:
        product = BigDecimal.ZERO;
        break;
    }
    // bound result to maximum allowable price
    if (product.compareTo(MAX) > 0) {
      product = MAX;
    }
    // cast the BigDecimal to an int, truncating anything after the decimal in the process
    int truncatedProduct = product.intValue();
    return String.valueOf(truncatedProduct);
  }

}
