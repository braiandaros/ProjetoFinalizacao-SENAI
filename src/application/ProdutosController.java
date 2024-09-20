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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProdutosController {
	
	@FXML
	private TextField produtonomeid;
	@FXML
	private TextField descricaoid;
	@FXML
	private TextField precoid;
	@FXML
	private TextField produtoid;
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
         
	        String bname,edition,price;
	        bname = produtonomeid.getText();
	        edition = descricaoid.getText();
	        price = precoid.getText();
	                    
	         try {
	            pst = con.prepareStatement("insert into produtos(nomeproduto, descricao, preco)values(?,?,?)");
	            pst.setString(1, bname);
	            pst.setString(2, edition);
	            pst.setString(3, price);
	            pst.executeUpdate();
	                           
	            produtonomeid.setText("");
	            descricaoid.setText("");
	            precoid.setText("");
	            produtonomeid.requestFocus();
	            table_load();
	           }

	        catch (SQLException e1) 
	            {            
	           e1.printStackTrace();
	        }
	}
	
	public void table_load() {
	    try {
	        pst = con.prepareStatement("select * from produtos");
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
		produtonomeid.setText("");
		descricaoid.setText("");
		precoid.setText("");
		produtonomeid.requestFocus();
	}
	@FXML
	private void update() {
        String bname,edition,price,bid;
        
        
        bname = produtonomeid.getText();
        edition = descricaoid.getText();
        price = precoid.getText();
        bid  = produtoid.getText();
        
         try {
                pst = con.prepareStatement("update produtos set nomeproduto = ?,descricao =?,preco =? where id =?");
                pst.setString(1, bname);
                pst.setString(2, edition);
                pst.setString(3, price);
                pst.setString(4, bid);
                pst.executeUpdate();
                table_load();
               
                produtonomeid.setText("");
                descricaoid.setText("");
                precoid.setText("");
                produtonomeid.requestFocus();
                table_load();
            }

            catch (SQLException e1) {
                
                e1.printStackTrace();
            }
	}
	@FXML
	private void delete() {
        String bid;
        bid  = produtoid.getText();
        
         try {
                pst = con.prepareStatement("delete from produtos where id =?");
        
                pst.setString(1, bid);
                pst.executeUpdate();
                table_load();
               
                produtonomeid.setText("");
                descricaoid.setText("");
                precoid.setText("");
                produtonomeid.requestFocus();
                table_load();
            }

            catch (SQLException e1) {
                
                e1.printStackTrace();
            }
	}
	@FXML
	private void search() { 
	    try {
	         
	           String id = produtoid.getText();
	
	               pst = con.prepareStatement("select nomeproduto,descricao,preco from produtos where id = ?");
	               pst.setString(1, id);
	               ResultSet rs = pst.executeQuery();
	
	           if(rs.next()==true)
	           {
	             
	               String name = rs.getString(1);
	               String edition = rs.getString(2);
	               String price = rs.getString(3);
	               
	               produtonomeid.setText(name);
	               descricaoid.setText(edition);
	               precoid.setText(price);
	
	           }   
	           else
	           {
	        	   produtonomeid.setText("");
	        	   descricaoid.setText("");
	        	   precoid.setText("");
	                
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
