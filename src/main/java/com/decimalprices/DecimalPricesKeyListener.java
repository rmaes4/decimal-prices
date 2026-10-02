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

  @Inject
  private Client client;
  @Inject
  private ClientThread clientThread;

  private boolean isTargetInput() {
    final int inputType = client.getVarcIntValue(VarClientID.MESLAYERMODE);
    return DecimalPricesInputMode.fromId(inputType) != null;
  }

  private void convertQuantity() {
    final int inputType = client.getVarcIntValue(VarClientID.MESLAYERMODE);
    final DecimalPricesInputMode inputMode = DecimalPricesInputMode.fromId(inputType);
    if (inputMode == null) {
      return;
    }
    final String rawInputText = client.getVarcStrValue(VarClientID.MESLAYERINPUT);
    if (rawInputText == null || rawInputText.isEmpty()) {
      return;
    }
    // convert to lowercase for validation
    final String lowerInputText = rawInputText.toLowerCase();
    // ensure input matches exactly (any amount of numbers)period(any amount of numbers)[one of only k, m, b or t]
    if (!inputMode.getPattern().matcher(lowerInputText).matches()) {
      return;
    }
    // convert the decimal input to an equivalent integer
    String transformedPrice = DecimalPricesUtil.transformDecimalPrice(lowerInputText, inputMode);
    // set the newly converted integer before it is sent to the server
    clientThread.invoke(() -> client.setVarcStrValue(VarClientID.MESLAYERINPUT, transformedPrice));
  }

  private void addDecimalToInputText() {
    // take current input text and append a period (decimal)
    final String currentInputText = client.getVarcStrValue(VarClientID.MESLAYERINPUT);
    if (currentInputText.isEmpty()) {
      return;
    }
    // prevent adding more than one decimal
    if (currentInputText.contains(".")) {
      return;
    }
    String newInputText = currentInputText + ".";
    clientThread.invoke(() -> client.setVarcStrValue(VarClientID.MESLAYERINPUT, newInputText));
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER && isTargetInput()) {
      // intercept quantity entry before it is sent to the server
      convertQuantity();
    } else if ((e.getKeyCode() == KeyEvent.VK_PERIOD || e.getKeyCode() == KeyEvent.VK_DECIMAL) && isTargetInput()) {
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
