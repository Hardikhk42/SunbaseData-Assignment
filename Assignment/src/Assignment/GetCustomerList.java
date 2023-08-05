package Assignment;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class GetCustomerList extends JFrame {
    private JTextArea responseTextArea;
    	private String token;
    public GetCustomerList(String authToken) {
    	this.token  = authToken;
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 400);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        responseTextArea = new JTextArea();
        responseTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(responseTextArea);
        scrollPane.setBounds(5, 5, 476, 332);
        contentPane.add(scrollPane);

        JButton retrieveButton = new JButton("Retrieve Customer List");
       
    
        retrieveButton.setBounds(227, 337, 254, 21);
    
        retrieveButton.addActionListener(e -> retrieveCustomerList(authToken));
        contentPane.add(retrieveButton);
        
        JButton btnAddNewCustomer = new JButton("Add New Customer");
        btnAddNewCustomer.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new CustomerCreationApp(token).setVisible(true);
        	}
        });
        btnAddNewCustomer.setBounds(5, 337, 222, 21);
        contentPane.add(btnAddNewCustomer);
    }

    private void retrieveCustomerList(String authToken) {
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + authToken)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JSONArray customerList = new JSONArray(responseBody);

                // Clear the text area before adding the new data
                responseTextArea.setText("");

                // Append each customer's information to the text area
                for (int i = 0; i < customerList.length(); i++) {
                    JSONObject customer = customerList.getJSONObject(i);
                    String customerInfo = String.format(
                            "Name: %s %s\nStreet: %s\nAddress: %s\nCity: %s\nState: %s\nEmail: %s\nPhone: %s\n\n",
                            customer.getString("first_name"), customer.getString("last_name"),
                            customer.getString("street"), customer.getString("address"),
                            customer.getString("city"), customer.getString("state"),
                            customer.getString("email"), customer.getString("phone"));
                    responseTextArea.append(customerInfo);
                }
            } else {
                responseTextArea.setText("Error: " + response.statusCode());
            }
        } catch (Exception ex) {
            responseTextArea.setText("An error occurred: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            String authToken = "your_bearer_token_here"; // Replace with actual token
//            new GetCustomerList(authToken).setVisible(true);
        });
    }
}
