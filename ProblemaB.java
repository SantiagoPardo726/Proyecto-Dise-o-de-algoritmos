
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Programa para calcular el grafo con minimo diferencial resultante de la suma de n grafos BC
 * @author Santiago Pardo - 202013024 y Kevin Cohen - 202011864
 */
public class ProblemaB {
	
	public static void main(String[] args) throws Exception{
		ArrayList<Graph> grafos = new ArrayList<Graph>();
		ArrayList<ArrayList<Graph>> gragra = new ArrayList<ArrayList<Graph>>();
		int vertexCount = 0;
		try ( 
				InputStreamReader is= new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(is);
			) { 
				String line = br.readLine();
					
				while(line!=null && line.length()>0 && !"0".equals(line)) {
					final String [] dataStr = line.split(" ");
					final int[] numeros = Arrays.stream(dataStr).mapToInt(f->Integer.parseInt(f)).toArray();
					if(numeros.length>1) {
						for(int i = 2;i<numeros.length;i++) {
							numeros[i] = numeros[i]+vertexCount;
						}
						Graph grafo = convert(numeros, vertexCount);
						grafos.add(grafo);
						vertexCount = vertexCount+numeros[0];
					}
					line = br.readLine();
					if(line.length()==1) {
						gragra.add(grafos);
						grafos = new ArrayList<Graph>();
					}
					
				}
			
				for(int j = 0;j<gragra.size();j++) {
					int minimal = MinimalBC(gragra.get(j));
					System.out.println(minimal);
				}
		
		}
	
	}
	
	
	
	/*1. Conectar por un nuevo eje los dos primeros grafos (todas las posibilidades)
	 *2. Cada uno de estos grafos se evalua con un DFS si es Bipartito y se lleva la cuenta de los del grupo a y b
	 *3. Se halla la diferenciabilidad mínima de todos estos y se guarda el grafo dueño de ella
	 *4. Se repite el proceso con el nuevo grafo y el siguiente en la lista (iterativamente)
	 *5. Cuando se termine el proceso, se retorna la diferenciabilidad en cuestion
	 *(NOTA) Pagina Bipartito DFS: https://www.techiedelight.com/determine-given-graph-bipartite-graph-using-dfs/ */
	
	public static int MinimalBC(ArrayList<Graph> grafos) {
		
		Graph grafoControl = null;
		int minimal = 10000000;
		Graph grafo1 = grafos.get(0);
		
		for(int i = 0;i<grafos.size()-1;i++) {
			
			Graph grafo2 = grafos.get(i+1);
			ArrayList<Integer> nuevosVertices = grafo1.getVertex();
			nuevosVertices.addAll(grafo2.getVertex());
			
			HashMap<Integer, ArrayList<Integer>> nuevosEjes = grafo1.getEdges();
			nuevosEjes.putAll(grafo2.getEdges());
			
			for(int j = 0; j<grafo1.getVertex().size();j++) {
				int veri = grafo1.getVertex().get(j);
				for(int k = 0;k<grafo2.getVertex().size();k++) {
					int vero = grafo2.getVertex().get(k);
					HashMap<Integer, ArrayList<Integer>> copyEjes = nuevosEjes;
					ArrayList<Integer> ne = copyEjes.get(veri);
					if(ne!=null) {
						ne.add(vero);
						copyEjes.put(veri, ne);
					}else if(ne == null) {
						ne = new ArrayList<Integer>();
						ne.add(vero);
						copyEjes.put(veri, ne);
					}
					ne = copyEjes.get(vero);
					if(ne!=null) {
						ne.add(veri);
						copyEjes.put(vero, ne);
					}else if(ne == null) {
						ne = new ArrayList<Integer>();
						ne.add(veri);
						copyEjes.put(vero, ne);
					}
					Graph grafoPre = new Graph(nuevosVertices,copyEjes);
					Integer bip = Bip(grafoPre);
					if(bip != null && bip<minimal) {
						grafoControl = grafoPre;
						minimal = bip;
					}
				}
			}
		grafo1 = grafoControl;	
		}
		return minimal;
		
		
	}
	
	
	private static class Graph {


