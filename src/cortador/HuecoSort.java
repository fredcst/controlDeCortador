package cortador;

public class HuecoSort {
	
		public void quickSort(Hueco arr[], int begin, int end) {
		    if (begin < end) {
		        int partitionIndex = partition(arr, begin, end);
		 
		        quickSort(arr, begin, partitionIndex-1);
		        quickSort(arr, partitionIndex+1, end);
		    }
		}
		
		private int partition(Hueco arr[], int begin, int end) {
		    double pivot = arr[end].getArea();
		    int i = (begin-1);
		 
		    for (int j = begin; j < end; j++) {
		        if (arr[j].getArea() >= pivot) {
		            i++;
		 
		            Hueco swapTemp = arr[i];
		            arr[i] = arr[j];
		            arr[j] = swapTemp;
		        }
		    }
		 
		    Hueco swapTemp = arr[i+1];
		    arr[i+1] = arr[end];
		    arr[end] = swapTemp;
		 
		    return i+1;
		}
		
	}


