/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Xaoo
 */
public class Quartile {

    private StatisticData first = new StatisticData();
    private StatisticData median = new StatisticData();
    private StatisticData third = new StatisticData();
    private StatisticData interquartileRange = new StatisticData();

    public StatisticData getFirst() {
        return first;
    }

    public void setFirst(StatisticData first) {
        this.first = first;
    }

    public StatisticData getMedian() {
        return median;
    }

    public void setMedian(StatisticData median) {
        this.median = median;
    }

    public StatisticData getThird() {
        return third;
    }

    public void setThird(StatisticData third) {
        this.third = third;
    }

    public StatisticData getInterquartileRange() {
        return interquartileRange;
    }

    public void setInterquartileRange(StatisticData interquartileRange) {
        this.interquartileRange = interquartileRange;
    }

    public Quartile(ArrayList<Car> cars) {
        int size = cars.size();
        if ((size % 2) == 1) {
            this.median.setHorsePower(cars.get(Math.round(size / 2)).getHorsePower());
            this.first.setHorsePower(cars.get(Math.round(size / 4)).getHorsePower());
            this.third.setHorsePower(cars.get(Math.round(size / 4) * 3).getHorsePower());
        } else {
            float q1 = (cars.size() - 1) / (float) 4;
            float q3 = q1 * 3;
            
            this.median.setHorsePower((double) (cars.get(Math.round(size / 2) - 1).getHorsePower() + cars.get(Math.round(size / 2)).getHorsePower()) / 2);
            this.first.setHorsePower(cars.get((int) q1).getHorsePower() + (cars.get((int) q1 + 1).getHorsePower() - cars.get((int) q1).getHorsePower()) * (q1 % 1));
            this.third.setHorsePower(cars.get((int) q3).getHorsePower() + (cars.get((int) q3 + 1).getHorsePower() - cars.get((int) q3).getHorsePower()) * (q3 % 1));
        }
        this.interquartileRange.setHorsePower(this.third.getHorsePower() - this.first.getHorsePower());
        QuartileY(cars);
    }

    public void QuartileY(ArrayList<Car> cars) {
        ArrayList<Car> c = cars;
        c.sort(new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                return o1.getVMax() - o2.getVMax();
            }
        });
        int size = cars.size();
        if ((size % 2) == 1) {
            this.median.setVMax(cars.get(Math.round(size / 2)).getVMax());
            this.first.setVMax(cars.get(Math.round(size / 4)).getVMax());
            this.third.setVMax(cars.get(Math.round(size / 4) * 3).getVMax());
        } else {
            this.median.setVMax((double) (cars.get(Math.round(size / 2) - 1).getVMax() + cars.get(Math.round(size / 2)).getVMax()) / 2);
            float q1 = (cars.size() - 1) / (float) 4;
            this.first.setVMax(cars.get((int) q1).getVMax() + (cars.get((int) q1 + 1).getVMax() - cars.get((int) q1).getVMax()) * (q1 % 1));
            float q3 = q1 * 3;
            this.third.setVMax(cars.get((int) q3).getVMax() + (cars.get((int) q3 + 1).getVMax() - cars.get((int) q3).getVMax()) * (q3 % 1));
        }
        this.interquartileRange.setVMax(this.third.getVMax() - this.first.getVMax());

    }

}
