package psp_t5_cliente;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
/**
 *
 * @author Adrian Raya Hernandez
 */
public class PSP_T5_cliente {

public static void main(String[] args) {
        System.setProperty("javax.net.ssl.trustStore", "clientTrustedCerts.jks");
        System.setProperty("javax.net.ssl.trustStoreePassword", "claveClienteTarea5");
        
        System.out.println("**************************************\r\n"
                + "*        Almacén multiusuario          *\r\n"
                + "****************************************\r\n"
                + "* Adrián Raya Hernández                *\r\n"
                + "****************************************\r\n");

        try {
            //Comandos que se irán introduciendo
            String comando = "";
            //Direcciones para realizar la conexión
            String direccionIp = "127.0.0.1";
            //Iniciamos la conexión del puerto, aunque vamos 
            //a introducirlo más adelante y deberá ser igual al que introduzcamos en el servidor
            int puerto = 1111; 
            
            //Flujo de entrada por teclado
            BufferedReader lectorDeComandos = new BufferedReader(new InputStreamReader(System.in));

            //Solicitamos la direccion Ip al cliente
            System.out.println("Introduzca la dirección ip del servidor: [127.0.0.1]");
            comando = lectorDeComandos.readLine();
            if (!comando.equalsIgnoreCase("")) {
                direccionIp = comando;
            }

            //Solicitamos el puerto con el servidor
            System.out.println("Introduzca el puerto al que conectarse: ");
            comando = lectorDeComandos.readLine();

            if (!comando.equalsIgnoreCase("")) {
                puerto = Integer.parseInt(comando);
            }

            //Pasamos la Ip y el puerto al socket
            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket cliente = (SSLSocket)sf.createSocket(direccionIp, puerto); 

            System.out.println("Conexión establecida.");

            //Flujo salida para enviar al servidor
            PrintWriter enviar = new PrintWriter(cliente.getOutputStream(), true);
            //Flujo entrada para recibir del servidor
            BufferedReader recibir = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            //Bucle en el Almacén de Ordenadores hasta escribir el comando salir
            while (!comando.equalsIgnoreCase("salir")) {
                System.out.println("ALMACÉN DE ORDENADORES");
                //Comandos que puede enviar el cliente para consultar, insertar, retirar y salir
                System.out.println("Introduzca un comando (consultar/insertar/retirar/salir): ");
                comando = lectorDeComandos.readLine();
                enviar.println(comando);
                System.out.println(recibir.readLine());
                
                //Si introducimos el comando insertar o retirar debemos pedir 
                //el número de ordenadores que queremos realizar esa acción
                if (comando.equals("insertar") || comando.equals("retirar")) {
                    System.out.println("Introduzca el número: ");
                    comando = lectorDeComandos.readLine();
                    enviar.println(comando);
                    System.out.println(recibir.readLine());
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(PSP_T5_cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
