package fr.pizzeria.dao;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


import fr.pizzeria.model.Pizza;

public class PizzaDbDao implements IPizzaDao {
	String userDB = null;
	String urlDB = null;
	String passDB = null;
	String driver = null;
	Connection connection = null;
	private List<Pizza> pizzas = null;


	public PizzaDbDao(){
		this.getProperties();
		try {
			Class.forName(this.driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.initializationDB();
	}


	public void getProperties(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			String filename = "jdbc.properties";
			input = new FileInputStream(filename);
			prop.load(input);
			this.userDB = prop.getProperty("dbuser");
			this.urlDB = prop.getProperty("database");
			this.passDB = prop.getProperty("dbpassword");
			this.driver = prop.getProperty("driverdb");
		}
		catch (IOException ex) {

			System.out.println("Sorry, unable to find " + "jdbc.properties");
			ex.printStackTrace();
		} 
		finally{
			if(input!=null){
				try {
					input.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	Connection newConnection() throws SQLException{
		return DriverManager.getConnection(urlDB, userDB, passDB);
	}


	public List<Pizza> findAllPizzas()  {
		Connection connection = null;
		ResultSet resultat = null;
		Statement statement = null;
		pizzas = new ArrayList<Pizza>();
		try {
			connection = newConnection();
			statement = connection.createStatement();
			resultat = statement.executeQuery("SELECT * FROM pizza");
			while(resultat.next()){
				int id = resultat.getInt("idpizza");
				String libelle = resultat.getString("libelle");
				Double prix = resultat.getDouble("prix");
				String code = resultat.getString("code");
				Pizza pizza = new Pizza(id,code,libelle,prix);
				pizzas.add(pizza);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				resultat.close();
				statement.close();
				connection.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return pizzas;

	}

	public void saveNewPizza(Pizza pizza) {
		Connection connection = null;
		int resultat = 0;
		PreparedStatement insertPizza = null;

		try {
			connection = newConnection();
			insertPizza = connection.prepareStatement("INSERT INTO pizza(code,libelle, prix) VALUES(?,?,?)");
			insertPizza.setString(1, pizza.getCode());
			insertPizza.setString(2, pizza.getLibelle());
			insertPizza.setDouble(3, pizza.getPrix());
			resultat = insertPizza.executeUpdate();
			System.out.println("Pizza bien ajouté");
		}
		catch(SQLException e) {

			e.printStackTrace();
			System.out.println("une erreur lors de l'ajout est survenue");
		}
		finally {
			try {
				insertPizza.close();
				connection.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}

	}

	public void updatePizza(String codePizza, Pizza pizza) {
		Connection connection = null;
		int resultat = 0;
		PreparedStatement updatePizza = null;

		try {
			connection = newConnection();
			updatePizza = connection.prepareStatement("UPDATE pizza SET code=?, libelle=?, prix=? where code=?");
			updatePizza.setString(1, pizza.getCode());
			updatePizza.setString(2, pizza.getLibelle());
			updatePizza.setDouble(3, pizza.getPrix());
			updatePizza.setString(4, codePizza);
			resultat = updatePizza.executeUpdate();
			System.out.println("Pizza bien modifié");
		}
		catch(SQLException e) {

			e.printStackTrace();
			System.out.println("une erreur lors de la modification est survenue");
		}
		finally {
			try {

				updatePizza.close();
				connection.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}

	}

	public void deletePizza(String codePizza) {
		Connection connection = null;
		int resultat = 0;
		PreparedStatement updatePizza = null;

		try {
			connection = newConnection();
			updatePizza = connection.prepareStatement("DELETE FROM pizza where code=?");
			updatePizza.setString(1, codePizza);
			resultat = updatePizza.executeUpdate();
			System.out.println("Pizza bien supprimer");
		}
		catch(SQLException e) {

			e.printStackTrace();
			System.out.println("une erreur lors de la supression est survenue");
		}
		finally {
			try {

				updatePizza.close();
				connection.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}


	}

	public Pizza findPizzaByCode(String codePizza) {
		Connection connection = null;
		ResultSet resultat = null;
		PreparedStatement searchPizza = null;

		try {
			connection = newConnection();
			searchPizza = connection.prepareStatement("SELECT* FROM pizza WHERE code=?");
			searchPizza.setString(1, codePizza);
			resultat = searchPizza.executeQuery();
			while(resultat.next()){
				int id = resultat.getInt("idpizza");
				String libelle = resultat.getString("libelle");
				Double prix = resultat.getDouble("prix");
				String code = resultat.getString("code");
				Pizza pizzaSearch = new Pizza(id,code,libelle,prix);
				return pizzaSearch;
			}

		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				resultat.close();
				searchPizza.close();
				connection.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	public boolean pizzaExists(String codePizza) {
		Connection connection = null;
		ResultSet resultat = null;
		PreparedStatement searchPizza = null;
		boolean found = false;

		try {
			connection = newConnection();
			searchPizza = connection.prepareStatement("SELECT* FROM pizza WHERE code=?");
			searchPizza.setString(1, codePizza);
			resultat = searchPizza.executeQuery();
			while(resultat.next()){
				int id = resultat.getInt("idpizza");
				String libelle = resultat.getString("libelle");
				Double prix = resultat.getDouble("prix");
				String code = resultat.getString("code");
				Pizza pizzaSearch = new Pizza(id,code,libelle,prix);
				if (pizzaSearch !=null){
					found = true;
				}
			}

		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				resultat.close();
				searchPizza.close();
				connection.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return found;
	}
	public void initializationDB(){
		pizzas = new ArrayList<Pizza>();
		pizzas.add(new Pizza("PEP", "Pépéroni", 12.50));
		pizzas.add(new Pizza("MAR", "Margherita", 14.00));
		pizzas.add(new Pizza("REIN", "La Reine", 11.50));
		pizzas.add(new Pizza("FRO", "La 4 fromages", 12.00));
		pizzas.add(new Pizza("CAN", "La cannibale", 12.50));
		pizzas.add(new Pizza("SAV", "La savoyarde", 13.00));
		pizzas.add(new Pizza("ORI", "L’orientale", 13.50));
		pizzas.add(new Pizza("IND", "L’indienne", 14.00));
		Iterator<Pizza> it = pizzas.iterator();
		while (it.hasNext()){
			Pizza p = it.next();
			saveNewPizza(p);

		}

	}

}