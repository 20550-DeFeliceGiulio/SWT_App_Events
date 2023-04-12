package it.edu.marconiverona.swt_app_events;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;

public class Main {

    public static void main(String[] args) {

        //------------------------------------
        // istanza finestra principale root
        //------------------------------------
        Display display = new Display();
        Shell root = new Shell(display);
        root.setLayout(new FillLayout(SWT.VERTICAL));

        //---- Istanze oggetti grafici + model + control=> CallBack/Listener
        //------------------------------------
        // istanze degli oggetti grafici
        //------------------------------------
        Label myLabel01 = new Label(root, SWT.NORMAL);
        myLabel01.setText("addendo 1");
        Button myButton01 = new Button(root, SWT.NORMAL);
        myButton01.setText("operazione");
        Text myField01 = new Text(root, SWT.BORDER);
        myField01.setSize(10, 1);;
        Text myField02 = new Text(root, SWT.BORDER);

        // Operazioni sulla finestra principale
        root.pack();
        root.open();

        //------------------------------------
        // repository view
        // Nota: viene usato il blocco staic  on the fly  della classe
        // HashMap per far eseguire in un metodo static i put delle key-Object
        //------------------------------------
        Map<String, Object> repository_View = new HashMap<String, Object>() {
            {
                put("root", root);
                put("myLabel01", myLabel01);
                put("myButton01", myButton01);
                put("myField01", myField01);
                put("myField02", myField02);
            }
        };

        //------------------------------------
        // Istanza di una classe capace di fare una elaborazione
        //------------------------------------
        Operazione operazioni_model = new Operazione();

        //------------------------------------
        // repository model
        //------------------------------------
        Map<String, Object> repository_Model = new HashMap<String, Object>() {
            {
                // costruttore della classe, non dell'istanza
                put("operazioni_model", operazioni_model);
            }
        };

        //------------------------------------
        // istanze oggetti  aventi interface Listener : Listener / CallaBack
        //------------------------------------
        Listener myButton01_listener
                = new Listener() {
            @Override
            public void handleEvent(Event e) {
                // legge contenuto delle TextEdit myField01 e myField02
                Text myField01 = (Text) Repository.repository_View.get("myField01");
                String text1 = myField01.getText();

                Text myField02 = (Text) Repository.repository_View.get("myField02");
                String text2 = myField02.getText();

                // recupera l'oggetto model per i calcoli
                Operazione op_model = (Operazione) Repository.repository_Model
                        .get("operazioni_model");

                // aggiorna il campo  myFiel
                myField02.setText(""
                        + op_model.somma(Integer.parseInt(text1),
                                Integer.parseInt(text2)));

            }
        };

        //------------------------------------
        // repository listener o callback
        //------------------------------------
        Map<String, Object> repository_Listener = new HashMap<String, Object>() {
            {
                // costruttore della classe, non dell'istanza
                put("myButton01_listener", myButton01_listener);
            }
        };

        //------------------------------------
        // inizializza il repository (ovvero l'App)
        //------------------------------------
        Repository.repository_View = repository_View;
        Repository.repository_Model = repository_Model;
        Repository.repository_Listener = repository_Listener;

        //------------------------------------
        // inizializza gli oggetti view che attivano i bottoni
        //------------------------------------
        myButton01.addListener(SWT.MouseDown,
                (Listener) Repository.repository_Listener.get("myButton01_listener"));

        //------------------------------------
        // finche'  la finestra non viene chiusa _
        //        { se ci sono eventi leggi coda degli eventi => esegui
        //          altrimenti in attesa }
        //-------------------------------------          
        while (!root.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();

    }
}
