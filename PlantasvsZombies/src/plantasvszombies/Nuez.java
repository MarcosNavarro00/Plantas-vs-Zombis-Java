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
public class Nuez implements Personaje{
    
     
    private int coste = 50;
    private int vida = 10;
    private int cicloCreacion;
    
    private int cY;
    private int cX;
    
     public Nuez( int cX, int cY, int cicloCreacion) {
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
    public int getX() {
        return cX;
    }

    public int getY() {
        return cY;
    }
    
    public void reducirVida() {
        this.vida = this.vida - 1;
    }
  

   @Override
    public String toString() {
        return "N(" +vida+ ")  ";
    }
    
    
} 

