package cat.dam.alex.mosquitoattack;

import android.graphics.drawable.AnimationDrawable;

public class Mosquito {
    private int velocity;
    private int xPos;
    private int yPos;
    public Mosquito(int xPos, int yPos, int velocity){
        xPos=this.xPos;
        yPos=this.yPos;
        velocity=this.velocity;
    }
    public Mosquito(int xPos, int yPos){
        xPos=this.xPos;
        yPos=this.yPos;
    }
    public Mosquito(){ }
    public void advanceHorizontally(){
        setxPos(getxPos()+velocity);
    }
    public void advanceVertically(){
        setyPos(getyPos()+velocity);
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
