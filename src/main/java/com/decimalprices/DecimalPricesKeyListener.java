package com.decimalprices;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarClientInt;
import net.runelite.api.VarClientStr;
import net.runelite.client.input.KeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

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
    // convert the decimal input to an equivalent integer
    String transformedPrice = DecimalPricesUtil.transformDecimalPrice(lowerInputText);
    // set the newly converted integer before it is sent to the server
    client.setVarcStrValue(VarClientStr.INPUT_TEXT, transformedPrice);
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
