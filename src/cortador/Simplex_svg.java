package cortador;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

public class Simplex_svg {
	/**
	 * Esta clase es la version java de simplexC_svg.c
	 * 
	 */
    
    static String directorio = DirectorioTrabajo.directorio;
    
	public Simplex_svg() {
	}

	public static void main(String[] args) {
	
		/*
		
		int pos, k = 0, k_ini = 0, vis, no_path, tamano_s;
		int no_leidos = 0, no_escritos = 0;
		char k_techo8, k_str8;
		*/
		String nombre_svgP = null;
		//String directorio = null;
		/*
		byte[] buffer1 = null;
		byte[] buffer = null;
		File fp = null;
		*/
		/************************ Entrada de comandos ****************************/
		System.out.println("No. de argumentos: " + args.length);
	//	directorio = "/home/ivan/work/";
			if (args.length >= 2)uso();
		if (args.length == 0) {
			System.out.println("Usando archivo default: drawing.svg");
			
			nombre_svgP = "drawing.svg";
			
		}
		/*
		 * String s1="Sachin "; String s2="Tendulkar"; String s3=s1.concat(s2);
		 */
		if (args.length == 1) {
			nombre_svgP = args[0];
			if (Objects.equals(nombre_svgP, "-h"))
				uso();
		}
		//nombre_svg = directorio + File.separator+nombre_svgP;
		//nom_pre =  null;
		Simplex_svg simplex_svg= new Simplex_svg();
		//nombre_svgP = "ballenasinh.svg";
		nombre_svgP=simplex_svg.trabajo(nombre_svgP);
		
	}

		
	public  String trabajo(String nombre_svgP) {
		
		
		int k = 0, k_ini = 0, vis, no_path;
		int no_leidos = 0;
		//String nombre_svgP = null;
		String nombre_svg = null;
		//String directorio = null;
		byte[] buffer1 = null;
		byte[] buffer = null;
		//System.out.println("Trabajando..");
		//String directorio_inkscape= directorio+ File.separator+ "objetos"+ File.separator+ "inkscape_svg";
		
		String directorio_inkscape = null;
		// DirectorioBase dbase = new DirectorioBase();
		File directorio_inkscapeD = null;

		// final File directorio_normalD = new File(directorio_normal);
		directorio_inkscape = DirectorioBase.getNombreFileFromResources("cortador"+File.separator+"objetos" + File.separator + "inkscape_svg");
	//	directorio_inkscapeD = new File(directorio_inkscape);
		//

		
		
		
		
		
		
		
		
		
		nombre_svg = directorio_inkscape + File.separator+nombre_svgP;
		

		/********************* fin de entrada de comandos ***************************/

		/*********** Lectura de archivo svg ****************************************/

		File file = new File(nombre_svg);
		if (!file.exists() || !file.isFile()) {
			System.out.println("No pude abrir archivo. " + nombre_svg + " Salgo...");
			System.exit(0);
		}
		long result = file.length();
		//System.out.println("tamano de archivo: " + result);
		buffer1 = new byte[(int) result];
		check1(buffer1, "Je ne peux pas générer Buffer");

		DataInputStream data_in = null;
		try {
			data_in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			aviso("Je ne peux pas générer stream");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			no_leidos = data_in.read(buffer1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			aviso("Je ne peux pas lire stream");
			// e.printStackTrace();
		}
		if (no_leidos != result) {
			System.out.format("Taille:%d Nº lus:%d%n", result, no_leidos);
			System.out.format("Toutes les données ont été lues..Je sort%n");
			System.exit(0);

		} else {
			System.out.format("Toutes les données ont été lues.%n");
		}

		try {
			data_in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			aviso("Je ne peux pas fermer stream");
			// e.printStackTrace();
		}

		/*
		 * try { scanner = new Scanner(file); } catch (FileNotFoundException e) { //
		 * TODO Auto-generated catch block
		 * System.out.println("No pude abrir archivo. Salgo..."); System.exit(0); //
		 * e.printStackTrace(); }
		 */

		/********************** fin de lectura *************************************/
	//	for (int i = 0; i < buffer1.length; i++)
	//		System.out.println("buffer1[]: " + (char) buffer1[i]);

		
		
		
		
		
		
		
		/*
		 * 
		 * Genera un directorio dosnde se depositaran  los archivos simple 
		 * 	
		 * */
		//String directorio_simple= directorio+ File.separator+ "objetos"+ File.separator+ "simple_svg";
		String directorio_simple = DirectorioBase.getNombreFileFromResources("cortador"+File.separator+"objetos" + File.separator + "simple_svg");
		
		
	      java.nio.file.Path   pathD = Paths.get(directorio_simple);
	        try {
				Files.createDirectories(pathD);
			} catch (IOException e1) {
				System.out.println("No pude crear directorio"+ pathD);
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
		
		
		
		
		
		
		
		
		
		
		
		
		
		/****************** Nombre de archivo de salida ****************************/
		nombre_svg = directorio_simple +  File.separator+ "simp_ws_" + nombre_svgP;
        String nombre_svgPN= "simp_ws_" + nombre_svgP;
		//System.out.format("Nombre de archivo de salida:%s \n", nombre_svg);
		/****************** Fin de nombre de archhivo de salida ********************/

		LinkedList<PropiedadDeDatosPath> datos_de_path_last, datos_de_path_first;
		LinkedList<PropiedadDeDatosPath> datos_de_path = new LinkedList<PropiedadDeDatosPath>();

		/********************************
		 * Localizacion de datos de path en archivo svg
		 *****************/
		/**************************
		 * Cpmienzo de parser de archivo svg
		 **************************************/

		boolean flag_path = false;
		boolean flag_d = false;
		boolean flag_comillas = false;
		boolean flag_svg = false;
		boolean flag_stroke = true;//Parte con visibilidad y detecta ausencia

		/******************
		 * COMIENZO DE RECORRIDO DE buffer1
		 ********************************************/
		int j = 0;// lee el eof
		no_path = 0;// contendra el nuero de paths encontrados
		while (j < no_leidos) {

			/***********
			 * Detecta el elemento: <svg
			 *************************************************/

			if ((j + 3 < (no_leidos - 1)) && ((char) buffer1[j] == '<') && ((char) buffer1[j + 1] == 's')
					&& ((char) buffer1[j + 2] == 'v') && ((char) buffer1[j + 3] == 'g')) {
				//System.out.format("encontre <svg: en posicion: %d\n", j);
				flag_svg = true;
			}
			/***************
			 * Fin de Detecta el elemento: <svg
			 **************************************/

			/***********
			 * Detecta el elemento: <path
			 *************************************************/
			if ((j + 4 < (no_leidos - 1)) && ((char) buffer1[j] == '<') && ((char) buffer1[j + 1] == 'p')
					&& ((char) buffer1[j + 2] == 'a') && ((char) buffer1[j + 3] == 't')
					&& ((char) buffer1[j + 4] == 'h') // detecta '<... path'
			) {
				//System.out.format("encontre <path: en posicion: %d \n", j);
				flag_path = true;
				//System.out.format("<path *********\n");
			}
			/***********
			 * Fin de Detecta el elemento: <path
			 ******************************************/

			/***********
			 * Detecta el elemento: stroke:#
			 *************************************************/
			//if ((j + 7 < (no_leidos - 1)) && ((char) buffer1[j] == 's') && ((char) buffer1[j + 1] == 't')
				//	&& ((char) buffer1[j + 2] == 'r') && ((char) buffer1[j + 3] == 'o')
					//&& ((char) buffer1[j + 4] == 'k') && ((char) buffer1[j + 5] == 'e')
				//	&& ((char) buffer1[j + 6] == ':') && ((char) buffer1[j + 7] == '#')) {
				//System.out.format("encontre stroke:# en posicion: %d\n", j);
			//	flag_stroke = true;
				//System.out.format("****** stroke:# *********\n");
			//}
			/*************************
			 * fin
			 ************************************************************/
			/***********
			 * Detecta el elemento: stroke:none
			 *************************************************/
			if ((j + 10 < (no_leidos - 1)) && ((char) buffer1[j] == 's') && ((char) buffer1[j + 1] == 't')
					&& ((char) buffer1[j + 2] == 'r') && ((char) buffer1[j + 3] == 'o')
					&& ((char) buffer1[j + 4] == 'k') && ((char) buffer1[j + 5] == 'e')
					&& ((char) buffer1[j + 6] == ':') 
					&& ((char) buffer1[j + 7] == 'n')
					&& ((char) buffer1[j + 8] == 'o')
					&& ((char) buffer1[j + 9] == 'n')
					&& ((char) buffer1[j + 10] == 'e')) {
				//System.out.format("encontre stroke:# en posicion: %d\n", j);
				if(flag_path == true)flag_stroke = false;
				//System.out.format("****** stroke:# *********\n");
			}
			/*************************
			 * fin
			 ************************************************************/
			/***********
			 * Detecta el elemento: stroke="none"
			 *************************************************/
			if ((j + 12 < (no_leidos - 1)) && ((char) buffer1[j] == 's') && ((char) buffer1[j + 1] == 't')
					&& ((char) buffer1[j + 2] == 'r') && ((char) buffer1[j + 3] == 'o')
					&& ((char) buffer1[j + 4] == 'k') && ((char) buffer1[j + 5] == 'e')
					&& ((char) buffer1[j + 6] == '=') 
					&& ((char) buffer1[j + 7] == '"')
					&& ((char) buffer1[j + 8] == 'n')
					&& ((char) buffer1[j + 9] == 'o')
					&& ((char) buffer1[j + 10] == 'n')
					&& ((char) buffer1[j + 11] == 'e')
					&& ((char) buffer1[j + 12] == '"')) {
				//System.out.format("encontre stroke:# en posicion: %d\n", j);
				if(flag_path == true)flag_stroke = false;
				//System.out.format("****** stroke:# *********\n");
			}
			/*************************
			 * fin
			 ************************************************************/
			/***********
			 * Detecta el elemento: d="
			 *************************************************/
			if (flag_path && (j + 3 < (no_leidos - 1)) && ((char) buffer1[j] == ' ') && ((char) buffer1[j + 1] == 'd')
					&& ((char) buffer1[j + 2] == '=') && ((char) buffer1[j + 3] == '"') // detecta d="
			) {
				//System.out.format("encontre stroke:# en posicion: %d\n", j);
				flag_d = true;
				flag_comillas = true;
				k = 0;
				k_ini = (j + 3);
				//System.out.format("****** d=\" *********\n");
			}
			/**** flag_f:=TRUE; **/
			/***** flag_comillas:=TRUE; **/
			/****
			 * k_ini marca comienzo en buffer1 de datos de path
			 *******************************/
			/*****
			 * k cuenta el numero de datos validos de path
			 ***********************************/
			/***
			 * fin Detecta el elemento: d="
			 *****************************************************/

			/***********
			 * Entra en zona de datos validos de path
			 **************************************/
			if (flag_d) {
				//System.out.format("dentro de comillas datos validos %d %c\n", j + 3, (char) buffer1[j + 3]);
				k = k + 1;
			}
			;
			/***** k se aumenta en 1 ***********************************/
			/*****
			 * fin Entra en zona de datos validos de path
			 ***************************************/

			/***********
			 * Detecta el elemento: " final
			 *************************************************/
			/*******************
			 * Guarda en lists datos de datos validos de PATH
			 *************************/

			if (flag_comillas && flag_path && flag_d && (j + 1 < (no_leidos - 1)) && ((char) buffer1[j] != '=')
					&& ((char) buffer1[j + 1] == '"') // encontro " final
			) {
				//System.out.format("Encontre \" en posicion %d. Termino de datos validos\n", j + 1);

				datos_de_path.add(new PropiedadDeDatosPath(k, k_ini, (flag_stroke ? 1 : 0)));

				k = 0;
				no_path++;
				flag_path = false;
				flag_comillas = false;
				flag_d = false;
				flag_stroke = true;//vuelve a plena visibilidad

			}
			/*
			 * guarda k=no. de caracteres validos, en lista: "datos_de_path", como su primer
			 * elemento. resetea k=0; guarda k_ini=posicion en buffer1 donde comienzan datos
			 * validos, como segundo elemento en lista :"datos_de_path". guarda informacion
			 * si encontro stroke:# y lo guarda como tercer elemento de la lista: Guarda en:
			 * "datos_de_path_all" la lista anterior. Resetea flags de elementos del parser
			 * a cero Se termino un path!! fin de Guarda en lists datos de datos validos de
			 * PATH
			 **************************/
			/****************** fin de Detecta el elemento: Z=" *****************/

			j++;

		}

		/********** Verifica que archivo es svg ********/
		if (!flag_svg) {
			// verifica que el archivo es svg antes de escribir
			aviso("Archivo no es .svg");
		}
		;

		/*************
		 * Escribe en pantalla datos de cada path
		 *********************************/

		/*************
		 * Escribe en pantalla datos de cada path
		 *********************************/
		/*
		 * PropDedatosPath p1=datos_de_path_first; datos_de_path.size()
		 * if(p1==NULL)aviso("p1 es NULL"); for(;p1!=NULL;p1=p1->sH){
		 * printf("tamano escrito:.%d k_ini:%d encontro #:%d\n",p1->k,p1->k_ini,p1->vis)
		 * ; }
		 */
		/***************
		 * fin de Escribe en pantalla datos de cada path
		 *************************/

		/****************************************************************************************/
		/******* Recorrido de buffer1 y transferencia de datos a archivo ***********/
		/* NO ESCRIBE No de PATHS ni TAMANO de PATHS (ver test_filecpy.li) */

		if (no_path == 0) {
			System.out.format("datos de path all vacia\n");
		} else {
			// solo trabaja si hay datos a transferir

			// "no. de paths: ".print; (datos_de_path_all.count).println;//muestra en
			// pantalla el
			// No. de paths encintrado en buffer1

			File fileo = new File(nombre_svg);// nombre de archivo de salida con: "simp_" prepended

			DataOutputStream data_out = null;
			try {
				data_out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileo)));
			} catch (FileNotFoundException e) {
				aviso("No pude generar Data out_stream");
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			/********************
			 * Comienzo de transferencia de datos de path
			 **********************/
			/**
			 * Recorre lista de datos de paths y transfiere desde buffer1 a buffer. Luego
			 * escribe buffer en archivo
			 */
			for (int i = 0; i < datos_de_path.size(); i++) {

				k = datos_de_path.get(i).k;// obtiene tamano de datos de path
				k_ini = datos_de_path.get(i).k_ini;// obtiene posicion donde comienzan datos de path
				vis = datos_de_path.get(i).vis;// obtiene condicion de presencia de stroke:#

			//	System.out.format("**************total; %d\n", k);
			//	System.out.format("**************k_ini: %d\n", k_ini);
			//	System.out.format("**************vis  : %d\n", vis);

				/***************
				 * Comienzo de transferencia a archivo de sector de datos validos
				 **************/
				/************************
				 * a buffer . se transfieren comillas
				 *****************************/
				buffer = new byte[(int) (k + 2)];
				/*
				 * revisa si encontro stroke:# y coloca 1(si) o 0(no) en buffer como primer
				 * elemento
				 */
				if (vis == 1) {
					buffer[0] = '1';
					buffer[1] = '^';
				//	System.out.format("path con #\n");
				} else {
				//	System.out.format("path sin #\n");
					buffer[0] = '0';
					buffer[1] = '^';
				}

				/****
				 * tranfiere datos validos de path en buffer1 a buffer para escribirlos en
				 * archivo
				 ******/

				for (j = 0; j < k; j++) {
					buffer[j + 2] = buffer1[k_ini + j];
				}

				try {
					data_out.write(buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					aviso("No pude escribir datos  de out stream");
					// e.printStackTrace();
				}

				/****************
				 * Fin de transferencia de datos validos a archivo
				 ******************/
				buffer = null;// resetea buffer
			}

			try {// cierra archivo
				data_out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				aviso("No pude cerrar outstream");
				// e.printStackTrace();
			}
		}
		//System.out.println("Datos escritos en archivo: " + nombre_svg + "\n Hasta luego...");
		
		
		
		
		return nombre_svgPN;
		
		
	}

	private static void check1(Object p, String frase) {
		if (p == null) {
			System.out.format("%s. Salgo...\n", frase);
			System.exit(0);
		}

		return;
	}

	private static void aviso(String frase) {
		System.out.println(" Salgo..." + frase);
		System.exit(0);
		return;
	}

	private static void uso() {

		System.out.format("Uso:\nsimplexC_svg <nombre_svg>\no\nsimplexC_svg\n");
		System.out.format("Donde <nombre_svg>, es el nombre del archivo generado por inkscqpe\n");
		System.exit(0);
		return;
	}

}
