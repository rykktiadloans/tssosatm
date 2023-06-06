package com.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The class that represents the edit panel 
 * @author User
 *
 */
public class EditPanel {
	private VBox box;
	private ComboBox<String> type;
	private TextField name;
	private TextField departure;
	private TextField fourth;
	private Button saveButton;
	private Label errorLabel;
	private ArrayList<Control> confields;
	private String selectedType;
	private Label nameLabel = new Label("Name");
	private Label departureLabel = new Label("Departure Time");
	private Label fourthLabel = new Label("");
	
	/**
	 * Returns the box of the edit panel 
	 * @return Returns the box of the edit panel
	 */
	public VBox box() {
		return this.box;
	}
	
	/**
	 * Returns the type combobox
	 * @return Returns the the type combobox
	 */
	public ComboBox<String> type(){
		return this.type;
	}
	
	/**
	 * Returns text field of the name
	 * @return Returns text field of the name
	 */
	public TextField name() {
		return this.name;
	}
	
	/**
	 * Returns text field of the departure time
	 * @return Returns text field the departure time
	 */
	public TextField departure() {
		return this.departure;
	}
	
	/**
	 * Returns text field of the fourth property
	 * @return Returns text field the the fourth property
	 */
	public TextField fourth() {
		return this.fourth;
	}
	
	/**
	 * Returns the save button
	 * @return Returns the save button
	 */
	public Button saveButton() {
		return this.saveButton;
	}
	
	/**
	 * Returns the label that describes the errors
	 * @return Returns the label that describes the errors
	 */
	public Label errorLabel() {
		return this.errorLabel;
	}
	
	/**
	 * Returns the arraylist of all the elements of the edit panel
	 * @return Returns the arraylist of all the elements of the edit panel
	 */
	public ArrayList<Control> confields(){
		return this.confields;
	}
	
	/**
	 * Returns the class of the selected event as a string
	 * @return Returns the class of the selected event as a string
	 */
	public String selectedType() {
		return this.selectedType;
	}
	
	/**
	 * Returns the label that t=describes the fourth property
	 * @return Returns the label that t=describes the fourth property
	 */
	public Label fourthLabel() {
		return this.fourthLabel;
	}
	
	/**
	 * Constructor
	 */
	public EditPanel() {
		this.type = new ComboBox<String>();
		this.name = new TextField();
		this.departure = new TextField();
		this.fourth = new TextField();
		this.saveButton = new Button("Save");
		this.errorLabel = new Label("");
		this.errorLabel.setTextFill(Color.color(1, 0, 0));
		this.errorLabel.setWrapText(true);
		this.confields = new ArrayList<Control>();
		this.confields.add(type);
		this.confields.add(name);
		this.confields.add(departure);
		this.confields.add(fourth);
		this.confields.add(saveButton);
		this.confields.add(errorLabel);
		this.box = new VBox();
		this.box.getChildren().addAll(type, nameLabel, name, departureLabel, departure, fourthLabel, fourth, saveButton, errorLabel);
		this.box.setPadding(new Insets(2,2,0,2));
		
	}
	
	/**
	 * Fills out all the fields using the data provided in the array
	 * @param al The ArrayList of Strings from which the data can be used: class of event, type, name, departure time,
	 *  and the fourth property depending on the class
	 */
	public void setValues(ArrayList<String> al) {
		this.type.getItems().clear();
		this.type.setValue(al.get(1));
		this.name.setText(al.get(2));
		this.departure.setText(al.get(3));
		this.fourth.setText(al.get(4));
		if(al.get(0) == "Calc") {
			for(int i = 0; i < CalculatedTransportationEventBox.getTypeList().size(); i++) {
				this.type.getItems().add(CalculatedTransportationEventBox.getTypeList().get(i));
			}
			this.selectedType = "Calc";
			this.type.setEditable(false);
			this.fourthLabel.setText("Distance in Km");
		}
		if(al.get(0) == "Set") {
			for(int i = 0; i < SetTransportationEventBox.getTypeList().size(); i++) {
				this.type.getItems().add(SetTransportationEventBox.getTypeList().get(i));
			}
			this.selectedType = "Set";
			this.type.setEditable(false);
			this.fourthLabel.setText("Arrival Time");
		}
		if(al.get(0) == "Misc") {
			for(int i = 0; i < MiscEventBox.getTypeList().size(); i++) {
				this.type.getItems().add(MiscEventBox.getTypeList().get(i));
			}
			this.selectedType = "Misc";
			this.type.setEditable(true);
			this.fourthLabel.setText("Arrival Time");
		}
	}
}
