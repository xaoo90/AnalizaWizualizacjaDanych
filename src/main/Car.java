/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Xaoo
 */
public class Car {

    private int horsePower;
    private int vMax;

    public Car(int horsePower, int vMax) {
        this.horsePower = horsePower;
        this.vMax = vMax;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    public int getVMax() {
        return vMax;
    }

    public void setVMax(int vMax) {
        this.vMax = vMax;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.horsePower;
        hash = 97 * hash + this.vMax;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        if (this.horsePower != other.horsePower) {
            return false;
        }
        if (this.vMax != other.vMax) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Car{" + "horsePower=" + horsePower + ", vMax=" + vMax + '}';
    }
    
    

}
