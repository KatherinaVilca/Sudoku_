package JuegoSudoku;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Tablero  {
	
	private Celda[][] matriz;
	private int jugadas_realizadas;
	private boolean existe_error;
	private Celda [][] matriz_aux;
	
	public Tablero() {
		 this.matriz = new Celda[9][9];
		 jugadas_realizadas=0;
		 existe_error=false;
	
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				 
				matriz[i][j]= new Celda(i,j);
						
				if((0<=i && i<=2)&& (0<=j && j<=2) ){
					matriz[i][j].setCuadrante(1);
					}
				if((0<=i && i<=2) && (3<=j && j<=5)){
					matriz[i][j].setCuadrante(2);
					}
				if((0<=i && i<=2)&& (6<=j && j<=8)){
					matriz[i][j].setCuadrante(3);
					}
				if((3<=i && i<=5)&& (0<=j && j<=2) ){
					matriz[i][j].setCuadrante(4);
					}
				if((3<=i && i<=5) && (3<=j && j<=5)){
					matriz[i][j].setCuadrante(5);
					}
				if((3<=i && i<=5)&& (6<=j && j<=8)){
					matriz[i][j].setCuadrante(6);
					}
				if((6<=i && i<=8)&& (0<=j && j<=2) ){
					matriz[i][j].setCuadrante(7);
					}
				if((6<=i && i<=8) && (3<=j && j<=5)){
					matriz[i][j].setCuadrante(8);
					}
				if((6<=i && i<=8)&& (6<=j && j<=8)){
					matriz[i][j].setCuadrante(9);
					}	
			}
		}
	}
	
	public boolean iniciar_tablero(String a) {
		
		boolean es_valido=false;
		try {
			InputStream in = Tablero.class.getClassLoader().getResourceAsStream(a);
			InputStreamReader scn = new InputStreamReader(in);
			
			BufferedReader buffer= new BufferedReader(scn);
			String linea=buffer.readLine();
			int columna= 0;
			int fila=0;
			boolean cortar=false;
			boolean invalido=false; 
			while (linea != null) {    
				
				for(int i=0; i<linea.length() && !cortar && !invalido; i++) {
						Character leido=linea.charAt(i);
					
							if(leido!=' ') {
								 if( !(nro_entero(leido)==99)) {
									matriz[fila][columna].setElement(nro_entero(leido));
									matriz[fila][columna].actualizar_Imagen(nro_entero(leido));
									matriz[fila][columna].setCeldaInicial(true);
									columna= columna+1;
									}
								 else invalido=true;
							}
							if(columna==9) cortar=true;
						}
				
						linea= buffer.readLine();
						cortar=false;
						fila=fila+1;
						columna=0;						
					}			
				
			buffer.close();
			
			if(validar_tablero() && !invalido) {
				es_valido=true;
				matriz_aux= new Celda[9][9];
				clonar_matriz(matriz,matriz_aux);
				estado_inicial();
			}
			
			else borrar_tablero();
		} catch ( IOException e) { System.out.println(e.getMessage());} 
		return es_valido;
	}
			
	private void borrar_tablero() {
		
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				matriz[i][j]=null;
			}
		}
	}
	
	private void estado_inicial() {
		
		Random r= new Random();
		int fila= 0;
		int columna=0;
		int contador=0;
		
		while(contador<57) {
			fila=0+r.nextInt(9);
			columna=0+r.nextInt(9);
			Celda celda=matriz[fila][columna];
			
				if(celda.getElement()!=0) {

					celda.setElement(0);
					contador=contador+1;
					celda.actualizar_Imagen(11); //pone imagen vacia
					celda.setCeldaJugada(false);
					celda.setCeldaInicial(false);
			}
		}
		jugadas_realizadas=81-contador;
	}
	
	private boolean validar_tablero() {
		
		boolean es_valida=true;
		
		for(int i=0; i<9 && es_valida ; i++) {
			for(int j=0; j<9 && es_valida; j++) {
				
				matriz[i][j].setCeldaEnCuestion(true);
				
				if( !validar_jugada(i,j, matriz[i][j].getCuadrante(),matriz[i][j].getElement())) {
					
					es_valida=false; // no es valida
					matriz[i][j].setCeldaEnCuestion(false);
				}
				
				matriz[i][j].setCeldaEnCuestion(false);
			}
		}
		return es_valida; 
	}
	
	private boolean validar_jugada(int fila, int columna, int cuadrante ,int elemento) {
		
		boolean validez_fila=validar_fila(fila,elemento);
		boolean validez_columna=validar_columna(columna,elemento) ;
		boolean validez_cuadrante=validar_cuadrante(cuadrante,elemento);
		
		return (validez_fila  && validez_columna && validez_cuadrante)? true:false;
	}
	
	private boolean validar_fila(int fila, int jugada) {
	
		boolean ret=true;
		
		for(int i=0; i<9 && ret; i++) {
			if( (!matriz[fila][i].getCeldaEnCuestion() &&  matriz[fila][i].getElement() == jugada ) ) {
				
				ret=false; //la jugada se repite
				matriz[fila][i].setConflicto(true);
			}
		}
		return ret;
	}
	
	private boolean validar_columna(int columna, int jugada) {
		
		boolean ret=true;
		
		for(int i=0; i<9 && ret; i++) {
			if( ( !matriz[i][columna].getCeldaEnCuestion() && matriz[i][columna].getElement() == jugada) ) {
				
				ret=false;
				matriz[i][columna].setConflicto(true);
			}
		}
		return ret;
	}
	
	private boolean validar_cuadrante(int cuadrante, int jugada) {
		
		boolean ret=true;
		int fila1=0;
		int fila2=0;
		int columna1=0;
		int columna2=0;
		
		switch(cuadrante) {
		case 1:  {fila2=3; break;}
		case 2:  {fila2=3; break;}
		case 3:  {fila2=3; break;}
		case 4:  {fila1=3;fila2=6; break;}
		case 5:  {fila1=3;fila2=6; break;}
		case 6:  {fila1=3;fila2=6; break;}
		case 7:  {fila1=6;fila2=9; break;}
		case 8:  {fila1=6;fila2=9; break;}
		case 9:  {fila1=6;fila2=9; break;}
		}
		
		switch(cuadrante) {
		case 1:  {columna2=3; break;}
		case 2:  {columna1=3; columna2=6; break;}
		case 3:  {columna1=6; columna2=9; break;}
		case 4:  {columna2=3; break;}
		case 5:  {columna1=3; columna2=6; break;}
		case 6:  {columna1=6; columna2=9; break;}
		case 7:  {columna2=3; break;}
		case 8:  {columna1=3; columna2=6; break;}
		case 9:  {columna1=6; columna2=9; break;}
		}
		
		for(int i= fila1; (i<fila2 && ret); i++) {
			for(int j= columna1; (j<columna2 && ret); j++) {
				if((!matriz[i][j].getCeldaEnCuestion() && matriz[i][j].getElement() == jugada )) {
					
					ret=false; 
					matriz[i][j].setConflicto(true);
				}
			}
		}
		return ret;
	}
	 
	public void agregar_jugada_tablero(int jugada, Celda celda) {
		
		int fila= celda.getFila();
		int columna=celda.getColumna();
		int cuadrante=celda.getCuadrante();
		boolean validacion= validar_jugada(fila,columna,cuadrante,jugada);
		matriz[fila][columna].setCeldaJugada(true);
		matriz[fila][columna].setElement(jugada);
		
		if(validacion) {
			
			matriz[fila][columna].actualizar_Imagen(jugada);
			jugadas_realizadas=jugadas_realizadas+1;	
		}
		else {
				matriz[fila][columna].setConflicto(true);
				matriz[fila][columna].actualizar_Imagen(10); 
				existe_error=true;	
		}
	}
	
	public void deshacer_jugada(Celda c) {
	
		int fila= c.getFila();
		int col=c.getColumna();
		
		matriz[fila][col].actualizar_Imagen(11); //imagen inicial
		matriz[fila][col].setCeldaJugada(false);//se puede jugar en la celda	
			
		if(!c.getConflicto()) {
			 //si quiero deshacer una jugada bien
			jugadas_realizadas=jugadas_realizadas-1;
			
		}
		else { 
				for (int i=0; i<9 ; i++) {
					for (int j=0; j<9 ; j++) {
					
						if(matriz[i][j].getConflicto() && matriz[i][j].getElement()==c.getElement()) {
							matriz[i][j].setConflicto(false);
						}
					}
				}
				existe_error=false;
			}
		matriz[fila][col].setElement(0);
	}		
	
	private int nro_entero(Character c) {
		
		switch(c) {
		case '1':  return 1;
		case '2':  return 2;
		case '3':  return 3;
		case '4':  return 4;
		case '5':  return 5;
		case '6':  return 6;
		case '7':  return 7;
		case '8':  return 8;
		case '9':  return 9;
		default: return 99;
      }
	}
	
	public boolean termine_juego() {
		
		return jugadas_realizadas==81;
	}

	public Celda getCelda(int fila, int columna) {
		
		return matriz[fila][columna];
	}

	public Celda getCeldaOpcion(int i) {
		return new Celda(i);
	}
 
	public boolean getExisteError() {
		return existe_error;
	}
	
	public void setExisteError(boolean e) {
		existe_error=e;
	}
	
	private void clonar_matriz(Celda[][] m1, Celda[][] m2){
		
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				m2[i][j]= new Celda( m1[i][j].getFila(),m1[i][j].getColumna()) ;
				m2[i][j].setElement(m1[i][j].getElement());
				m2[i][j].setCeldaInicial(m1[i][j].getCeldaInicial());
				m2[i][j].actualizar_Imagen(m1[i][j].getElement());	
				m2[i][j].setCuadrante(m1[i][j].getCuadrante());
			}
		}
	}
	
	public void reiniciar_juego() {
		
		jugadas_realizadas=0;
		clonar_matriz(matriz_aux,matriz);
		estado_inicial();
		existe_error=false;
	}
	
}
