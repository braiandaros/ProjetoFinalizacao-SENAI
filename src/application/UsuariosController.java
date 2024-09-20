package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UsuariosController {
	
	@FXML
	private TextField usuarionameid;
	@FXML
	private TextField emailid;
	@FXML
	private PasswordField senhaid;
	@FXML
	private TextField cpfid;
	@FXML
	private TextField usuarioid;
	@FXML
	private TableColumn ident;
	@FXML
	private TableView tabelaid;
	@FXML
	private Button btnid;
	
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	
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
	private void save() {
	    // Assuming Connect() establishes a connection and initializes 'con'
         
	        String Uname,email,senha, cpf;
	        Uname = usuarionameid.getText();
	        email = emailid.getText();
	        senha = senhaid.getText();
	        cpf = cpfid.getText();
	                    
	         try {
	            pst = con.prepareStatement("insert into usuarios(nome,email,senha,cpf)values(?,?,?,?)");
	            pst.setString(1, Uname);
	            pst.setString(2, email);
	            pst.setString(3, senha);
	            pst.setString(4, cpf);
	            pst.executeUpdate();
	                           
	            usuarionameid.setText("");
	            emailid.setText("");
	            senhaid.setText("");
	            cpfid.setText("");
	            usuarioid.requestFocus();
	            table_load();
	           }

	        catch (SQLException e1) 
	            {            
	           e1.printStackTrace();
	        }
	}
	
	public void table_load() {
	    try {
	        pst = con.prepareStatement("select * from usuarios");
	        rs = pst.executeQuery();

	        // Create ObservableList to store data
	        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

	        // Assuming 'table' is an instance of TableView in your class
	        tabelaid.getColumns().clear();

	        // Retrieve metadata (column names) from the ResultSet
	        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
	            final int j = i;
	            TableColumn<ObservableList<String>, String> col = new TableColumn<>(rs.getMetaData().getColumnName(i + 1));
	            col.setCellValueFactory(param -> {
	                return new ReadOnlyObjectWrapper<>(param.getValue().get(j));
	            });
	            tabelaid.getColumns().addAll(col);
	        }

	        // Retrieve data from the ResultSet
	        while (rs.next()) {
	            ObservableList<String> row = FXCollections.observableArrayList();
	            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
	                row.add(rs.getString(i));
	            }
	            data.add(row);
	        }

	        // Set the items in the TableView
	        tabelaid.setItems(data);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
    public void initialize() {
        // This method is automatically called when the FXML file is loaded
	    Connect();
        // Call the table_load method to populate the table
        table_load();

        // Other initialization logic...
    }
	
	@FXML
	private void exit() {
		paginaAtual("Menu.fxml");
	}
	
	@FXML
	private void clear() {
		usuarionameid.setText("");
		emailid.setText("");
		senhaid.setText("");
		cpfid.setText("");
		usuarioid.requestFocus();
	}
	@FXML
	private void update() {
        String uname,email,senha,uid,cpf;
        
        
        uname = usuarionameid.getText();
        email = emailid.getText();
        senha = senhaid.getText();
        cpf = cpfid.getText();
        uid  = usuarioid.getText();
        
         try {
                pst = con.prepareStatement("update usuarios set nome = ?,email =?,senha =?, cpf = ? where id =?");
                pst.setString(1, uname);
                pst.setString(2, email);
                pst.setString(3, senha);
                pst.setString(4, cpf);
                
                pst.setString(5, uid);
                pst.executeUpdate();
                table_load();
               
                usuarionameid.setText("");
                emailid.setText("");
                senhaid.setText("");
                cpfid.setText("");
                usuarioid.requestFocus();
                table_load();
            }

            catch (SQLException e1) {
                
                e1.printStackTrace();
            }
	}
	@FXML
	private void delete() {
        String uid;
        uid  = usuarioid.getText();
        
         try {
                pst = con.prepareStatement("delete from usuarios where id =?");
        
                pst.setString(1, uid);
                pst.executeUpdate();
                table_load();
               
                usuarionameid.setText("");
                emailid.setText("");
                senhaid.setText("");
                cpfid.setText("");
                usuarioid.requestFocus();
                table_load();
            }

            catch (SQLException e1) {
                
                e1.printStackTrace();
            }
	}
	@FXML
	private void search() { 
	    try {
	         
	           String id = usuarioid.getText();
	
	               pst = con.prepareStatement("select nome,email,senha,cpf from usuarios where id = ?");
	               pst.setString(1, id);
	               ResultSet rs = pst.executeQuery();
	
	           if(rs.next()==true)
	           {
	             
	               String nome = rs.getString(1);
	               String email = rs.getString(2);
	               String senha = rs.getString(3);
	               String cpf = rs.getString(4);
	               
	               usuarionameid.setText(nome);
	               emailid.setText(email);
	               senhaid.setText(senha);
	               cpfid.setText(cpf);
	
	           }   
	           else
	           {
	        	   usuarionameid.setText("");
	        	   emailid.setText("");
	        	   senhaid.setText("");
	        	   cpfid.setText("");
	                
	           }
	       } 
	   
	    catch (SQLException ex) {
	          
	       }
       
	}
	private void paginaAtual(String Pagina) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Pagina));
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource(Pagina));

			Scene currentScene = btnid.getScene();
			currentScene.setRoot(root);
			
			currentScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
