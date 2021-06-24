package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	Graph<String, DefaultWeightedEdge> grafo;
	private List<String> result;
	
	public Model() {
		this.dao = new FoodDao();
		
		
	}
	
	public List<String> getAllPortions(int c) {
		return this.dao.listVertici(c);
	}

	public void creaGrafo(int c, String value) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.listVertici(c));
		
		for(Adiacenza a: this.dao.getArchi(c)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(), a.getPeso());
		}
		
		System.out.println("Grafo creato!");
		System.out.println("Numero vertici: " + this.grafo.vertexSet().size());
		System.out.println("Numero archi: " + this.grafo.edgeSet().size());	
	}
	
	public List<String> getCorrelate(String porzione){
		List<String> result = new ArrayList<>()	;
		for(String s: Graphs.neighborListOf(this.grafo, porzione)) {
			if(!result.contains(s))
				result.add(s);
		}
		if(!result.isEmpty()) {
			for(String ss: result) {
				System.out.println(ss);
			}
		}
		return result;
	}

	// PASSI INTESO COME NUMERO SPOSTAMENTI, QUINDI N+1
	public List<String> cercaCammino(int n, String partenza) {
		result = new ArrayList<>();
		List<String> parziale = new ArrayList<>();
		parziale.add(partenza);
		int max = 0;
		cerca(n, parziale, max);
		for(String s: result)
			System.out.println(s);
		return result;
		
	}

	private void cerca(int n, List<String> parziale, int max) {
		
		if(parziale.size() == n+1) {
			int peso = contaPeso(parziale);
			if(peso > max) {
				max = peso;
				result = new ArrayList<>(parziale);
				return;
			}
		}else {
			String ultima = parziale.get(parziale.size()-1);
			for(String s: Graphs.successorListOf(this.grafo, ultima)) {
				if(!parziale.contains(s)) {
					parziale.add(s);
					cerca(n, parziale, max);
					parziale.remove(s);
				}
			}
		}
		
	}

	public int contaPeso(List<String> parziale) {
		int pesoTot = 0;
		for(int i = 0; i<parziale.size()-1; i++) {
			DefaultWeightedEdge e = this.grafo.getEdge(parziale.get(i), parziale.get(i+1));
			pesoTot += this.grafo.getEdgeWeight(e);
			
		}
		return pesoTot;
	}

	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
}
