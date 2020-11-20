package hu.johetajava.prizm;

import hu.johetajava.prizm.Encoder;

public class Motor {
    final int id;
    final Encoder encoder;

    double targetPosition = 0;

    Motor(Encoder encoder, int id){
        this.id = id;
        this.encoder = encoder;
    }


}
