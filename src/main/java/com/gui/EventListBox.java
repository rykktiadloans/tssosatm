package com.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The class that represent the list of events and it's representation in JavaFX. Implements Serializable interface
 * @author User
 *
 */
public class EventListBox implements Serializable{
	private ArrayList<EventBox> list = null;  
	transient private VBox box;
	transient private ScrollPane pane;
	transient private EventBox selected = null;

	/**
	 * Constructor.
	 */
	public EventListBox() {
		this.list = new ArrayList<EventBox>();
		this.box = new VBox();
		this.pane = new ScrollPane();
		this.pane.setContent(box);
	}
	
	/** 
	 * Returns the ArrayList of EventBoxes that the object holds.
	 * @return Returns the ArrayList of EventBoxes that the object holds.
	 */
	public ArrayList<EventBox> list(){
		return this.list;
	}
	
	/**
	 * Returns the VBox element of the event list.
	 * @return Returns the VBox element of the event list.
	 */
	public VBox box() {
		return this.box;
	}
	
	/**
	 * Returns the ScrollPane element of the event list.
	 * @return Returns the ScrollPane element of the event list.
	 */
	public ScrollPane pane() {
		return this.pane;
	}
	
	/**
	 * Returns the selected event.
	 * @return Returns the selected event.

	 */
	public EventBox selected() {
		return this.selected;
	}
	
	/**
	 * Sets the event as the selected one.
	 * @param b The event to be selected.
	 */
	public void setSelected(EventBox b) {
		this.selected = b;
	}

	/**
	 * Adds the calculated transportation event.
	 * @param type Type of the event
	 * @param name Name of the event
	 * @param departure Departure time of the event
	 * @param distanceKm Distance to the destination
	 * @return true if everything goes ok, false if otherwise
	 */
	public boolean addCalcEventBox(String type, String name, LocalTime departure, double distanceKm) {
		CalculatedTransportationEventBox event = new CalculatedTransportationEventBox(type, name, departure, distanceKm);
		if(this.checkCollision(event)) {
			return false;
		}
		this.list.add(event);
		this.box.getChildren().add(event.getBox());
		this.sortList();
		return true;
	}
	
	/**
	 * Adds the static transportation event.
	 * @param type Type of the event
	 * @param name Name of the event
	 * @param departure Departure time of the event
	 * @param arrival Arrival time of the event
	 * @return true if everything goes ok, false if otherwise
	 */
	public boolean addSetEventBox(String type, String name, LocalTime departure, LocalTime arrival) {
		SetTransportationEventBox event = new SetTransportationEventBox(type, name, departure, arrival);
		if(this.checkCollision(event)) {
			return false;
		}
		this.list.add(event);
		this.box.getChildren().add(event.getBox());
		this.sortList();
		return true;
	}
	
	/**
	 * Adds the misc transportation event.
	 * @param type Type of the event
	 * @param name Name of the event
	 * @param departure Departure time of the event
	 * @param arrival Arrival time of the event
	 * @return true if everything goes ok, false if otherwise
	 */
	public boolean addMiscEventBox(String type, String name, LocalTime departure, LocalTime arrival) {
		MiscEventBox event = new MiscEventBox(type, name, departure, arrival);
		if(this.checkCollision(event)) {
			return false;
		}
		this.list.add(event);
		this.box.getChildren().add(event.getBox());
		this.sortList();
		return true;
	}
	/** 
	 * Delets an event.
	 * @param eventbox The event to be deleted
	 * @return true if everything goes ok, false if otherwise

	 */
	public boolean deleteEvent(EventBox eventbox) {
		if(eventbox == null) {
			return false;
		}
		this.list.remove(eventbox);
		this.box.getChildren().remove(eventbox.getBox());
		this.sortList();
		return true;
	}

	/**
	 * Sorts the list according to the departure time of the events
	 */
	public void sortList() {
		for(int i = 0; i < this.list.size(); i++) {
			for(int j = 0; j < this.list.size(); j++) {
				EventBox a = this.list.get(i);
				EventBox b = this.list.get(j);
				if(a.getDeparture().compareTo(b.getDeparture()) < 0){
					EventBox c = a;
					this.list.set(i, this.list.get(j)) ;
					this.list.set(j, c) ;
				}
			}
		}
		
		this.box.getChildren().clear();
		for(int i = 0; i < this.list.size(); i++) {
			this.box.getChildren().add(this.list.get(i).getBox());
		}
	}

	/**
	 * Checks whether the event causes any time collisions.
	 * @param eb The event to check
	 * @return true if there is a collision, false if not
	 */
	public boolean checkCollision(EventBox eb) {
		if(eb.getDeparture().compareTo(eb.getArrival()) > 0) {
			return true;
		}
		
		boolean horror = this.list.stream().reduce(false, (past,cur) -> {
			boolean a = false;
			if((cur.getDeparture().compareTo(eb.getDeparture()) < 0 && cur.getArrival().compareTo(eb.getDeparture()) > 0) ||
					(cur.getDeparture().compareTo(eb.getArrival()) < 0 && cur.getArrival().compareTo(eb.getArrival()) > 0)) {
				a = true;
			}
			if((eb.getDeparture().compareTo(cur.getDeparture()) < 0 && eb.getArrival().compareTo(cur.getDeparture()) > 0) ||
					(eb.getDeparture().compareTo(cur.getArrival()) < 0 && eb.getArrival().compareTo(cur.getArrival()) > 0)) {
				a = true;
			}
			return a;
		}, (a, b) -> a || b);
		
		return horror;
	}
}