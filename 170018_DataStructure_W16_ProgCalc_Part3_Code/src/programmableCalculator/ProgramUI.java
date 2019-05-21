package programmableCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import dictionary.Dictionary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;

/**
 * <p>
 * Title: UserInterface Class.
 * </p>
 * 
 * <p>
 * Description: The Java/FX-based user interface for the calculator. The class
 * works with String objects and passes work to other classes to deal with all
 * other aspects of the computation.
 * </p>
 * 
 * <p>
 * 
 * @author: Garvit
 * @version 1.02 programmable calculator of part-3
 * </p>
 */
public class ProgramUI {
	private String theExpression = "";
	
	private TextArea writeArea = new TextArea();
	private static TextArea outputArea = new TextArea();
	
	private TextField txt_Filename = new TextField();
	private Button btn_load = new Button("Load");
	private Button edit = new Button("Edit");
	private Button Save = new Button("Save");
	private Button create = new Button("Create New file");
	private Button run = new Button("Run");
	private Button debug = new Button("Debug");
	
	private Label filename = new Label("Enter File Name");
	private Label selectfile = new Label("Select File");
	private Label prog = new Label("Program");
	private Label prog_output = new Label("Output");
	private Label lbl_errMessage = new Label();

	ObservableList<String> list = FXCollections.observableArrayList();
	private ComboBox<String> combobox = new ComboBox<String>(list);
	private Button delete = new Button("Delete");
	File theDirectory = new File("Repository");

	File[] name = theDirectory.listFiles();
	// The following are the attributes that support the scanning and lexing of the
	// input
	private static Scanner theReader;
	private static Lexer lexer;
	private static Token current;
	private static Token next;

	// The following are the stacks that are used to transform the parse output into
	// a tree
	private static Stack<ExprNode> exprStack = new Stack<>();
	private static Stack<Token> opStack = new Stack<>();

	Dictionary<String> dict = new Dictionary<String>();

	public ProgramUI(Stage resultStage) throws IOException {
		Pane theRoot = new Pane();

		writeArea.setDisable(true);
		writeArea.setEditable(false);
		outputArea.setDisable(true);
		btn_load.setDisable(true);
		delete.setDisable(true);
		debug.setDisable(true);

		create.setDisable(true);
		run.setDisable(true);
		Save.setDisable(true);
		edit.setDisable(true);
		
		btn_load.setLayoutX(220);
		btn_load.setLayoutY(90);
		btn_load.setMinWidth(80);

		lbl_errMessage.setFont(Font.font("Arial", FontPosture.REGULAR, 14));
		lbl_errMessage.setTextFill(Color.RED);

		txt_Filename.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!(txt_Filename.getText().length() == 0)) {
				create.setDisable(false);

			}

