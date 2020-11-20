package hu.johetajava.prizm;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import hu.johetajava.Main;

import java.util.ArrayList;
import java.util.EventListener;

public class Encoder {
    final GpioPinDigitalInput portA;
    final GpioPinDigitalInput portB;

    private int encoderCount = 0;

    private ArrayList<EncoderChangeListener> changeListeners = new ArrayList<>();

    public Encoder(Pin pinA, Pin pinB) {
        portA = Main.gpio.provisionDigitalInputPin(pinA);
        portB = Main.gpio.provisionDigitalInputPin(pinB);

        portA.addListener((GpioPinListenerDigital) event -> {
            if (event.getState().getValue() == portB.getState().getValue()) {
                encoderCount++;
            } else {
                encoderCount--;
            }
            changeListeners.forEach(listener -> listener.onChange(encoderCount));
        });
    }

    public int value() {
        return encoderCount;
    }

    public void addListener(EncoderChangeListener changeListener) {
        this.changeListeners.add(changeListener);
    }

    public void removeListener(EncoderChangeListener changeListener) {
        this.changeListeners.remove(changeListener);
    }
}


interface EncoderChangeListener extends EventListener {
    void onChange(int encoderValue);
}
