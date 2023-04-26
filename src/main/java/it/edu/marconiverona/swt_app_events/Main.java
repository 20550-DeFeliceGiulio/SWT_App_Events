package it.edu.marconiverona.swt_app_events;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;

public class Main {

    public static void bottoneOperazione(String operazione) throws NumberFormatException {
        // ottieni gli elementi grafici dal repository view
        Text myField01 = (Text) Repository.repository_View.get("myField01");
        Text myField02 = (Text) Repository.repository_View.get("myField02");
        Text myField03 = (Text) Repository.repository_View.get("myField03");
        
        // leggi il contenuto dei myField
        Float operatore1 = Float.parseFloat( myField01.getText() );
        Float operatore2 = Float.parseFloat( myField02.getText() );
        
        // ottieni l'elemento model per i calcoli
        Operazione op_model = (Operazione) Repository.repository_Model
                .get("operazioni_model");
        
        // calcola il risultato
        float risultato = 0;
        switch (operazione) {
            case "somma":
                risultato = op_model.somma(operatore1, operatore2);
                break;
            case "differenza":
                risultato = op_model.differenza(operatore1, operatore2);
                break;
            case "moltiplicazione":
                risultato = op_model.moltiplicazione(operatore1, operatore2);
                break;
            case "divisione":
                risultato = op_model.divisione(operatore1, operatore2);
                break;
        }

        // aggiorna il campo myLabel02
        myField03.setText("" + risultato);
    }
    
    public static void main(String[] args) {

        //------------------------------------
        // istanza finestra principale root
        //------------------------------------
        Display display = new Display();
        Shell root = new Shell(display);
        root.setLayout(new GridLayout(2, false));
        root.setText("Calcolatrice");
        
        GridData fillHorizontally = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        fillHorizontally.horizontalIndent = 6;
        
        //---- Istanze oggetti grafici + model + control=> CallBack/Listener
        //------------------------------------
        // istanze degli oggetti grafici
        //------------------------------------
        Label myLabel01 = new Label(root, SWT.NORMAL);
        myLabel01.setText("Operando 1");
        Text myField01 = new Text(root, SWT.BORDER);
        myField01.setLayoutData(fillHorizontally);
        
        Label myLabel02 = new Label(root, SWT.NORMAL);
        myLabel02.setText("Operando 2");
        Text myField02 = new Text(root, SWT.BORDER);
        myField02.setLayoutData(fillHorizontally);
        
        Label myLabel03 = new Label(root, SWT.NORMAL);
        myLabel03.setText("Operazioni");
        Composite buttonComposite = new Composite(root, SWT.NORMAL);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        buttonComposite.setLayout(gridLayout);
        Button myButton01 = new Button(buttonComposite, SWT.NORMAL);
        myButton01.setText("somma");
        Button myButton02 = new Button(buttonComposite, SWT.NORMAL);
        myButton02.setText("differenza");
        Button myButton03 = new Button(buttonComposite, SWT.NORMAL);
        myButton03.setText("moltiplicazione");
        Button myButton04 = new Button(buttonComposite, SWT.NORMAL);
        myButton04.setText("divisione");
        
        Label myLabel04 = new Label(root, SWT.NORMAL);
        myLabel04.setText("Risultato");
        Text myField03 = new Text(root, SWT.BORDER);
        myField03.setLayoutData(fillHorizontally);
        myField03.setEditable(false);


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
                put("myLabel02", myLabel02);
                put("myLabel03", myLabel03);
                put("myLabel04", myLabel04);
                put("myButton01", myButton01);
                put("myButton02", myButton02);
                put("myButton03", myButton03);
                put("myButton04", myButton04);
                put("myField01", myField01);
                put("myField02", myField02);
                put("myField03", myField03);
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
        Map<String, Object> repository_Listener = new HashMap<String, Object>();
        int i = 0;
        for (Object value : repository_View.values()) {
            if (value instanceof Button) {
                Button button = (Button) value;
                Listener buttonListener = new Listener() {
                    @Override
                    public void handleEvent(Event e) {
                        try {
                            bottoneOperazione(button.getText());
                        } catch (NumberFormatException exc) {
                            myField03.setText("Errore");
                        }
                    }
                };
                i++;
                repository_Listener.put("myButton0" + i + "_listener", buttonListener);
                button.addListener(SWT.MouseDown, buttonListener);
            }
        }
        
        //------------------------------------
        // inizializza il repository (ovvero l'App)
        //------------------------------------
        Repository.repository_View = repository_View;
        Repository.repository_Model = repository_Model;
        Repository.repository_Listener = repository_Listener;
        
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
