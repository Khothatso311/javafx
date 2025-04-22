package com.example.vehicle;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HelloApplication extends Application {
    public class DBUtil {
        private static final String URL = "jdbc:mysql://localhost:3306/vehicle_rental";
        private static final String USER = "root";
        private static final String PASSWORD = "123456";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    // Hardcoded credentials (username and password)
    static final String ADMIN_USERNAME = "admin";
    static final String ADMIN_PASSWORD = "admin_password";
    static final String EMPLOYEE_USERNAME = "employee";
    static final String EMPLOYEE_PASSWORD = "employee_password";
    static final String CUSTOMER_USERNAME = "customer";
    static final String CUSTOMER_PASSWORD = "customer_password";


    private final List<Vehicle> vehicles = new ArrayList<>();  // List to store vehicles
    private final List<Customer> customers = new ArrayList<>();  // List to store customers
    private final List<Booking> bookings = new ArrayList<>();


    @Override
    public void start(Stage stage) {
        // Test data (remove later)
        vehicles.add(new Vehicle(1, "Toyota", "Camry", "Sedan", 50.0, true));
        int id = 1234;
        customers.add(new Customer(id, "Anthonia", "69068730", "DL12345"));

        showLoginScreen(stage);
    }

    public void showLoginScreen(Stage stage) {
        // Create labels, fields, and buttons
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Login");
        Label statusLabel = new Label();

        // Add action handler for login button
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            String role = checkLogin(username, password); // Check login credentials

            if (role != null) {
                statusLabel.setText("Login successful as " + role);
                statusLabel.setStyle("-fx-text-fill: #28A745;");  // Green text for success

                // Show dashboard based on role
                if (role.equals("admin")) {
                    showAdminDashboard(stage);
                } else if (role.equals("employee")) {
                    showEmployeeDashboard(stage);
                } else if (role.equals("customer")) {
                    showCustomerDashboard(stage);
                }
            } else {
                statusLabel.setText("Invalid username or password.");
                statusLabel.setStyle("-fx-text-fill: #DC3545;");  // Red text for failure
            }
        });

        // Create HBox for username and password fields
        HBox userHBox = new HBox(10, userLabel, userField);
        HBox passHBox = new HBox(10, passLabel, passField);

        // Apply left alignment
        userHBox.setAlignment(Pos.CENTER_LEFT);
        passHBox.setAlignment(Pos.CENTER_LEFT);

        // Create main VBox layout
        VBox vbox = new VBox(20, userHBox, passHBox, loginButton, statusLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(40));
        vbox.setStyle("-fx-background-color: beige;"); // Light background for the form

        // Styling
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #343A40;");
        passLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #343A40;");
        userField.setStyle("-fx-border-color: #007BFF; -fx-border-radius: 5; -fx-padding: 7;");
        passField.setStyle("-fx-border-color: #007BFF; -fx-border-radius: 5; -fx-padding: 7;");
        loginButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;"); // Bootstrap primary color
        statusLabel.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");

        // Set the scene and stage
        Scene scene = new Scene(vbox, 400, 300);
        stage.setTitle("Vehicle Rental Login");
        stage.setScene(scene);
        stage.show();
    }

    // Check login credentials
    public String checkLogin(String username, String password) {
        // Placeholder hardcoded credentials (use a secure way to handle passwords in real-world apps)
        final String ADMIN_USERNAME = "admin";
        final String ADMIN_PASSWORD = "admin123";
        final String EMPLOYEE_USERNAME = "employee";
        final String EMPLOYEE_PASSWORD = "employee123";
        final String CUSTOMER_USERNAME = "customer";
        final String CUSTOMER_PASSWORD = "customer123";

        // Match credentials and return role
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return "admin";
        } else if (username.equals(EMPLOYEE_USERNAME) && password.equals(EMPLOYEE_PASSWORD)) {
            return "employee";
        } else if (username.equals(CUSTOMER_USERNAME) && password.equals(CUSTOMER_PASSWORD)) {
            return "customer";
        }
        return null;  // Invalid login
    }


    public void showAdminDashboard(Stage stage) {
        VBox vbox = new VBox(15); // Spacing between components

        // Title Label
        Label adminLabel = new Label("Welcome Admin! You have full access.");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2E86C1;");

        // Buttons
        Button manageVehiclesButton = new Button("Manage Vehicles");
        Button manageCustomersButton = new Button("Manage Customers");
        Button manageBookingsButton = new Button("Manage Bookings");
        Button backButton = new Button("Logout");

        // Set actions
        manageVehiclesButton.setOnAction(e -> showVehicleManagementScreen(stage));
        manageCustomersButton.setOnAction(e -> showCustomerManagementScreen(stage));
        manageBookingsButton.setOnAction(e -> showBookingManagementScreen(stage));

        // Logout action with confirmation
        backButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText("Are you sure you want to logout?");
            alert.setContentText("You will need to log in again to access the system.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                showLoginScreen(stage);
            }
        });

        // Set button widths to align
        double buttonWidth = 200;
        manageVehiclesButton.setPrefWidth(buttonWidth);
        manageCustomersButton.setPrefWidth(buttonWidth);
        manageBookingsButton.setPrefWidth(buttonWidth);
        backButton.setPrefWidth(buttonWidth);

        // VBox alignment
        vbox.setAlignment(Pos.CENTER); // Centered alignment for better aesthetics
        vbox.setPadding(new Insets(30));
        vbox.getChildren().addAll(
                adminLabel,
                manageVehiclesButton,
                manageCustomersButton,
                manageBookingsButton,
                backButton
        );

        // Scene and Stage setup
        Scene scene = new Scene(vbox, 450, 350);
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private void showCustomerManagementScreen(Stage stage) {
        // Create layout
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        // Title label
        Label customerLabel = new Label("Manage Customers");
        customerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2E86C1;");

        // Creating the form for customer data input
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer name");

        Label contactLabel = new Label("Contact Info:");
        TextField contactField = new TextField();
        contactField.setPromptText("Enter contact number");

        Label licenseLabel = new Label("Driving License Number:");
        TextField licenseField = new TextField();
        licenseField.setPromptText("Enter license number");

        // Buttons for actions
        Button registerButton = new Button("Register Customer");
        Button updateButton = new Button("Update Customer");
        Button viewButton = new Button("View All Customers");
        Button backButton = new Button("Back");  // Back button

        // Set button styles through classes instead of inline
        registerButton.getStyleClass().add("button");
        registerButton.getStyleClass().add("register");

        updateButton.getStyleClass().add("button");
        updateButton.getStyleClass().add("update");

        viewButton.getStyleClass().add("button");
        viewButton.getStyleClass().add("view");

        backButton.getStyleClass().add("button");
        backButton.getStyleClass().add("back");

        // Text area for displaying results or errors
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(150);
        resultArea.setWrapText(true);
        resultArea.getStyleClass().add("text-area");

        // Register customer action (code unchanged)
        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String contact = contactField.getText();
            String license = licenseField.getText();

            if (name.isEmpty() || contact.isEmpty() || license.isEmpty()) {
                resultArea.appendText("All fields are required.\n");
            } else {
                try (Connection conn = DBUtil.getConnection()) {
                    String sql = "INSERT INTO customers (name, contact_info, driving_license) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, name);
                    stmt.setString(2, contact);
                    stmt.setString(3, license);
                    stmt.executeUpdate();
                    resultArea.appendText("Customer saved to database.\n");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    resultArea.appendText("Error: " + ex.getMessage() + "\n");
                }
                // Clear the input fields
                nameField.clear();
                contactField.clear();
                licenseField.clear();
            }
        });

        // Update customer action (Placeholder logic)
        updateButton.setOnAction(e -> {
            resultArea.appendText("Update functionality not implemented yet.\n");
        });

        // View customers action (Placeholder logic)
        viewButton.setOnAction(e -> {
            resultArea.appendText("View functionality not implemented yet.\n");
        });

        // Back button action
        backButton.setOnAction(e -> {
            showAdminDashboard(stage);
        });
        // Add button for exporting customers to CSV
        Button exportCSVButton = new Button("Export Customers to CSV");
        exportCSVButton.setOnAction(e -> exportCustomersToCSV());

        // Add all components to the layout
        vbox.getChildren().addAll(customerLabel, nameLabel, nameField, contactLabel,
                contactField, licenseLabel, licenseField,
                registerButton, updateButton, viewButton, backButton, resultArea);

        // Create scene and set stage
        Scene scene = new Scene(vbox, 400, 450);

        // Load and apply CSS
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("Customer Management");
        stage.setScene(scene);
        stage.show();
    }
    private void exportCustomersToCSV() {
        String csvFile = "customers.csv"; // Define your CSV file path

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            // Write header
            writer.write("Name,Contact Info,Driving License Number");
            writer.newLine(); // Move to the next line

            // Write customer data
            for (Customer customer : customers) {
                writer.write(String.format("%s,%s,%s", customer.getName(), customer.getContactInfo(), customer.getDrivingLicenseNumber()));
                writer.newLine(); // Move to the next line after each customer
            }

            System.out.println("Customers have been exported to " + csvFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to export customers to CSV.");
        }
    }

    public void showEmployeeDashboard(Stage stage) {
        // Welcome label with custom style
        Label label = new Label("Welcome Employee! Limited access granted.");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;"); // Dark grey text for the label

        // Create buttons for the features available to employees
        Button viewVehiclesButton = new Button("View Available Vehicles");
        viewVehiclesButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px;");

        Button viewBookingsButton = new Button("View Bookings");
        viewBookingsButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px;");

        Button addVehicleButton = new Button("Add/Modify Vehicle");
        addVehicleButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px;");

        // Actions for each button
        viewVehiclesButton.setOnAction(e -> showAvailableVehicles(stage));
        viewBookingsButton.setOnAction(e -> showBookings(stage));
        addVehicleButton.setOnAction(e -> showAddModifyVehicle(stage));

        // Back Button (Logout)
        Button backButton = new Button("Logout");
        backButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px;");
        backButton.setOnAction(e -> showLoginScreen(stage));

        // Add all components to the VBox
        VBox vbox = new VBox(20, label, viewVehiclesButton, viewBookingsButton, addVehicleButton, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        // Set a gradient background for the VBox
        vbox.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #E0F7FA, #B2EBF2);"); // Light blue gradient

        // Create a scene and set it on the stage
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Employee Dashboard"); // Set the title for the window
        stage.show();
    }

    // Example placeholder for viewing available vehicles
    private void showAvailableVehicles(Stage stage) {
        // Title label with inline styling
        Label label = new Label("List of Available Vehicles");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4A4A4A;"); // Dark grey color for the label

        // Back button with inline styling
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;"); // Dodger Blue with white text
        backButton.setOnAction(e -> showEmployeeDashboard(stage));  // Go back to the employee dashboard

        // VBox with padding, alignment, and background color
        VBox vbox = new VBox(20, label, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #F0EAD6;"); // Light beige background color for the VBox

        // Create scene and set the stage
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    // Example placeholder for viewing bookings
    private void showBookings(Stage stage) {
        Label label = new Label("List of Bookings");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4A4A4A;");

        // Sample data for bookings (You would typically fetch this from your data source)
        List<String> bookings = List.of("Booking 1 - John Doe", "Booking 2 - Jane Smith", "Booking 3 - Bob Johnson");

        // ListView to display the bookings
        ListView<String> bookingsListView = new ListView<>();
        bookingsListView.getItems().addAll(bookings);
        bookingsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Display booking details in an alert when a booking is selected
        bookingsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Booking Details");
                alert.setHeaderText(null);
                alert.setContentText("Details of " + newValue);
                alert.showAndWait();
            }
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;");
        backButton.setOnAction(e -> showEmployeeDashboard(stage));  // Go back to the employee dashboard

        // VBox Layout
        VBox vbox = new VBox(20, label, bookingsListView, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #F0EAD6;"); // Light beige background

        // Create and set scene
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    // Example placeholder for adding or modifying vehicles
    private void showAddModifyVehicle(Stage stage) {
        Label label = new Label("Add or Modify Vehicle");

        // Create input fields for vehicle details
        TextField makeField = new TextField();
        makeField.setPromptText("Enter vehicle make");

        TextField modelField = new TextField();
        modelField.setPromptText("Enter vehicle model");

        ComboBox<String> availabilityBox = new ComboBox<>();
        availabilityBox.getItems().addAll("Available", "Not Available");
        availabilityBox.setValue("Available");

        Button saveButton = new Button("Save Vehicle");
        saveButton.setOnAction(e -> {
            String make = makeField.getText();
            String model = modelField.getText();
            String availability = availabilityBox.getValue();
            // Save the vehicle to the system (e.g., add to a database or list)
            System.out.println("Saved Vehicle: " + make + " " + model + " - " + availability);
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showEmployeeDashboard(stage));  // Go back to the employee dashboard

        VBox vbox = new VBox(20, label, makeField, modelField, availabilityBox, saveButton, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }


    // Manage Vehicles Screen
    public void showVehicleManagementScreen(Stage stage) {
        // Create Labels and Fields
        Label vehicleLabel = new Label("Manage Vehicles");
        vehicleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A4A4A;"); // Dark grey for title

        Label vehicleIdLabel = new Label("Vehicle ID:");
        vehicleIdLabel.setStyle("-fx-text-fill: #333333;"); // Dark grey for labels
        TextField vehicleIdField = new TextField();

        Label brandLabel = new Label("Brand:");
        brandLabel.setStyle("-fx-text-fill: #333333;"); // Dark grey for labels
        TextField brandField = new TextField();

        Label modelLabel = new Label("Model:");
        modelLabel.setStyle("-fx-text-fill: #333333;"); // Dark grey for labels
        TextField modelField = new TextField();

        Label categoryLabel = new Label("Category:");
        categoryLabel.setStyle("-fx-text-fill: #333333;"); // Dark grey for labels
        TextField categoryField = new TextField();

        Label priceLabel = new Label("Rental Price per Day:");
        priceLabel.setStyle("-fx-text-fill: #333333;"); // Dark grey for labels
        TextField priceField = new TextField();

        // Buttons with inline styling
        Button addButton = new Button("Add Vehicle");
        addButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;"); // Green color for add

        Button updateButton = new Button("Update Vehicle");
        updateButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;"); // Orange for update

        Button viewButton = new Button("View All Vehicles");
        viewButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;"); // Blue for view

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-border-color: #007BFF; -fx-background-color: #FFFFFF; -fx-border-radius: 5;"); // Blue border and white background for readability

        // Add Vehicle functionality
        addButton.setOnAction(e -> {
            try {
                int vehicleId = Integer.parseInt(vehicleIdField.getText());
                String brand = brandField.getText();
                String model = modelField.getText();
                String category = categoryField.getText();
                double price = Double.parseDouble(priceField.getText());

                Vehicle newVehicle = new Vehicle(vehicleId, brand, model, category, price, true);  // Set vehicle as available
                vehicles.add(newVehicle);
                resultArea.appendText("Vehicle added: " + newVehicle + "\n");
            } catch (NumberFormatException ex) {
                resultArea.appendText("Invalid input. Please enter valid values.\n");
            }
        });

        // Update Vehicle functionality
        updateButton.setOnAction(e -> {
            try {
                int vehicleId = Integer.parseInt(vehicleIdField.getText());
                Vehicle vehicleToUpdate = findVehicleById(vehicleId);
                if (vehicleToUpdate != null) {
                    vehicleToUpdate.setBrand(brandField.getText());
                    vehicleToUpdate.setModel(modelField.getText());
                    vehicleToUpdate.setCategory(categoryField.getText());
                    vehicleToUpdate.setRentalPricePerDay(Double.parseDouble(priceField.getText()));

                    resultArea.appendText("Vehicle updated: " + vehicleToUpdate + "\n");
                } else {
                    resultArea.appendText("Vehicle not found.\n");
                }
            } catch (NumberFormatException ex) {
                resultArea.appendText("Invalid input. Please enter valid values.\n");
            }
        });

        // View All Vehicles functionality
        viewButton.setOnAction(e -> {
            resultArea.clear();
            for (Vehicle vehicle : vehicles) {
                resultArea.appendText(vehicle + "\n");
            }
        });

        // Back Button with styling
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;"); // Red for back
        backButton.setOnAction(e -> showAdminDashboard(stage));

        // VBox layout with padding and background color
        VBox vbox = new VBox(15, vehicleLabel, vehicleIdLabel, vehicleIdField, brandLabel, brandField, modelLabel, modelField,
                categoryLabel, categoryField, priceLabel, priceField, addButton, updateButton, viewButton, resultArea, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #F0EAD6;"); // Light beige background for the VBox

        Scene scene = new Scene(vbox, 400, 600);
        stage.setTitle("Vehicle Management");
        stage.setScene(scene);
        stage.show();
    }

    // Helper method to find a vehicle by ID
    private Vehicle findVehicleById(int vehicleId) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId() == vehicleId) {
                return vehicle;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Vehicle class
    public static class Vehicle {
        private final int vehicleId;
        private String brand;
        private String model;
        private String category;
        private double rentalPricePerDay;
        private boolean availabilityStatus;

        public Vehicle(int vehicleId, String brand, String model, String category, double rentalPricePerDay, boolean availabilityStatus) {
            this.vehicleId = vehicleId;
            this.brand = brand;
            this.model = model;
            this.category = category;
            this.rentalPricePerDay = rentalPricePerDay;
            this.availabilityStatus = availabilityStatus;
        }

        public int getVehicleId() {
            return vehicleId;
        }

        public String getBrand() {
            return brand;
        }

        public String getModel() {
            return model;
        }

        public String getCategory() {
            return category;
        }

        public double getRentalPricePerDay() {
            return rentalPricePerDay;
        }

        public boolean isAvailabilityStatus() {
            return availabilityStatus;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setRentalPricePerDay(double rentalPricePerDay) {
            this.rentalPricePerDay = rentalPricePerDay;
        }

        public void setAvailabilityStatus(boolean availabilityStatus) {
            this.availabilityStatus = availabilityStatus;
        }

        @Override
        public String toString() {
            return "Vehicle[ID=" + vehicleId + ", Brand=" + brand + ", Model=" + model + ", Category=" + category + ", Rental Price=" + rentalPricePerDay + ", Available=" + availabilityStatus + "]";
        }
    }

    // Customer class for storing customer data
    public static class Customer {
        private String name;
        private String contactInfo;
        private String drivingLicenseNumber;
        private final List<String> rentalHistory;

        public Customer(int id, String name, String contactInfo, String drivingLicenseNumber) {
            this.name = name;
            this.contactInfo = contactInfo;
            this.drivingLicenseNumber = drivingLicenseNumber;
            this.rentalHistory = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public String getContactInfo() {
            return contactInfo;
        }

        public String getDrivingLicenseNumber() {
            return drivingLicenseNumber;
        }

        public List<String> getRentalHistory() {
            return rentalHistory;
        }

        public void addRental(String vehicle) {
            rentalHistory.add(vehicle);
        }

        public void updateCustomerInfo(String name, String contactInfo, String drivingLicenseNumber) {
            this.name = name;
            this.contactInfo = contactInfo;
            this.drivingLicenseNumber = drivingLicenseNumber;
        }

        @Override
        public String toString() {
            return "Customer[Name=" + name + ", Contact=" + contactInfo + ", License=" + drivingLicenseNumber + "]";
        }
    }

    // Booking class
    public static class Booking {
        private final Customer customer;
        private final Vehicle vehicle;
        private String startDate;
        private String endDate;

        public Booking(Customer customer, Vehicle vehicle, String startDate, String endDate) {
            this.customer = customer;
            this.vehicle = vehicle;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        @Override
        public String toString() {
            return "Booking[Customer=" + customer.getName() + ", Vehicle=" + vehicle.getBrand() + " " + vehicle.getModel() +
                    ", From=" + startDate + ", To=" + endDate + "]";
        }

    }

    public void showBookingManagementScreen(Stage stage) {
        Label title = new Label("Booking System");

        // Customer name input
        Label customerNameLabel = new Label("Customer Name:");
        TextField customerNameField = new TextField();

        // Vehicle ID input
        Label vehicleIdLabel = new Label("Vehicle ID:");
        TextField vehicleIdField = new TextField();

        // Rental dates
        Label startLabel = new Label("Start Date (YYYY-MM-DD):");
        TextField startDateField = new TextField();

        Label endLabel = new Label("End Date (YYYY-MM-DD):");
        TextField endDateField = new TextField();

        // Buttons
        Button createBookingButton = new Button("Create Booking");
        Button viewBookingsButton = new Button("View All Bookings");
        Button cancelBookingButton = new Button("Cancel Booking");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        // Create Booking
        createBookingButton.setOnAction(e -> {
            String customerName = customerNameField.getText();
            int vehicleId;
            try {
                vehicleId = Integer.parseInt(vehicleIdField.getText());
            } catch (NumberFormatException ex) {
                outputArea.appendText("Invalid Vehicle ID.\n");
                return;
            }

            String startDate = startDateField.getText();
            String endDate = endDateField.getText();

            // ** Fix this part: You may need to validate customer and vehicle properly **
            Customer customer = findCustomerByName(customerName);
            Vehicle vehicle = findVehicleById(vehicleId);

            if (customer != null && vehicle != null && vehicle.isAvailabilityStatus()) {
                // Check for date overlap
                boolean dateConflict = false;
                for (Booking booking : bookings) {
                    if (booking.getVehicle().equals(vehicle) &&
                            isDateOverlap(startDate, endDate, booking.getStartDate(), booking.getEndDate())) {
                        dateConflict = true;
                        break;
                    }
                }

                if (dateConflict) {
                    outputArea.appendText("Booking conflict: Vehicle already booked for this period.\n");
                } else {
                    Booking newBooking = new Booking(customer, vehicle, startDate, endDate);
                    bookings.add(newBooking);
                    vehicle.setAvailabilityStatus(false); // Mark vehicle as unavailable
                    outputArea.appendText("Booking created: " + newBooking + "\n");
                }
            } else {
                outputArea.appendText("Invalid customer or vehicle unavailable.\n");
            }
        });

        // View Bookings
        viewBookingsButton.setOnAction(e -> {
            outputArea.clear();
            if (bookings.isEmpty()) {
                outputArea.appendText("No bookings made yet.\n");
            } else {
                for (Booking booking : bookings) {
                    outputArea.appendText(booking + "\n");
                }
            }
        });

        // Cancel Booking
        cancelBookingButton.setOnAction(e -> {
            String customerName = customerNameField.getText();
            int vehicleId;
            try {
                vehicleId = Integer.parseInt(vehicleIdField.getText());
            } catch (NumberFormatException ex) {
                outputArea.appendText("Invalid Vehicle ID.\n");
                return;
            }

            // ** Fix this part: Add logic to cancel booking properly **
            Customer customer = findCustomerByName(customerName);
            Vehicle vehicle = findVehicleById(vehicleId);

            if (customer != null && vehicle != null) {
                Booking foundBooking = null;
                for (Booking booking : bookings) {
                    if (booking.getCustomer().equals(customer) && booking.getVehicle().equals(vehicle)) {
                        foundBooking = booking;
                        break;
                    }
                }
                if (foundBooking != null) {
                    bookings.remove(foundBooking);
                    vehicle.setAvailabilityStatus(true); // Mark vehicle as available again
                    outputArea.appendText("Booking canceled: " + foundBooking + "\n");
                } else {
                    outputArea.appendText("Booking not found.\n");
                }
            }
        });

        // Layout the Booking UI
        VBox vbox = new VBox(10, title, customerNameLabel, customerNameField, vehicleIdLabel, vehicleIdField,
                startLabel, startDateField, endLabel, endDateField, createBookingButton,
                viewBookingsButton, cancelBookingButton, outputArea);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 400, 500);
        stage.setTitle("Booking Management");
        stage.setScene(scene);
    }

    // Helper function to check if booking dates overlap
    private boolean isDateOverlap(String startDate1, String endDate1, String startDate2, String endDate2) {
        LocalDate start1 = LocalDate.parse(startDate1);
        LocalDate end1 = LocalDate.parse(endDate1);
        LocalDate start2 = LocalDate.parse(startDate2);
        LocalDate end2 = LocalDate.parse(endDate2);

        return (start1.isBefore(end2) && end1.isAfter(start2));
    }


    private Customer findCustomerByName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            System.out.println("Customer name is null or empty.");
            return null;
        }

        System.out.println("Searching for customer in DB: " + customerName);

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM customers WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerName.trim());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Found the customer
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String contact = rs.getString("contact");
                String license = rs.getString("license");

                System.out.println("Customer found in DB: " + name);
                return new Customer(id, name, contact, license); // Assuming your Customer class has this constructor
            } else {
                System.out.println("Customer not found in DB.");
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void showCustomerDashboard(Stage stage) {
        // Welcome label with custom style
        Label label = new Label("Welcome Customer!");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;"); // Increased font size for emphasis

        // Feedback label for messages (e.g., booking confirmation or logout success)
        Label feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: green;");

        // Button for booking a vehicle with custom style
        Button bookVehicleButton = new Button("Book a Vehicle");
        bookVehicleButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-min-width: 150px; -fx-pref-width: 150px;");
        bookVehicleButton.setOnAction(e -> {
            feedbackLabel.setText(""); // Clear previous feedback
            showBookingManagementScreen(stage);  // Reuse booking screen
        });

        // Button for logging out with custom style
        Button backButton = new Button("Log Out");
        backButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-min-width: 150px; -fx-pref-width: 150px;");
        backButton.setOnAction(e -> {
            feedbackLabel.setText("Successfully logged out.");
            showLoginScreen(stage);  // Assuming you have showLoginScreen implemented
        });

        // Create layout and add buttons, label, and feedback
        VBox vbox = new VBox(20, label, bookVehicleButton, backButton, feedbackLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        // Set a gradient background for the VBox
        vbox.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #E0F7FA, #B2EBF2);"); // Light blue gradient background

        // Set scene and apply inline styles
        Scene scene = new Scene(vbox, 400, 300);

        stage.setScene(scene);
        stage.setTitle("Customer Dashboard");  // Title for the window
        stage.show();  // Show the stage
    }
}


