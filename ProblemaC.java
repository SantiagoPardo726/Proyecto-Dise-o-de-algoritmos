import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Programa para resover el ProblemaA
 * @authores: Kevin Cohen 202011864 y Santiago Pardo 202013024
 */
public class ProblemaC {

	private int minimo = Integer.MAX_VALUE;
	
	private String respuesta = "";
	
	public static void main(String[] args) throws IOException {
		ProblemaC solucion = new ProblemaC();
		try (InputStreamReader is = new InputStreamReader(System.in); BufferedReader br = new BufferedReader(is);) {
			String line = br.readLine();
			String[] data = line.split(" ");
			int cant = Integer.parseInt(data[0]);
			int leng = Integer.parseInt(data[1]);
			String[] problema = new String[cant];
			line = br.readLine();
			while (line != null && line.length() > 0 && !"0".equals(line)) {
				int i = 0;
				while(i<cant) {
					problema[i] = line;
					if(i==cant-1) {
						solucion.AllRecursive(cant, problema);
						System.out.println(solucion.respuesta);
						solucion.minimo=Integer.MAX_VALUE;
						solucion.respuesta="";
					}
					line=br.readLine();
					i ++;
				}
				i=0;
				if(line.equals("0")) {
					break;
				}
				data = line.split(" ");
				cant = Integer.parseInt(data[0]);
				leng = Integer.parseInt(data[1]);
				problema = new String[cant];
				line = br.readLine();
			}
		}
	}
	
	public void AllRecursive(
			  int n, String[] elements) {
			    if(n == 1) {
			        String pos = mixString(elements);
			        if(pos.length()<this.minimo) {
			        	this.minimo = pos.length();
			        	this.respuesta = pos;
			        }
			    } else {
			        for(int i = 0; i < n-1; i++) {
			            AllRecursive(n - 1, elements);
			            if(n % 2 == 0) {
			                swap(elements, i, n-1);
			            } else {
			                swap(elements, 0, n-1);
			            }
			        }
			        AllRecursive(n - 1, elements);
			    }
			}

	private void swap(String[] input, int a, int b) {
	    String tmp = input[a];
	    input[a] = input[b];
	    input[b] = tmp;
	}
	
	
	private String mixString(String[] lista) {
		String res=lista[0];
		int i = 0;
		while(i<lista.length-1) {
			int num = overlap(lista[i], lista[i+1]);
			res=res.substring(0,res.length()-num);
			res += lista[i+1];
			i ++;
		}
		return res;
	}
	
	private int overlap(String first,String second) {
		int res = 0;
		for (int k = first.length(); k >= 0; --k) {
          		if (first.endsWith(second.substring(0, k))) {
              			res = k;
              			break;
          		}
		}
		return res;
	}
}