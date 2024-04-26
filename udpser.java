import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class udpser
 {
    public static void main (String[] args){

       // Comprobación de los argumentos de la línea de comandos
       if (args.length != 2) {
        System.out.println("Sintaxis incorrecta. Uso correcto: udpser port_numer secreto");
        return;
    }
    //Se obtine el puerto del servidor y el secreto por linea de comandos 
    int serverPort = Integer.parseInt(args[0]);
    int secreto = Integer.parseInt(args[1]);

    DatagramSocket datagramSocket = null;

    try {
        datagramSocket = new DatagramSocket(serverPort);
    } catch (SocketException e) {
        System.out.println("Error en la creación del socket");
    }

    // Mostramos por pantalla que se ha iniciado bien el servidor 
    System.out.println("Servidor iniciado correctamente en el puerto " + serverPort);

    while (true){
        // Se crea el paquete de llegada para recibir la info del cliente
        byte[] Datos = new byte [1024];
        DatagramPacket paqueteRecivo = new DatagramPacket (Datos, Datos.length);

        // Primero vemos si recibimos info del cliente
        try{
            datagramSocket.receive(paqueteRecivo);
        }catch(IOException e) {
            System.out.println("Error en la recepción del paquete");
        }

        // Convertimos los datos que recibimos del cliente en una string
        String operacion= new String(paqueteRecivo.getData(), 0, paqueteRecivo.getLength());
        System.out.println("Operacion del cliente: "+ operacion);


         // Analizar la operación y realizar el cálculo correspondiente

         String[] parts = operacion.split("\\+|-|\\*|/");
         int operand1 = Integer.parseInt(parts[0]);
         char operator = operacion.replaceAll("[0-9]", "").charAt(0);
         int operand2 = Integer.parseInt(parts[1]);

        // String[] parts = operacion.split("\\s+");
        // int operand1 = Integer.parseInt(parts[0]);
         //char operator = parts[1].charAt(0);
        // int operand2 = Integer.parseInt(parts[2]);
         int result = 0;

         switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                result = operand1 / operand2;
                break;
        }

        //Añadimos el secreto al resultado
        result += secreto;

        // Convertir el resultado en bytes para enviarlo al cliente
        byte[] DatosEnvio = String.valueOf(result).getBytes();

        //Obtenemos la direccion del cliente a partir del paquete entrante
        InetAddress clientAddress = paqueteRecivo.getAddress();
        // Se obtiene el puerto del cliente
        int clientPort= paqueteRecivo.getPort();

        // SE crea el paquete con el resultado y lo enviamos al cliente
        DatagramPacket envioCliente = new DatagramPacket (DatosEnvio, DatosEnvio.length, clientAddress, clientPort);
        // Envio del paquete
        try {
            datagramSocket.send((envioCliente));
        } catch (IOException e) {
            System.out.println("Error al enviar el paquete");
        }
    }
   }
}
