/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plantasvszombies;

/**
 *
 * @author marco_000
 */
public class Girasol implements Personaje{

    private int coste = 20;
    private int vida = 1;
    private int cX;
    private int cY;
    private int cicloCreacion;
    
    public Girasol(int cX,int cY, int cicloCreacion) {
        this.cY = cY;
        this.cicloCreacion = cicloCreacion;
        this.cX = cX;
    }
    
    public int getCoste() {
        return coste;
    }

  

    public int getX() {
        return cX;
    }

    public int getY() {
        return cY;
    }

    public void reducirVida() {
        this.vida = this.vida - 1;
    }
    
    public int getVida() {
        return vida;
    }
    
    public int generar(int ciclo){
        int soles = 0;
        
        if (ciclo != cicloCreacion && (((ciclo - cicloCreacion) % 2) == 0)){//cada dos ciclo se suman 10 soles de cada girasol
            soles = 10;
        }
        return soles;
    }
  
    @Override
    public String toString() {
        return "G(" +vida+ ")  "; //se imprime el girasol con la vida
    }
   
}
