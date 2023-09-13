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
public class Zombie implements Personaje{
    private int vida = 5;
    private int daño = 1;
    private int cY;
    private int cX;
    private int cicloCreacion;
    
     public Zombie( int cX, int cY, int cicloCreacion) {
        this.cX = cX;
        this.cY = cY;
        this.cicloCreacion = cicloCreacion;
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
    
    public boolean desplazar(int ciclo){
        boolean mueve = false;
        
        if (ciclo != cicloCreacion && (((ciclo - cicloCreacion) % 2) == 0)){// cada dos ciclos se desplaza
            this.cX = this.cX - 1;
            mueve = true;
        }
        
        return mueve;
    }
    
    public void reducirVida() {
        this.vida = this.vida - 1;
    }
    
    @Override
    public String toString() {
        return "Z(" +vida+ ")  ";
    }
}