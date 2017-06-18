/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Xaoo
 */
public class Cars extends ArrayList<Car> {

    private StatisticData min = new StatisticData();
    private StatisticData max = new StatisticData();
    private StatisticData avg = new StatisticData();
    private StatisticData standardDeviaton = new StatisticData();
    private List<Car> distantPointsX = new ArrayList<>();
    private List<Car> distantPointsY = new ArrayList<>();

    public StatisticData getMax() {
        return max;
    }

    public void setMax(StatisticData max) {
        this.max = max;
    }

    public StatisticData getMin() {
        return min;
    }

    public void setMin(StatisticData min) {
        this.min = min;
    }

    public StatisticData getAvg() {
        return avg;
    }

    public void setAvg(StatisticData avg) {
        this.avg = avg;
    }

    public StatisticData getStandardDeviaton() {
        return standardDeviaton;
    }

    public void setStandardDeviaton(StatisticData standardDeviaton) {
        this.standardDeviaton = standardDeviaton;
    }

    public List<Car> getDistantPointsX() {
        return distantPointsX;
    }

    public void setDistantPointsX(List<Car> distantPointsX) {
        this.distantPointsX = distantPointsX;
    }

    public List<Car> getDistantPointsY() {
        return distantPointsY;
    }

    public void setDistantPointsY(List<Car> distantPointsY) {
        this.distantPointsY = distantPointsY;
    }