			else {
				create.setDisable(true);
			}

		});

		btn_load.setOnAction((event) -> {

			delete.setDisable(false);
			debug.setDisable(false);
			run.setDisable(false);
			edit.setDisable(false);
			Save.setDisable(false);
			try {
				dict.Read(writeArea, Save, Save, combobox);
			} catch (IOException e) {

				e.printStackTrace();
			}

		});
		filename.setLayoutX(50);
		filename.setLayoutY(20);
		combobox.setLayoutX(50);
		combobox.setLayoutY(90);
		combobox.setOnMouseClicked((event) -> {

			combobox.getItems().clear();
			if (combobox.getSelectionModel() == null) {
				btn_load.setDisable(true);
			} else {
				btn_load.setDisable(false);
			}
			ShowContent();
		});
		selectfile.setLayoutX(50);
		selectfile.setLayoutY(70);
		combobox.setMinWidth(150);
		combobox.setMinHeight(10);
		
		run.setLayoutX(100);
		run.setLayoutY(250);
		run.setMinWidth(100);
		
		run.setOnAction((event) -> {
			outputArea.setDisable(false);
			try {
				output();
			} catch (NoSuchElementException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		create.setLayoutX(100);
		create.setLayoutY(170);
		create.setMinWidth(100);
		create.setOnAction((event) -> {

			writeArea.setDisable(false);
			writeArea.setEditable(true);
			edit.setDisable(true);
			Save.setDisable(false);

		});
		debug.setLayoutX(100);
		debug.setLayoutY(340);
		debug.setMinWidth(100);
		
		debug.setOnAction((event) -> {
			Debug();
			writeArea.setEditable(true);

		});
		
		prog.setLayoutX(500);
		prog.setLayoutY(90);
		
		prog_output.setLayoutX(500);
		prog_output.setLayoutY(330);
		
		txt_Filename.setLayoutX(50);
		txt_Filename.setLayoutY(40);

		delete.setMinWidth(100);
		delete.setLayoutX(100);
		delete.setLayoutY(420);
		delete.setOnAction((event) -> {
			dict.Delete(combobox, writeArea);

		});

		edit.setLayoutX(100);
		edit.setLayoutY(500);
		edit.setMinWidth(100);
		edit.setOnAction((event) -> {
			writeArea.setEditable(true);
			Save.setDisable(false);
		});

		writeArea.setLayoutX(400);
		writeArea.setLayoutY(130);
		writeArea.setMaxWidth(220);
		
		outputArea.setLayoutX(400);
		outputArea.setLayoutY(360);
		outputArea.setMaxWidth(250);

		lbl_errMessage.setLayoutX(10);
		lbl_errMessage.setLayoutY(350);

		Save.setLayoutX(100);
		Save.setLayoutY(600);
		Save.setMinWidth(100);
		
		Save.setOnAction((event) -> {
			outputArea.setDisable(false);
			run.setDisable(false);
			dict.create(writeArea, txt_Filename);
			if (txt_Filename.getText().length() == 0) {
				lbl_errMessage.setText("Please Enter file name");
			}

			txt_Filename.selectionProperty().addListener(e -> {
				lbl_errMessage.setText("");
			});

		});

		Scene resultScene = new Scene(theRoot, 700, 600);
		resultStage.setScene(resultScene);
		resultStage.show();
		theRoot.getChildren().addAll(edit, run, debug, btn_load, delete, writeArea, txt_Filename, filename, combobox, selectfile, prog, create,
				Save, outputArea, prog_output, lbl_errMessage);

		return;

	}

	private static boolean addSubExpr() {

		// The method assumes the input is a sequence of additive/subtractive elements
		// separated
		// by addition and/or subtraction operators
		if (mpyDivExpr()) {

			// Once an additive/subtractive element has been found, it can be followed by a
			// sequence of addition or subtraction operators followed by another
			// additive/subtractive element. Therefore we start by looking for a "+" or a
			// "-"
			while ((current.getTokenKind() == Kind.SYMBOL) && ((current.getTokenCode() == 6) || // The "+" operator
					(current.getTokenCode() == 7))) { // The "-" operator

				// When you find a "+" or a "-", push it onto the operator stack
				opStack.push(current);

				// Advance to the next input token
				current = next;
				next = lexer.accept();

				// Look for the next additive/subtractive element
				if (mpyDivExpr()) {

					// If one is found, pop the two operands and the operator
					ExprNode expr2 = exprStack.pop();
					ExprNode expr1 = exprStack.pop();
					Token oper = opStack.pop();

					// Create an Expression Tree node from those three items and push it onto
					// the expression stack
					exprStack.push(new ExprNode(oper, true, expr1, expr2));
				} else {

					// If we get here, we saw a "+" or a "-", but it was not followed by a valid
					// additive/subtractive element
					System.out.println("Parse error: A required additive/subtractive element was not found");
					return false;
				}
			}

			// Reaching this point indicates that we have processed the sequence of
			// additive/subtractive elements
			return true;
		} else

			// This indicates that the first thing found was not an additive/subtractive
			// element
			return false;
	}

	/**********
	 * The mpyDiv Expression method parses a sequence of expression elements that
	 * are multiplied together, divided from one another, or a blend of them.
	 * 
	 * @return The method returns a boolean value indicating if the parse was
	 *         successful
	 */
	private static boolean mpyDivExpr() {

		// The method assumes the input is a sequence of terms separated by
		// multiplication and/or
		// division operators
		if (term()) {

			// Once an multiplication/division element has been found, it can be followed by
			// a
			// sequence of multiplication or division operators followed by another
			// multiplication/division element. Therefore we start by looking for a "*" or a
			// "/"
			while ((current.getTokenKind() == Kind.SYMBOL) && ((current.getTokenCode() == 8) || // The "*" operator
					(current.getTokenCode() == 9))) { // The "/" operator

				// When you find a "*" or a "/", push it onto the operator stack
				opStack.push(current);

				// Advance to the next input token
				current = next;
				next = lexer.accept();

				// Look for the next multiplication/division element
				if (term()) {

					// If one is found, pop the two operands and the operator
					ExprNode expr2 = exprStack.pop();
					ExprNode expr1 = exprStack.pop();
					Token oper = opStack.pop();

					// Create an Expression Tree node from those three items and push it onto
					// the expression stack
					exprStack.push(new ExprNode(oper, true, expr1, expr2));
				} else {

					// If we get here, we saw a "*" or a "/", but it was not followed by a valid
					// multiplication/division element
					System.out.println("Parse error: a term was not found");
					return false;
				}
			}

			// Reaching this point indicates that we have processed the sequence of
			// additive/subtractive elements
			return true;
		} else

			// This indicates that the first thing found was not a multiplication/division
			// element
			return false;
	}

	/**********
	 * The term Expression method parses constants.
	 * 
	 * @return The method returns a boolean value indicating if the parse was
	 *         successful
	 */
	private static boolean term() {

		// Parse the term
		if (current.getTokenKind() == Kind.FLOAT || current.getTokenKind() == Kind.INTEGER) {

			// When you find one, push a corresponding expression tree node onto the stack
			exprStack.push(new ExprNode(current, false, null, null));

			// Advance to the next input token
			current = next;
			next = lexer.accept();

			// Signal that the term was found
			return true;
		} else

			// Signal that a term was not found
			return false;
	}

	/**********
	 * The compute method is passed a tree as an input parameter and computes the
	 * value of the tree based on the operator nodes and the value node in the tree.
	 * Precedence is encoded into the tree structure, so there is no need to deal
	 * with it during the evaluation.
	 * 
	 * @param r - The input parameter of the expression tree
	 * 
	 * @return - A double value of the result of evaluating the expression tree
	 */
	public static double compute(ExprNode r) {

		// Check to see if this expression tree node is an operator.
		if ((r.getOp().getTokenKind() == Kind.SYMBOL) && ((r.getOp().getTokenCode() == 6)
				|| (r.getOp().getTokenCode() == 7) || (r.getOp().getTokenCode() == 8) || (r.getOp().getTokenCode() == 9)
				|| (r.getOp().getTokenCode() == 4) || (r.getOp().getTokenCode() == 5))) {

			// if so, fetch the left and right sub-tree references and evaluate them
			double leftValue = compute(r.getLeft());
			double rightValue = compute(r.getRight());

			// Give the value for the left and the right sub-trees, use the operator code
			// to select the correct operation
			double result = 0;
			switch ((int) r.getOp().getTokenCode()) {

			case 6:
				result = leftValue + rightValue;
				break;
			case 7:
				result = leftValue - rightValue;
				break;
			case 8:
				result = leftValue * rightValue;
				break;
			case 9:
				result = leftValue / rightValue;
				break;

			}

			// Display the actual computation working from the leaves up to the root

			// Return the result to the caller
			return result;
		}

		// If the node is not an operator, determine what it is and fetch the value
		else if (r.getOp().getTokenKind() == Kind.INTEGER) {
			Scanner convertInteger = new Scanner(r.getOp().getTokenText());
			Double result = convertInteger.nextDouble();
			convertInteger.close();
			return result;
		} else if (r.getOp().getTokenKind() == Kind.FLOAT) {
			Scanner convertFloat = new Scanner(r.getOp().getTokenText());
			Double result = convertFloat.nextDouble();
			convertFloat.close();
			return result;
		}

		// If it is not a recognized element, treat it as a value of zero
		else
			return 0.0;

	}

	// If the node is not an operator, determine what it is and fetch the value

	public void output() throws NoSuchElementException, FileNotFoundException {

		outputArea.setEditable(true);

		

		if (!writeArea.getText().isEmpty() && !writeArea.getText().contains("print") && !writeArea.getText().contains("input") &&  !writeArea.getText().contains("=")) {
		
			try {
				tableData();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}

			theReader = new Scanner(writeArea.getText());
			// Show the source of the expression
			System.out.println();
			outputArea.appendText("The expression is: " + theExpression);
			System.out.println();

			// Set up the Scanner and the Lexer

			while (theReader.hasNextLine()) {

				String j = theReader.nextLine();
				Scanner t = new Scanner(j);

				lexer = new Lexer(t);
				current = lexer.accept();
				next = lexer.accept();

				// Invoke the parser and the tree builder
				boolean isValid = addSubExpr();
				outputArea.appendText("The expression is valid: " + isValid + "\n");
				if (isValid) {

					// Display the expression tree
					ExprNode theTree = exprStack.pop();
					String f = theTree.toString();

					outputArea.appendText(f);
					// Evaluate the expression tree
					outputArea.appendText("\nThe evaluation of the tree:");
					outputArea.appendText("\nThe resulting value is: " + compute(theTree));
				}
			}
		}

		else if ((writeArea.getText().contains("print") || writeArea.getText().contains("input") || writeArea.getText().contains("="))
				&& (outputArea.getText().isEmpty() || !outputArea.getText().isEmpty())) {

			outputArea.clear();
			outputArea.setEditable(true);
			Scanner scan = new Scanner(writeArea.getText());

			while (scan.hasNextLine()) {

				String t = scan.nextLine();
				
				if(t.contains("=")) {
					Scanner scan17 = new Scanner(t);

					String to1 = scan17.nextLine();

					StringTokenizer yaar = new StringTokenizer(to1, "=");
					while (yaar.hasMoreTokens()) {

						String t9 = yaar.nextToken();
						Object t91 = yaar.nextElement();

						String tty = t91.toString();
						StringTokenizer kl = new StringTokenizer(tty, "-?\\d+(\\.\\d+)?\n");

						File theDirectory = new File("Repository");
						if (!theDirectory.exists()) {
							theDirectory.mkdir();
							theDirectory.setReadable(true);
							theDirectory.setWritable(true);

						}

						DefinitionsUserInterface b = new DefinitionsUserInterface();
						File theDataFile = b.getTextFieldData();
						
						
						
						while (kl.hasMoreTokens()) {

							String k1 = kl.nextToken();

							Scanner scanner = new Scanner(theDataFile);
							while (scanner.hasNextLine()) {

								String[] tokens = scanner.nextLine().split(" ");

								if (k1.matches(tokens[0])) {

									tty = tty.replaceAll(k1, tokens[2]);

								}

							}
						}

						theReader = new Scanner(tty);

						lexer = new Lexer(theReader);
						current = lexer.accept();
						next = lexer.accept();

						// Invoke the parser and the tree builder
						boolean isValid = addSubExpr();

						// Display the expression tree
						ExprNode theTree1 = exprStack.pop();

						try (FileWriter writer = new FileWriter(theDataFile, true)) {

							writer.write("\n" + t9 + " " + "?" + " " + compute(theTree1) + " " + "?" + " " + "?");
							outputArea.appendText("Data is stored"+"\n");

						} catch (IOException e) {
							e.printStackTrace();
						}

					
					}
					
				}

				else if (t.contains("print")) {

					Scanner scan1 = new Scanner(t);
					scan1.skip("print ");

					String j = scan1.nextLine();

					DefinitionsUserInterface b = new DefinitionsUserInterface();
					File name = b.getTextFieldData();

						if(!name.exists()) {
					
							outputArea.appendText(j + "\n");
						}
						else {
						StringTokenizer kl = new StringTokenizer(j);
						
						while (kl.hasMoreTokens()) {

							String k1 = kl.nextToken();

							Scanner scanner = new Scanner(name);
							while (scanner.hasNextLine()) {

								String[] tokens = scanner.nextLine().split(" ");

								if (k1.matches(tokens[0])) {

									j = j.replaceAll(k1, tokens[2]);
									
									

									outputArea.appendText(j + "\n");

								}
								
							}

							

						}
						

						
						}
						
					
					

				}

				else if (t.contains("input") ) {
					outputArea.appendText("");

					outputArea.setOnKeyPressed((event) -> {

						if (event.getCode() == KeyCode.ENTER) {

							Scanner scan3 = new Scanner(outputArea.getText());
							Scanner scan5 = new Scanner(writeArea.getText());

							while (scan3.hasNextLine() && scan5.hasNextLine()) {

								String j = scan5.nextLine();

								String m = scan3.nextLine();

								if (m.matches("-?\\d+(\\.\\d+)?") && j.contains("input")) {

									Scanner scan8 = new Scanner(j);
									scan8.skip("input ");
									Scanner scan6 = new Scanner(m);
									String h = scan6.nextLine();
									String n = scan8.nextLine();

									File theDirectory = new File("Repository");
									if (!theDirectory.exists()) {
										theDirectory.mkdir();
										theDirectory.setReadable(true);
										theDirectory.setWritable(true);

									}

									DefinitionsUserInterface b = new DefinitionsUserInterface();
									File theDataFile = b.getTextFieldData();
									

									try (FileWriter writer = new FileWriter(theDataFile, true)) {

										writer.write("\n" + n + " " + "?" + " " + h + " " + "?" + " " + "?");
										outputArea.appendText("Data is Stored in a File" + "\n");

									} catch (IOException e) {
										e.printStackTrace();
									}

								}
							}
						}
					});

				}

				else {

					outputArea.setOnKeyPressed((event) -> {

						if (event.getCode() == KeyCode.ENTER) {

							Scanner scan3 = new Scanner(outputArea.getText());
							Scanner scan5 = new Scanner(writeArea.getText());

							while (scan3.hasNextLine() && scan5.hasNextLine()) {

								String j = scan5.nextLine();

								String m = scan3.nextLine();

								if (m.matches("-?\\d+(\\.\\d+)?") && j.contains("input")) {

									Scanner scan8 = new Scanner(j);
									scan8.skip("input ");
									Scanner scan6 = new Scanner(m);
									String h = scan6.nextLine();
									String n = scan8.nextLine();

									File theDirectory = new File("Repository");
									if (!theDirectory.exists()) {
										theDirectory.mkdir();
										theDirectory.setReadable(true);
										theDirectory.setWritable(true);

									}
									

									DefinitionsUserInterface b = new DefinitionsUserInterface();
									File theDataFile = b.getTextFieldData();

									

                                   

                                    
									
									try (FileWriter writer = new FileWriter(theDataFile, true)) {

										writer.write("\n" + n + " " + "?" + " " + h + " " + "?" + " " + "?");

									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							try {
								tableData();
							} catch (FileNotFoundException e) {

								e.printStackTrace();
							}

							Scanner scan11 = new Scanner(outputArea.getText());
							while (scan11.hasNextLine()) {

								String w = scan11.nextLine();
								if (w.contains("+") || w.contains("-") || w.contains("/") || w.contains("*")
										|| w.contains("(") || w.contains(")")) {
									Scanner scan12 = new Scanner(w);
									String e1 = scan12.nextLine();

									theReader = new Scanner(e1);
									// Show the source of the expression
									System.out.println();
									outputArea.appendText("The expression is: " + theExpression);
									System.out.println();

									// Set up the Scanner and the Lexer

									while (theReader.hasNextLine()) {

										String j1 = theReader.nextLine();
										Scanner t1 = new Scanner(j1);

										lexer = new Lexer(t1);
										current = lexer.accept();
										next = lexer.accept();

										// Invoke the parser and the tree builder
										boolean isValid = addSubExpr();
										outputArea.appendText("The expression is valid: " + isValid + "\n");
										if (isValid) {

											// Display the expression tree
											ExprNode theTree = exprStack.pop();
											String f = theTree.toString();

											outputArea.appendText(f);
											// Evaluate the expression tree
											outputArea.appendText("\nThe evaluation of the tree:");
											outputArea.appendText("\nThe resulting value is: " + compute(theTree));
										}
										
										else {
											outputArea.appendText("Variable not exist or some expression mistake" + "\n");
											outputArea.appendText("Load the Different file if you thing Necessary" + "\n");
											
										}
										
									}
								}
							}}
							}
						}

					);

				}

			}

		}

	}

	private void ShowContent() {
		List<String> a = new ArrayList<String>();
		for (File file : name) {
			if (file.isFile()) {
				a.add(file.getName());
			}
		}
		list.addAll(a);

	}
	public void Debug() {
		String n = writeArea.getText();
		Scanner a = new Scanner(n);
		while (a.hasNextLine()) {
			String s = a.nextLine();
			if (s.startsWith("*") || s.startsWith("/") || s.startsWith(".") || s.endsWith("+") || s.endsWith("-")
					|| s.endsWith("*") || s.endsWith("/") || s.endsWith(".")) {
				lbl_errMessage.setText("You must enter an operand after " + s);
				lbl_errMessage.setTextFill(Color.RED);

			} else {
				lbl_errMessage.setText(" ");
			}
		}
		a.close();
	}

	private void tableData() throws NoSuchElementException, FileNotFoundException {
		if (!writeArea.getText().isEmpty() && (writeArea.getText().contains("print") || writeArea.getText().contains("input"))) {
			String bb = writeArea.getText();

			DefinitionsUserInterface b = new DefinitionsUserInterface();
			File name = b.getTextFieldData();

			Scanner scan13 = new Scanner(bb);
			while (scan13.hasNextLine()) {
				String k = scan13.nextLine();
				if (!k.contains("print") && !k.contains("input")) {

					Scanner scan14 = new Scanner(k);

					String ll = scan14.nextLine();

					StringTokenizer kl = new StringTokenizer(ll, "-?\\d+(\\.\\d+)?\n");

					while (kl.hasMoreTokens()) {

						String k1 = kl.nextToken();

						Scanner scanner = new Scanner(name);
						while (scanner.hasNextLine()) {

							String[] tokens = scanner.nextLine().split(" ");

							if (k1.matches(tokens[0])) {

								ll = ll.replaceAll(k1, tokens[2]);

							}

						}

					}
					outputArea.appendText(ll + "\n");

				}

			}

		}

		else {

			String bb = writeArea.getText();

			DefinitionsUserInterface b = new DefinitionsUserInterface();
			File name = b.getTextFieldData();
			Scanner scan14 = new Scanner(writeArea.getText());
			while (scan14.hasNextLine()) {

				String ty = scan14.nextLine();

				StringTokenizer kl = new StringTokenizer(ty, "-?\\d+(\\.\\d+)?\n");

				while (kl.hasMoreTokens()) {

					String k1 = kl.nextToken();

					Scanner scanner = new Scanner(name);
					while (scanner.hasNextLine()) {

						String[] tokens = scanner.nextLine().split(" ");

						if (k1.matches(tokens[0])) {

							bb = bb.replaceAll(k1, tokens[2]);

							writeArea.setText(bb);
						}

					}

				}

			}

		}

	}
}
