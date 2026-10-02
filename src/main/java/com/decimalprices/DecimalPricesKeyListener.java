package com.decimalprices;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.gameval.VarClientID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.input.KeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

@Slf4j
class DecimalPricesKeyListener implements KeyListener {

  private static final int INPUT_TYPE = VarClientID.MESLAYERMODE;
  private static final int INPUT_TEXT = VarClientID.MESLAYERINPUT;
  @Inject
  private Client client;
  @Inject
  private ClientThread clientThread;

  private boolean isQuantityInput() {
        /*
        Determine user is typing into a quantity input field.
        Known types:
        2  Add friend input
        3  Delete friend input
        6  Send private message input
        7  Enter a quantity input (ge, bank, trade, coffer etc.)
        30 Enter a price input
         */
    final int inputType = client.getVarcIntValue(INPUT_TYPE);
    return inputType == 7 || inputType == 30;
  }

  private void convertQuantity() {
    final String rawInputText = client.getVarcStrValue(INPUT_TEXT);
    // convert to lowercase for validation
    final String lowerInputText = rawInputText.toLowerCase();
    // ensure input matches exactly (any amount of numbers)period(any amount of numbers)[one of only k, m or b]
    if (!lowerInputText.matches("[0-9]+\\.[0-9]+[kmb]")) {
      return;
    }
    // convert the decimal input to an equivalent integer
    String transformedPrice = DecimalPricesUtil.transformDecimalPrice(lowerInputText);
    // set the newly converted integer before it is sent to the server
    clientThread.invoke(() -> client.setVarcStrValue(INPUT_TEXT, transformedPrice));
  }

  private void addDecimalToInputText() {
    // take current input text and append a period (decimal)
    final String currentInputText = client.getVarcStrValue(INPUT_TEXT);
    if (currentInputText.isEmpty()) {
      return;
    }
    // prevent adding more than one decimal
    if (currentInputText.contains(".")) {
      return;
    }
    String newInputText = currentInputText + ".";
    clientThread.invoke(() -> client.setVarcStrValue(INPUT_TEXT, newInputText));
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
