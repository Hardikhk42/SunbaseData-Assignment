package Assignment;

import javax.swing.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginApp extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginApp() {
        setTitle("Integration App - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        initializeUI();
    }

    private void initializeUI() {
    	 JPanel mainPanel = new JPanel(new GridLayout(3, 2));
         JLabel usernameLabel = new JLabel("Username:");
         JLabel passwordLabel = new JLabel("Password:");
         usernameField = new JTextField();
         passwordField = new JPasswordField();
         loginButton = new JButton("Login");

         mainPanel.add(usernameLabel);
         mainPanel.add(usernameField);
         mainPanel.add(passwordLabel);
         mainPanel.add(passwordField);
         mainPanel.add(new JLabel()); // Empty label for spacing
         mainPanel.add(loginButton);

         add(mainPanel);

        // Action Listener for Login Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                String authToken = authenticateUser(username, password);

                if (authToken != null) {
                    SwingUtilities.invokeLater(() -> {
                        new GetCustomerList(authToken).setVisible(true);
                        dispose(); // Close the login window
                    });
                } else {
                    JOptionPane.showMessageDialog(LoginApp.this, "Authentication failed. Please check your credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private String authenticateUser(String username, String password) {
    	  try {
              String authUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
              String authBody = "{\"login_id\": \"" + username + "\", \"password\": \"" + password + "\"}";

              HttpClient client = HttpClient.newHttpClient();

              HttpRequest authRequest = HttpRequest.newBuilder()
                      .uri(new URI(authUrl))
                      .header("Content-Type", "application/json")
                      .POST(HttpRequest.BodyPublishers.ofString(authBody))
                      .build();

              HttpResponse<String> authResponse = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

              if (authResponse.statusCode() == 200) {
                  // Extract the token from the JSON response
                  String responseBody = authResponse.body();
                  String token = extractTokenFromResponse(responseBody);
                  return token;
              }

              return null;
          } catch (Exception ex) {
              ex.printStackTrace();
              return null;
          }
      }

      private String extractTokenFromResponse(String responseBody) {
          try {
              // Parse the JSON response and extract the token
              JSONObject jsonObject = new JSONObject(responseBody);
              String token = jsonObject.getString("access_token");
              return token;
          } catch (JSONException e) {
              e.printStackTrace();
              return null;
          }
      }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginApp().setVisible(true);
        });
    }
}
