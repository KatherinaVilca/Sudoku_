package GUI_Juego;

import java.awt.Color;
import JuegoSudoku.*;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class GUI extends JFrame {

	private Tablero juego;
	
	private Tiempo tiempo;
	
	private JPanel contentPane;
	private JPanel panel_casillas;
	private JPanel panel_opciones;
	private JPanel paneles[][];
	private JPanel panel_numeracion_columnas;
	private JPanel panel_numeracion_filas;
	
	private JButton btnDeshacerJugada;
	private JButton btnPausarJuego;
	private JButton btnIniciarJuego;
	private JButton btnReiniciarJuego;
	private JButton boton[][];
	
	private JLabel label[][];
	private JLabel titulo_juego;
	private JLabel lblFila;
	private JLabel lblColumna;
	private JLabel lblIndiqueFilaY;
	
	private JTextField ingreso_fila;
	private JTextField ingreso_columna;
	
	private boolean detuve_tiempo;
	private int jugada;
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		
		inicializacion_paneles();
		
		//******** TITULO
		titulo_juego = new JLabel("                 SUDOKU");
		titulo_juego.setForeground(Color.BLACK);
		titulo_juego.setFont(new Font("Sitka Text", Font.PLAIN, 22));
		titulo_juego.setBounds(214, 0, 298, 38);
		contentPane.add(titulo_juego);
				
		inicializacion_botones();
		inicializacion_funcion_eliminar();
				
		//SUBPANELES
		paneles= new JPanel [3][3];
		label= new JLabel[9][9];	
		boton=new JButton[3][3];
	
		//*****JUEGO	
		juego = new Tablero();
		boolean se_inicia= juego.iniciar_tablero("Solucion/Solucion.txt");
		
		if( !se_inicia) {

			btnDeshacerJugada.setVisible(false);
			btnPausarJuego.setVisible(false);
			btnIniciarJuego.setVisible(false);
			ingreso_fila.setVisible(false);
			ingreso_columna.setVisible(false);
			panel_numeracion_columnas.setVisible(false);
			panel_numeracion_filas.setVisible(false);
			lblIndiqueFilaY.setVisible(false);
			lblColumna.setVisible(false);
			lblFila.setVisible(false);
		}
		else { 
				habilitar_funcionalidades(false);
				btnReiniciarJuego.setVisible(false);
				
				btnIniciarJuego.addActionListener(new ActionListener() {
				
					public void actionPerformed(ActionEvent e) {
						
						if(detuve_tiempo) { // **** REINICIAR EL JUEGO ***
							tiempo.start();
							detuve_tiempo=false;
						
							if(juego.getExisteError()) 								
								estado_botones(false);
							else estado_botones(true);
						}					
						else {// **** INICIA NUEVO JUEGO***
								tiempo= new Tiempo();
								contentPane.add(tiempo.getPanel_t());
								panel_de_opciones();
								iniciar_subpaneles();
								iniciar_imagenes();
								estado_botones(true);
								jugar();
								
							}	
						estado_casillas(true);
						habilitar_funcionalidades(true);
						btnIniciarJuego.setEnabled(false);
						
						btnReiniciarJuego.setVisible(true);
					}
				});
				
				btnPausarJuego.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						tiempo.stop();
						estado_casillas(false);
						estado_botones(false);
						detuve_tiempo=true;
						habilitar_funcionalidades(false);
						btnIniciarJuego.setEnabled(true);
					}
				});
				
				btnReiniciarJuego.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e2) {
					
						tiempo.restart();
						juego.reiniciar_juego();
						habilitar_funcionalidades(true);
						
						for(int i=0;i<9 ;i++) {
							int m=(int) i/3;
							for(int j=0; j<9;j++) {
								int n=(int) j/3;
								paneles[m][n].remove(label[i][j]);
							}
						}
						iniciar_imagenes();
						jugar();
					}
				
				});
		}
	}

	private void jugar() {
		
		for(int d=0; d<9;d++) {
			for(int e=0; e<9; e++) {
				Celda celda = juego.getCelda(d,e);				
					accion_agregar(celda);
				}
			}
			accion_borrar();	
	}
	
	private void iniciar_imagenes() {
		
		for(int i=0;i<9;i++) {
			
			int m=(int) i/3;
		
			for(int j=0 ;j<9; j++) {
				
				int n=(int) j/3;
				Celda celda = juego.getCelda(i,j);
				ImageIcon grafico = celda.getEntidadGrafica().getImagen();
				label [i][j]= new JLabel();
				paneles[m][n].add(label[i][j]);
				
				label[celda.getFila()][celda.getColumna()].addComponentListener(new ComponentAdapter() {
					public void componentResized(ComponentEvent e) {
						reDimensionar(label[celda.getFila()][celda.getColumna()], grafico);
					}
				});
				if(celda.getCeldaInicial()) {
					
					label[i][j].setBorder(BorderFactory.createEtchedBorder());
				 }
			
			}
		}
	}
	
	private void iniciar_subpaneles() {
		
		for(int i=0;i<3;i++) { //para jugar 1 poner a panel casillas en 3
			for(int j=0 ;j<3; j++) {
				paneles[i][j]= new JPanel();
				 paneles[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				 paneles[i][j].setLayout(new GridLayout(3, 0, 2, 2));
				 panel_casillas.add(paneles[i][j]);		
			}
		}
		
	}
	
	private void inicializacion_paneles() {
		
		//**** VENTANA PRINCIPAL ****
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 721, 737);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);
				
		//***** PANEL CASILLAS ****
		panel_casillas = new JPanel();
		panel_casillas.setBounds(157, 60, 401, 422);
		contentPane.add(panel_casillas);
		panel_casillas.setLayout(new GridLayout(3, 0, 2, 2));
		panel_casillas.setVisible(true);
				
		//******* PANEL OPCIONES ******
		panel_opciones = new JPanel();
		panel_opciones.setBounds(250, 507, 220, 180);
		contentPane.add(panel_opciones);
		panel_opciones.setLayout(new GridLayout(3, 3, 0, 0));
		
		//*** PANEL NUMERACiON FILAS
		panel_numeracion_filas = new JPanel();
		panel_numeracion_filas.setBackground(Color.WHITE);
		panel_numeracion_filas.setBounds(137, 60, 15, 422);
		contentPane.add(panel_numeracion_filas);
		panel_numeracion_filas.setLayout(new GridLayout(0, 1, 9, 0));
		
		for(int i=1; i<10; i++) {
			panel_numeracion_filas.add(new JLabel(" "+i));
		}
						
		//****** PANEL NUMERACION COLUMNAS
		panel_numeracion_columnas = new JPanel();
		panel_numeracion_columnas.setBackground(Color.WHITE);
		panel_numeracion_columnas.setBounds(157, 40, 401, 14);
		contentPane.add(panel_numeracion_columnas);
		panel_numeracion_columnas.setLayout(new GridLayout(0, 9, 1, 0));
				
		for(int i=1; i<10; i++) {
			panel_numeracion_columnas.add(new JLabel("      "+i+"  "));
		}
		
	}
	
	private void inicializacion_botones() {
		
		//******** BOTON INICIAR JUEGO
		btnIniciarJuego = new JButton("Iniciar juego");
		btnIniciarJuego.setBounds(493, 544, 164, 32);
		contentPane.add(btnIniciarJuego);
				
		//******** BOTON PAUSAR JUEGO
		btnPausarJuego = new JButton("Pausar juego");
		btnPausarJuego.setBounds(493, 590, 164, 32);
		contentPane.add(btnPausarJuego);
		
		//******** BOTON REINICIAR
		btnReiniciarJuego = new JButton("Reiniciar juego");
		btnReiniciarJuego.setBounds(509, 647, 130, 26);
		contentPane.add(btnReiniciarJuego);
	}

	private void inicializacion_funcion_eliminar() {
		
		//***** CASILLA DE INGRESO FILA
		ingreso_fila = new JTextField();
		ingreso_fila.setBounds(125, 535, 86, 20);
		contentPane.add(ingreso_fila);
		ingreso_fila.setColumns(10);
				
		// ***** CASILLA DE INGRESO COLUMNA
		ingreso_columna = new JTextField();
		ingreso_columna.setBounds(125, 566, 86, 20);
		contentPane.add(ingreso_columna);
		ingreso_columna.setColumns(10);
				
		//**** LABEL FILA
		lblFila = new JLabel("Fila:");
		lblFila.setBounds(23, 535, 86, 20);
		contentPane.add(lblFila);
		
		//*** LABEL COLUMNA
		lblColumna = new JLabel("Columna:");
		lblColumna.setBounds(23, 572, 97, 14);
		contentPane.add(lblColumna);
			
		// ****** SUBTITULO
		lblIndiqueFilaY = new JLabel("Indique fila y columna de la jugada a eliminar");
		lblIndiqueFilaY.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblIndiqueFilaY.setBounds(10, 498, 234, 26);
		contentPane.add(lblIndiqueFilaY);
		
		//******* BOTON DESHACER JUGADA
		btnDeshacerJugada = new JButton("Deshacer jugada");
		btnDeshacerJugada.setBounds(55, 597, 130, 41);
		contentPane.add(btnDeshacerJugada);
	}
	
	private void accion_agregar(Celda celda) {
		
		label[celda.getFila()][celda.getColumna()].addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				
				if(jugada!= 0 && !celda.getConflicto() && !celda.getCeldaJugada() ) { // seleccione una opcion
					
					juego.agregar_jugada_tablero(jugada,celda);
					reDimensionar(label[celda.getFila()][celda.getColumna()],celda.getEntidadGrafica().getImagen());
					jugada=0;
					actualizar_labels(label,juego);
					
					if(juego.getExisteError()) {
					
						estado_botones(false);
						btnReiniciarJuego.setEnabled(false);
						
						JOptionPane.showMessageDialog(null,"Debe eliminar la jugada erronea antes de continuar");
					}
					
					if(juego.termine_juego()) {
						JOptionPane.showMessageDialog(null,"¡Juego completado!");
						contentPane.setVisible(false);
					}
				}
			}
		});
	}
	
	private void accion_borrar() {
				
		btnDeshacerJugada.addActionListener(new ActionListener() {
							
			public void actionPerformed(ActionEvent e2) {
		
					String b =ingreso_fila.getText(); //fila
					ingreso_fila.setText(null);
	
					String c =ingreso_columna.getText(); //columna
					ingreso_columna.setText(null);
				
					boolean es_numero1=isNumeric(b);
					boolean es_numero2 =isNumeric(c);
				
					if(es_numero1 && es_numero2) {
						
						Integer b1=Integer.valueOf(b);
						Integer c1=Integer.valueOf(c);
			
						if( (1<=b1 && b1<=9)&& (1<=c1 && c1<=9 )) {
				
							Celda celda=juego.getCelda(b1-1,c1-1);
						
							if(!celda.getCeldaInicial() && celda.getCeldaJugada()) {
						
								juego.deshacer_jugada(celda);
								ImageIcon grafic = celda.getEntidadGrafica().getImagen();										
								reDimensionar(label[celda.getFila()][celda.getColumna()],grafic);	
								actualizar_labels(label,juego);
								estado_botones(true);
								btnReiniciarJuego.setEnabled(true);
							}
							else JOptionPane.showMessageDialog(null," No puede borrar una jugada pre-establecida");
						}
						else JOptionPane.showMessageDialog(null,"Ingrese numeros entre 1 y 9");
					}
			}
		});
		juego.setExisteError(false);
	}
	
	private void habilitar_funcionalidades(boolean b) {
		
		btnDeshacerJugada.setEnabled(b);
		btnPausarJuego.setEnabled(b);
		ingreso_fila.setEnabled(b);
		ingreso_columna.setEnabled(b);
	}

	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	private void estado_casillas(boolean e) {
		
		for (int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				label[i][j].setEnabled(e);
			}
		}
	}
	
	private void estado_botones(boolean e) {
		
			for (int i=0; i<3; i++) {
				for(int j=0; j<3; j++) {
					boton[i][j].setEnabled(e);
				}
			}
	}
	
	private void actualizar_labels(JLabel[][] l, Tablero juego) {
		
		for (int i=0 ; i<9 ; i++) {
			for (int j=0; j<9; j++) {
				Celda celda=juego.getCelda(i,j);
				if (celda.getConflicto()) {
					label[i][j].setBackground(Color.RED);
				} 
				else					
					label[i][j].setBackground(Color.WHITE);
			}
		}
	}
	
	private void panel_de_opciones() {
		
		int cont=1;
		for(int i =0; i<3; i++) {
			for(int j=0; j<3 ; j++) {
				
				boton [i][j]= new JButton();
				panel_opciones.add(boton[i][j]);
				Celda celda=juego.getCeldaOpcion(cont);
				ImageIcon grafico = celda.getEntidadGrafica().get_imagenes_disponibles(cont);
				JButton b=boton[i][j];	
				
				boton[i][j].addComponentListener(new ComponentAdapter() {
					public void componentResized(ComponentEvent e) {
						reDimensionarBoton(grafico,b.getWidth(), b.getHeight());
						b.repaint();
						b.setIcon(grafico);
					}
				});
				cont=cont+1;	
				
				boton[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						jugada= celda.getElement();
					}
				});
			}
		}
	}

	private void reDimensionar(JLabel label, ImageIcon grafico) {
		
		Image image = grafico.getImage();
		if (image != null) {  
			Image newimg = image.getScaledInstance(label.getWidth(), label.getHeight()-3, java.awt.Image.SCALE_SMOOTH);
			grafico.setImage(newimg);
			
			label.repaint();
			label.setIcon(grafico);
		}
	}
	
	private void reDimensionarBoton(ImageIcon grafico,int w,int h) {
		Image image = grafico.getImage();
		if (image != null) {  
			Image newimg = image.getScaledInstance(w,h, Image.SCALE_SMOOTH);
			grafico.setImage(newimg);
		}
	}
}