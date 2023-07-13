package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public void creaArchi(Map<String, Genes> genesIdMap, List<Arco> archi) {
		
		String sql = "SELECT g1.GeneID AS gene1, g2.GeneID AS gene2, i.Expression_Corr AS ex "
				+ "FROM (SELECT g.* "
				+ "FROM genes g "
				+ "WHERE g.Essential = \"Essential\" "
				+ "GROUP BY g.GeneID) g1, "
				+ "(SELECT g.* "
				+ "FROM genes g "
				+ "WHERE g.Essential = \"Essential\" "
				+ "GROUP BY g.GeneID) g2, "
				+ "interactions i "
				+ "WHERE g1.GeneID = i.GeneID1 AND g2.GeneID = i.GeneID2 OR g2.GeneID = i.GeneID1 AND g1.GeneID = i.GeneID2 "
				+ "HAVING gene1 > gene2";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				double peso = 0;
				Genes gene1 = genesIdMap.get(res.getString("gene1"));
				Genes gene2 = genesIdMap.get(res.getString("gene2"));
				if (gene1.getChromosome() == gene2.getChromosome()) {
					peso = Math.abs(res.getDouble("ex")*2);
				}
				else  {
					peso = Math.abs(res.getDouble("ex"));
				}
				Arco a = new Arco(gene1, gene2, peso);
				archi.add(a);
				
			}
			res.close();
			st.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void creaVertici(Map<String, Genes> genesIdMap) {
		
		String sql = "SELECT g.* "
				+ "FROM genes g "
				+ "WHERE g.Essential = \"Essential\" "
				+ "GROUP BY g.GeneID";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				genesIdMap.put(res.getString("GeneID") ,genes);
			}
			res.close();
			st.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	


	
}
