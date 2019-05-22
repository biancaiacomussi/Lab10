package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	Map<Integer, Author> autoriMap;
	Map<Integer, Paper> paperMap;
	PortoDAO dao ;
	Graph<Author,DefaultEdge> grafo;
	
	public Model() {
		autoriMap = new HashMap<>();
		paperMap = new HashMap<>();
		dao = new PortoDAO();
		dao.getAllAuthors(autoriMap);
		dao.getAllPapers(paperMap);
	}

	public Map<Integer, Author> getAutoriMap() {
		return autoriMap;
	}
	
	public void setGrafo() {
		grafo = new SimpleGraph<>(DefaultEdge.class);
		for(Author a : autoriMap.values()) {
			grafo.addVertex(a);
		}
		for(Adiacenza a : dao.getAdiacenti(autoriMap)) {
			grafo.addEdge(a.getA1(), a.getA2());
		}
	}

	public Graph<Author, DefaultEdge> getGrafo() {
		return grafo;
	}
	
	public List<Author> getCoautori(Author autore){
		List<Author> vicini = new ArrayList<>();
		vicini = Graphs.neighborListOf(grafo, autore);
		Collections.sort(vicini);
		return vicini;
	}
	
	public List<Author> nonVicini(Author autore){
		List<Author> nonVicini = new ArrayList<>();
		List<Author> vicini = Graphs.neighborListOf(grafo, autore);
		
		for(Author a : this.autoriMap.values()) {
			if(!vicini.contains(a))
				nonVicini.add(a);
		}
		Collections.sort(nonVicini);
		return nonVicini;
	}
	
	public List<Author> trovaCamminoMinimo(Author partenza, Author arrivo) {
		DijkstraShortestPath<Author, DefaultEdge> dijstra = new DijkstraShortestPath<>(this.grafo) ;
		GraphPath<Author, DefaultEdge> path = dijstra.getPath(partenza, arrivo) ;
		return path.getVertexList();
	}
	
	public List<Paper> getPapers(Author partenza, Author arrivo){
		List<Author> minimo = trovaCamminoMinimo(partenza, arrivo);
		List <Paper> papers = dao.getPapers(minimo, paperMap);
		return papers;
	}

}
