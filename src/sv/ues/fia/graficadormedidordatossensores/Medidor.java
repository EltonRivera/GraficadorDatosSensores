package sv.ues.fia.graficadormedidordatossensores;

import java.awt.*;
import javax.swing.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.*;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

public class Medidor extends JFrame {
    
     MedidorPanel medidorPanel;
    
    public String temperatura = "0";

    //Construcctor
    public Medidor(String s) {
        super(s);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(crearMedidorPanel());
    }
    

     class MedidorPanel extends JPanel{

        float temperatura = 0;
        DefaultValueDataset dataset;

        //Medidor por defecto de JFreeChart
        public  JFreeChart createStandardDialChart(String s, String s1, ValueDataset valuedataset, double d, double d1, double d2, int i) {
            DialPlot dialplot = new DialPlot();
            dialplot.setDataset(valuedataset);
            dialplot.setDialFrame(new StandardDialFrame());
            dialplot.setBackground(new DialBackground());
            DialTextAnnotation dialtextannotation = new DialTextAnnotation(s1);
            dialtextannotation.setFont(new Font("Dialog", 1, 14));
            dialtextannotation.setRadius(0.69999999999999996D);
            dialplot.addLayer(dialtextannotation);
            DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
            dialplot.addLayer(dialvalueindicator);
            StandardDialScale standarddialscale = new StandardDialScale(d, d1, -120D, -300D, 10D, 4);
            standarddialscale.setMajorTickIncrement(d2);
            standarddialscale.setMinorTickCount(i);
            standarddialscale.setTickRadius(0.88D);
            standarddialscale.setTickLabelOffset(0.14999999999999999D);
            standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
            dialplot.addScale(0, standarddialscale);
            dialplot.addPointer(new org.jfree.chart.plot.dial.DialPointer.Pin());
            DialCap dialcap = new DialCap();
            dialplot.setCap(dialcap);
            return new JFreeChart(s, dialplot);
        }
        
        //Creando y personalizando medidor panel en base al medidor por defecto
        public MedidorPanel() {
            super(new BorderLayout());
            dataset = new DefaultValueDataset(0.0);
            
            //Creamos un tipo de gráfico Dial en el cual nos basamos para crear el medidor
            JFreeChart jfreechart = createStandardDialChart("Medidor de temperatura", "°C", dataset, -40D, 125D, 10D, 4);
            DialPlot medidor = (DialPlot) jfreechart.getPlot();
            StandardDialRange standarddialrange = new StandardDialRange(80D, 120D, Color.red); //Creamos zona de temperatura alta
            //Dobles radios
            standarddialrange.setInnerRadius(0.52000000000000002D);
            standarddialrange.setOuterRadius(0.55000000000000004D);
            //Agregamos la capa que dibuja los radios
            medidor.addLayer(standarddialrange);
            StandardDialRange standarddialrange1 = new StandardDialRange(40D, 80D, Color.orange);//Creamos zona de temperatura media
            //Dobles radios
            standarddialrange1.setInnerRadius(0.52000000000000002D);
            standarddialrange1.setOuterRadius(0.55000000000000004D);
            //Agregamos la capa que dibuja los radios
            medidor.addLayer(standarddialrange1);
            StandardDialRange standarddialrange2 = new StandardDialRange(-40D, 40D, Color.green);//Creamos zona de temperatura baja
            //Dobles radios
            standarddialrange2.setInnerRadius(0.52000000000000002D);
            standarddialrange2.setOuterRadius(0.55000000000000004D);
            //Agregamos la capa que dibuja los radios
            medidor.addLayer(standarddialrange2);

            //Creamos un gradiente combinando 3 colores
            GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
            //Convertirmos gradiente en el fondo para el medidor
            DialBackground dialbackground = new DialBackground(gradientpaint);
            dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
            //Agregamos fondo al medidor
            medidor.setBackground(dialbackground);

            //Removemos aguja por defecto
            medidor.removePointer(0);
            //Instanciamos nueva aguja
            org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer();
            //Agregamos la aguja al medidor
            medidor.addPointer(pointer);

            //Creamos panel para mostrar el medidor y sus dimensiones
            ChartPanel chartpanel = new ChartPanel(jfreechart);
            chartpanel.setPreferredSize(new Dimension(400, 400));
             add(chartpanel);
        }
    }//Fin de clase

    //Enviamos el medidor creado y perzonalizado
    public  JPanel crearMedidorPanel() {
        medidorPanel = new MedidorPanel();
        return medidorPanel;
    }

    public void setTemperatura(String temperatura) {
        medidorPanel.dataset.setValue(Float.parseFloat(temperatura));
    }
    
}
