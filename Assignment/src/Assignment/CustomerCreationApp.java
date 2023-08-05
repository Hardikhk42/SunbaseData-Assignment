package Assignment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CustomerCreationApp extends JFrame {

	private JPanel contentPane;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField streetField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField emailField;
    private JTextField phoneField;
    private String authToken; // Store the authentication token here

    public CustomerCreationApp(String authToken) {
        this.authToken = authToken;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 500);
         contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(10, 2, 10, 10));

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        streetField = new JTextField();
        addressField = new JTextField();
        cityField = new JTextField();
        stateField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();

        contentPane.add(new JLabel("First Name:"));
        contentPane.add(firstNameField);
        contentPane.add(new JLabel("Last Name:"));
        contentPane.add(lastNameField);
        contentPane.add(new JLabel("Street:"));
        contentPane.add(streetField);
        contentPane.add(new JLabel("Address:"));
        contentPane.add(addressField);
        contentPane.add(new JLabel("City:"));
        contentPane.add(cityField);
        contentPane.add(new JLabel("State:"));
        contentPane.add(stateField);
        contentPane.add(new JLabel("Email:"));
        contentPane.add(emailField);
        contentPane.add(new JLabel("Phone:"));
        contentPane.add(phoneField);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get customer information from fields
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String street = streetField.getText();
                String address = addressField.getText();
                String city = cityField.getText();
                String state = stateField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();

                // Call API to create customer
                createCustomer(firstName, lastName, street, address, city, state, email, phone);
            }
        });

        contentPane.add(createButton);

        setTitle("Create Customer");
        setVisible(true);
    }

    private void createCustomer(String firstName, String lastName, String street, String address, String city, String state, String email, String phone) {
        String createUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create";
        String customerJson = String.format(
                "{\"first_name\":\"%s\",\"last_name\":\"%s\",\"street\":\"%s\",\"address\":\"%s\",\"city\":\"%s\",\"state\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\"}",
                firstName, lastName, street, address, city, state, email, phone);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(createUrl))
                .header("Authorization", "Bearer " + authToken) // Prepend "Bearer " to the token
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(customerJson))
                .build();




        try {
            HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());

            if (createResponse.statusCode() == 201) {
                JOptionPane.showMessageDialog(this, "Customer created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                dispose(); // Close the window after successful creation
            } else {
                JOptionPane.showMessageDialog(this, "Error creating customer. Status code: " + createResponse.statusCode(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create an instance of CustomerCreationApp by passing the authToken
            // new CustomerCreationApp("your-auth-token").setVisible(true);
        });
    }
}
