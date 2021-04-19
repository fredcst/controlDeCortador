package cortador;

public class QsortH {


		/*
		 * This function takes last element as pivot, places the pivot element at its
		 * correct position in sorted array, and places all smaller (smaller than pivot)
		 * to left of pivot and all greater elements to right of pivot
		 * 
		 * Menor a Mayor Utiliza dimensiones de cada pieza 0: alto 1: ancho 2: area
		 */
		int partition(PiezaH arr[], int low, int high, int tipo) {
			double pivot = 0., px = 0.;
			int i = (low - 1); // index of smaller element

			if (tipo == 0) {
				pivot = arr[high].getAlto();
			}
			if (tipo == 1) {
				pivot = arr[high].getAncho();
			}
			if (tipo == 2) {
				pivot = arr[high].getArea();
			}

			for (int j = low; j < high; j++) {
				// If current element is smaller than the pivot

				if (tipo == 0) {
					px = arr[j].getAlto();
				}
				if (tipo == 1) {
					px = arr[j].getAncho();
				}
				if (tipo == 2) {
					px = arr[j].getArea();
				}
				if (px > pivot) {
					// if (px < pivot) {
					i++;

					// swap arr[i] and arr[j]
					PiezaH temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
			}

			// swap arr[i+1] and arr[high] (or pivot)
			PiezaH temp = arr[i + 1];
			arr[i + 1] = arr[high];
			arr[high] = temp;

			return i + 1;
		}

		/*
		 * The main function that implements QuickSort() arr[] --> Array to be sorted,
		 * low --> Starting index, high --> Ending index
		 */
		void sort(PiezaH arr[], int low, int high, int tipo) {
			if (tipo == 0 || tipo == 1 || tipo == 2) {
				if (low < high) {
					/*
					 * pi is partitioning index, arr[pi] is now at right place
					 */
					int pi = partition(arr, low, high, tipo);

					// Recursively sort elements before
					// partition and after partition
					sort(arr, low, pi - 1, tipo);
					sort(arr, pi + 1, high, tipo);
				}
			} else {
				System.out.format("Tipo de comparacion no conocido\n");
				System.exit(0);

			}
		}

		

		public static String tipoComoString(int tipo) {
			String frase = null;
			switch (tipo) {
			case 0:
				frase = "alto";
				break;
			case 1:
				frase = "ancho";
				break;
			case 2:
				frase = "area";
				break;
			default:
				frase = "deconocido";
			}
			return frase;

		}

		public static int tipoComoNumero(String tipoS) {
			int tip = -1;
			switch (tipoS) {
			case "alto":
				tip = 0;
				break;
			case "ancho":
				tip = 1;
				break;
			case "area":
				tip = 2;
				break;
			default:
				tip = -1;
			}
			return tip;

		}
	}


