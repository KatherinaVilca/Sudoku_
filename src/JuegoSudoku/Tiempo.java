package JuegoSudoku;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
/**
 * @author Katherina vilca
 *
 */
@SuppressWarnings("serial")
public class Tiempo extends javax.swing.JFrame{
	
	
	private Timer t;
	private int hora, minutos,segundos;
	private JPanel panel_tiempo ;
	private JLabel label[];
	private Celda celda[];
	
	/**
	 * Constructor de la clase tiempo
	 */
	public Tiempo() {
		
		inicializar_graficos();
		t=new Timer(1000,actualizar);
		t.start();
	}

	/**
	 * Inicializa las imagenes del tiempo
	 */
	private void inicializar_graficos() {
		
		label= new JLabel[8];
		celda=new Celda[8];
		panel_tiempo = new JPanel();
		panel_tiempo.setBounds(485, 500, 180, 23);
		panel_tiempo.setLayout(new GridLayout(0, 8, 0, 0));
		
		for(int i=0 ; i<8 ;i++) {
			label[i]= new JLabel();
			panel_tiempo.add(label[i]);
			JLabel l=label[i];
			
			if(  i==2 || i==5) {
				
				celda[i]= new Celda(12);
			}
			else celda[i]= new Celda(0);
			
			ImageIcon imagen= celda[i].getEntidadGrafica().getImagen();
			label[i].addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					
						reDimensionar(l, imagen);
				}
			});
		}
		hora = minutos = segundos = 0;
	}
	
	/**
	 * Redimensiona cada imagen al tamaño del panel
	 * @param label . Label donde ubicar la imagen
	 * @param grafico . Imagen correspondiente
	 */
	private void reDimensionar(JLabel label, ImageIcon grafico) {
		Image image = grafico.getImage();
		
		if (image != null) {  
			Image newimg = image.getScaledInstance(label.getWidth(), label.getHeight(), java.awt.Image.SCALE_SMOOTH);
			grafico.setImage(newimg);
			label.repaint();
			label.setIcon(grafico);
		}
	}
	
	/**
	 * Actualiza el tiempo y las imagenes de cada numero
	 */
	private ActionListener actualizar = new ActionListener(){

        public void actionPerformed(ActionEvent ae) {
                    
            if(segundos<59){
                segundos=segundos+1;
            }
            else {
            		if(minutos<59) {
            		minutos=minutos+1;
            		segundos=0;
            		}
            		else {
	            			hora=hora+1;
	            			minutos=0;
	            			segundos=0;	
	            		 }              
            }
            actualizarLabel();
        }    
    };
    
    
    /**
     * @return Retorna el panel de tiempo
     */
    public JPanel getPanel_t() {
    	return panel_tiempo;
    }
    
    /**
     * Actualiza cada imagen segun el tiempo transcurrido
     */
    private void actualizarLabel() {
    	
    	if(hora<= 9) {
			celda[0].actualizar_Imagen(0);
			celda[1].actualizar_Imagen(hora);
		}
		else {
				celda[0].actualizar_Imagen(hora/10);
				celda[1].actualizar_Imagen(hora%10);
			}
    	if(minutos<= 9) {
			celda[3].actualizar_Imagen(0);
			celda[4].actualizar_Imagen(minutos);
		}
		else {
				celda[3].actualizar_Imagen(minutos/10);
				celda[4].actualizar_Imagen(minutos%10);
			}
    	if(segundos<= 9) {
			celda[6].actualizar_Imagen(0);
			celda[7].actualizar_Imagen(segundos);
		}
		else { 
				celda[6].actualizar_Imagen(segundos/10);
				celda[7].actualizar_Imagen(segundos%10);
			}
    	
    	for(int i=0; i<8 ; i++){
    	    		
    		ImageIcon imagen= celda[i].getEntidadGrafica().getImagen();
    		reDimensionar(label[i], imagen);
    	}
    }
        
    /**
     *  Detiene el tiempo
     */
    public void stop() {
    	t.stop();
    }
   
    /**
     * Inicia el tiempo
     */
    public void start() {
    	t.start();
    }
    
    /**
     * Reinicia el tiempo de cero
     */
    public void restart() {
    	
    	hora = minutos = segundos = 0;
		t.restart();
	}
}