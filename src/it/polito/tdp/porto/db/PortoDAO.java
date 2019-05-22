package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.porto.model.Adiacenza;
import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	
	public void getAllAuthors(Map<Integer,Author> map){
		final String sql = "SELECT * FROM author";
		

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				if(map.get(rs.getInt("id"))==null) {
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				map.put(rs.getInt("id"), autore);
				
				} 
			}

			conn.close();
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Adiacenza>getAdiacenti(Map<Integer,Author> map){
		final String sql = "SELECT c1.authorid, c2.authorid FROM creator c1, creator c2 WHERE c1.eprintid = c2.eprintid AND c1.authorid>c2.authorid GROUP BY c1.authorid, c2.authorid";
		List<Adiacenza> adiacenti = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Author a1 = map.get(rs.getInt("c1.authorid"));
				Author a2 = map.get(rs.getInt("c2.authorid"));
				Adiacenza a = new Adiacenza(a1,a2);
				adiacenti.add(a);
				
				} 
			conn.close();
			return adiacenti;

			
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Paper> getPapers(List<Author> autori, Map<Integer, Paper> paperMap){
		final String sql = "SELECT p.eprintid " + 
				"FROM paper p, creator c1, creator c2 " + 
				"WHERE p.eprintid=c1.eprintid AND " + 
				"p.eprintid=c2.eprintid AND c1.eprintid=c2.eprintid " + 
				"AND c1.authorid LIKE ? AND c2.authorid LIKE ? " + 
				"group BY p.eprintid";
		List<Paper> papers = new ArrayList<>();
		
		try {Connection conn = DBConnect.getConnection();
			
			for(int i=0; i<autori.size()-1;i++) {
				 
				Author a1 = autori.get(i);
				Author a2 = autori.get(i+1);
					
					PreparedStatement st = conn.prepareStatement(sql);
					
					st.setInt(1, a1.getId());
					st.setInt(2, a2.getId());

					ResultSet rs = st.executeQuery();

					if (rs.next()) {

						Paper p = paperMap.get(rs.getInt("p.eprintid"));
						papers.add(p);
					}

					
				}
			conn.close();
			return papers;
			

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
		
		
	
	
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public void getAllPapers(Map<Integer, Paper> paperMap) {
		final String sql = "SELECT * FROM paper";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				if(!paperMap.containsKey(rs.getInt("eprintid"))) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				paperMap.put(rs.getInt("eprintid"), paper);
				}
				
				
			}

			conn.close();

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
	}
}