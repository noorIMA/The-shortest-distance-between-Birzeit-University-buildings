package com.example.projectalgorithm3;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    static ArrayList<Building> buildings = new ArrayList<>();
    static HashMap<String, Node> table = new HashMap<>(); //used to store the nodes representing
                                                            // each building in the graph traversal algorithm
    private static ArrayList<Edge> edges = new ArrayList<>();
    private ObservableList<String> pathBuilding = FXCollections.observableArrayList();
    private ObservableList<String> buildingList = FXCollections.observableArrayList();
    private ArrayList<Line> lines = new ArrayList<>();
    Circle circle = new Circle(2.5);
    static int counter = 0;
    TextField Distancetextfield = new TextField(); //Display the distance between buildings
    Group s = new Group();
    private ListView<String> pathListView = new ListView<>();
    Pane pane = new Pane();
    static int counterline = 0;

    @Override
    public void start(Stage stage) throws Exception {
        // Called methods
        getbuilding();
        getDistances();
        addAdjacents();

        Pane p = new Pane();
        for (int i = 0; i < buildings.size(); i++) { // To place red dots on the map at the locations of buildings
            circle = new Circle(3);             // Display xy coordinates for all buildings
            circle.setFill(Color.RED);
            circle.setCenterX(buildings.get(i).getXaxis());
            circle.setCenterY(buildings.get(i).getYaxis());
            buildings.get(i).setCircle(circle);

            p.getChildren().add(buildings.get(i).getCircle());

        }

        Pane mainpane = new Pane(); // In order to show the names of the buildings
        for (int i = 0; i < buildings.size(); i++) {
            buildingList.add(buildings.get(i).getName()); //display it in a list
        }

        mainpane.setStyle("-fx-background-color :  green"); //Making the main interface and its components
        Label sourcelabel = new Label("source");
        sourcelabel.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 20));
        sourcelabel.setTextFill(Color.BLACK);

        ComboBox<String> SourcecomboBox = new ComboBox<>(); // It contains all buildings
        SourcecomboBox.setItems(buildingList);               // In order to determine the starting point of the path
        sourcelabel.setLayoutX(700);
        sourcelabel.setLayoutY(30);
        SourcecomboBox.setPrefWidth(200);
        SourcecomboBox.setLayoutX(820);
        SourcecomboBox.setLayoutY(30);
        Label Targetlabel = new Label("Target");     //   In order to specify the end point of the path
        Targetlabel.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 20));
        Targetlabel.setTextFill(Color.BLACK);
        ComboBox<String> TargetcomboBox = new ComboBox<>(); // It contains all buildings
        TargetcomboBox.setItems(buildingList);
        Targetlabel.setLayoutX(700);
        Targetlabel.setLayoutY(80);
        TargetcomboBox.setLayoutX(820);
        TargetcomboBox.setLayoutY(80);
        TargetcomboBox.setPrefWidth(200);
        Button playButton = new Button("Play");
        playButton.setLayoutX(820);
        playButton.setLayoutY(150);

        playButton.setPrefSize(150, 70);
        playButton.setTextFill(Color.GREEN);
        playButton.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 20));

        Label pathlabel = new Label("Path");
        pathlabel.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 20));
        pathlabel.setTextFill(Color.BLACK);
        TextField pathtextfield = new TextField();
        pathlabel.setLayoutX(700);
        pathlabel.setLayoutY(250);
        pathListView.setLayoutX(820);

        pathListView.setLayoutY(280);
        pathListView.setPrefWidth(200);
        pathListView.setPrefHeight(200);
        Label Distancelabel = new Label("Distance");
        Distancelabel.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, FontPosture.ITALIC, 20));
        Distancelabel.setTextFill(Color.BLACK);

        Distancelabel.setLayoutX(700);
        Distancelabel.setLayoutY(500);
        Distancetextfield.setLayoutX(820);
        Distancetextfield.setLayoutY(520);
        Image image = new Image(new FileInputStream("map.png"));
        ImageView imageview = new ImageView(image);
        imageview.setFitWidth(600);
        imageview.setFitHeight(600);
        pane.getChildren().addAll(imageview);
        pane.getChildren().addAll(lines);

        s.getChildren().addAll(pane);
        Group ea = new Group();
        ea.getChildren().addAll(s, p);



        p.setOnMouseClicked(new EventHandler<MouseEvent>() {   //This function changes the color of the
                                                                    // dot from red to green when you click on it
            public void handle(MouseEvent event) {
                if (counter < 2) {
                    for (int i = 0; i < buildings.size(); i++) {
                        if (buildings.get(i).getXaxis() + 5 > event.getX() && buildings.get(i).getXaxis() - 5 < event.getX()
                                && buildings.get(i).getYaxis() + 5 > event.getY()
                                && buildings.get(i).getYaxis() - 5 < event.getY()) {
                            if (buildings.get(i).circle.getFill().equals(Color.RED)) {
                                counter++;
                                buildings.get(i).circle.setFill(Color.GREEN);
                                if (counter == 1) { //If the counter is equal to 1, and he puts or
                                    SourcecomboBox.setValue(buildings.get(i).getName()); // changes the name of the building inside the source lable

                                }
                                if (counter == 2) { //If the counter is equal to 2, and he puts or
                                    TargetcomboBox.setValue(buildings.get(i).getName()); // changes the name of the building inside the Target lable

                                }

                            }
                            break;
                        }
                    }

                }

            }

        });

        playButton.setOnAction(e -> {
            counterline = 0;
            Distancetextfield.clear();
            pathListView.getItems().clear();
            for (int i = 0; i < lines.size(); i++) {
                pane.getChildren().remove(lines.get(i));
            }

            Alert s;
            if (SourcecomboBox.getValue() == null) {  //checks if the value of  "SourcecomboBox" is null.
                 Alert c;
                c = new Alert(AlertType.INFORMATION);
                c.setContentText("you must select source building "); // If it is, an alert dialog box is displayed with a message
                c.showAndWait();

            }
                //The method calculates the shortest path between the two
                // buildings and returns a list of Line objects that represent the path
            if (!getBuilding(SourcecomboBox.getValue()).equals(getBuilding(TargetcomboBox.getValue()))) {
                getShortestPath(getBuilding(SourcecomboBox.getValue()), getBuilding(TargetcomboBox.getValue()));
                for (int i = 0; i < lines.size(); i++) {
                    lines.get(i).setStrokeWidth(2); //The width of each line is set to 2 pixels.
                    pane.getChildren().add(lines.get(i));
                    counterline++;

                }
            } else {//If the source building is equal to the target building,
                s = new Alert(AlertType.INFORMATION);
                s.setContentText("you choose the same building "); // an alert dialog box is displayed with a message
                s.showAndWait();

                System.out.println("erroe");
            }
            counter = 0;
            for (int i = 0; i < buildings.size(); i++) {
                if (buildings.get(i).circle.getFill() == Color.GREEN) {
                    buildings.get(i).circle.setFill(Color.RED);
                }
            }

        });

        mainpane.getChildren().addAll(playButton, sourcelabel, SourcecomboBox, Targetlabel, TargetcomboBox, pathlabel,
                pathListView, Distancelabel, Distancetextfield, ea);

        Scene scene = new Scene(mainpane, 1100, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void getShortestPath(Building sourcebuilding, Building targetbuilding) {
        //This method creates a graph of all the buildings and their adjacent buildings.
        graph(sourcebuilding, targetbuilding);

        for (int i = 0; i < lines.size(); i++) {
            s.getChildren().remove(lines.get(i));
        }
        lines.clear();

        pathBuilding.clear();

        pathListView.getItems().clear();

        Distancetextfield.setText("0.0");
            //The distance of the shortest path is displayed in a text field named "Distancetextfield".
        if (table.get(targetbuilding.getName()).getDistance() != Double.POSITIVE_INFINITY
                && table.get(targetbuilding.getName()).getDistance() != 0) {
            shortestPath(sourcebuilding, targetbuilding);// The code then adds each building in the path to the ArrayList "pathBuilding"
            Node t = table.get(targetbuilding.getName());
            Distancetextfield.setText("" + t.getDistance());

            pathListView.getItems().add(sourcebuilding.getName() + " (start) --->");

            for (int i = pathBuilding.size() - 1; i >= 0; i--) {
                if (i == 0) {
                    pathListView.getItems().add(pathBuilding.get(i) + " (end)");

                } else { // and displays the path in the list view "pathListView".
                    pathListView.getItems().add(pathBuilding.get(i) + " --->");

                }
            }
        } else { //If the target building is not adjacent to the source building,
            Alert e;
            e = new Alert(AlertType.INFORMATION);
            e.setContentText("building that you add does not have adjecnt ");// an alert dialog box is displayed with a message
            e.showAndWait();
            System.out.println("error");

        }

    }

    private void shortestPath(Building sourcebuilding, Building targetbuilding) {
        //The method first adds the name of the target building to an ArrayList named "pathBuilding".
        pathBuilding.add(targetbuilding.getName());
        Node t = table.get(targetbuilding.getName());
        if (t.getSourcebuilding() == null) {
            return;
        }
        if (t.getSourcebuilding() == sourcebuilding) {
            if (sourcebuilding != targetbuilding) {
                lines.add(new Line(t.getSourcebuilding().getXaxis(), t.getSourcebuilding().getYaxis(), targetbuilding.getXaxis(),
                        targetbuilding.getYaxis()));// the method adds a Line object to a list named "lines" that represents the path
                                                                        // between the source building and target building
                t.getSourcebuilding().circle.setFill(Color.GREEN);
                targetbuilding.circle.setFill(Color.GREEN);
            }
            return;
        }
        // A Line object representing the path between these two buildings is added to the list "lines".
        lines.add(new Line(t.getSourcebuilding().getXaxis(), t.getSourcebuilding().getYaxis(), targetbuilding.getXaxis(),
                targetbuilding.getYaxis())); // the method recursively calls itself with the source building
        shortestPath(sourcebuilding, t.getSourcebuilding());// and the source building associated with this Node object as arguments.
		t.getSourcebuilding().circle.setFill(Color.GREEN);
		targetbuilding.circle.setFill(Color.GREEN);

    }

    public static void graph(Building current, Building target) {
        for (Building i : buildings) { // creates a HashMap "table" that maps the name of each building to a Node object.
            // Each Node object is initialized with a distance of positive infinity
            table.put(i.getName(), new Node(null, i, Double.POSITIVE_INFINITY, false));
        }
        Node node = table.get(current.getName());
        ComparTO compareTo = new ComparTO();
        PriorityQueue<Node> queue = new PriorityQueue<>(10, compareTo);
        node.setDistance(0.0); //the distance of the Node object associated with the current building is set to 0.0
        node.setKnown(true);
        queue.add(node); //The Node object is then added to the queue.
        while (!queue.isEmpty()) {
            Node temp = queue.poll();// The Node object with the smallest distance value is removed from the queue
            temp.setKnown(true); //and its boolean value is set to true
            if (temp.getCurrentbuilding() == target) {
                break;
            }
            ArrayList<Adjecent> adjecnt = temp.getCurrentbuilding().getAdjacents();
            System.out.println(temp.getCurrentbuilding().getName());
            for (Adjecent A: adjecnt) {
                Node targetN = table.get(A.getBuilding().getName());

                if (targetN.isKnown()) {
                    continue; //If this Node object has already been processed, it is skipped
                }
                if (queue.contains(targetN)) {
                    queue.remove(targetN);// If this Node object is already in the queue, it is removed
                }
                double newDis = A.getDistance() + temp.getDistance(); //The distance between this Node object and the current Node object
                                                                    // is calculated and compared to its current distance value
                if (newDis < targetN.getDistance()) {
                    targetN.setSourcebuilding(temp.getCurrentbuilding());
                    targetN.setDistance(newDis); //If it is less than its current distance value,
                                    // its source building and distance value are updated

                }

                queue.add(targetN); //Node  is added to the queue.
            }

        }

    }
// reads data from a file named "Faculty.txt" and creates Building objects based on this data
    private static void getbuilding() throws FileNotFoundException {
        File file = new File("Faculty.txt"); // reads data from a file named "Faculty.txt" and creates Building objects based on this data
        Scanner input = new Scanner(file);
        if (file.exists()) {
            while (input.hasNextLine()) {
                String str = input.nextLine();
                if (str.isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] buildingData = str.trim().split(",");
                buildings.add(new Building(Double.parseDouble(buildingData[1]), Double.parseDouble(buildingData[2]), buildingData[0],
                        new Circle())); //Circle is created and associated with this Building object
                //the Building object is then added to an ArrayList named "buildings"

            }
            input.close();
        }
    }


    private static Building getBuilding(String buildingName) {

        for (int i = 0; i < buildings.size(); i++) { //compares the name of each Building object
            if (buildings.get(i).getName().equalsIgnoreCase(buildingName)) {
                return buildings.get(i); // If a match is found, the Building object is returned.
            }
        }
        return null; // If no match is found, null is returned.
    }

    private static void getDistances() throws FileNotFoundException {
        File file = new File("data.txt"); // reads data from "data.txt"
        Scanner input = new Scanner(file);
        while (input.hasNextLine()) {
            String[] spStr = input.nextLine().trim().split(",");
            //creates Edge objects based on this data
            edges.add(new Edge(getBuilding(spStr[0]), getBuilding(spStr[1]), Double.parseDouble(spStr[2])));
        }//added to an ArrayList named "edges".
        input.close();
    }

    private static void addAdjacents() {
        for (int i = 0; i < buildings.size(); i++) { //creates Adjacent objects for each Building object
            for (int j = 0; j < edges.size(); j++) { // based on the Edge objects in an ArrayList
                if (buildings.get(i).getName().equalsIgnoreCase(edges.get(j).getSourcebuilding().getName())) {
                    Adjecent n = new Adjecent(edges.get(j).getTargetbuilding(), edges.get(j).getDistance());
                    buildings.get(i).getAdjacents().add(n);
                } else if (buildings.get(i).getName().equalsIgnoreCase(edges.get(j).getTargetbuilding().getName())) {
                    Adjecent n = new Adjecent(edges.get(j).getSourcebuilding(), edges.get(j).getDistance());
                    buildings.get(i).getAdjacents().add(n);
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        launch(args);

    }


}
