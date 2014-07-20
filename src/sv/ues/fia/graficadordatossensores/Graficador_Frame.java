package sv.ues.fia.graficadordatossensores;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import static java.awt.image.ImageObserver.ERROR;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @author racsoraul
 */
public class Graficador_Frame extends javax.swing.JFrame implements SerialPortEventListener {

    //Variables de conexión
    private OutputStream output = null;
    private BufferedReader input = null;
    SerialPort serialPort;

    /*
     El siguiente arreglo de puertos se utiliza para no ingresar un puerto 
     estáticamente.
     */
    private static final String PUERTOS[] = {
        "/dev/tty.usbserial-A9007UX1", // Mac OS 
        "/dev/ttyACM0", "/dev/ttyUSB0", "/dev/ttyUSB1", "/dev/ttyUSB2", "/dev/ttyUSB3", "/dev/ttyUSB4", "/dev/ttyUSB5", "/dev/ttyUSB6", "/dev/ttyUSB7", "/dev/ttyUSB8", "/dev/ttyUSB9", "/dev/ttyUSB10", "/dev/ttyUSB11", "/dev/ttyUSB12", "/dev/ttyUSB13", "/dev/ttyUSB14", "/dev/ttyUSB15", "/dev/ttyUSB16", "/dev/ttyUSB17", "/dev/ttyUSB18", "/dev/ttyUSB19", "/dev/ttyUSB20", "/dev/ttyUSB21", "/dev/ttyACM1", // Linux
        "COM3", "COM0", "COM1", "COM2", "COM4", "COM5", "COM6", "COM7", "COM3", "COM8", "COM9", "COM10", "COM11", "COM12" // Windows
    };

    private static final int TIMEOUT = 2000; //Milisegundos de tiempo de espera
    private static final int DATA_RATE = 9600; //Velicidad de transmisión

    //Constructor de la clase
    public Graficador_Frame() {
        initComponents();
        //Iniciando conexión con puerto serial
        inicializarConexion();
    }

    //Estableciendo conexión con Arduino
    private void inicializarConexion() {
        try {
            //Almacenará el identificador del puerto
            CommPortIdentifier puertoID = null;
            //Enumeracion de los identificadores de los puertos
            Enumeration puertoEnum = CommPortIdentifier.getPortIdentifiers();

            //Se comparan los elementos de la Enumeracion con el arreglo PUERTOS, al obtener
            //una igualdad entonces se almacena el puerto que usará.
            while (puertoEnum.hasMoreElements()) {
                CommPortIdentifier actualPuertoID = (CommPortIdentifier) puertoEnum.nextElement();
                for (String puerto : PUERTOS) {
                    if (actualPuertoID.getName().equals(puerto)) {
                        puertoID = actualPuertoID;
                        break;
                    }
                }
            }

            if (puertoID == null) {
                mostrarError("No se puede conectar al puerto");
                System.exit(ERROR);
            }
            //Obtenemos puerto utilizando el identificador
            serialPort = (SerialPort) puertoID.open(this.getClass().getName(), TIMEOUT);

            //Parámetros puerto serie
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            /*Para entrada y salida de datos*/
            // Abrir streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // Agregando listener
            serialPort.addEventListener(this);
            //Habilitando evento en el que estamos interesado
            serialPort.notifyOnDataAvailable(true);

            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));

            /*FIN de parametros de entrada y salida de datos*/
        } catch (PortInUseException | UnsupportedCommOperationException | IOException | TooManyListenersException ex) {
            Logger.getLogger(Graficador_Frame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_graficar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton_graficar.setText("Graficar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jButton_graficar)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jButton_graficar)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_graficar;
    // End of variables declaration//GEN-END:variables

    //Recepción de datos.
    @Override
    public void serialEvent(SerialPortEvent spe) {
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();

            } catch (IOException ex) {
                Logger.getLogger(Graficador_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
