package sv.ues.fia.graficadordatossensores;

/**
 * @author racsoraul
 */
public class GraficadorDatosSensores {

    public static void main(String[] args) {
        
        Graficador_Frame graficador_Frame = new Graficador_Frame();
        graficador_Frame.setVisible(true);
        

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Graficador_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Graficador_Frame().setVisible(true);
            }
        });
    }
}
