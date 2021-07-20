package psp_t5_servidor;

import java.io.BufferedReader;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
/**
 *
 * @author Adrián Raya Hernández
 */
public class PSP_T5_servidor extends Thread{

    //Inicilizamos variables encargadas de realizar operaciones con los ordenadores
    static int ordenadores = 0;
    static boolean operaciones = false; //True para realizar operaciones
    static boolean operacionTerminada = true; //True si se terminó de realizar operaciones
    static boolean sumaresta; //True si es suma y False si es resta

    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.keyStore", "serverKey.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "claveTarea5");
        
        System.out.println("**************************************\r\n"
                + "*           Almacén multiusuario       *\r\n"
                + "****************************************\r\n"
                + "* Adrián Raya Hernández                *\r\n"
                + "****************************************\r\n");
        try {
            //Mantener la conexión hasta que salir sea true
            boolean salir = false;
            //Variable para el puerto que vamos a introducir
            int puerto;
            //Variable para los datos por teclado que introducimos
            String teclado;

            //Flujo de entrada de datos por teclado
            BufferedReader lectorDePuerto = new BufferedReader(new InputStreamReader(System.in));

            //Pedimos que se introduzca el puerto en el que va a estar el servidor
            System.out.println("Introduzca el puerto con el que quiere iniciar:");
            teclado = lectorDePuerto.readLine();
            puerto = Integer.parseInt(teclado);
            
            //Pedimos el Stock inicial de Ordenadores
            System.out.println("Introduzca el Stock de Ordenadores:");
            teclado = lectorDePuerto.readLine();
            ordenadores = Integer.parseInt(teclado);

            //Creamos un SSLServerSocket con el puerto introducido
            SSLServerSocketFactory ssf = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            SSLServerSocket servidor = (SSLServerSocket)ssf.createServerSocket(puerto);

            System.out.println("Esperando conexión con el cliente...");

            //Objeto de la clase socket que acepta la conexión con el cliente
            SSLSocket cliente = (SSLSocket)servidor.accept();
            System.out.println(cliente.toString());

            //Flujo de entrada para recibir la información del cliente
            BufferedReader recibir = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            //Creamos un flujo de salida para enviar al cliente
            PrintWriter enviar = new PrintWriter(cliente.getOutputStream(), true);

            //Entramos en un bucle para que se mantenga la conexión 
            //con el cliente hasta que se mande el comando salir
            while (!salir) {
                //Comando que recibe del cliente
                String comando = recibir.readLine();

                //Condición que utilizamos cuando el cliente envia el comando 
                //insertar o retirar para así realizar operaciones en el almacen
                if (operaciones == true) {
                    System.out.println("Comando numero recibido: " + comando);
                    //Condición si se está realizando una suma
                    if (sumaresta == true) {
                        int suma = Integer.parseInt(comando);
                        ordenadores = ordenadores + suma;
                        enviar.println("++ Ha insertado " + suma + " Ordenadores ++");
                        System.out.println("Ordenadores en el almacén: " + ordenadores);
                    //Condición si se está realizando la resta    
                    } else if (sumaresta == false) {
                        int resta = Integer.parseInt(comando);
                        ordenadores = ordenadores - resta;
                        enviar.println("-- Ha retirado " + resta + " Ordenadores --");
                        System.out.println("Ordenadores en el almacén: " + ordenadores);

                    }

                    salir = false;
                    operacionTerminada = false;
                    operaciones = false;
                    
                //Condición cuando no se está realizando operaciones y cuando ha terminado de hacerlas    
                } else if (operaciones == false && operacionTerminada == true) {
                    System.out.println("Comando recibido: " + comando);
                    //Switch para los comandos del cliente
                    switch (comando) {
                        case "consultar":
                            enviar.println("Ordenadores en el almacén: " + ordenadores);
                            salir = false;
                            break;
                        case "insertar":
                            System.out.println("Ordenadores en el almacén: " + ordenadores);
                            enviar.println("++OPCION INSERTAR Ordenadores++");
                            sumaresta = true;
                            operaciones = true;
                            salir = false;
                            break;
                        case "retirar":
                            System.out.println("Ordenadores en el almacén: " + ordenadores);
                            enviar.println("--OPCION RETIRAR Ordenadores--");
                            sumaresta = false;
                            operaciones = true;
                            salir = false;
                            break;
                        case "salir":
                            salir = true;
                            break;
                        default:
                            enviar.println("Comando no reconocido.");
                            System.out.println("Comando no reconocido.");
                            break;
                    }
                }
                operacionTerminada = true;
            }
            cliente.close();
            servidor.close();

        } catch (IOException ex) {
            Logger.getLogger(PSP_T5_servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
