package org.example;

import javax.print.attribute.standard.Media;
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


public class MediaForkJoin extends RecursiveTask<Double> {
    private static final int LONGITUD_ARRAY = 1000;
    private Double[] a_Vector = null;
    private int a_Inicio;
    private int a_Fin = 0;


    public MediaForkJoin() { }


    public MediaForkJoin(Double[] p_Vector, int p_Inicio, int p_Fin)
    {
        this.a_Vector = p_Vector;
        this.a_Inicio = p_Inicio;
        this.a_Fin = p_Fin;
    }   // MediaForkJoin()

    private Double getMedia(){
        Double suma = 0.0;
        for (int i = a_Inicio; i < a_Fin; i++) {
            suma += a_Vector[i];
        }
        return (suma/(a_Fin-a_Inicio));
    }

    @Override
    protected Double compute(){
        Double l_Resultado;
        l_Resultado = getMedia();
        return (l_Resultado);
    }   //compute()

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


    public static void main(String[] args)
    {
        Double[] l_Data = MediaForkJoin.crearArray(LONGITUD_ARRAY);
        int l_Inicio = 0;
        int l_Fin = l_Data.length;
        int l_Medio = (l_Inicio + (l_Fin-l_Inicio)/2);
        long l_TiempoInicial = System.currentTimeMillis();

        System.out.println("Inicio del cálculo.");

        // Crea la tarea, la lanza, y obtiene el resultado "invoke".
        MediaForkJoin l_PrimeraTarea = new MediaForkJoin(l_Data, l_Inicio, l_Medio);
        MediaForkJoin l_SegundaTarea = new MediaForkJoin(l_Data, l_Medio, l_Fin);

        ForkJoinPool l_Pool = new ForkJoinPool();
        Double l_ResultadoInvokePrimeraTarea = l_Pool.invoke(l_PrimeraTarea);
        Double l_ResultadoInvokeSegundaTarea = l_Pool.invoke(l_SegundaTarea);

        System.out.println("Milisegundos empleados: " + (System.currentTimeMillis() - l_TiempoInicial));
        System.out.println("La primera media es: " + l_ResultadoInvokePrimeraTarea + " y la segunda: " + l_ResultadoInvokeSegundaTarea);
    }   // main()

}   // MaximoForkJoin