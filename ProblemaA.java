import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * Programa para resover el ProblemaA
 * @authores: Kevin Cohen 202011864 y Santiago Pardo 202013024
 */
public class ProblemaA {
	
	private static final DecimalFormat df = new DecimalFormat("0.0000");

	public static void main(String[] args) throws Exception {
		ProblemaA instancia = new ProblemaA();
		try (InputStreamReader is = new InputStreamReader(System.in); BufferedReader br = new BufferedReader(is);) {
			String line = br.readLine();

			while (line != null && line.length() > 0 && !"0".equals(line)) {
				final String[] dataStr = line.split(" ");
				int n = Integer.parseInt(dataStr[0]);
				float a = Float.parseFloat(dataStr[1]);
				float b = Float.parseFloat(dataStr[2]);
				float c = Float.parseFloat(dataStr[3]);
				float d = Float.parseFloat(dataStr[4]);
				float res = instancia.calcular(n, a, b, c, d);
				String print = df.format(res);
				System.out.println(print);
				line = br.readLine();
			}
		}
	}

	public float calcular(int n, float a, float b, float c, float d) {
		ArrayList<Float> arreglo = new ArrayList<Float>();
		arreglo.add(0, a);
		arreglo.add(1, b);
		int pos = 2;
		while (pos <= n) {
			float valorPos = c * arreglo.get(pos - 2) + d * arreglo.get(pos - 1);
			arreglo.add(pos, valorPos);
			pos++;
		}
		float varSumatoria = 0;
		int k = 0;
		while (k <= n) {
			varSumatoria += (arreglo.get(k) * arreglo.get(n - k));
			k++;
		}
		return varSumatoria;
	}

}