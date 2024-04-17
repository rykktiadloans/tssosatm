package com.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


/** A class that represents a button that, when pressed, does a certain action
 */
public class AddButton {
	/** A field that represents the actual button that can be pressed
	 * 
	 */
	private Button button;
	/** A field that represents an object that describes the action to be done
	 * 
	 */
	private AddBehavior behavior;

	/** 
	 * @param text The text of the button
	 * @param b The action that button does when pressed
	 */
	public AddButton(String text, AddBehavior b) {
		this.button = new Button(text);
		this.behavior = b;
	}

	/** Does the thing that the button does when pressed.
	 * @return Returns true if the action is done properly, and false there was an error of some sort
	 */
	public boolean action() {
		return this.behavior.addEvent();
	}

	/**
	 * Returns the JavaFX button
	 * @return returns the JavaFX button
	 */
	public Button getButton() {
		return this.button;
	}


	/** Interface that defines the action to be done
	 *
	 */
	public static interface AddBehavior {
		/**
		 * The action
		 * @return true if everything goes ok, false otherwise
		 */
		public boolean addEvent();
	}

	/** A class that implements the AddBehavior interface. Appends a single CalculatedTransportationEventBox to 
	 * the end of the Event List. The Event is 0 minutes long and uses Slow Walk
	 */	
	public static class AddCalculatedBehavior implements AddBehavior {

		/** A field that represents the EventList the action is done on
		 * 
		 */
		private EventListBox list;

		/** @param l EventListBox object on which the action is done
		 */
		public AddCalculatedBehavior(EventListBox l) {
			this.list = l;
		}

		/**
		 * Appends a single CalculatedTransportationEventBox to 
		 * the end of the Event List. The Event is 0 minutes long and uses Slow Walk
		 * @return Returns true if the action is done correctly, false if not
		 */
		@Override
		public boolean addEvent() {
			for(int i = this.list.list().size() - 1; i >= 0; i--) {
				EventBox event = this.list.list().get(i);
				LocalTime departure = event.getArrival();
				boolean cond = this.list.addCalcEventBox("Slow Walk", "New Event", departure, 0.01);
				if(cond) {
					return true;
				}
			}
			if(this.list.list().size() == 0) {
				this.list.addCalcEventBox("Slow Walk", "New Event", LocalTime.of(8, 00), 0.01);
			}
			return false;
		}

	}

	/** A class that implements the AddBehavior interface. Deletes the selected event	 */	
	public static class AddDeleteBehavior implements AddBehavior {

		/** A field that represents the EventList the action is done on
		 * 
		 */
		private EventListBox list;

		/** @param l EventListBox object on which the action is done
		 */
		public AddDeleteBehavior(EventListBox l) {
			this.list = l;
		}

		/**
		 * Deletes the selected event
		 * @return Returns true if the action is done correctly, false if not
		 */
		@Override
		public boolean addEvent() {
			if(list.selected() == null) {
				return false;
			}
			list.deleteEvent(list.selected());
			list.setSelected(null);
			return true;
		}

	}

	/** A class that implements the AddBehavior interface. Appends a single MiscEventBox to 
	 * the end of the Event List. The Event is 0 minutes long
	 */		
	public static class AddMiscBehavior implements AddBehavior {

		/** A field that represents the EventList the action is done on
		 * 
		 */
		private EventListBox list;

		/** @param l EventListBox object on which the action is done
		 */
		public AddMiscBehavior(EventListBox l) {
			this.list = l;
		}

		/**
		 * Appends a single MiscEventBox to 
		 * the end of the Event List. The Event is 0 minutes long
		 * @return Returns true if the action is done correctly, false if not
		 */
		@Override
		public boolean addEvent() {
			for(int i = this.list.list().size() - 1; i >= 0; i--) {
				EventBox event = this.list.list().get(i);
				LocalTime departure = event.getArrival();
				boolean cond = this.list.addMiscEventBox("Misc Event", "New Event", departure, departure);
				if(cond) {
					return true;
				}
			}
			if(this.list.list().size() == 0) {
				this.list.addMiscEventBox("Misc Event", "New Event", LocalTime.of(8, 00), LocalTime.of(8, 00));
			}
			return false;
		}

	}

	/** A class that implements the AddBehavior interface. Imports an event list from a .txt file
	 */		
	public static class AddOpenFileBehavior implements AddBehavior {
		/** A field that represents the EventList the action is done on
		 * 
		 */
		private EventListBox list;

