package programmableCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import programmableCalculator.DefinitionsUserInterface.Quantity;
import dictionary.Dictionary;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/******
 * <p> Title: ManageDefinitions Class. </p>
 * 
 * <p> Description: The Class describing the functionalities of the Definitions UserInterface </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2016 </p>
 * 
 * @author Lynn Robert Carter
 * @author Garvit
 * @version 5.00	2019-02-26 The implementation of functionalities of CRUD (Create, Read, Update, Delete)
 */

public class ManageDefinitions {

	public Dictionary<String> dictionary;

	public ManageDefinitions() {

	}

	/***
	 * Create a new File
	 * 
	 * @param table Name of the Table
	 */
	public void create(TableView<Quantity> table) {
		List<List<String>> list = new ArrayList<>();
		Quantity data = new Quantity();
		for (int i = 0; i < table.getItems().size(); i++) {
			data = table.getItems().get(i);
			list.add(new ArrayList<>());
			list.get(i).add(data.nameValue.get());
			list.get(i).add(" " + data.isConstantValue.get());
			list.get(i).add(" " + data.measureValue.get());
			list.get(i).add(" " + data.errorTermValue.get());
			list.get(i).add(" " + data.unitsValue.get());
		}
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				System.out.print(list.get(i).get(j));
			}
		}
	}
	
	/***
	 * Read/ fetch the values of the variables or constants from the file and put them into the respective text fields and combo box
	 * 
	 * @param Operand1
	 * @param Operand1Errorterm
	 * @param combobox
	 * @throws NoSuchElementException
	 * @throws FileNotFoundException
	 */
	public void read(TextField Operand1, TextField Operand1Errorterm, ComboBox<String> combobox) throws NoSuchElementException, FileNotFoundException {
		DefinitionsUserInterface a = new DefinitionsUserInterface();
		File name = a.getTextFieldData();

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(name);
		while (scanner.nextLine() != null) {
			while (scanner.hasNextLine()) {
				try {
					String[] tokens = scanner.nextLine().split(" ");
					if (Operand1.getText().matches(tokens[0])) {
						Operand1.setText(tokens[2]);
						Operand1Errorterm.setText(tokens[3]);
						combobox.getSelectionModel().select(tokens[4]);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

	/***
	 * Update the existing File
	 * 
	 * @param table Name of the Table
	 * @param FileName Name of the File
	 */
	public void update(TableView<Quantity> table, Scanner FileName) {
		List<List<String>> list = new ArrayList<>();
		Quantity data = new Quantity();
		for (int i = 0; i < table.getItems().size(); i++) {
			data = table.getItems().get(i);
			list.add(new ArrayList<>());
			list.get(i).add(data.nameValue.get());
			list.get(i).add(" " + data.isConstantValue.get());
			list.get(i).add(" " + data.measureValue.get());
			list.get(i).add(" " + data.errorTermValue.get());
			list.get(i).add(" " + data.unitsValue.get() + " ");
		}
		dictionary = new Dictionary<String>();
		dictionary.saveDictionary(list, FileName);
	}
	
	/***
	 * Delete a variable or constant
	 * 
	 * @param table Name of Table contaaining variables and constants
	 * @param tableData List of entries in the table
	 * @param btn_Delete Name of the button 
	 */
	public void delete(TableView<Quantity> table, ObservableList<Quantity> tableData, Button btn_Delete) {
		// Create a new row after last row in the table
		Quantity q = new Quantity("?", "?", "?", "?", "?");
		tableData.add(q);
		btn_Delete.setDisable(false);
		int row = tableData.size() - 1;

		// Select the row that was just created
		table.requestFocus();
		table.getSelectionModel().select(row);
		table.getFocusModel().focus(row);
	}

}
