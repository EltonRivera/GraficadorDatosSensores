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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;

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

    /*Variables para la creación del gráfico */
    //Varibles que contendrán los datos del sensor
    XYSeries serieTemperatura = new XYSeries("Temperatura"); //Guarda los valores del sensor
    XYSeriesCollection coleccionDeSeries = new XYSeriesCollection(); //Guarda las series de datos
    //Variable para el gráfico
    JFreeChart grafico;
    //Constantes del gráfico
    final String TITULO_DEL_GRAFICO = "Temperatura vs Tiempo";
    final String EJE_HORIZONTAL_X = "Tiempo (s)";
    final String EJE_VERTICAL_Y = "Temperatura (°C)";

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
        jButton_graficar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_graficarActionPerformed(evt);
            }
        });

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

    private void jButton_graficarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_graficarActionPerformed

        //Elaborando gráfico
        armarGrafico();
        //Mostrando gráfico
        mostrarGrafico();
    }//GEN-LAST:event_jButton_graficarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_graficar;
    // End of variables declaration//GEN-END:variables

    int i = 0;

    //Recepción de datos.

    @Override
    public void serialEvent(SerialPortEvent spe) {
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                i++;
                String inputLine = input.readLine();
                serieTemperatura.add(i, Integer.parseInt(inputLine));
            } catch (IOException ex) {
                Logger.getLogger(Graficador_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void armarGrafico() {
        /*Armar gráfico*/
        serieTemperatura.add(0, 0); //Coordenada en el origen para que el gráfico inicie ahi.
        coleccionDeSeries.addSeries(serieTemperatura);
        grafico = ChartFactory.createXYLineChart(TITULO_DEL_GRAFICO, EJE_HORIZONTAL_X, EJE_VERTICAL_Y,
                coleccionDeSeries, PlotOrientation.VERTICAL, true, true, false);
        
        /*Agregando subtítulos al gráfico*/
        //Agregando subtítulo personalizado
        TextTitle source = new TextTitle(
                "Taller COMPDES 2014"
        );
        source.setPosition(RectangleEdge.BOTTOM);
        source.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        grafico.addSubtitle(source);
        
        //Agregando subtitulos en posición por defecto
        grafico.addSubtitle(new TextTitle("Universidad de El Salvador"));
        grafico.addSubtitle(new TextTitle("Facultad de Ingeniería y Arquitectura"));

    }
    
    private void mostrarGrafico() {
        //Creando panel para el gráfico
        ChartPanel chartPanel = new ChartPanel(grafico);
        //Creando ventana para agregar panel del gráfico
        JFrame ventana = new JFrame("Gráfico de sensores");
        //Agregando panel a la ventana
        ventana.getContentPane().add(chartPanel);
        //Asignando dimensiones por defecto
        ventana.pack();
        //Volviendo visible la ventana
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
