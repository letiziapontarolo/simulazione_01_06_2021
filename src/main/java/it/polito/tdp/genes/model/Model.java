package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	private Graph<Genes, DefaultWeightedEdge> grafo;
	private GenesDao dao;
	private Map<String, Genes> genesIdMap;
	private List<Arco> archi;
	
	public Model() {
		dao = new GenesDao();	
	}
	
	public void creaGrafo() {
		
		genesIdMap = new HashMap<String, Genes>();
		grafo = new SimpleWeightedGraph<Genes, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		archi = new ArrayList<Arco>();
		
		this.dao.creaVertici(genesIdMap);
		Graphs.addAllVertices(this.grafo, genesIdMap.values());
		
		this.dao.creaArchi(genesIdMap, archi);
		for (Arco a : archi) {
		Graphs.addEdgeWithVertices(this.grafo, a.getGene1(), a.getGene2(), a.getPeso());
		}
		
	}
	
	public List<String> listaGeni() {
		List<String> result = new ArrayList<String>();
		for (Genes g : genesIdMap.values()) {
			result.add(g.getGeneId());
		}
		Collections.sort(result);
		return result;
	}
	
	public String adiacenti(String s) {
		
		List<Double> listaPesi = new ArrayList<Double>();
		List<Arco> lista = new ArrayList<Arco>();
		String result = "";
		Genes gene1 = genesIdMap.get(s);
		List<Genes> vicini = Graphs.neighborListOf(this.grafo, gene1);
		
		for (Genes gene2 : vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(gene1, gene2));
			Arco a = new Arco(gene1, gene2, peso);
			lista.add(a);
		}

		
		Collections.sort(lista, new Comparator<Arco>() {
			 public int compare(Arco o1, Arco o2) {
			 return (int) (o1.getPeso() - o2.getPeso ());
			 }});

		Collections.reverse(lista);

		for (Arco a : lista) {
			result = result + a.getGene2() + ",   peso: " + a.getPeso() + "\n";
		}
		
		return result;
		
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
		}
	
		 public int numeroArchi() {
		return this.grafo.edgeSet().size();
		}
	
	
}
