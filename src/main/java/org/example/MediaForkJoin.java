package org.example;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/***************************************************************************************
 *  APLICACIÓN: "BuscaMediasForkJoin"
 ***************************************************************************************
 *  PROGRAMACIÓN DE SERVICIOS Y PROCESOS 2DAM - IntelliJ IDEA(2023.2.5)
 ***************************************************************************************
 *  @author  M.Nadal
 *
 *  @version 1.1 - Versión con comentarios y excepciones
 *           1.0 - Versión inicial del algoritmo.
 *
 *  @since  15NOV2023
 *          13NOV2023
 ***************************************************************************************
 *  COMENTARIOS:
 *
 *      - A partir de un array de enteros en dos mitades, calcula de forma iterativas
 *  mediante dos tareas paralelas la media de cada una de estas mitades.
 ***************************************************************************************/


public class MediaForkJoin extends RecursiveTask<Double> {
    private static final int LONGITUD_ARRAY = 1000;
    private Double[] a_Vector = null;
    private int a_Inicio, a_Fin = 0;


    public MediaForkJoin() { }


    public MediaForkJoin(Double[] p_Vector, int p_Inicio, int p_Fin)
    {
        this.a_Vector = p_Vector;
        this.a_Inicio = p_Inicio;
        this.a_Fin = p_Fin;
    }   // MediaForkJoin()

    //Funcion que calcula la media de un array
    private Double getMedia(){
        Double suma = 0.0;
        for (int i = a_Inicio; i < a_Fin; i++) {
            suma += a_Vector[i];
        }
        return (suma/(a_Fin-a_Inicio));
    }   //getMedia()

    //Devuelve el resultado, siendo la media de el array
    @Override
    protected Double compute(){
        Double l_Resultado;
        l_Resultado = getMedia();
        return (l_Resultado);
    }   //compute()

    //Crea un array con los números desde 0 hasta el metido por parámetro
    private static Double[] crearArray(int p_Longitud)
    {
        Double[] l_Array = new Double[p_Longitud];
        int l_Contador = 0;

        for (l_Contador = 0; l_Contador < p_Longitud; l_Contador++)
        {
            l_Array[l_Contador] = (double) l_Contador;
        }

        return (l_Array);
    }   // crearArray()


    public static void main(String[] args) {
        //Crear el array total con todos los números a través de la función crearArray()
        Double[] l_Data = MediaForkJoin.crearArray(LONGITUD_ARRAY);
        //El valor inicial
        int l_Inicio = 0;
        //El valor final
        int l_Fin = l_Data.length;
        //La mitad del array
        int l_Medio = (l_Inicio + (l_Fin-l_Inicio)/2);
        //Calcula el tiempo al empezar el proceso
        long l_TiempoInicial = System.currentTimeMillis();
        MediaForkJoin l_PrimeraTarea = null, l_SegundaTarea = null;
        Double l_ResultadoInvokePrimeraTarea = null, l_ResultadoInvokeSegundaTarea = null;

        System.out.println("Inicio del cálculo.");

        // Crea la tarea, la lanza, y obtiene el resultado "invoke".
        l_PrimeraTarea = new MediaForkJoin(l_Data, l_Inicio, l_Medio);
        l_SegundaTarea = new MediaForkJoin(l_Data, l_Medio, l_Fin);
        try(ForkJoinPool l_Pool = new ForkJoinPool()){
            l_ResultadoInvokePrimeraTarea = l_Pool.invoke(l_PrimeraTarea);
            l_ResultadoInvokeSegundaTarea = l_Pool.invoke(l_SegundaTarea);
        }catch(SecurityException ex){
            System.out.println("Error de seguridad en el ForkJooinPool. " + ex.getMessage());
        }

        //Se toma el tiempo al finalizar el proceso y restando por el tiempo iniciado, se
        //sabrá cuanto tiempo ha tardado en total el proceso
        System.out.println("Milisegundos empleados: " + (System.currentTimeMillis() - l_TiempoInicial));
        System.out.println("La primera media es: " + l_ResultadoInvokePrimeraTarea + " y la segunda: " + l_ResultadoInvokeSegundaTarea);
    }   // main()

}   // MediaForkJoin