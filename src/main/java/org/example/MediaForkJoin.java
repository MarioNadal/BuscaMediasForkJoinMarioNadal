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
 *  @version 1.0 -
 *
 *  @since   13NOV2023
 ***************************************************************************************
 *  COMENTARIOS:
 *
 *      - A partir de un array de enteros en dos mitades, calcula de forma iterativas
 *  mediante dos tareas paralelas la media de cada una de estas mitades.
 ***************************************************************************************/


public class MediaForkJoin extends RecursiveTask<Short>
{
    // private static final int UMBRAL = 10_000;     // Umbral alternativo para pruebas.
    private static final int UMBRAL = 10_000_000;
    private static final int LONGITUD_ARRAY = 100_000_000;
    private static final int DUMMY_MAX = 9_999;
    private short[] a_Vector = null;
    private int a_Inicio, a_Fin = 0;


    public MediaForkJoin() { }


    public MediaForkJoin(short[] p_Vector, int p_Inicio, int p_Fin)
    {
        this.a_Vector = p_Vector;
        this.a_Inicio = p_Inicio;
        this.a_Fin = p_Fin;
    }   // MaximoForkJoin()


    private short getMaxRec()     // Ejecución recursiva.
    {
        int l_Medio = ( (a_Inicio + a_Fin) / 2 ) + 1;
        MediaForkJoin l_Tarea1 = new MediaForkJoin(a_Vector, a_Inicio, l_Medio);;
        MediaForkJoin l_Tarea2 = new MediaForkJoin(a_Vector, l_Medio, a_Fin);

        // No tratamos el caso trivial pues cortaremos la recursividad en el UMBRAL.
        l_Tarea1.fork();
        l_Tarea2.fork();

        return ( (short) Math.(l_Tarea1.join(), l_Tarea2.join()) );
    }   // getMaxRec()

    @Override
    protected Short compute()     // Llamado por invoke() e invokeAll().
    {
        Short l_Retorno = 0;
        l_Retorno = getMaxRec();   // Ejecución recursiva.

        return (l_Retorno);
    }   // compute()


    private short[] crearArray(int p_Longitud)
    {
        short[] l_Array = new short[p_Longitud];
        int l_Contador = 0;

        for (l_Contador = 0; l_Contador < p_Longitud; l_Contador++)
        {
            // Decide un lugar fijo donde insertar el máximo para que
            // el tiempo de cada búsqueda en concreto no dependa de dónde esté.
            if (l_Contador == ((short)(p_Longitud*0.9)))  l_Array[l_Contador] = DUMMY_MAX;
            else l_Array[l_Contador] = (short) (1_000 * Math.random());
        }

        return (l_Array);
    }   // crearArray()


    public static void main(String[] args)
    {
        MediaForkJoin l_Tarea = new MediaForkJoin();
        short[] l_Data = l_Tarea.crearArray(LONGITUD_ARRAY);
        int l_Inicio = 0;
        int l_Fin = l_Data.length;
        int l_ResultadoInvoke = 0;
        int l_ResultadoJoin = 0;
        long l_TiempoInicial = System.currentTimeMillis();
        ForkJoinPool l_Pool = new ForkJoinPool();

        System.out.println("Inicio del cálculo.");

        // Crea la tarea, la lanza, y obtiene el resultado "invoke".
        l_Tarea = new MediaForkJoin(l_Data, l_Inicio, l_Fin);
        l_ResultadoInvoke = l_Pool.invoke(l_Tarea);
        // Obtención del resultado "join". 
        l_ResultadoJoin = l_Tarea.join();

        System.out.println("Milisegundos empleados: " + (System.currentTimeMillis() - l_TiempoInicial));
        System.out.println("El máximo según ‘invoke’ es: " + l_ResultadoInvoke);
        System.out.println("Coincide con el máximo según ‘join’ que es: " + l_ResultadoJoin);
    }   // main()

}   // MaximoForkJoin