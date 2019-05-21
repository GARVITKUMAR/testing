package programmableCalculator;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/******
 * <p> Title: DefiniionsUserInterface Class. </p>
 * 
 * <p> Description: The UserInterface class for the Definition window </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2016 </p>
 * 
 * @author Lynn Robert Carter
 * @author Garvit
 * @version 5.00	2019-02-26 The implementation of UI for Definitions window
 */

public class DefinitionsUserInterface {

	ManageDefinitions perform = new ManageDefinitions();
	private Label lbl_EditingGuidance = // A Label used to guide the user
			new Label("Editing a Table Cell!  When finished, press <enter> or <return> to commit the change.");

	private static boolean whenSorting = false; // A flag to signal when to ignore case

	private ObservableList<Quantity> tableData = // The list of values being defined
			FXCollections.observableArrayList();
	public static TextField txt_FileName = new TextField();
	final Scanner fileName = new Scanner(txt_FileName.getText());

	/**********
	 * This inner class is used to define the various fields required by the
	 * variable/constant definitions.
	 * 
	 * @author lrcarter
	 *
	 */
	public static class Quantity {
		final SimpleStringProperty nameValue; // The name of the value
		final SimpleStringProperty isConstantValue; // Specifies if this is a constant
		final SimpleStringProperty measureValue; // The measured value
		final SimpleStringProperty errorTermValue; // Error term, if there is one
		final SimpleStringProperty unitsValue; // Units, if there is one

		/*****
		 * This fully-specified constructor establishes all of the fields of a Quantity
		 * object
		 * 
		 * @param n - A String that specifies the name of the constant or variable
		 * @param c - A String that serves as a T or F flag as to where or not this is a
		 *          constant
		 * @param m - A String that specifies the measured value / value, if there is
		 *          one
		 * @param e - A String that specifies the error term, if there is one
		 * @param u - A String that specifies the units definition, if there is one
		 */
		public Quantity(String n, String c, String m, String e, String u) {
			this.nameValue = new SimpleStringProperty(n);
			this.isConstantValue = new SimpleStringProperty(c);
			this.measureValue = new SimpleStringProperty(m);
			this.errorTermValue = new SimpleStringProperty(e);
			this.unitsValue = new SimpleStringProperty(u);
		}

		public Quantity() {
			nameValue = null;
			isConstantValue = null;
			measureValue = null;
			errorTermValue = null;
			unitsValue = null;
		}

		/*****
		 * This getter gets the value of the variable / constant name field - If the
		 * whenSorting flag is true, this method return the String converted to lower
		 * case - otherwise, it return the String as is
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this
		 * code generates method calls to these routines given the name of the field, it
		 * follows this naming pattern.
		 * 
		 * @return String - of the name of the variable / constant
		 */
		public String getNameValue() {
			if (whenSorting)
				return nameValue.get().toLowerCase();
			else
				return nameValue.get();
		}

		/*****
		 * This Setter sets the value of the variable / constant name field
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this
		 * code generates method calls to these routines given the name of the field, it
		 * follows this naming pattern.
		 */
		public void setNameValue(String n) {
			nameValue.set(n);
		}

		/*****
		 * This getter gets the value of the isConstant flag field - If this field is
		 * true, the item being defined is a constant and the calculator will not be
		 * allowed to alter the value (but the calculator's user may editing the value
		 * of this item).
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this
		 * code generates method calls to these routines given the name of the field, it
		 * follows this naming pattern.
		 * 
		 * @return String - Either a "T" or an "F" String
		 */
		public String getIsConstantValue() {
			return isConstantValue.get();
		}

		/*****
		 * This Setter sets the value of the isConstant flag field - If the parameter c
		 * starts with a "T" or a "t", the field is set to "T", else it is set to "F".
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this
		 * code generates method calls to these routines given the name of the field, it
		 * follows this naming pattern.
		 * 
		 * @param c String - The first letter is used to determine if this is a "T" or
		 *          "F"
		 */
		public void setIsConstantValue(String c) {
			if (c.startsWith("T") || c.startsWith("t"))
				isConstantValue.set("T");
			else
				isConstantValue.set("F");
		}

		/*****
		 * This getter gets the value of the measureValue field.
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this
		 * code generates method calls to these routines given the name of the field, it
		 * follows this naming pattern.
		 * 
		 * @return String - A String of the measuredValue specification is returned
		 */
		public String getMeasureValue() {
			return measureValue.get();
		}

