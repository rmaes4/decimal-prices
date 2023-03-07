package com.decimalprices;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarClientInt;
import net.runelite.api.VarClientStr;
import net.runelite.client.input.KeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.lang.Math;

@Slf4j
class DecimalPricesKeyListener implements KeyListener {

  @Inject
  private Client client;

  private boolean isQuantityInput() {
        /*
        Determine user is typing into a quantity input field.
        Known types:
        2 Add friend input
        3 Delete friend input
        6 Send private message input
        7 Enter a quantity input (ge, bank, trade, coffer etc.)
         */
    return client.getVarcIntValue(VarClientInt.INPUT_TYPE) == 7;
  }

  private void convertQuantity() {
    final String rawInputText = client.getVarcStrValue(VarClientStr.INPUT_TEXT);
    // convert to lowercase for validation
    final String lowerInputText = rawInputText.toLowerCase();
    // ensure input matches exactly (any amount of numbers)period(any amount of numbers)[one of only k, m or b]
    if (!lowerInputText.matches("[0-9]+\\.[0-9]+[kmb]")) {
      return;
    }
    // get the unit from the end of string, k (thousands), m (millions) or b (billions)
    char unit = lowerInputText.charAt(lowerInputText.length() - 1);
    // get the number xx.xx without the unit and parse as a double
    double amount = Double.parseDouble(lowerInputText.substring(0, lowerInputText.length() - 1));
    // multiply the number and the unit
    double product;
    switch (unit) {
      case 'k':
        product = amount * 1000;
        break;
      case 'm':
        product = amount * 1000000;
        break;
      case 'b':
        product = amount * 1000000000;
        break;
      default:
        product = 0;
        break;
    }
    // cast the double to an int, truncating anything after the decimal in the process
    int truncProduct = (int) Math.ceil(product);
    // set the newly converted integer before it is sent to the server
    client.setVarcStrValue(VarClientStr.INPUT_TEXT, String.valueOf(truncProduct));
  }

  private void addDecimalToInputText() {
    // take current input text and append a period (decimal)
    final String currentInputText = client.getVarcStrValue(VarClientStr.INPUT_TEXT);
    if (currentInputText.equals("")) {
      return;
    }
    // prevent adding more than one decimal
    if (currentInputText.contains(".")) {
      return;
    }
    String newInputText = currentInputText + ".";
    client.setVarcStrValue(VarClientStr.INPUT_TEXT, newInputText);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER && isQuantityInput()) {
      // intercept quantity entry before it is sent to the server
      convertQuantity();
    } else if ((e.getKeyCode() == KeyEvent.VK_PERIOD || e.getKeyCode() == KeyEvent.VK_DECIMAL) && isQuantityInput()) {
      // allow typing of decimal in quantity input field which is otherwise not possible to do
      addDecimalToInputText();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  @Override
  public void keyTyped(KeyEvent e) {

  }
}
