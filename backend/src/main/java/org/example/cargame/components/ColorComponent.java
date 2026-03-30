package org.example.cargame.components;

public class ColorComponent extends Component {
    private volatile String color;

    public void setColor(String color){
        this.color = color;
    }

    public String getColor(){
        return this.color;
    }
}