		/*****
		 * This Setter sets the value of the measuredValue field
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this
		 * code generates method calls to these routines given the name of the field, it
		 * follows this naming pattern.
		 * 
		 * @param m The value is assumed to be a value numeric string. It must be checked before this routine is used.
		 */
		public void setMeasureValue(String m) {
			measureValue.set(m);
		}

		/*****
		 * This getter gets the value of the errorTerm field.
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this
		 * code generates method calls to these routines given the name of the field, it
		 * follows this naming pattern.
		 * 
		 * @return String - A String of the errorTerm specification is returned
		 */
		public String getErrorTermValue() {
			return errorTermValue.get();
		}

		/*****
		 * This Setter sets the value of the errorTerm field
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this
		 * code generates method calls to these routines given the name of the field, it
		 * follows this naming pattern.
		 * 
		 * @param e String - The value is assumed to be a value numeric string. It must
		 *          be checked before this routine is used.
		 */
		public void setErrorTermValue(String e) {
			errorTermValue.set(e);
		}

		/*****
		 * This getter gets the value of the units field.
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this
		 * code generates method calls to these routines given the name of the field, it
		 * follows this naming pattern.
		 * 
		 * @return String - A String of the units specification is returned
		 */
		public String getUnitsValue() {
			return unitsValue.get();
		}

		/*****
		 * This Setter sets the value of the unitsValue field
		 * 
		 * NOTE: Be very careful with the name, especially the capitalization as this
		 * code generates method calls to these routines given the name of the field, it
		 * follows this naming pattern.
		 * 
		 * @param u String - The value is assumed to be a value units string. It must be
		 *          checked before this routine is used.
		 */
		public void setUnitsValue(String u) {
			unitsValue.set(u);
		}
	}

