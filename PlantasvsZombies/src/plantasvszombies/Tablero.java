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

import java.util.Scanner;
import java.io.*;
import java.util.Locale;
import java.util.ArrayList; 
import java.util.Random;
import java.util.Collections; 
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import plantasvszombies.Interface.Ventana;
import plantasvszombies.Personaje;
import plantasvszombies.Interface.PrimeraVentana;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*; 
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablero {
    public static boolean hayZombies = true;
    public static boolean partidaPerdida = false;
            
    private static HashMap<String, ArrayList<Integer>> baja = new HashMap<String, ArrayList<Integer>>();
    private static HashMap<String, ArrayList<Integer>> media = new HashMap<String, ArrayList<Integer>>();
    private static HashMap<String, ArrayList<Integer>> alta = new HashMap<String, ArrayList<Integer>>();
    private static HashMap<String, ArrayList<Integer>> imposible = new HashMap<String, ArrayList<Integer>>();
       
    
    public static void main(String args[]) throws InterruptedException{
        int columnas;
        int filas;
        int nZombies = 0;
        int turnosNoZombies = 0;
        int turnosMax = 30;
        int soles = 50;
        int puntos = 0;
        int counterPartidasGanadas = 0;
        int counterPartidasJugadas = 0;
        
        
        String DNI = null;
        String nombre = null;
        String palabra = null;
        
        TimeUnit time = TimeUnit.SECONDS;
        long timeToSleep = 1;
        
        ArrayList<Personaje> personajes = new ArrayList<>();
        ArrayList<Integer> rondasZombies = new ArrayList<>();
        int ciclo = 0;
        Random rand = new Random();
        Scanner  entrada = new Scanner (System.in); 
        
        cargarDatos();

        PrimeraVentana vent2 = new PrimeraVentana(); 
        vent2.show();
        
        while(nombre == null){
            time.sleep(timeToSleep);
            nombre = vent2.getNombre();
            DNI = vent2.getDNI();
            palabra = vent2.getNivel();

        }
        vent2.dispose();

        
        Ventana vent1 = new Ventana(soles,ciclo,nombre,baja,media,alta,imposible);
        vent1.show();
        
    
        
        counterPartidasJugadas++;
        filas = 7;
        columnas = 7;
        
        
        if("BAJA".equals(palabra)){
            
            nZombies = 5;//zombies totales
            turnosNoZombies = 10;//rondas iniciales sin zombies
        }
        if("MEDIA".equals(palabra)){
           nZombies = 15;
           turnosNoZombies = 7;
        }
        if("ALTA".equals(palabra)){
           nZombies = 25;
           turnosNoZombies = 5;
        }
        if("IMPOSIBLE".equals(palabra)){
           nZombies = 50;
           turnosNoZombies = 5;

        }
        if(!"IMPOSIBLE".equals(palabra) && (!"ALTA".equals(palabra)) && (!"MEDIA".equals(palabra)) && (!"BAJA".equals(palabra))) {

           return;
        }

        Personaje matrizPersonajes [][]= new Personaje[filas][columnas];
        inicializarMatriz(matrizPersonajes, filas, columnas);
         
        for (int i=0; i < nZombies; i++) {// se generan todos los zombies de la partida indicando en que ciclo se genera cada uno
           int r= rand.nextInt(29)+turnosNoZombies;
           rondasZombies.add(i,r);// se introduce en un array
        }
        Collections.sort(rondasZombies);// se ordena el array
        
        
         //**********************************//
         
         mostrarTablero ( columnas,  filas,  matrizPersonajes,  vent1 );
         
         while (hayZombies==true && partidaPerdida == false){//bucle donde se hara cada ciclo de la partida hasta que los booleans se diferentes
             
            
            soles = comandos ( soles,  ciclo,  filas,  columnas,  matrizPersonajes, vent1, time, timeToSleep );
            soles = posicionPersonajes ( matrizPersonajes ,  filas,  columnas,  ciclo, soles, vent1);
            vent1.setSoles(soles);

            generarZombie ( columnas, matrizPersonajes , filas, rondasZombies, ciclo);
            comprobarMuertos ( matrizPersonajes ,  filas,  columnas,  ciclo);
            ciclo++;
            comprobarZombies ( filas,  columnas,  matrizPersonajes,  ciclo, puntos, soles, vent1);
            mostrarTablero ( columnas,  filas,  matrizPersonajes, vent1 );
            //
            vent1.setCiclo(ciclo);
        }
        if (partidaPerdida == false){
            puntos = puntos ( filas,  columnas,  matrizPersonajes,  ciclo, puntos,  soles,  vent1 );
        }
        
        setDatos(palabra, nombre, puntos);
        guardarDatos ();
        vent1.dispose();

    }
          //************************//  
    public static void inicializarMatriz(Personaje matrizPersonajes [][], int filas, int columnas){
        for (int i=0; i < filas; i++) {
            for (int j=0; j < columnas; j++) {
                matrizPersonajes[i][j] =  null;// iguala todas las posiciones de la matriz a null
            }
        }
    }
    
    public static boolean comprobarPosicion (Personaje matrizPersonajes [][], int fila, int columna){
        boolean disponible = true;
      
        if (matrizPersonajes[fila][columna] != null){//se comprueba que en cada posicion si es diferente a null significa que hay algun personaje
            disponible = false;
        }
        
        return disponible;
    }
    public static void generarZombie (int columnas, Personaje matrizPersonajes [][],int filas, ArrayList<Integer> rondasZombies, int ciclo){
        Random rand = new Random();
        int libresEspacios = 0;
        int numeroZombiesCiclo = 0;
        
        
        
        for(int i=0; i<rondasZombies.size(); i++) {
            if ((Integer) rondasZombies.get(i) == ciclo){//rondas indica en que ronda sale cada zombie
               numeroZombiesCiclo++;// se inicia un contador de cuantos zombies habra en ese ciclo 
               
               for(int j=0; j<filas; j++) {
                    if (comprobarPosicion ( matrizPersonajes ,  j, columnas -1)== true ){//comprueba cuantos huecos sin personajes hay en la ultima columna donde se generan los zombies
                        libresEspacios++;//contador que indica cuantos huecos libres hay
                    
                    }
                }
                
                while ( numeroZombiesCiclo !=0){// si el contador de zombies es diferente a cero significa que en ese ciclo tiene que salir al menos un zombie
                    int filaAleatoria = rand.nextInt(libresEspacios);// se crea una fila aleatoria donde tiene que salir el zombie a traves de los huecos disponibles
                    int ZoC = rand.nextInt(2);//se genera aleaotriamente si el debe salir un zombie o un cara cubo 
                    System.out.print(ZoC);
                    if (comprobarPosicion (matrizPersonajes ,  filaAleatoria, columnas -1)== false){ // si en la fila aleatoria donde tiene que salir un zombie ya hay un zombie
                        for(int x=0; x<filas; x++) {// se vuelve a recorren las filas
                            if (ZoC==0){
                                if (comprobarPosicion ( matrizPersonajes ,  x, columnas -1)== true ){//se comprueba si al recorrer las filas en la ultima columna hay un hueco disponible y si lo hay se introduce el zombie
                                    CaraCubo cuboAux = new CaraCubo(x, columnas-1, ciclo);
                                    matrizPersonajes[x][columnas-1] = cuboAux;
                                    return;
                                }
                            }
                        }
                    }
                    if (comprobarPosicion (matrizPersonajes ,  filaAleatoria, columnas -1)== false){ // si en la fila aleatoria donde tiene que salir un zombie ya hay un zombie
                        for(int x=0; x<filas; x++) {
                            if(ZoC==1){ 
                                if (comprobarPosicion ( matrizPersonajes ,  x, columnas -1)== true ){//se comprueba si al recorrer las filas en la ultima columna hay un hueco disponible y si lo hay se introduce el zombie
                                    Zombie zombieAux = new Zombie(x, columnas-1, ciclo);
                                    matrizPersonajes[x][columnas-1] = zombieAux;
                                    return;
                                }
                            }
                        }
                    }
                    if (libresEspacios > numeroZombiesCiclo){//comprobar que el numero de filas es mayor que el numero de zombies 
                        if (ZoC==0){
                            CaraCubo cuboAux = new CaraCubo(filaAleatoria, columnas-1, ciclo);
                            matrizPersonajes[filaAleatoria][columnas-1] = cuboAux;
                        }
                        if (ZoC==1){
                            Zombie zombieAux = new Zombie(filaAleatoria, columnas-1, ciclo);
                            matrizPersonajes[filaAleatoria][columnas-1] = zombieAux;
                        }
                    }
                    if(libresEspacios == 1 &&  numeroZombiesCiclo== 1){
                        Zombie zombieAux = new Zombie(libresEspacios, columnas-1, ciclo);//como solo hay un hueco disponible el zombie es insertado en el hueco
                        matrizPersonajes[libresEspacios][columnas-1] = zombieAux;
                        libresEspacios = 0;
                    }else{
                        libresEspacios--;    
                    }
                    numeroZombiesCiclo--;
                    libresEspacios = 0; 
                    
                }
            }
            
            
        }

      
    }    
        
    
    public static int posicionPersonajes (Personaje matrizPersonajes [][], int filas, int columnas, int ciclo, int soles, Ventana vent){
        
        LanzaGuisantes guisantesAux = new LanzaGuisantes(0, 0, 0);
        Zombie zombieAux = new Zombie(0, 0, 0);
        Girasol girasolAux = new Girasol(0, 0, 0);
        Nuez nuezAux = new Nuez(0, 0, 0);
        CaraCubo cuboAux = new CaraCubo(0, 0, 0);
        
        for (int i=0; i < filas; i++) {
            for (int j=0; j < columnas; j++) {// se recorrela matriz
                if ((matrizPersonajes[i][j] != null)){ 
                    if (matrizPersonajes[i][j].getClass() == zombieAux.getClass()||matrizPersonajes[i][j].getClass() == cuboAux.getClass()){// en las coordenadas x y j se encuentra un zombie
                        if (j == 0){//si en la ultima columna se encuentran zombies se termina la partida
                            vent.getTerminar();
//                            
                            partidaPerdida = true;
                        }
                        else{
                            if (!comprobarPosicion ( matrizPersonajes , i, j-1)){
                                if(matrizPersonajes[i][j-1].getClass() == guisantesAux.getClass()){// en la misma fila y en la columna -1 se encuentra tambien un zombie este ataca y reduce la vida a la planta
                                    guisantesAux = (LanzaGuisantes) matrizPersonajes[i][j-1];
                                    guisantesAux.reducirVida();//s ereduce la vida
                                    matrizPersonajes[i][j-1] = guisantesAux;
                                }

                                if(matrizPersonajes[i][j-1].getClass() == girasolAux.getClass()){// en la misma fila y en la columna -1 se encuentra tambien un zombie este ataca y reduce la vida a la planta
                                    girasolAux = (Girasol) matrizPersonajes[i][j-1];
                                    girasolAux.reducirVida();   
                                }
                                if(matrizPersonajes[i][j-1].getClass() == nuezAux.getClass()){// en la misma fila y en la columna -1 se encuentra tambien un zombie este ataca y reduce la vida a la planta
                                   nuezAux = (Nuez) matrizPersonajes[i][j-1];
                                   nuezAux.reducirVida();//se reduce la vida
                                   matrizPersonajes[i][j-1] = nuezAux;
                                }
                             }
                            else{// si no se encuentra nada en la posicion de delante del zombie se desplaza
                                if(matrizPersonajes[i][j].getClass() == zombieAux.getClass()){
                                    zombieAux = (Zombie) matrizPersonajes[i][j];
                                    if(zombieAux.desplazar(ciclo)){
                                        matrizPersonajes[i][j] = null;// se cambia a null la posicion en la que estaba y -1 para avanzar
                                        matrizPersonajes[i][j-1] = zombieAux;
                                    }
                                }
                                if (comprobarPosicion ( matrizPersonajes , i, j-1)){

                                    if(matrizPersonajes[i][j].getClass() == cuboAux.getClass()){
                                        cuboAux = (CaraCubo) matrizPersonajes[i][j];

                                        if(cuboAux.desplazar(ciclo)){
                                            matrizPersonajes[i][j] = null;// se cambia a null la posicion en la que estaba y -1 para avanzar
                                            matrizPersonajes[i][j-1] = cuboAux;
                                        }                                   
                                    }
                                }

                            }                            
                        }
                        
 
                    }
                    
                    else{
                        if (matrizPersonajes[i][j].getClass() == guisantesAux.getClass()){//si se encuentra un zombie y un lanzaguisante en la misma fila, se reduce la vida al zombie
                            for(int c=j; c < columnas; c++){
                                if (!comprobarPosicion ( matrizPersonajes , i, c)){
                                    if (matrizPersonajes[i][c].getClass() == zombieAux.getClass()){
                                        zombieAux = (Zombie) matrizPersonajes[i][c];
                                        zombieAux.reducirVida();
                                        matrizPersonajes[i][c] = zombieAux;
                                        break;
                                    }
                                }
                                if (!comprobarPosicion ( matrizPersonajes , i, c)){
                                    if (matrizPersonajes[i][c].getClass() == cuboAux.getClass()){
                                        cuboAux = (CaraCubo) matrizPersonajes[i][c];
                                        cuboAux.reducirVida();
                                        matrizPersonajes[i][c] = cuboAux;
                                        break;
                                    }
                                }
                            }
                        }
                        
                        else{
                            if (matrizPersonajes[i][j].getClass() == girasolAux.getClass()){// se recolecta los soles llamando a la funcion generar dentro de la clase girasol
                               girasolAux = (Girasol)matrizPersonajes[i][j];
                               soles = soles + girasolAux.generar(ciclo);
                            }
                        }
                    }
                                     
                }
            }
        }    
        
        return soles;
    }
    
    public static void  comprobarMuertos (Personaje matrizPersonajes [][], int filas, int columnas, int ciclo){
        
        Girasol girasolAux = new Girasol(0, 0, 0);
        Zombie zombieAux = new Zombie(0, 0, 0);
        LanzaGuisantes guisantesAux = new LanzaGuisantes(0, 0, 0);
        Nuez nuezAux = new Nuez(0, 0, 0);
        CaraCubo cuboAux = new CaraCubo(0, 0, 0);
        

      
        for (int i=0; i < filas; i++) {
            for (int j=0; j < columnas; j++) {//recorre la matriz
                if ((matrizPersonajes[i][j] != null)){//se comprueba cada posicion de la matriz
                    
                    if (matrizPersonajes[i][j].getClass() == girasolAux.getClass()){
                        girasolAux = (Girasol)matrizPersonajes[i][j];

                        if (girasolAux.getVida() <= 0){// si el girasol tiene igual menor vida 0 se cambia su posicion por un null y se saca del tablero
                            
                            matrizPersonajes[i][j] =  null;// se iguala su poscion a null
                           
                       }
                    }
                }
                if ((matrizPersonajes[i][j] != null)){
                    if (matrizPersonajes[i][j].getClass() == zombieAux.getClass()){
                        zombieAux = (Zombie)matrizPersonajes[i][j];
                        
                        if (zombieAux.getVida()<=0){// si el zombie tiene igual menor vida 0 se cambia su posicion por un null y se saca del tablero
                            matrizPersonajes[i][j] =  null;// se iguala su poscion a null
                        }
                    }
                }
                if ((matrizPersonajes[i][j] != null)){
                
                    if (matrizPersonajes[i][j].getClass() == guisantesAux.getClass()){
                        guisantesAux = (LanzaGuisantes)matrizPersonajes[i][j];
                        
                        if (guisantesAux.getVida() <=0){// si el zombie tiene igual menor vida 0 se cambia su posicion por un null y se saca del tablero
                            matrizPersonajes[i][j] =  null;// se iguala su poscion a null
                        }
                    }
                } 
                if ((matrizPersonajes[i][j] != null)){
                
                    if (matrizPersonajes[i][j].getClass() == nuezAux.getClass()){
                        nuezAux = (Nuez)matrizPersonajes[i][j];
                        
                        if (nuezAux.getVida() <=0){// si el zombie tiene igual menor vida 0 se cambia su posicion por un null y se saca del tablero
                            matrizPersonajes[i][j] =  null;// se iguala su poscion a null
                        }
                    }
                }
                if ((matrizPersonajes[i][j] != null)){
                
                    if (matrizPersonajes[i][j].getClass() == cuboAux.getClass()){
                        cuboAux = (CaraCubo)matrizPersonajes[i][j];
                        
                        if (cuboAux.getVida() <=0){// si el zombie tiene igual menor vida 0 se cambia su posicion por un null y se saca del tablero
                            matrizPersonajes[i][j] =  null;// se iguala su poscion a null
                        }
                    }
                } 
            }
        }
       
    }
    
    public static void mostrarTablero (int columnas, int filas, Personaje matrizPersonajes [][], Ventana vent){
        for (int i=0; i <filas ; i++){
//           
            for (int j=0; j <columnas ; j++){
//                
                if (comprobarPosicion ( matrizPersonajes, i, j) == true){
                    vent.setCesped(i,j);
//                   
                }else{
                   
//                    
                    
                    vent.getMostrar(matrizPersonajes[i][j], i, j);
                }
            }
//           
       }
//       
    }
    
    public static int comandos (int soles, int ciclo, int filas, int columnas, Personaje matrizPersonajes[][], Ventana vent, TimeUnit time, long timeToSleep ) throws InterruptedException{
       
        String comando = null;
        while(comando == null){
            time.sleep(timeToSleep);
            comando = vent.getComando();
        }        
        vent.setComandoNull();
        

       
        if (comando.indexOf("S") == 0 || comando.equals("s") ){// si el comando intrudce es la s, hayZombies y partidaPerdida toman los avlores necesarios para salirse del while y terminar el programa 
           hayZombies = false;
           partidaPerdida =true;
           
        }
        
      
        if("ayuda".equals(comando)|| ("AYUDA".equals(comando))){

            vent.getAyuda();
            comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent, time, timeToSleep );

        }

        if (((comando.indexOf("G") ) == 0 || (comando.indexOf("g") == 0 ))||(((comando.indexOf("L") ) == 0 || (comando.indexOf("l") == 0 )))||(comando.indexOf("N")==0)||(comando.indexOf("n")==0)){//coge la primera letra intrducida
            String primeraLetra = comando.substring(0, 1);
            if("G".equals(primeraLetra) || ("g".equals(primeraLetra))){
                int filaInsertar = Integer.parseInt(comando.substring(2, 3));//coge el primer numero intrducido que ocupa la posicion 2 y 3 debido al espacio que hay que dejar
                int columnaInsertar = Integer.parseInt(comando.substring(4, 5));

                if (filaInsertar <= filas-1){
                    if (columnaInsertar<=columnas-1){//verficar que la posicion indicada está relacionada con el tablero
                        if(comprobarPosicion (matrizPersonajes, filaInsertar, columnaInsertar)){
                            if (soles >= 20 ){// se necesita tener mas o igual de 20 soles para poder utilizar un girasol
                                soles = soles - 20;
                                Girasol girasolAux = new Girasol(filaInsertar, columnaInsertar, ciclo);
                                matrizPersonajes[filaInsertar][columnaInsertar] = girasolAux;
                                vent.setSoles(soles);
                                comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent, time, timeToSleep  );// por si el jugador uiere poner ,as de una planta en el mismo ciclo

                            }else{
                                vent.getSinSoles();
                                comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent, time, timeToSleep  );

                            }

                        }
                        else{
                            vent.getPosicionErronea();
                            comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );

                        }
                    }else{
                       vent.getColumnasErroneas();
                        comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );

                }
                }else{
                    vent.getFilasErroneas();
                        comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );

                }
            }

            if((("L".equals(primeraLetra)) || ("l".equals(primeraLetra)))){
                int filaInsertar = Integer.parseInt(comando.substring(2, 3));
                int columnaInsertar = Integer.parseInt(comando.substring(4, 5));

                if (filaInsertar <= filas-1){
                    if (columnaInsertar<=columnas-1){
                        if(comprobarPosicion (matrizPersonajes, filaInsertar, columnaInsertar)){
                            if (soles >= 50 ){// se necesita tener mas o igual de 50 soles para poder utilizar un lanzaguisantes
                                soles = soles - 50;
                                LanzaGuisantes guisantesAux = new LanzaGuisantes(filaInsertar, columnaInsertar, ciclo);
                                matrizPersonajes[filaInsertar][columnaInsertar] = guisantesAux;
                                vent.setSoles(soles);

                                comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );// por si el jugador uiere poner ,as de una planta en el mismo ciclo

                           }else{
                               vent.getSinSoles();
                               comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );
                           }

                        }
                        else{
                            vent.getPosicionErronea();
                           comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );
                        }
                    }else{
                        vent.getColumnasErroneas();
                       comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );
                    }
                }else{
                    System.out.println("Error: Numero de filas que has introducido es mayor de las que existen en el tablero");
                    comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );
                }
            }
            if((("N".equals(primeraLetra)) || ("n".equals(primeraLetra)))){
                int filaInsertar = Integer.parseInt(comando.substring(2, 3));
                int columnaInsertar = Integer.parseInt(comando.substring(4, 5));

                if (filaInsertar <= filas-1){
                    if (columnaInsertar<=columnas-1){
                        if(comprobarPosicion (matrizPersonajes, filaInsertar, columnaInsertar)){
                            if (soles >= 50 ){// se necesita tener mas o igual de 50 soles para poder utilizar un lanzaguisantes
                                soles = soles - 50;
                                Nuez nuezAux = new Nuez(filaInsertar, columnaInsertar, ciclo);
                                matrizPersonajes[filaInsertar][columnaInsertar] = nuezAux;
                                vent.setSoles(soles);

                                comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );// por si el jugador uiere poner ,as de una planta en el mismo ciclo

                           }else{
                               vent.getSinSoles();
                               comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );
                           }

                        }
                        else{
                            vent.getPosicionErronea();
                           comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent, time, timeToSleep  );
                        }
                    }else{
                        vent.getColumnasErroneas();
                        comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );
                    }
                }else{
                     vent.getFilasErroneas();
                   comandos ( soles,  ciclo,  filas,  columnas, matrizPersonajes, vent , time, timeToSleep );
                }
            }
        }
       
       return soles;
     
    }
   
    public static void comprobarZombies (int filas, int columnas, Personaje matrizPersonajes[][], int ciclo,int puntos, int soles, Ventana vent){
        
        Girasol girasolAux = new Girasol(0, 0, 0);
        Zombie zombieAux = new Zombie(0, 0, 0);
        LanzaGuisantes guisantesAux = new LanzaGuisantes(0, 0, 0);
        Nuez nuezAux = new Nuez(0, 0, 0);
        CaraCubo cuboAux = new CaraCubo(0, 0, 0);
        
        int counterZ = 0;
        int counterC = 0;
        
        if ( ciclo >=40){//desde el ciclo 40 porque es la ultima vez que puede generarse un zombie
            for (int i=0; i < filas; i++) {
                for (int j=0; j < columnas; j++) {
                    if ((matrizPersonajes[i][j] != null)){ 
                        
                        if (matrizPersonajes[i][j].getClass() == zombieAux.getClass()||matrizPersonajes[i][j].getClass() == cuboAux.getClass()){
                            if (matrizPersonajes[i][j].getClass() == zombieAux.getClass()){
                                zombieAux = (Zombie) matrizPersonajes[i][j];
                                counterZ++;// con este contador se cuenta el numero de zombies que hay en el tablero
                            }
                            if (matrizPersonajes[i][j].getClass() == cuboAux.getClass()){
                                cuboAux = (CaraCubo) matrizPersonajes[i][j];
                                counterC++;
                            }
                            
                        }
                    }
                }   
            } 
            if (counterZ == 0 && counterC == 0){// si el contador es cero es que no hay ningun zombie en el tablero y no quedan por generarse mas por lo que ha terminado la partida
               
                vent.getGanar();
              
           
                hayZombies = false;
                
            }
            counterZ = 0;
            counterC = 0;//cada ciclo se termina en cero para que en el siguiente vuelva a contar
        }
        
    }   
    public static int puntos (int filas, int columnas, Personaje matrizPersonajes[][], int ciclo,int puntos, int soles, Ventana vent ){
         Girasol girasolAux = new Girasol(0, 0, 0);
        Zombie zombieAux = new Zombie(0, 0, 0);
        LanzaGuisantes guisantesAux = new LanzaGuisantes(0, 0, 0);
        Nuez nuezAux = new Nuez(0, 0, 0);
        CaraCubo cuboAux = new CaraCubo(0, 0, 0);
 
        int counterG = 0;
        int counterL = 0;
        int counterN = 0;
        
        for (int i=0; i < filas; i++) {
            for (int j=0; j < columnas; j++) {
                if ((matrizPersonajes[i][j] != null)){ 
                    if (matrizPersonajes[i][j].getClass() == girasolAux.getClass()){
                        counterG++;
                    }
                }
                if ((matrizPersonajes[i][j] != null)){ 
                    if (matrizPersonajes[i][j].getClass() == guisantesAux.getClass()){
                        counterL++;
                    }
                }
                if ((matrizPersonajes[i][j] != null)){ 
                    if (matrizPersonajes[i][j].getClass() == nuezAux.getClass()){
                        counterN++;
                    }
                }
            }
        }
        
        puntos = (counterG*20)+(counterL*50)+(counterN*50)+soles;
        
        return puntos;
    }
    

    public static void cargarDatos(){              
        try
        {
            FileInputStream fis = new FileInputStream("baja.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            baja = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe)
        {
        }catch(ClassNotFoundException c)
        {
            c.printStackTrace();
        }
       
           
        try
        {
            FileInputStream fis = new FileInputStream("media.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            media = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe)
        {
        }catch(ClassNotFoundException c)
        {
            
            c.printStackTrace();

        }

        try
        {
            FileInputStream fis = new FileInputStream("alta.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            alta = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe)
        {
        }catch(ClassNotFoundException c)
        {

            c.printStackTrace();

        }

        try
        {
            FileInputStream fis = new FileInputStream("imposible.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            imposible = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe)
        {
        }catch(ClassNotFoundException c)
        {

            c.printStackTrace();

        }
 
       
    }
    
    public static void setDatos(String palabra, String nombre, int puntos){
        ArrayList<Integer> numeros = new ArrayList<Integer>();
        int PG = 0;
        int PP = 0;
        int PJ = 0;
        int puntosTotales = 0;
        
        if (palabra == "BAJA"){
            if (baja.containsKey(nombre)){
                numeros = baja.get(nombre);
                
                PJ = numeros.get(0) +1;
                if (puntos>0){
                    PG = numeros.get(1) +1;
                    PP = numeros.get(2);
                }
                else{
                    PG = numeros.get(1);
                    PP = numeros.get(2) +1;
                }
                
                puntosTotales = numeros.get(3) + puntos;
            }
            else{
                PJ = 1;
                if (puntos>0){
                    PG = 1;
                    PP = 0;
                }
                else{
                    PG = 0;
                    PP = 1;
                }
                
                puntosTotales = puntos;
            }
           
            numeros.clear();
            
            numeros.add(PJ);
            numeros.add(PP);
            numeros.add(PG);
            numeros.add(puntosTotales);

            baja.put(nombre, numeros);
        }
        
        if (palabra == "MEDIA"){
            if (media.containsKey(nombre)){
                numeros = media.get(nombre);
                
                PJ = numeros.get(0) +1;
                if (puntos>0){
                    PG = numeros.get(1) +1;
                    PP = numeros.get(2);
                }
                else{
                    PG = numeros.get(1);
                    PP = numeros.get(2) +1;
                }
                
                puntosTotales = numeros.get(3) + puntos;
            }
            else{
                PJ = 1;
                if (puntos>0){
                    PG = 1;
                    PP = 0;
                }
                else{
                    PG = 0;
                    PP = 1;
                }
                
                puntosTotales = puntos;
            }
           
            numeros.clear();
            
            numeros.add(PJ);
            numeros.add(PP);
            numeros.add(PG);
            numeros.add(puntosTotales);

            media.put(nombre, numeros);
        }
        
        if (palabra == "ALTA"){
            if (alta.containsKey(nombre)){
                numeros = alta.get(nombre);
                
                PJ = numeros.get(0) +1;
                if (puntos>0){
                    PG = numeros.get(1) +1;
                    PP = numeros.get(2);
                }
                else{
                    PG = numeros.get(1);
                    PP = numeros.get(2) +1;
                }
                
                puntosTotales = numeros.get(3) + puntos;
            }
            else{
                PJ = 1;
                if (puntos>0){
                    PG = 1;
                    PP = 0;
                }
                else{
                    PG = 0;
                    PP = 1;
                }
                
                puntosTotales = puntos;
            }
           
            numeros.clear();
            
            numeros.add(PJ);
            numeros.add(PP);
            numeros.add(PG);
            numeros.add(puntosTotales);

            alta.put(nombre, numeros);
        }
        
        if (palabra == "IMPOSIBLE"){
            if (imposible.containsKey(nombre)){
                numeros = imposible.get(nombre);
                
                PJ = numeros.get(0) +1;
                if (puntos>0){
                    PG = numeros.get(1) +1;
                    PP = numeros.get(2);
                }
                else{
                    PG = numeros.get(1);
                    PP = numeros.get(2) +1;
                }
                
                puntosTotales = numeros.get(3) + puntos;
            }
            else{
                PJ = 1;
                if (puntos>0){
                    PG = 1;
                    PP = 0;
                }
                else{
                    PG = 0;
                    PP = 1;
                }
                
                puntosTotales = puntos;
            }
           
            numeros.clear();
            
            numeros.add(PJ);
            numeros.add(PP);
            numeros.add(PG);
            numeros.add(puntosTotales);

            imposible.put(nombre, numeros);
        }
    }
    
    public static void guardarDatos (){        
        
        try{
            FileOutputStream fos = new FileOutputStream("baja.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(baja);
            oos.close();
            fos.close();


        }
        catch(IOException ioe){
        }
        
        try (FileOutputStream file = new FileOutputStream("media.ser")) {
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeObject(media);
            oos.close();
            file.close();

        }
        catch (IOException ioe){

        }
           
        try (FileOutputStream file = new FileOutputStream("alta.ser")) {
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeObject(alta);
            oos.close();
            file.close();

        }catch (IOException ioe){

        }
        
        try (FileOutputStream file = new FileOutputStream("imposible.ser")) {
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeObject(imposible);
            oos.close();
            file.close();

        }catch (IOException ioe){

        }
    
    }
    
   

}

   


     
         
     
      
     
