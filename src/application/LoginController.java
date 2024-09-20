package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginController {
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	
	@FXML
	private TextField userid;
	@FXML
	private PasswordField senhaid;
	
	public void initialize() {
	    Connect();
    }
    
	public void Connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/prova", "root","");
        }
        catch (ClassNotFoundException ex) 
        {
          ex.printStackTrace();
        }
        catch (SQLException ex) 
        {
               ex.printStackTrace();
        }
    }
	
	@FXML
	public void logar() {
        String nome,senha;
        nome = userid.getText();
        senha = senhaid.getText();
        
        try {
			pst = con.prepareStatement("SELECT * FROM usuarios WHERE nome = ? AND senha = ?");
			pst.setString(1, nome);
			pst.setString(2, senha);
			
			rs = pst.executeQuery();

	        if (rs.next()) {
	        	paginaAtual("Menu.fxml");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void paginaAtual(String Pagina) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Pagina));
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource(Pagina));

			Scene currentScene = senhaid.getScene();
			currentScene.setRoot(root);
			
			currentScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}