	public DefinitionsUserInterface(Pane thePane1) {
		TableView<Quantity> table = new TableView<>();
		Button btn_Create = new Button("Create");
		Button btn_Load = new Button("Load");
		Button btn_Save = new Button("Save");
		Button btn_Cancel = new Button("Cancel");
		Label lbl_FileName = new Label("Enter name of the File:");
		btn_Save.setDisable(true);
		btn_Cancel.setDisable(true);
		// Establish a button to add a new row into the TableView into the set of
		// definitions
		Button btn_Add = new Button("Add a new Item");
		setupButton(btn_Add, 150, 130, 15);
		btn_Add.setDisable(true);

		Button btn_Delete = new Button("Delete an Item");
		btn_Add.setOnAction((event) -> {
			btn_Save.setDisable(false);
			perform = new ManageDefinitions();
			perform.delete(table, tableData, btn_Delete);
		});

		// Establish a button to delete a row in the TableView into the set of
		// definitions
		setupButton(btn_Delete, 150, 300, 15);
		setupButton(btn_Cancel, 100, 470, 15);
		btn_Cancel.setOnAction(e -> {
			tableData.clear();
			btn_Load.setDisable(false);
			btn_Add.setDisable(true);
			btn_Delete.setDisable(true);
			btn_Cancel.setDisable(true);
			btn_Save.setDisable(true);
			lbl_EditingGuidance.setVisible(false);
		});
		// If there is no data in the table, then disable the Delete Button else enable
		// it
		if (tableData.size() <= 0)
			btn_Delete.setDisable(true);
		else
			btn_Delete.setDisable(false);

		// This button handler deals with the various cases that arise when deleting a
		// table row
		btn_Delete.setOnAction((event) -> {
			// Get selected row and delete
			int ix = table.getSelectionModel().getSelectedIndex();
			if (ix <= -1)
				return;
			tableData.remove(ix);
			if (table.getItems().size() == 0) {
				btn_Delete.setDisable(true);
				return;
			}
			if (ix != 0) {
				ix = ix - 1;
			}
			table.requestFocus();
			table.getSelectionModel().select(ix);
			table.getFocusModel().focus(ix);
		});

		// Make the table editable and position it in the pop-up window
		table.setEditable(true);
		table.setLayoutX(20);
		table.setLayoutY(60);

		// **********//
		// Define each of the columns in the table view and set up the handlers to
		// support editing

		// This is the column that support the Name column. When the name of a
		// definition is changed
		// this code will cause the table of data to be re-sorted and rearranged so the
		// rows will
		// shown in the table as sorted.
		TableColumn<Quantity, String> col_NameValue = new TableColumn<Quantity, String>("Variable/Constant\nName");
		col_NameValue.setMinWidth(130);
		col_NameValue.setCellValueFactory(new PropertyValueFactory<>("nameValue"));
		col_NameValue.setCellFactory(TextFieldTableCell.<Quantity>forTableColumn());

		// When one starts editing a Name column, a message is displayed giving guidance
		// on how to
		// commit the change when done.
		col_NameValue.setOnEditStart((CellEditEvent<Quantity, String> t) -> {
			lbl_EditingGuidance.setVisible(true);
		});

		// When the user commits the change, the editing guidance message is once again
		// hidden and
		// the system sorts the data in the table so the data will always appear sorted
		// in the table
		col_NameValue.setOnEditCommit((CellEditEvent<Quantity, String> t) -> {
			((Quantity) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNameValue(t.getNewValue());
			whenSorting = true;
			tableData.sort(Comparator.comparing(Quantity::getNameValue));
			whenSorting = false;
			lbl_EditingGuidance.setVisible(false);
		});

		// **********//
		// This is the column that supports the IsConstantValue field.
		TableColumn<Quantity, String> col_IsConstantValue = new TableColumn<Quantity, String>("Is a\nConstant");
		col_IsConstantValue.setMinWidth(75);
		col_IsConstantValue.setCellValueFactory(new PropertyValueFactory<>("isConstantValue"));
		col_IsConstantValue.setCellFactory(TextFieldTableCell.<Quantity>forTableColumn());

		// When one starts editing the IsConstantValue column, a message is displayed
		// giving
		// guidance on how to commit the change when done.
		col_IsConstantValue.setOnEditStart((CellEditEvent<Quantity, String> t) -> {
			lbl_EditingGuidance.setVisible(true);
		});

		// When the user commits the change, the editing guidance message is once again
		// hidden and
		// the system sorts the data in the table so the data will always appear sorted
		// in the table
		col_IsConstantValue.setOnEditCommit((CellEditEvent<Quantity, String> t) -> {
			((Quantity) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setIsConstantValue(t.getNewValue());
			lbl_EditingGuidance.setVisible(false);
		});

		// **********//
		// This is the column that supports the MeasureValue field.
		TableColumn<Quantity, String> col_MeasureValue = new TableColumn<Quantity, String>("Measure or Value");
		col_MeasureValue.setMinWidth(175);
		col_MeasureValue.setCellValueFactory(new PropertyValueFactory<>("measureValue"));
		col_MeasureValue.setCellFactory(TextFieldTableCell.<Quantity>forTableColumn());

		// When one starts editing the MeasureValue column, a message is displayed
		// giving
		// guidance on how to commit the change when done.
		col_MeasureValue.setOnEditStart((CellEditEvent<Quantity, String> t) -> {
			lbl_EditingGuidance.setVisible(true);
		});

		// When the user commits the change, the editing guidance message is once again
		// hidden and
		// the system sorts the data in the table so the data will always appear sorted
		// in the table
		col_MeasureValue.setOnEditCommit((CellEditEvent<Quantity, String> t) -> {
			((Quantity) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setMeasureValue(t.getNewValue());
			lbl_EditingGuidance.setVisible(false);
		});

		// **********//
		// This is the column that supports the ErrorTermValue field.
		TableColumn<Quantity, String> col_ErrorValue = new TableColumn<Quantity, String>("Error Term");
		col_ErrorValue.setMinWidth(100);
		col_ErrorValue.setCellValueFactory(new PropertyValueFactory<>("errorTermValue"));
		col_ErrorValue.setCellFactory(TextFieldTableCell.<Quantity>forTableColumn());

		// When one starts editing the ErrorTermValue column, a message is displayed
		// giving
		// guidance on how to commit the change when done.
		col_ErrorValue.setOnEditStart((CellEditEvent<Quantity, String> t) -> {
			lbl_EditingGuidance.setVisible(true);
		});

		// When the user commits the change, the editing guidance message is once again
		// hidden and
		// the system sorts the data in the table so the data will always appear sorted
		// in the table
		col_ErrorValue.setOnEditCommit((CellEditEvent<Quantity, String> t) -> {
			((Quantity) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setErrorTermValue(t.getNewValue());
			lbl_EditingGuidance.setVisible(false);
		});

		// **********//
		// This is the column that supports the UnitsValue field.
		TableColumn<Quantity, String> col_UnitsValue = new TableColumn<Quantity, String>("Units");
		col_UnitsValue.setMinWidth(100);
		col_UnitsValue.setCellValueFactory(new PropertyValueFactory<>("unitsValue"));
		col_UnitsValue.setCellFactory(TextFieldTableCell.<Quantity>forTableColumn());

		// When one starts editing the UnitsValue column, a message is displayed giving
		// guidance on how to commit the change when done.
		col_UnitsValue.setOnEditStart((CellEditEvent<Quantity, String> t) -> {
			lbl_EditingGuidance.setVisible(true);
		});

		// When the user commits the change, the editing guidance message is once again
		// hidden and
		// the system sorts the data in the table so the data will always appear sorted
		// in the table
		col_UnitsValue.setOnEditCommit((CellEditEvent<Quantity, String> t) -> {
			((Quantity) t.getTableView().getItems().get(t.getTablePosition().getRow())).setUnitsValue(t.getNewValue());
			lbl_EditingGuidance.setVisible(false);
		});

		// **********//
		// The follow sets up the editing guidance text,. positions it below the table,
		// sets the
		// text red, and hides the text so it is only shown during the edit process.
		lbl_EditingGuidance.setMinWidth(600);
		lbl_EditingGuidance.setLayoutX(20);
		lbl_EditingGuidance.setLayoutY(470);
		lbl_EditingGuidance.setTextFill(Color.RED);
		lbl_EditingGuidance.setVisible(false);

		// The right-most three columns are grouped into a single column as they define
		// the value
		// elements of the definition.
		TableColumn<Quantity, String> col_ValueGroup = new TableColumn<Quantity, String>("Value");
		col_ValueGroup.getColumns().add(col_MeasureValue);
		col_ValueGroup.getColumns().add(col_ErrorValue);
		col_ValueGroup.getColumns().add(col_UnitsValue);

		// As we are setting up the GUI, we begin by sorting the data with which we
		// start
		whenSorting = true;
		tableData.sort(Comparator.comparing(Quantity::getNameValue));
		whenSorting = false;

		// This loads the data from the ObservableList into the table, so the TableView
		// code can
		// display it and provide all of the functions that it provides
		table.setItems(tableData);

		// This calls add the three major column titles into the table. Notice that the
		// right most
		// column is a composite of the three value fields (measure, error term, and
		// units)
		table.getColumns().add(col_NameValue);
		table.getColumns().add(col_IsConstantValue);
		table.getColumns().add(col_ValueGroup);

		setupLabelUI(lbl_FileName, "Arial", 16, 20, Pos.BASELINE_LEFT, 20, 495);

		setupTextFieldUI(txt_FileName, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 520, true);

		setupButton(btn_Create, 100, 20, 15);
		btn_Create.setOnAction(e -> {
			btn_Add.setDisable(false);
			btn_Load.setDisable(true);
			btn_Cancel.setDisable(false);
			perform = new ManageDefinitions();
			perform.create(table);
		});

		setupButton(btn_Load, 100, 360, 520);
		btn_Load.setOnAction(e -> {

			try {
				loadFile();
				btn_Add.setDisable(false);
				btn_Delete.setDisable(false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		});

		setupButton(btn_Save, 100, 480, 520);
		btn_Save.setOnAction(e -> {
			final Scanner fileName = new Scanner(txt_FileName.getText());
			perform = new ManageDefinitions();
			perform.update(table, fileName);
		});

		// With all of the GUI elements defined and initialized, we add them to the
		// window pane
		thePane1.getChildren().addAll(btn_Add, btn_Delete, table, lbl_EditingGuidance, lbl_FileName, txt_FileName,
				btn_Save, btn_Create, btn_Load, btn_Cancel);

	}

	public DefinitionsUserInterface() {
	}

	private void setupButton(Button b, int w, int x, int y) {
		b.setMinWidth(w);
		b.setAlignment(Pos.BASELINE_CENTER);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a text field
	 */
	private void setupTextFieldUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e) {
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);
		t.setEditable(e);
	}

	/**********
	 * Private local method to initialize the standard fields for a label
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}

	public File getTextFieldData() {
		File file = new File("Repository/" + txt_FileName.getText() + ".txt");
		return file;
	}

	public void loadFile() throws NoSuchElementException, IOException {
		File file = new File("Repository/" + txt_FileName.getText() + ".txt");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(file);
		try {
			while (scanner.nextLine() != null) {
				while (scanner.hasNextLine()) {
					String[] tokens = scanner.nextLine().split(" ");
					int p = 0;
					try {
						Quantity q = new Quantity(tokens[p++], tokens[p++], tokens[p++], tokens[p++], tokens[p++]);
						tableData.add(q);
					} catch (ArrayIndexOutOfBoundsException e) {
						break;
					}
				}
			}
		} catch (NoSuchElementException e) {
			
		}

		return;

	}

	public boolean get_table() {
		if (tableData.isEmpty())
			;
		return false;
	}

}
