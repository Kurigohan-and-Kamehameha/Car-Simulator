package org.example.cargameFx.components;

public class SpeedComponent extends Component{
    private volatile double speed;

    public SpeedComponent(double speed){
        this.speed = speed;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public double getSpeed(){
        return this.speed;
    }
}
