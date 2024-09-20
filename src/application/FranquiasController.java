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

public class FranquiasController {
	
	@FXML
	private TextField nomeid;
	@FXML
	private TextField localid;
	@FXML
	private TextField cidadeid;
	@FXML
	private TextField cepid;
	@FXML
	private TextField rendaid;
	@FXML
	private TextField franquiaid;
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
         
	        String nome,local,cidade,cep,renda;
	        nome = nomeid.getText();
	        local = localid.getText();
	        cidade = cidadeid.getText();
	        cep = cepid.getText();
	        renda = rendaid.getText();
	                    
	         try {
	            pst = con.prepareStatement("insert into franquias(nome,local,cidade,cep,renda)values(?,?,?,?,?)");
	            pst.setString(1, nome);
	            pst.setString(2, local);
	            pst.setString(3, cidade);
	            pst.setString(4, cep);
	            pst.setString(5, renda);
	            pst.executeUpdate();
	                           
	            nomeid.setText("");
	            localid.setText("");
	            cidadeid.setText("");
	            cepid.setText("");
	            rendaid.setText("");
	            franquiaid.requestFocus();
	            table_load();
	           }

	        catch (SQLException e1) 
	            {            
	           e1.printStackTrace();
	        }
	}
	
	public void table_load() {
	    try {
	        pst = con.prepareStatement("select * from franquias");
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
		nomeid.setText("");
		localid.setText("");
		cidadeid.setText("");
		cepid.setText("");
		rendaid.setText("");
		franquiaid.requestFocus();
	}
	@FXML
	private void update() {
		
        String nome,local,cidade,cep,renda,franquia;
        nome = nomeid.getText();
        local = localid.getText();
        cidade = cidadeid.getText();
        cep = cepid.getText();
        renda = rendaid.getText();
        franquia  = franquiaid.getText();
        
         try {
                pst = con.prepareStatement("update franquias set nome = ?,local =?,cidade =?, cep = ?, renda = ? where id =?");
                pst.setString(1, nome);
                pst.setString(2, local);
                pst.setString(3, cidade);
                pst.setString(4, cep);
                pst.setString(5, renda);
                pst.setString(6, franquia);
                pst.executeUpdate();
                table_load();
                
                nomeid.setText("");
                localid.setText("");
                cidadeid.setText("");
                cepid.setText("");
                rendaid.setText("");
                franquiaid.requestFocus();
                table_load();
            }

            catch (SQLException e1) {
                
                e1.printStackTrace();
            }
	}
	@FXML
	private void delete() {
        String franquia;
        franquia  = franquiaid.getText();
        
         try {
                pst = con.prepareStatement("delete from franquias where id =?");
        
                pst.setString(1, franquia);
                pst.executeUpdate();
                table_load();
               
                nomeid.setText("");
                localid.setText("");
                cidadeid.setText("");
                cepid.setText("");
                rendaid.setText("");
                franquiaid.requestFocus();
                table_load();
            }

            catch (SQLException e1) {
                
                e1.printStackTrace();
            }
	}
	@FXML
	private void search() { 
	    try {
	         
	           String Franquia = franquiaid.getText();
	
	               pst = con.prepareStatement("select nome,local,cidade,cep,renda from franquias where id = ?");
	               pst.setString(1, Franquia);
	               ResultSet rs = pst.executeQuery();
	
	           if(rs.next()==true)
	           {
	             
	               String nome = rs.getString(1);
	               String local = rs.getString(2);
	               String cidade = rs.getString(3);
	               String cpf = rs.getString(4);
	               String renda = rs.getString(5);
	               
	               nomeid.setText(nome);
	               localid.setText(local);
	               cidadeid.setText(cidade);
	               cepid.setText(cpf);
	               rendaid.setText(renda);
	
	           }   
	           else
	           {
	        	   nomeid.setText("");
	        	   localid.setText("");
	        	   cidadeid.setText("");
	        	   cepid.setText("");
	        	   rendaid.setText("");
	                
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
