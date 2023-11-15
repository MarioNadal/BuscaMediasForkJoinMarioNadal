package org.example;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/*************************************************************************************
 *  APLICACIÓN: "BuscaMediasForkJoin"
 ***************************************************************************************
 *  PROGRAMACIÓN DE SERVICIOS Y PROCESOS 2DAM - IntelliJ IDEA(2023.2.5)
 ***************************************************************************************
 *  @author  M.Nadal
 *
 *  @version 1.0 - Versión inicial del algoritmo.
 *
 *  @since   13NOV2023
 ***************************************************************************************
 *  COMENTARIOS:
 *
 *      - A partir de un array de enteros en dos mitades, calcula de forma iterativas
 *  mediante dos tareas paralelas la media de cada una de estas mitades.
 ***************************************************************************************/


public class MediaForkJoin extends RecursiveTask<double[]> {
    private static final int UMBRAL = 500;
    private static final int LONGITUD_ARRAY = 1000;
    private double[] a_Vector = null;
    private int a_Inicio, a_Fin = 0;


    public MediaForkJoin() { }


    public MediaForkJoin(double[] p_Vector, int p_Inicio, int p_Fin)
    {
        this.a_Vector = p_Vector;
        this.a_Inicio = p_Inicio;
        this.a_Fin = p_Fin;
    }   // MediaForkJoin()

    private double[] getMedia(){
        int l_Medio = ((a_Fin-a_Inicio)/2);
        MediaForkJoin l_primeraMitad = new MediaForkJoin(a_Vector, a_Inicio, l_Medio);;
        MediaForkJoin l_segundaMitad = new MediaForkJoin(a_Vector, l_Medio, a_Fin);l_primeraMitad.fork();
        l_primeraMitad.fork();
        l_segundaMitad.fork();
        double[] l_primeraMitadResultado = l_primeraMitad.join();
        double[] l_segundaMitadResultado = l_segundaMitad.join();

        return new double[]{l_primeraMitadResultado[0], l_segundaMitadResultado[0]};
    }

    @Override
    protected double[] compute(){
        if((a_Fin-a_Inicio) <= UMBRAL){
            double suma = 0;

            for (int i = a_Inicio; i < a_Fin; i++) {
                suma += a_Vector[i];
            }
            return new double[]{(suma / (a_Fin - a_Inicio))};
        }else{
            double[] l_Resultado;
            l_Resultado = getMedia();
            return (l_Resultado);
        }
    }   //compute()

    private double[] crearArray(int p_Longitud)
    {
        double[] l_Array = new double[p_Longitud];
        int l_Contador = 0;

        for (l_Contador = 0; l_Contador < p_Longitud; l_Contador++)
        {
            l_Array[l_Contador] = (double)(l_Contador);
        }

        return (l_Array);
    }   // crearArray()


    public static void main(String[] args)
    {
        MediaForkJoin l_Tarea = new MediaForkJoin();
        double[] l_Data = l_Tarea.crearArray(LONGITUD_ARRAY);
        int l_Inicio = 0;
        int l_Fin = l_Data.length;
        double[] l_ResultadoInvoke;
        long l_TiempoInicial = System.currentTimeMillis();
        ForkJoinPool l_Pool = new ForkJoinPool();

        System.out.println("Inicio del cálculo.");

        // Crea la tarea, la lanza, y obtiene el resultado "invoke".
        l_Tarea = new MediaForkJoin(l_Data, l_Inicio, l_Fin);
        l_ResultadoInvoke = l_Pool.invoke(l_Tarea);

        System.out.println("Milisegundos empleados: " + (System.currentTimeMillis() - l_TiempoInicial));
        System.out.println("Las primera media es: " + l_ResultadoInvoke[0] + " y la segunda: " + l_ResultadoInvoke[1]);
    }   // main()

}   // MaximoForkJoin