package com.gui;

import java.time.LocalTime;
import java.util.ArrayList;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/** An interface that is implemented by all the events
 */
public interface EventBox {
	
	/** Returns the JavaFX region of the event
	 * 
	 * @return Returns the JavaFX region of the event
	 */
	public Region getBox();
	
	/** Returns the type of the event as a string
	 * 
	 * @return Returns the type of the event as a string
	 */
	public String getType();
	
	/** Returns the name of the event as a string
	 * 
	 * @return Returns the name of the event as a string
	 */
	public String getName();
	
	/** Returns the departure time of the event
	 * 
	 * @return Returns the departure time of the event
	 */
	public LocalTime getDeparture();
	
	/** Returns the arrival time of the event
	 * 
	 * @return Returns the arrival time of the event
	 */
	public LocalTime getArrival();
	
	/** Returns an array of strings that has all the properties of the event
	 * 
	 * @return Returns an array of strings that has all the properties of the event: class of event, type, name, departure time,
	 *  and the fourth property depending on the class
	 */
	public ArrayList<String> getEditable();
	
	/**
	 * Turns the event box green
	 */
	default void showSelected() {
		this.getBox().setBackground(new Background(new BackgroundFill(Color.PALEGREEN,null,null) ));
	}
	
	/**
	 * Turns the event box gray
	 */
	default void hideSelected() {
		this.getBox().setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,null,null) ));

	}
}