		private ArrayList<Integer> vertex;

		private HashMap<Integer, ArrayList<Integer>> edges;
		
		
		public Graph(ArrayList<Integer> vertex, HashMap<Integer, ArrayList<Integer>> edges) {

			this.vertex = vertex;

			this.edges = edges;
			
		}
		public ArrayList<Integer> getVertex() {
			return vertex;
		}

		public HashMap<Integer, ArrayList<Integer>> getEdges() {
			return edges;
		}
			
	}
	
	public static Graph convert(int[] ints, int count) {
		
		ArrayList<Integer> vertices = new ArrayList<Integer>();
		HashMap<Integer, ArrayList<Integer>> edges = new HashMap<Integer, ArrayList<Integer>>();
		
		if (ints.length>2) {
			
			for(int j=0;j<ints.length;j++) {
				if(j == 0) {
				}else if (j>1) {
					if(!vertices.contains(ints[j])){
						vertices.add(ints[j]);
					}
					if(!edges.containsKey(ints[j]) && j%2==0) {
						ArrayList<Integer> ver = new ArrayList<Integer>();
						ver.add(ints[j+1]);
						edges.put(ints[j], ver);
						if(!edges.containsKey(ints[j+1])){
							ArrayList<Integer> ver1 = new ArrayList<Integer>();
							ver1.add(ints[j]);
							edges.put(ints[j+1], ver1);
						}else {
							ArrayList<Integer> ver1 = edges.get(ints[j]);
							ver1.add(ints[j]);
							edges.put(ints[j+1], ver1);
						}
					}else if (edges.containsKey(ints[j]) && j%2==0) {
						ArrayList<Integer> ver = edges.get(ints[j]);
						ver.add(ints[j+1]);
						edges.put(ints[j], ver);
						if(!edges.containsKey(ints[j+1])){
							ArrayList<Integer> ver1 = new ArrayList<Integer>();
							ver1.add(ints[j]);
							edges.put(ints[j+1], ver1);
						}else {
							ArrayList<Integer> ver1 = edges.get(ints[j]);
							ver1.add(ints[j]);
							edges.put(ints[j+1], ver1);
						}
					}
				}
			}
			
		}else {
			for(int i = count;i<ints[0]+count;i++) {
				vertices.add(i);
			}
			edges = new HashMap<Integer, ArrayList<Integer>>();
		}
		Graph grafo = new Graph(vertices, edges);
		
		return grafo;
	}
	
	public static Integer Bip (Graph grafo) {
		
		Boolean[] marcados = new Boolean[grafo.getVertex().size()];
		Arrays.fill(marcados, false);
		Boolean[] colores = new Boolean[grafo.getVertex().size()];
		Stack<Integer> stack = new Stack<Integer>();
		colores[0] = true;
		marcados[0] = true;
		stack.push(0);
		
		Integer dif = 0;
	
		while(stack.size()>0) {
			int ver = stack.pop();
			int veri = grafo.getVertex().get(ver);
			ArrayList<Integer> verte = grafo.getEdges().get(veri);
			for(int i = 0;i<verte.size();i++) {
				int ind = grafo.getVertex().indexOf(verte.get(i));
				if(!marcados[ind]) {
					stack.push(ind);
					marcados[ind] = true;
					if(colores[ind] == null) {
						colores[ind] = !colores[ver];
					}else if(colores[ind]==colores[ver]) {
						return null;
					}
				}
			}
			
		}
		int trues = 0;
		int falses = 0;
		for(Boolean color:colores) {
			if(color) {
				trues++;
			}else {
				falses++;
			}
		}
		dif = Math.abs(trues-falses);
		return dif;
	}
}

