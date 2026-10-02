package com.decimalprices;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
public enum DecimalPricesInputMode {
  MAX_BILLIONS(7, BigDecimal.valueOf(2_147_483_647), Pattern.compile("[0-9]+\\.[0-9]*[kmb]")), MAX_TRILLIONS(30, BigDecimal.valueOf(2_149_631_130_647L), Pattern.compile("[0-9]+\\.[0-9]*[kmbt]"));

  private static final Map<Integer, DecimalPricesInputMode> BY_ID = new HashMap<>();

  static {
    for (DecimalPricesInputMode mode : values()) {
      BY_ID.put(mode.id, mode);
    }
  }

  private final int id;
  private final BigDecimal maxPrice;
  private final Pattern pattern;

  DecimalPricesInputMode(int id, BigDecimal maxPrice, Pattern pattern) {
    this.id = id;
    this.maxPrice = maxPrice;
    this.pattern = pattern;
  }

  public static DecimalPricesInputMode fromId(int id) {
    return BY_ID.get(id);
  }

}
