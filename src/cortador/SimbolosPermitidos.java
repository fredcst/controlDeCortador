package cortador;

public class SimbolosPermitidos {

	public static boolean ES_DIGITO(char C) {
		return (((C) == '0') || ((C) == '1') || ((C) == '2') || ((C) == '3') || ((C) == '4') || ((C) == '5')
				|| ((C) == '6') || ((C) == '7') || ((C) == '8') || ((C) == '9'));
	}

	public static boolean ES_PUNTO(char C) {
		return (C) == '.';
	}

	public static boolean ES_COMA(char C) {
		return (C) == ',';
	}
	public static boolean ES_COMILLAS(char C) {
		return (C) == '"';
	}
	public static boolean ES_MINUSCULA(char C) {
		return (((C) == 'a') || ((C) == 'b') || ((C) == 'c') || ((C) == 'd') || ((C) == 'e') || ((C) == 'f')
				|| ((C) == 'g') || ((C) == 'h') || ((C) == 'i') || ((C) == 'j') || ((C) == 'k') || ((C) == 'l')
				|| ((C) == 'm') || ((C) == 'n') || ((C) == 'o') || ((C) == 'p') || ((C) == 'q') || ((C) == 'r')
				|| ((C) == 's') || ((C) == 't') || ((C) == 'u') || ((C) == 'v') || ((C) == 'w') || ((C) == 'x')
				|| ((C) == 'y') || ((C) == 'z'));
	}

	public static boolean ES_MAYUSCULA(char C) {
		return (((C) == 'A') || ((C) == 'B') || ((C) == 'C') || ((C) == 'D') || ((C) == 'E') || ((C) == 'F')
				|| ((C) == 'G') || ((C) == 'H') || ((C) == 'I') || ((C) == 'J') || ((C) == 'K') || ((C) == 'L')
				|| ((C) == 'M') || ((C) == 'N') || ((C) == 'O') || ((C) == 'P') || ((C) == 'Q') || ((C) == 'R')
				|| ((C) == 'S') || ((C) == 'T') || ((C) == 'U') || ((C) == 'V') || ((C) == 'W') || ((C) == 'X')
				|| ((C) == 'Y') || ((C) == 'Z'));
	}

	public static boolean ES_ESPACIO(char C) {
		return ((C) == ' ');

	}

	public static boolean ES_TAB(char C) {
		return ((C) == '\t');
	}

	public static boolean ES_NEWLINE(char C) {
		return ((C) == '\n');
	}

	public static boolean ES_SIGNO(char C) {
		return ((C) == '-');
	}

	public static boolean ES_LETRA(char C) {
		return (ES_MINUSCULA((C)) || ES_MAYUSCULA((C)));
	}

	public static boolean NO_ES_PERMITIDO(char C) {
		return ES_LETRA(C) && (!(((C) == 'c') || ((C) == 'C') || ((C) == 'q') || ((C) == 'Q') || ((C) == 'M')
				|| ((C) == 'm') || ((C) == 'l') || ((C) == 'L') || ((C) == 'z') || ((C) == 'Z') || ((C) == 'h')
				|| ((C) == 'H') || ((C) == 'v') || ((C) == 'V')))

		;
	}
	/*
	 * #define ES_PUNTO( C ) ( (C)=='.' )
	 * 
	 * #define ES_COMA( C ) ( (C)==',' )
	 * 
	 * #define ES_MINUSCULA( C ) ( ( (C)=='a' )|| ( (C)=='b' )|| ( (C)=='c' )|| (
	 * (C)=='d' )|| ( (C)=='e' )|| ( (C)=='f' )|| ( C=='g' )|| ( C=='h' )|| (
	 * (C)=='i' )||( (C)=='j' )|| ( (C)=='k' )|| ( (C)=='l' )|| ( (C)=='m' )|| (
	 * (C)=='n' )|| ( (C)=='o' )|| ( (C)=='p' )|| ( (C)=='q' )|| ( (C)=='r' )|| (
	 * (C)=='s' )|| ( (C)=='t' )|| ( (C)=='u' )|| ( (C)=='v' )|| ( (C)=='w' )||(
	 * (C)=='x' )|| ( (C)=='y' )|| ( (C)=='z' ))
	 * 
	 * #define ES_MAYUSCULA( C ) ( ( (C)=='A' )|| ( (C)=='B' )|| ( (C)=='C' )|| (
	 * (C)=='D' )|| ( (C)=='E' )|| ( (C)=='F' )|| ( (C)=='G' )|| ( (C)=='H' )|| (
	 * (C)=='I' )|| ( (C)=='J' )|| ( (C)=='K' )|| ( (C)=='L' )|| ( (C)=='M' )|| (
	 * (C)=='N' )|| ( (C)=='O' )|| ( (C)=='P' )|| ( (C)=='Q' )|| ( (C)=='R' )|| (
	 * (C)=='S' )|| ( (C)=='T' )|| ( (C)=='U' )|| ( (C)=='V' )|| ( (C)=='W' )|| (
	 * (C)=='X' )|| ( (C)=='Y' )|| ( (C)=='Z' )) #define ES_COMILLAS( C ) ( (C)=='"'
	 * )
	 * 
	 * #define ES_ESPACIO( C ) ( (C)==' ' ) #define ES_TAB( C ) ( (C)=='\t' )
	 * #define ES_NEWLINE( C ) ( (C)=='\n' ) #define MALLOC(type)
	 * ((type*)malloc(sizeof(type))) #define ES_SIGNO( C ) ( (C)=='-') #define
	 * ES_LETRA( C ) (ES_MINUSCULA( (C) )||ES_MAYUSCULA( (C) )) #define
	 * NO_ES_PERMITIDO( C ) ES_LETRA( C )&&( !(( (C)=='c' )||( (C)== 'C' )||( (C)==
	 * 'q' )||( (C)=='Q' )||( (C)=='M' )||( (C)=='m' )||( (C) == 'l')||( (C)== 'L'
	 * )||( (C) == 'z')||( (C)== 'Z' )||( (C) == 'h')||( (C)== 'H' )||( (C) ==
	 * 'v')||( (C)== 'V' ) )) #define TRUE 1 #define FALSE 0
	 * 
	 * 
	 * 
	 */

}
