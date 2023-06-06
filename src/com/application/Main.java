package com.application;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import com.gui.*;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * The class that describes the operation of the application
 * @author User
 *
 */
public class Main extends Application {
	/**
	 * Set up all the stuff the applicatoin needs to run.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			final int WINDOWHEIGHT = 500;
			final int WINDOWWIDTH = 600;
			primaryStage.setTitle("Temporally Structured Sequence of Stays and Transits Master");

			primaryStage.setResizable(false);
			GridPane root = new GridPane();
			root.setGridLinesVisible(true);

			EventListBox eventList = new EventListBox();

			root.getColumnConstraints().add(new ColumnConstraints(WINDOWWIDTH - 150));
			root.getColumnConstraints().add(new ColumnConstraints(150));
			root.getRowConstraints().add(new RowConstraints(WINDOWHEIGHT));

			GridPane.setConstraints(eventList.pane(), 0, 0);
			root.getChildren().add(eventList.pane());

			GridPane panel = new GridPane();
			GridPane.setConstraints(panel, 1, 0);
			panel.setGridLinesVisible(true);
			panel.getColumnConstraints().add(new ColumnConstraints(150));
			panel.getRowConstraints().add(new RowConstraints(200));
			panel.getRowConstraints().add(new RowConstraints(300));
			root.getChildren().add(panel);

			VBox buttons = new VBox(2);
			buttons.setPadding(new Insets(2,0,0,2));
			AddButton addCalculated = new AddButton("Add Calculated Event", new AddButton.AddCalculatedBehavior(eventList));
			AddButton addMisc = new AddButton("Add Misc Event", new AddButton.AddMiscBehavior(eventList));
			AddButton addStatic = new AddButton("Add Static Event", new AddButton.AddSetBehavior(eventList));
			AddButton deleteButton = new AddButton("Delete selected", new AddButton.AddDeleteBehavior(eventList));
			AddButton saveFileButton = new AddButton("Save File", new AddButton.AddSaveFileBehavior(eventList));
			AddButton openFileButton = new AddButton("Open File", new AddButton.AddOpenFileBehavior(eventList));
			ArrayList<AddButton> buttonList = new ArrayList<AddButton>();
			buttonList.add(addCalculated);
			buttonList.add(addStatic);
			buttonList.add(addMisc);
			buttonList.add(deleteButton);
			buttonList.add(saveFileButton);
			buttonList.add(openFileButton);
			for(AddButton but : buttonList) {
				buttons.getChildren().add(but.getButton());
			}
			EditPanel editPanel = new EditPanel();

			GridPane.setConstraints(buttons, 0, 0);
			GridPane.setConstraints(editPanel.box(), 0, 1);
			panel.getChildren().addAll(buttons, editPanel.box());

			Scene scene = new Scene(root,WINDOWWIDTH, WINDOWHEIGHT);
			primaryStage.setScene(scene);
			primaryStage.show();


			EventHandler<javafx.scene.input.MouseEvent> selectBox = 
					new EventHandler<javafx.scene.input.MouseEvent>() { 

				@Override 
				public void handle(javafx.scene.input.MouseEvent e) { 
					for(int i = 0; i < eventList.list().size(); i++) {
						if(eventList.list().get(i).getBox() == e.getTarget()) {
							if(eventList.selected() != null) {
								eventList.selected().hideSelected();;
							}
							eventList.setSelected(eventList.list().get(i));
							eventList.selected().showSelected();;

							ArrayList<String> al = eventList.selected().getEditable();
							editPanel.setValues(al);
							editPanel.errorLabel().setText("");

						}
					}
				} 
			};
			eventList.box().addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, selectBox);

			EventHandler<javafx.scene.input.MouseEvent> buttonPanelClick = 
					new EventHandler<javafx.scene.input.MouseEvent>() { 

				@Override 
				public void handle(javafx.scene.input.MouseEvent e) { 
					String b = ((Button )e.getSource()).getText();
					for(int i = 0; i < buttonList.size(); i++) {
						if(buttonList.get(i).getButton().getText() == b) {
							buttonList.get(i).action();
						}
					}
				} 
			};
			for(AddButton but : buttonList) {
				but.getButton().addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, buttonPanelClick);
			}

			EventHandler<javafx.scene.input.MouseEvent> pressSaveEdit = 
					new EventHandler<javafx.scene.input.MouseEvent>() { 

				@Override 
				public void handle(javafx.scene.input.MouseEvent e) { 
					if(eventList.selected() == null) {
						return;
					}
					String name = editPanel.name().getText();
					String type = editPanel.type().getValue();
					LocalTime departure = null;
					try {
						departure = LocalTime.parse(editPanel.departure().getText(), DateTimeFormatter.ofPattern("HH:mm"));
					}
					catch (DateTimeParseException ex) {
						editPanel.errorLabel().setText("Incorrect departure time formatting! (XX:XX)");
						return;
					}
					String fourth = editPanel.fourth().getText();
					EventBox neuSel = eventList.selected();
					int position = eventList.list().indexOf(neuSel);
					boolean neu = false;
					if(neuSel.getClass() == CalculatedTransportationEventBox.class) {
						double thing = 0;
						try {
							thing = Double.parseDouble(fourth);
						}
						catch (NumberFormatException ex) {
							editPanel.errorLabel().setText("Incorrect distance formatting!");
							return;
						}
						eventList.deleteEvent(eventList.selected());
						neu = eventList.addCalcEventBox(type, name, departure, thing);
					}
					else if(neuSel.getClass() == SetTransportationEventBox.class) {
						LocalTime arrival = null;
						try {
							arrival = LocalTime.parse(fourth, DateTimeFormatter.ofPattern("HH:mm"));
						}
						catch (DateTimeParseException ex) {
							editPanel.errorLabel().setText("Incorrect arrival time formatting! (XX:XX)");
							return;
						}
						eventList.deleteEvent(eventList.selected());
						neu = eventList.addSetEventBox(type, name, departure, arrival);
					}
					else if(neuSel.getClass() == MiscEventBox.class) {
						LocalTime arrival = null;
						try {
							arrival = LocalTime.parse(fourth, DateTimeFormatter.ofPattern("HH:mm"));
						}
						catch (DateTimeParseException ex) {
							editPanel.errorLabel().setText("Incorrect arrival time formatting! (XX:XX)");
							return;
						}
						eventList.deleteEvent(eventList.selected());
						neu = eventList.addMiscEventBox(type, name, departure, arrival);
					}

					if(neu) {
						eventList.setSelected(null);
						editPanel.errorLabel().setText("");
						editPanel.type().setValue("");
						editPanel.name().setText("");
						editPanel.departure().setText("");
						editPanel.fourth().setText("");
					}
					else {
						editPanel.errorLabel().setText("Time collison!");

						if(neuSel.getClass() == CalculatedTransportationEventBox.class) {
							ArrayList<String> arr = neuSel.getEditable();
							eventList.addCalcEventBox(neuSel.getType(), neuSel.getName(), neuSel.getDeparture(), Double.parseDouble(arr.get(4)));
							eventList.setSelected(eventList.list().get(position));
							eventList.selected().showSelected();
						}
						if(neuSel.getClass() == SetTransportationEventBox.class) {
							eventList.addSetEventBox(neuSel.getType(), neuSel.getName(), neuSel.getDeparture(), neuSel.getArrival());
							eventList.setSelected(eventList.list().get(position));
							eventList.selected().showSelected();
						}
						if(neuSel.getClass() == MiscEventBox.class) {
							eventList.addMiscEventBox(neuSel.getType(), neuSel.getName(), neuSel.getDeparture(), neuSel.getArrival());
							eventList.setSelected(eventList.list().get(position));
							eventList.selected().showSelected();
						}
					}


				} 
			};
			editPanel.saveButton().addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, pressSaveEdit);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Primary loop of the app.
	 * @param args arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
