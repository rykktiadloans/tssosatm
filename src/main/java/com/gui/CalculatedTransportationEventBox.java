package com.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * A class that represents the transportation event whose arrival time is calculated based on the distance to the destination 
 * and speed of travel. Implements the EventBox and Serializable interfaces
 */
public class CalculatedTransportationEventBox implements EventBox, Serializable{
	transient private GridPane box = null;
	private String type;
	private String name;
	private LocalTime departure;
	private LocalTime arrival;
	private Double speedKmH;
	private Double distanceKm;
	transient private Label typeLabel;
	transient private Label nameLabel;
	transient private Label timeLabel;

	/**
	 * Returns the dictionary of travel modes and their respective speed.
	 * <table style = "border: 1px solid;">
	 * <caption>Mode/Speed table</caption>
	 *    <tr>
	 *         <th>Mode</th>
	 *          <th>Speed</th>
	 *       </tr>
	 *
	 *    <tr>
	 *         <th>Walking</th>
	 *          <th>5.1</th>
	 *       </tr>
	 *
	 *    <tr>
	 *         <th>Running</th>
	 *          <th>15.5</th>
	 *       </tr>
	 *
	 *    <tr>
	 *         <th>Car</th>
	 *          <th>75.0</th>
	 *       </tr>
	 *
	 *    <tr>
	 *         <th>Bicycle</th>
	 *          <th>31.5</th>
	 *       </tr>
	 *
	 *   <tr>
	 *        <th>Electrobike</th>
	 *         <th>21.1</th>
	 *   </tr>
	 *
	 *    <tr>
	 *         <th>Scooter</th>
	 *          <th>31.5</th>
	 *       </tr>
	 *
	 *       <tr>
	 *     <th>Slow Walk</th>
	 *      <th>1.0</th>
	 *   </tr>
	 *</table>
	 * @return Returns the dictionary of travel modes and their respective speed
	 */
	public static Dictionary<String, Double> getSpeedDic(){
		Dictionary<String, Double> SPEEDDIC = new Hashtable<>();
		SPEEDDIC.put("Walking", 5.1);
		SPEEDDIC.put("Running", 15.5);
		SPEEDDIC.put("Car", 75.0);
		SPEEDDIC.put("Bicycle", 31.5);
		SPEEDDIC.put("Electrobike", 21.1);
		SPEEDDIC.put("Scooter", 31.5);
		SPEEDDIC.put("Slow Walk", 1.0);
		return SPEEDDIC;
	}

	/**
	 * Return the list of types
	 * @return Return the arraylist of strings of types
	 */
	public static ArrayList<String> getTypeList(){
		ArrayList<String> typeList = new ArrayList<String>();
		typeList.add("Walking");
		typeList.add("Running");
		typeList.add("Car");
		typeList.add("Bicycle");
		typeList.add("Electrobike");
		typeList.add("Scooter");
		typeList.add("Slow Walk");
		return typeList;
	}

	/**
	 * 
	 * @param type Type of the event
	 * @param name Name of the event
	 * @param departure Departure time of the event
	 * @param distanceKm Distance to the destination
	 */
	public CalculatedTransportationEventBox(String type, String name, LocalTime departure, double distanceKm){
		this.type = type;
		this.name = name;
		this.departure = departure;
		this.speedKmH = CalculatedTransportationEventBox.getSpeedDic().get(type);
		this.distanceKm = distanceKm;
		this.arrival = departure.plusMinutes((int)((this.distanceKm / this.speedKmH) * 60));
		this.box = new GridPane();
		box.getRowConstraints().add(new RowConstraints(50));
		box.getColumnConstraints().add(new ColumnConstraints(80));
		box.getColumnConstraints().add(new ColumnConstraints(290));
		box.getColumnConstraints().add(new ColumnConstraints(50));
		box.setPadding(new Insets(0,0,0,10));
		box.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,null,null) ));
		this.typeLabel = new Label(type);
		this.nameLabel = new Label(name);
		this.timeLabel = new Label(departure.format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" + arrival.format(DateTimeFormatter.ofPattern("HH:mm")));
		GridPane.setConstraints(typeLabel, 0, 0);
		GridPane.setConstraints(nameLabel, 1, 0);
		GridPane.setConstraints(timeLabel, 2, 0);
		this.typeLabel.setTextFill(Color.ORANGE);


		box.getChildren().addAll(this.typeLabel, this.nameLabel, this.timeLabel);
	}

	/**
	 * Calculate arrival time
	 * @param dep Departure time
	 * @param dis Distance
	 * @param speed Speed
	 * @return Returns arrival time
	 */
	public LocalTime getArrival(LocalTime dep, double dis, double speed) {
		return dep.plusMinutes((int)((dis / speed) * 60));
	}

	/** Returns the JavaFX region of the event
	 * 
	 * @return Returns the JavaFX region of the event
	 */
	@Override
	public Region getBox() {
		return this.box;
	}

	/** Returns the name of the event as a string
	 * 
	 * @return Returns the name of the event as a string
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/** Returns the departure time of the event
	 * 
	 * @return Returns the departure time of the event
	 */
	@Override
	public LocalTime getDeparture() {
		return this.departure;
	}

	/** Returns the arrival time of the event
	 * 
	 * @return Returns the arrival time of the event
	 */
	@Override
	public LocalTime getArrival() {
		return this.arrival;
	}

	/** Returns an array of strings that has all the properties of the event
	 * 
	 * @return Returns an array of strings that has all the properties of the event: class of event, type, name, departure time,
	 *  and the fourth property depending on the class
	 */
	@Override
	public ArrayList<String> getEditable() {
		ArrayList<String> al = new ArrayList<String>();
		al.add("Calc");
		al.add(this.type);
		al.add(this.name);
		al.add(this.departure.format(DateTimeFormatter.ofPattern("HH:mm")));
		al.add(this.distanceKm.toString());
		return al;
	}

	/** Returns the type of the event as a string
	 * 
	 * @return Returns the type of the event as a string
	 */
	@Override
	public String getType() {
		return this.type;
	}



}
