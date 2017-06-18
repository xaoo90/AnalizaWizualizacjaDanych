/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Xaoo
 */
public class MainControler implements Initializable {

    static Stage stage;
    
    Cars cars = new Cars();

    ObservableList<Car> carsOb = FXCollections.observableList(cars);
    @FXML
    private TableView<Car> tableCars = new TableView();
    @FXML
    private TableColumn<Car, Integer> hp;
    @FXML
    private TableColumn<Car, Integer> vmax;
    @FXML
    private Label lngList;
    @FXML
    private Label lblMax;
    @FXML
    private Label lblMin;
    @FXML
    private Label lblAvg;
    @FXML
    private Label lblFirstQ;
    @FXML
    private Label lblMedian;
    @FXML
    private Label lblThirdQ;
    @FXML
    private Label lblIRQ;
    @FXML
    private Label lblStandard;
    
    @FXML
    private Label lblMaxY;
    @FXML
    private Label lblMinY;
    @FXML
    private Label lblAvgY;
    @FXML
    private Label lblFirstQY;
    @FXML
    private Label lblMedianY;
    @FXML
    private Label lblThirdQY;
    @FXML
    private Label lblIRQY;
    @FXML
    private Label lblStandardY;
    
    
    @FXML
    private Label lblPearsonCorrelation;
    @FXML
    private Label lblLinearRegression;
    @FXML
    private Label lblDistantPoints;
    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField txtCount;
    @FXML
    private TextField txtFrom;
    @FXML
    private TextField txtTo;
    @FXML
    private TableView<Car> tableCarsEstimation = new TableView();
    @FXML
    private TableColumn<Car, Integer> hpEstimation;
    @FXML
    private TableColumn<Car, Integer> vmaxEstimation;

    @FXML
    private TextField txtHP;

    ObservableList<Car> carsEstimation = FXCollections.observableArrayList();
        
    @FXML
    private TableView<Car> distantPointTableCars = new TableView();
    @FXML
    private TableColumn<Car, Integer> distantPointHp;
    @FXML
    private TableColumn<Car, Integer> distantPointVmax;

    @FXML
    public void estimationRandom() {
        Random rand = new Random();
        int hp, vMax;
        int min = Integer.parseInt(txtFrom.getText());
        int max = Integer.parseInt(txtTo.getText());
        int count = Integer.parseInt(txtCount.getText());
        for (int i = 0; i < count; i++) {
            hp = rand.nextInt((max - min) + 1) + min;
            vMax = (int) (cars.linearRegression()[0] * hp + cars.linearRegression()[1]);
            carsEstimation.add(new Car(hp, vMax));
        }
        showEstimation();
    }

    @FXML
    public void estimation() {
        int hp = Integer.parseInt(txtHP.getText());
        int vMax = (int) (cars.linearRegression()[0] * hp + cars.linearRegression()[1]);
        carsEstimation.add(new Car(hp, vMax));
        showEstimation();
    }

    public void showEstimation() {
        hpEstimation.setCellValueFactory(new PropertyValueFactory("horsePower"));
        vmaxEstimation.setCellValueFactory(new PropertyValueFactory("vMax"));
        tableCarsEstimation.setItems(carsEstimation);
    }

    public void showCars() {
        hp.setCellValueFactory(new PropertyValueFactory("horsePower"));
        vmax.setCellValueFactory(new PropertyValueFactory("vMax"));
        tableCars.setItems(carsOb);
    }

    public void showChart() {
        NumberAxis xAxis = new NumberAxis(0, cars.getMax().getHorsePower() + 40, 20);

        NumberAxis yAxis = new NumberAxis(0, cars.getMax().getVMax() + 40, 20);
        final LineChart<Number, Number> lc = new LineChart<>(xAxis, yAxis);

        cars.sortCarsHP();

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Samochód");

        for (Car car : cars) {
            series1.getData().add(new XYChart.Data(car.getHorsePower(), car.getVMax()));
        }

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Prosta regresji");
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Punkty oddalone");
        
        for(Car car : cars.getDistantPointsX()){
            series3.getData().add(new XYChart.Data(car.getHorsePower(), car.getVMax()));
        }
        int x = (int) (cars.getMin().getHorsePower() - 20);
        series2.getData().add(new XYChart.Data(x, (int) (cars.linearRegression()[0] * x + cars.linearRegression()[1])));
        x = (int) (cars.getMax().getHorsePower() + 20);
        series2.getData().add(new XYChart.Data(x, (int) (cars.linearRegression()[0] * x + cars.linearRegression()[1])));
        lc.setAnimated(false);
        lc.setCreateSymbols(true);

        lc.getData().addAll(series1, series2, series3);
        xAxis.setLabel("Moc [KM]");
        yAxis.setLabel("Prędkość maksymalna [km/h]");

        lc.getStylesheets().add(getClass().getResource("root.css").toExternalForm());

        borderPane.setCenter(lc);

    }

    public void calculateData(Quartile quantile) {
        cars.calculateMax();
        cars.calculateMin();
        cars.calculateAvg();
        cars.calculateStandardDeviation();
        cars.distantPoint(quantile);
    }
    
    
    public void showDistantPoints(Quartile quantile) {
        
        ObservableList<Car> distantsPoints = FXCollections.observableList(cars.getDistantPointsX());
        distantPointHp.setCellValueFactory(new PropertyValueFactory("horsePower"));
        distantPointVmax.setCellValueFactory(new PropertyValueFactory("vMax"));
        distantPointTableCars.setItems(distantsPoints);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FileChooser chooser = new FileChooser();        
        File file = chooser.showOpenDialog(stage);
        cars.openXMLFile(file);
        cars.sortCarsHP();
        Quartile quartile = new Quartile(cars);
        showCars();
        calculateData(quartile);
        showChart();
        tableCarsEstimation.setPlaceholder(new Label(""));
        lngList.setText("Liczba danych: " + carsOb.size());
        lblMax.setText(cars.getMax().getHorsePower() + "");
        lblMin.setText(cars.getMin().getHorsePower() + "");
        lblAvg.setText(cars.getAvg().getHorsePower() + "");
        lblFirstQ.setText(quartile.getFirst().getHorsePower() + "");
        lblMedian.setText(quartile.getMedian().getHorsePower() + "");
        lblThirdQ.setText(quartile.getThird().getHorsePower() + "");
        lblIRQ.setText(quartile.getInterquartileRange().getHorsePower() + "");
        lblStandard.setText(cars.getStandardDeviaton().getHorsePower() + "");
        
        lblMaxY.setText("" + cars.getMax().getVMax());
        lblMinY.setText("" + cars.getMin().getVMax());
        lblAvgY.setText("" + cars.getAvg().getVMax());
        lblFirstQY.setText("" + quartile.getFirst().getVMax());
        lblMedianY.setText("" + quartile.getMedian().getVMax());
        lblThirdQY.setText("" + quartile.getThird().getVMax());
        lblIRQY.setText("" + quartile.getInterquartileRange().getVMax());
        lblStandardY.setText("" + cars.getStandardDeviaton().getVMax());
        
        
        lblPearsonCorrelation.setText(cars.pearsonCorrelation() + "");
        lblLinearRegression.setText("y = " + cars.linearRegression()[0] + "x +" + cars.linearRegression()[1]);
        showDistantPoints(quartile);
    }

}
