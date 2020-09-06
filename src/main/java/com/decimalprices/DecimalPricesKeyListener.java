package com.decimalprices;

import net.runelite.api.Client;
import net.runelite.api.VarClientStr;
import net.runelite.client.input.KeyListener;

import java.awt.event.KeyEvent;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
class DecimalPricesKeyListener implements KeyListener {

    @Inject
    private Client client;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            final String inputText = client.getVar(VarClientStr.INPUT_TEXT);
            if(inputText.equals("")) {
                return;
            }
            if(!inputText.contains(".")) {
                return;
            }
            if(!(inputText.contains("k") || inputText.contains("m") || inputText.contains("b"))){
                return;
            }
            String inputTextParts[] = inputText.split("\\.");
            int beforeDec = Integer.parseInt(inputTextParts[0]);
            int afterDec;
            char unit;
            if (inputTextParts[1].contains("k")) {
                String split[] = inputTextParts[1].split("k");
                afterDec = Integer.parseInt(split[0]);
                unit = 'k';
            }
            else if (inputTextParts[1].contains("m")) {
                String split[] = inputTextParts[1].split("m");
                afterDec = Integer.parseInt(split[0]);
                unit = 'm';
            }
            else if (inputTextParts[1].contains("b")) {
                String split[] = inputTextParts[1].split("b");
                afterDec = Integer.parseInt(split[0]);
                unit = 'b';
            } else {
                return;
            }
           String multiplier = "" + beforeDec + "." + afterDec;
            double m = Double.parseDouble(multiplier);
            double price;
            if (unit == 'k') {
                price = m * 1000;
            } else if (unit =='m') {
                price = m * 1000000;
            } else if (unit == 'b') {
                price = m * 1000000000;
            } else {
                return;
            }
            int intPrice = (int) price;
            client.setVar(VarClientStr.INPUT_TEXT, String.valueOf(intPrice));
        } else if(e.getKeyCode() == KeyEvent.VK_PERIOD) {
            final String inputText = client.getVar(VarClientStr.INPUT_TEXT);
            if(inputText.equals("")) {
                return;
            }
            String newText = inputText + ".";
            client.setVar(VarClientStr.INPUT_TEXT, newText);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