		/** @param l EventListBox object on which the action is done
		 */
		public AddOpenFileBehavior(EventListBox l) {
			this.list = l;
		}

		/**
		 * Imports an event list from a .txt file
		 * @return Returns true if the action is done correctly, false if not
		 */
		@Override
		public boolean addEvent() {
			FileChooser fileSys = new FileChooser();
			fileSys.setTitle("Open");
			fileSys.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
			File file = fileSys.showOpenDialog(this.list.box().getScene().getWindow());
			if(file == null) {
				return false;
			}
			try {
			    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
			    EventListBox eventList = (EventListBox) objectInputStream.readObject();
			    objectInputStream.close(); 
				int am = this.list.list().size();
				int newSize = eventList.list().size();

				for(int i = 0; i < am; i++) {
					this.list.deleteEvent(this.list.list().get(0));
				}

				for(int i = 0; i < newSize; i++) {
					EventBox box = eventList.list().get(i);
					Class<? extends EventBox> boxClass = box.getClass();
					String type = box.getType();
					String name = box.getName();
					LocalTime departure = box.getDeparture();
					if(boxClass == CalculatedTransportationEventBox.class) {
						double thing = Double.parseDouble(box.getEditable().get(4));
						this.list.addCalcEventBox(type, name, departure, thing);
					}
					else if(boxClass == SetTransportationEventBox.class) {
						LocalTime arrival = box.getArrival();
						this.list.addSetEventBox(type, name, departure, arrival);
					}
					else if(boxClass == MiscEventBox.class) {
						LocalTime arrival = box.getArrival();
						this.list.addMiscEventBox(type, name, departure, arrival);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			return true;

		}
	}

	/** A class that implements the AddBehavior interface. Exports an event list to a .txt file
	 */	
	public static class AddSaveFileBehavior implements AddBehavior {

		/** A field that represents the EventList the action is done on
		 * 
		 */
		private EventListBox list;

		/** @param l EventListBox object on which the action is done
		 */
		public AddSaveFileBehavior(EventListBox l) {
			this.list = l;
		}

		/**
		 * Exports an event list to a .txt file
		 * @return Returns true if the action is done correctly, false if not
		 */
		@Override
		public boolean addEvent() {
//			StringBuilder string = new StringBuilder();
//			for(int i = 0; i < this.list.list().size(); i++) {
//				ArrayList<String> e = this.list.list().get(i).getEditable();
//				for(int j = 0; j < 5; j++) {
//					string.append(e.get(j) + "\n");
//				}
//				string.append("\n");
//			}
			FileChooser fileSys = new FileChooser();
			fileSys.setTitle("Save");
			fileSys.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
			File file = fileSys.showSaveDialog(this.list.box().getScene().getWindow());
			if(file == null) {
				return false;
			}
		    try {
		    	ObjectOutputStream objectOutputStream  = new ObjectOutputStream(new FileOutputStream(file));
				objectOutputStream.writeObject(this.list);
				objectOutputStream.flush();
				objectOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			
//			try (BufferedWriter bw = new BufferedWriter(new PrintWriter(file))) {
//				bw.write(string.toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			return true;
		}

	}

	/** A class that implements the AddBehavior interface. Appends a single SetTransportationEventBox to 
	 * the end of the Event List. The Event is 0 minutes long
	 */		
	public static class AddSetBehavior implements AddBehavior {

		/** A field that represents the EventList the action is done on
		 * 
		 */
		private EventListBox list;

		/** @param l EventListBox object on which the action is done
		 */
		public AddSetBehavior(EventListBox l) {
			this.list = l;
		}

		/**
		 * Appends a single SetTransportationEventBox to 
		 * the end of the Event List. The Event is 0 minutes long
		 * @return Returns true if the action is done correctly, false if not
		 */
		@Override
		public boolean addEvent() {
			for(int i = this.list.list().size() - 1; i >= 0; i--) {
				EventBox event = this.list.list().get(i);
				LocalTime departure = event.getArrival();
				boolean cond = this.list.addSetEventBox("Public Transit", "New Event", departure, departure);
				if(cond) {
					return true;
				}
			}
			if(this.list.list().size() == 0) {
				this.list.addSetEventBox("Public Transit", "New Event", LocalTime.of(8, 00), LocalTime.of(8, 00));
			}
			return false;
		}

	}

}
