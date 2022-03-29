//package userinterface;
//
//import impresario.IModel;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextAlignment;
//
//import java.util.Properties;
//
//public class StudentBorrowerView extends View {
//    {
//
//        // GUI components
//        protected TextField name;
//        protected TextField year;
//        protected TextField pubYear;
//        protected ComboBox<String> comboBox;
//
//
//
//
//        protected Button submitButton;
//        protected Button doneButton;
//
//        // For showing error message
//        protected MessageView statusLog;
//
//        // constructor for this class -- takes a model object
//        //----------------------------------------------------------
//        public BookView(IModel book)
//        {
//            super(book, "BookView");
//
//            // create a container for showing the contents
//            VBox container = new VBox(10);
//            container.setPadding(new Insets(15, 5, 5, 5));
//
//            // Add a title for this panel
//            container.getChildren().add(createTitle());
//
//            // create our GUI components, add them to this Container
//            container.getChildren().add(createFormContent());
//
//            container.getChildren().add(createStatusLog("             "));
//
//            getChildren().add(container);
//
//            populateFields();
//
//
//            myModel.subscribe("TransactionError", this);
//        }
//
//
//        // Create the title container
//        //-------------------------------------------------------------
//        private Node createTitle()
//        {
//            HBox container = new HBox();
//            container.setAlignment(Pos.CENTER);
//
//            Text titleText = new Text(" Library System ");
//            titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//            titleText.setWrappingWidth(300);
//            titleText.setTextAlignment(TextAlignment.CENTER);
//            titleText.setFill(Color.DARKGREEN);
//            container.getChildren().add(titleText);
//
//            return container;
//        }
//
//        // Create the main form content
//        //-------------------------------------------------------------
//        private VBox createFormContent()
//        {
//            VBox vbox = new VBox(10);
//
//            GridPane grid = new GridPane();
//            grid.setAlignment(Pos.CENTER);
//            grid.setHgap(10);
//            grid.setVgap(10);
//            grid.setPadding(new Insets(25, 25, 25, 25));
//
//            Text prompt = new Text("BOOK INFORMATION");
//            prompt.setWrappingWidth(400);
//            prompt.setTextAlignment(TextAlignment.CENTER);
//            prompt.setFill(Color.BLACK);
//            grid.add(prompt, 0, 0, 2, 1);
//
//            Text titlLab = new Text(" Title : ");
//            Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
//            titlLab.setFont(myFont);
//            titlLab.setWrappingWidth(150);
//            titlLab.setTextAlignment(TextAlignment.RIGHT);
//            grid.add(titlLab, 0, 1);
//
//            bookTitle = new TextField();
//            bookTitle.setEditable(true);
//            grid.add(bookTitle, 1, 1);
//
//            Text authLab = new Text(" Author : ");
//            authLab.setFont(myFont);
//            authLab.setWrappingWidth(150);
//            authLab.setTextAlignment(TextAlignment.RIGHT);
//            grid.add(authLab, 0, 2);
//
//            author = new TextField();
//            author.setEditable(true);
//            grid.add(author, 1, 2);
//
//            Text pubLab = new Text(" Publication Year : ");
//            pubLab.setFont(myFont);
//            pubLab.setWrappingWidth(150);
//            pubLab.setTextAlignment(TextAlignment.RIGHT);
//            grid.add(pubLab, 0, 3);
//
//            pubYear = new TextField();
//            pubYear.setEditable(true);
//            grid.add(pubYear, 1, 3);
//
//            Text statLab = new Text(" Activity Status : ");
//            statLab.setFont(myFont);
//            statLab.setWrappingWidth(150);
//            statLab.setTextAlignment(TextAlignment.RIGHT);
//            grid.add(statLab, 0, 4);
//
//            ComboBox<String> comboBox = new ComboBox<String>();
//            //Setting the prompt text of the combo box
//            //Getting the observable list of the combo box
//            ObservableList<String> list = comboBox.getItems();
//            //Adding items to the combo box
//            list.add("Active");
//            list.add("Inactive");
//            grid.add(comboBox, 1, 4);
//
//
//
//
//            submitButton = new Button("Submit");
//            submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//            submitButton.setOnAction(new EventHandler<ActionEvent>() {
//
//                @Override
//                public void handle(ActionEvent e) {
//                    clearErrorMessage();
//                    processBookData();
//
//
//                }
//            });
//
//            HBox doneCont = new HBox(10);
//            doneCont.setAlignment(Pos.CENTER);
//            doneButton = new Button("Done");
//            doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//            doneButton.setOnAction(new EventHandler<ActionEvent>() {
//
//                @Override
//                public void handle(ActionEvent e) {
//                    clearErrorMessage();
//                    myModel.stateChangeRequest("CancelTransaction", null);
//                }
//            });
//            doneCont.getChildren().add(submitButton);
//            doneCont.getChildren().add(doneButton);
//
//
//
//            vbox.getChildren().add(grid);
//            vbox.getChildren().add(doneCont);
//
//            return vbox;
//        }
//
//
//        // Create the status log field
//        //-------------------------------------------------------------
//        protected MessageView createStatusLog(String initialMessage)
//        {
//            statusLog = new MessageView(initialMessage);
//
//            return statusLog;
//        }
//
//        //-------------------------------------------------------------
//        public void populateFields()
//        {
//            bookTitle.setText((String)myModel.getState("bookTitle"));
//            author.setText((String)myModel.getState("author"));
//            pubYear.setText((String)myModel.getState("pubYear"));
//
//
//        }
//
//        /**
//         * Update method
//         */
//        //---------------------------------------------------------
//        public void updateState(String key, Object value)
//        {
//            clearErrorMessage();
//
//            if (key.equals("TransactionError") == true)
//            {
//                String val = (String)value;
//                if (val.startsWith("Err") || (val.startsWith("ERR")))
//                    displayErrorMessage( val);
//                else
//                    displayMessage(val);
//            }
//        }
//
//        /**
//         * Display error message
//         */
//        //----------------------------------------------------------
//        public void displayErrorMessage(String message)
//        {
//            statusLog.displayErrorMessage(message);
//        }
//
//        /**
//         * Display info message
//         */
//        //----------------------------------------------------------
//        public void displayMessage(String message)
//        {
//            statusLog.displayMessage(message);
//        }
//
//        /**
//         * Clear error message
//         */
//        //----------------------------------------------------------
//        public void clearErrorMessage()
//        {
//            statusLog.clearErrorMessage();
//        }
//
//        //---------------------------------------------------------
//        public void processBookData() {
//            // DEBUG: System.out.println("DepositAmountView.processAction()");
//
//            clearErrorMessage();
//
//            String bookTitleEntered = bookTitle.getText();
//            String authorEntered = author.getText();
//            String pubYearEntered = pubYear.getText();
//
//
//            if ((bookTitleEntered == null) || (bookTitleEntered.length() == 0)){
//                displayErrorMessage("Please enter a book title to be entered.");
//            }
//            else
//            if ((authorEntered == null) || (authorEntered.length() == 0)){
//                displayErrorMessage("Please enter a author to be entered");
//            }
//            else
//            {
//                double pYear = Double.parseDouble(pubYearEntered);
//                if((pYear < 1800) || (pYear > 2022)){
//                    displayErrorMessage("Please enter publisher year between 1800 and 2022");
//                }
//                Properties props = new Properties();
//                props.setProperty("bookTitle", bookTitleEntered);
//                props.setProperty("author", authorEntered);
//                props.setProperty("pubYear", pubYearEntered);
//                props.setProperty("status", "Active");
//                myModel.stateChangeRequest("BookData", props);
//            }
//
//
//
//
//        }
//
//
//
//
//
//
//    }
//
//    @Override
//    public void updateState(String key, Object value) {
//
//    }
//}
