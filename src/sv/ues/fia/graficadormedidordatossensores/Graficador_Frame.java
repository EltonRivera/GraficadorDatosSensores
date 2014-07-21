package sv.ues.fia.graficadormedidordatossensores;

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
    XYSeriesCollection coleccionDeSeries; //Guarda las series de datos
    //Variable para el gráfico
    JFreeChart grafico;
    //Constantes del gráfico
    final String TITULO_DEL_GRAFICO = "Temperatura vs Tiempo";
    final String EJE_HORIZONTAL_X = "Tiempo (s)";
    final String EJE_VERTICAL_Y = "Temperatura (°C)";

    Medidor medidor = new Medidor("Temperatura °C");

    //Constructor de la clase
    public Graficador_Frame() {
        initComponents();
        //Iniciando conexión con puerto serial
        inicializarConexion();
        //Elaborando gráfico
        armarGrafico();
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

            /*FIN de parametros de entrada y salida de datos*/
        } catch (PortInUseException | UnsupportedCommOperationException | IOException | TooManyListenersException ex) {
            Logger.getLogger(Graficador_Frame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    float maxC = 0, minC = 100;  //Variables para ir comprobando maximos y minimos de temperatura
    float gradosC = 0;
    //Salida de informacion en TextArea
    String salidaActualMaximosMinimos = "";
    //Hace el valor de los segundos
    int X = 0;

    //Recepción de datos.

    @Override
    public void serialEvent(SerialPortEvent spe) {
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                X++;
                //Lectura de lo que se encuentra en el puerto serial
                String inputLine = input.readLine();
                //Conversion de la entrada de ºC a float
                gradosC = Float.parseFloat(inputLine);
                //Comprobacion de maximos y minimos de temperatura
                if (maxC < gradosC) {
                    maxC = gradosC;
                }
                if (gradosC < minC) {
                    minC = gradosC;
                }
                salidaActualMaximosMinimos = "";
                //Salidas en TextArea
                salidaActualMaximosMinimos = "Medida actual:\n "+gradosC+"ºC";
                salidaActualMaximosMinimos = salidaActualMaximosMinimos + "\nMedida máxima:\n "+maxC+"ºC";
                salidaActualMaximosMinimos = salidaActualMaximosMinimos + "\nMedida mínima:\n "+minC+"ºC";
                jTextArea_salida.setText(salidaActualMaximosMinimos);
                
                //Estableciendo serie de datos para la grafica
                serieTemperatura.add(X, gradosC);
                //Estableciendo valor para el medidor de temperatura
                medidor.setTemperatura(inputLine);
            } catch (IOException ex) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_grafica = new javax.swing.JButton();
        jButton_medidor = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_salida = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Medidor de temperatura");
        setBackground(new java.awt.Color(51, 51, 51));
        setResizable(false);

        jButton_grafica.setText("Gráfica");
        jButton_grafica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_graficaActionPerformed(evt);
            }
        });

        jButton_medidor.setText("Medidor");
        jButton_medidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_medidorActionPerformed(evt);
            }
        });

        jTextArea_salida.setEditable(false);
        jTextArea_salida.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea_salida.setColumns(20);
        jTextArea_salida.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        jTextArea_salida.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea_salida.setRows(5);
        jScrollPane1.setViewportView(jTextArea_salida);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton_medidor, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_grafica, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 3, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_medidor)
                    .addComponent(jButton_grafica))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_graficaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_graficaActionPerformed

        //Mostrando gráfico
        mostrarGrafico();
    }//GEN-LAST:event_jButton_graficaActionPerformed

    private void jButton_medidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_medidorActionPerformed

        //Mostrar medidor
        mostrarMedidor();
    }//GEN-LAST:event_jButton_medidorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_grafica;
    private javax.swing.JButton jButton_medidor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_salida;
    // End of variables declaration//GEN-END:variables

    private void armarGrafico() {
        /*Armar gráfico*/
        serieTemperatura.add(0, 0); //Coordenada en el origen para que el gráfico inicie ahi
        coleccionDeSeries = new XYSeriesCollection(); //Inicializamos la coleccion de datos
        coleccionDeSeries.addSeries(serieTemperatura); //Agreganos la serie de datos
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

    private void mostrarMedidor() {
        medidor.setVisible(true);
        medidor.pack();
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
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
