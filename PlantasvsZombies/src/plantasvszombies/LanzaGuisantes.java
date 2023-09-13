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
public class LanzaGuisantes implements Personaje{
     
    private int coste = 50;
    private int vida = 3;
    private int daño = 1;
    private int cicloCreacion;
    
    private int cY;
    private int cX;
    
     public LanzaGuisantes( int cX, int cY, int cicloCreacion) {
        this.cX = cX;
        this.cY = cY;
        this.cicloCreacion = cicloCreacion;
     }
    
    public int getCoste() {
        return coste;
    }

    public int getVida() {
        return vida;
    }

    public int getDaño() {
        return daño;
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
    
    public boolean disparar(int ciclo){
        boolean disparo = false;
        
        if (ciclo != cicloCreacion){
            disparo = true;
        }
        
        return disparo;
    }

   @Override
    public String toString() {
        return "L(" +vida+ ")  ";
    }
    
    
}