    public static NodeList readXMLFile(File fXmlFile) {

        NodeList nList = null;
        try {

            //File fXmlFile = new File("D:/git/aiwd/dane.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            nList = doc.getElementsByTagName("car");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nList;
    }

    public void openXMLFile(File file) {
        NodeList nList = readXMLFile(file);
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                String x, y;
                String elementX = eElement.getElementsByTagName("X").item(0).getTextContent();
                String elementY = eElement.getElementsByTagName("Y").item(0).getTextContent();

                Pattern pattern = Pattern.compile("\\d*");
                Matcher Xmatcher = pattern.matcher(elementX);
                Matcher Ymatcher = pattern.matcher(elementY);

                if (!Xmatcher.matches()) {
                    continue;
                }
                if (!Ymatcher.matches()) {
                    continue;
                }

                if ("".equals(elementX)) {
                    x = "-1";
                } else {
                    x = elementX;
                }

                if ("".equals(elementY)) {
                    y = "-1";
                } else {
                    y = elementY;
                }

                this.add(new Car(Integer.parseInt(x),
                        Integer.parseInt(y)));
            }
        }
        carDataValidator();
    }

    public void carDataValidator() {
        int countHP = 0;
        int countVMax = 0;
        int sumHP = 0;
        int sumVMax = 0;

        for (Car car : this) {
            if (car.getHorsePower() > 0) {
                sumHP += car.getHorsePower();
                countHP++;
            }
            if (car.getVMax() > 0) {
                sumVMax += car.getVMax();
                countVMax++;
            }
        }

        int avgHP = sumHP / countHP;
        int avgVMax = sumVMax / countVMax;

        for (Car car : this) {
            if (car.getHorsePower() < 0) {
                car.setHorsePower(avgHP);
            }
            if (car.getVMax() < 0) {
                car.setVMax(avgVMax);
            }
        }

    }

    public void calculateMax() {
        int maxVMax = 0;
        int maxHP = 0;
        for (Car car : this) {
            if (car.getVMax() > maxVMax) {
                maxVMax = car.getVMax();
            }
            if (car.getHorsePower() > maxHP) {
                maxHP = car.getHorsePower();
            }
        }
        this.max.setVMax(maxVMax);
        this.max.setHorsePower(maxHP);
    }

    public void calculateMin() {
        int minVMax = Integer.MAX_VALUE;
        int minHP = Integer.MAX_VALUE;
        for (Car car : this) {
            if (car.getVMax() < minVMax) {
                minVMax = car.getVMax();
            }
            if (car.getHorsePower() < minHP) {
                minHP = car.getHorsePower();
            }
        }
        this.min.setVMax(minVMax);
        this.min.setHorsePower(minHP);
    }

    private int sumValueY() {
        int sum = 0;

        for (Car car : this) {
            sum = sum + car.getVMax();
        }
        return sum;
    }

    private int sumValueX() {
        int sum = 0;

        for (Car car : this) {
            sum = sum + car.getHorsePower();
        }
        return sum;
    }

    public void calculateAvg() {
        double avgVMax = (double) Math.round((double) sumValueY() / this.size() * 100) / 100;
        double avgHP = (double) Math.round((double) sumValueX() / this.size() * 100) / 100;
        this.avg.setHorsePower(avgHP);
        this.avg.setVMax(avgVMax);
    }

    public void calculateStandardDeviation() {
        double variationVMax = 0;
        double variationHP = 0;
        for (Car car : this) {
            variationVMax = variationVMax + Math.pow(car.getVMax() - avg.getVMax(), 2);
            variationHP = variationHP + Math.pow(car.getHorsePower() - avg.getHorsePower(), 2);
        }
        this.standardDeviaton.setVMax((double) Math.round(Math.sqrt(variationVMax / this.size()) * 100) / 100);
        this.standardDeviaton.setHorsePower((double) Math.round(Math.sqrt(variationHP / this.size()) * 100) / 100);
    }

    private int sumValueXY() {
        int sum = 0;

        for (Car car : this) {
            sum = sum + car.getHorsePower() * car.getVMax();
        }
        return sum;
    }

    private int sumValueSquareX() {
        int sum = 0;

        for (Car car : this) {
            sum = sum + (int) Math.pow(car.getHorsePower(), 2);
        }
        return sum;
    }

    private int sumValueSquareY() {
        int sum = 0;

        for (Car car : this) {
            sum = sum + (int) Math.pow(car.getVMax(), 2);
        }
        return sum;
    }

    public double pearsonCorrelation() {
        double numerator;
        double denominator;
        numerator = this.size() * sumValueXY() - sumValueX() * sumValueY();
        denominator = Math.sqrt((this.size() * sumValueSquareX() - Math.pow(sumValueX(), 2)) * (this.size() * sumValueSquareY() - Math.pow(sumValueY(), 2)));     
        return (double) Math.round(numerator / denominator * 100) / 100;
    }

    public double[] linearRegression() {
        double a;
        double b;
        int size = this.size();

        a = (size * sumValueXY() - sumValueX() * sumValueY()) / (size * sumValueSquareX() - Math.pow(sumValueX(), 2));
        a = (double) Math.round(a * 100) / 100;
        b = ((double) 1 / size) * (sumValueY() - a * sumValueX());
        b = (double) Math.round(b * 100) / 100;

        return new double[]{a, b};
    }

    public void distantPoint(Quartile quantile) {
        double a = 0;
        double b = 0;
        a = quantile.getFirst().getHorsePower() - 1.5 * quantile.getInterquartileRange().getHorsePower();
        b = quantile.getThird().getHorsePower() + 1.5 * quantile.getInterquartileRange().getHorsePower();
        for (Car car : this) {
            if (car.getHorsePower() < a || car.getHorsePower() > b) {
                this.distantPointsX.add(car);
            }
        }
        a = quantile.getFirst().getVMax()- 1.5 * quantile.getInterquartileRange().getVMax();
        b = quantile.getThird().getVMax() + 1.5 * quantile.getInterquartileRange().getVMax();
        for (Car car : this) {
            if (car.getVMax() < a || car.getVMax() > b) {
                this.distantPointsY.add(car);
            }
        }
    }

    public void sortCarsVMax() {
        this.sort(new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                return o1.getVMax() - o2.getVMax();
            }
        });
    }

    public void sortCarsHP() {
        this.sort(new Comparator<Car>() {
            @Override
            public int compare(final Car o1, final Car o2) {
                return o1.getHorsePower() - o2.getHorsePower();
            }
        });
    }

}
