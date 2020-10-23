package JuegoSudoku;
import javax.swing.ImageIcon;


/**
 * @author Katherina Vilca
 *
 */
public class EntidadGrafica {
	private ImageIcon imagen;
	private String[] secuencia_numerica;
	
	
	/**
	 * Constructor de Entidad Grafica
	 */
	public EntidadGrafica() {
		imagen= new ImageIcon();
		secuencia_numerica= new String[13] ;
		secuencia_numerica[0]="/ImagenesTiempo/cero.png";
		secuencia_numerica[1]="/Imagenes/n1.jpg";
		secuencia_numerica[2]="/Imagenes/n2.jpg";
		secuencia_numerica[3]="/Imagenes/n3.jpg";
		secuencia_numerica[4]="/Imagenes/n4.jpg";
		secuencia_numerica[5]="/Imagenes/n5.jpg";
		secuencia_numerica[6]="/Imagenes/n6.jpg";
		secuencia_numerica[7]="/Imagenes/n7.jpg";
		secuencia_numerica[8]="/Imagenes/n8.jpg";
		secuencia_numerica[9]="/Imagenes/n9.jpg";
		secuencia_numerica[10]="/Imagenes/x.jpg";
		secuencia_numerica[11]="/Imagenes/celda_vacia.jpg";
		secuencia_numerica[12]="/ImagenesTiempo/puntos.jpg";
	
	}
	
	/**
	 * Actualiza la imagen con la ubicacion de cierta imagen i
	 * @param i . Locacion de la imagen nueva
	 */
	public void actualizar_Imagen(int i) {
		
		if(i<secuencia_numerica.length+1) {
				
			ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(secuencia_numerica[i]) ); 		
			this.imagen.setImage(imageIcon.getImage());
		}
	}
		
	/**
	 * Modifica la imagen actual
	 * @param imagen . Imagen a colocar
	 */
	public void setImagen(ImageIcon imagen) {
		this.imagen = imagen;
	}
	
	/** 
	 * Retorna imagen actual
	 * @return imagen.
	 */
	public ImageIcon getImagen() {
		return imagen;
	}
	
	/**
	 * Retorna una coleccion de imagenes disponibles
	 * @return coleccion de imagenes
	 */
	public String[] getImagenes() {
		return secuencia_numerica;
	}
	
	/**
	 * Retorna una imagen especifica i
	 * @param i . Ubicacion de la imagen
	 * @return Retorna imagen
	 */
	public ImageIcon get_imagenes_disponibles(int i) {
		return new ImageIcon(this.getClass().getResource(secuencia_numerica[i])) ;
	}
}
