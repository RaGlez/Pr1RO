import java.io.IOException;
import java.net.*;

public class udpcli{

    public static void main (String[] args)
    {
        if (args.length != 3) {
            System.out.println("Sintaxis incorrecta. Uso correcto: udpcli ip_address_servidor port_servidor operacion");
            System.out.println("Ejemplo: udpcli 127.0.0.1 12345 \"5 + 3\"");
            return;
        } 

        //Se obtiene la dirección del servidor a partir del argumento indicado por línea de comandos
        InetAddress serverAddress = null;

        try{
            //
            serverAddress = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println("Error en la IP del servidor");
        }
        // Se obtine el puerot del servidor por la linea de comandos
        int serverPort = Integer.parseInt(args[1]);
        // Obtener la operación del argumento de la línea de comandos
        String operation = args[2];

        //Creamos el Soket 
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error en la creación del socket");
        }
    
        // Convertimos la operacion en bytes para poder enviarla al servidor
        byte[] operationByte = operation.getBytes();
        //Creamos el paquete de datos para enviar al servidor
        DatagramPacket paqueteEnvio = new DatagramPacket(operationByte, operationByte.length, serverAddress, serverPort);
        //Vemos si enviamos el paquete
        try {
            datagramSocket.send(paqueteEnvio);
        } catch (IOException e) {
            System.out.println("No se pudo enviar el paquete");
        }

        // Creamos un array de bytes para guardar el resultado de la opracion del servidor
        byte[] result= new byte [1024];
        // Se crea el paquete de llegada 
        DatagramPacket paqueteRecivo= new DatagramPacket (result, result.length);
        // Creamos el tiempo de espera y vemos si el servidor esta activo
        try{
            datagramSocket.setSoTimeout(10000);
            datagramSocket.receive(paqueteRecivo);
        } catch (IOException e) {
            //Si salta el timeout se avisa y se cierra el programa
            System.out.println("\nNo hay respuesta del servidor " + args[0] + ":" + args[1] + " tras 10 segundos");
            return;
        }

        // Al recebir los datos los convertimos y lo mostramos por pantalla
        String num= new String (paqueteRecivo.getData(), 0, paqueteRecivo.getLength());
        System.out.println("Resultado recibido del servidor: " + num);

        // Cerramos el Soket
        datagramSocket.close();

        


    }



}