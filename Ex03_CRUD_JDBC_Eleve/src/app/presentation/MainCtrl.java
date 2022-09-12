package app.presentation;

import app.beans.Personne;
import app.beans.PersonneManager;
import app.exceptions.MyDBException;
import app.helpers.DateTimeLib;
import app.helpers.JfxPopup;
import app.workers.DbWorker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import java.io.File;
import app.workers.DbWorkerItf;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javafx.application.Platform;

/**
 *
 * @author PA/STT
 */
public class MainCtrl implements Initializable {

    // DBs à tester
    private enum TypesDB {
        MYSQL, HSQLDB, ACCESS
    };

    // DB par défaut
    final static private TypesDB DB_TYPE = TypesDB.MYSQL;

    private DbWorkerItf dbWrk;
    private PersonneManager pManager;
    private DateTimeLib dateLib;
    private boolean modeAjout;

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtPK;
    @FXML
    private TextField txtNo;
    @FXML
    private TextField txtRue;
    @FXML
    private TextField txtNPA;
    @FXML
    private TextField txtLocalite;
    @FXML
    private TextField txtSalaire;
    @FXML
    private CheckBox ckbActif;
    @FXML
    private Button btnDebut;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnEnd;
    @FXML
    private Button btnSauver;
    @FXML
    private Button btnAnnuler;
    @FXML
    private DatePicker dateNaissance;

    /*
   * METHODES NECESSAIRES A LA VUE
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbWrk = new DbWorker();
        pManager = new PersonneManager();
        dateLib = new DateTimeLib();
        ouvrirDB();
    }

    @FXML
    public void actionPrevious(ActionEvent event) {
        afficherPersonne(pManager.precedentPersonne());
    }

    @FXML
    public void actionNext(ActionEvent event) {
        afficherPersonne(pManager.suivantPersonne());
    }

    @FXML
    private void actionEnd(ActionEvent event) {
        afficherPersonne(pManager.finPersonne());
    }

    @FXML
    private void debut(ActionEvent event) {
        afficherPersonne(pManager.debutPersonne());
    }

    @FXML
    private void menuAjouter(ActionEvent event) throws MyDBException {
        modeAjout = true;
        if (modeAjout) {
            rendreVisibleBoutonsDepl(false);
            effacerContenuChamps();
        }
        List<Personne> list = dbWrk.lirePersonnes();
        Personne pers = pManager.setPersonnes(list);
        dbWrk.creer(pers);
    }

    @FXML
    private void menuModifier(ActionEvent event) throws MyDBException {
        modeAjout = true;
        if (modeAjout) {
            rendreVisibleBoutonsDepl(false);
        }
        List<Personne> list = dbWrk.lirePersonnes();
        Personne pers = pManager.setPersonnes(list);
        dbWrk.modifier(pers);
    }

    @FXML
    private void menuEffacer(ActionEvent event) throws MyDBException {
        modeAjout = true;
        if (modeAjout) {
            rendreVisibleBoutonsDepl(false);
        }
        List<Personne> list = dbWrk.lirePersonnes();
        Personne pers = pManager.setPersonnes(list);
        dbWrk.effacer(pers);
    }

    @FXML
    private void menuQuitter(ActionEvent event) {
        try {
            dbWrk.deconnecter(); // ne pas oublier !!!
        } catch (MyDBException ex) {
            System.out.println(ex.getMessage());
        }
        Platform.exit();
    }

    @FXML
    private void annulerPersonne(ActionEvent event) throws MyDBException {
        List<Personne> list = dbWrk.lirePersonnes();
        Personne pers = pManager.setPersonnes(list);
        afficherPersonne(pers);
    }

    @FXML
    private void sauverPersonne(ActionEvent event) {
        java.sql.Date date = java.sql.Date.valueOf(dateNaissance.getValue());
        try {
            if (txtPK.getText().equals("")) {
                Personne p = new Personne(txtNom.getText(), txtPrenom.getText(), date, Integer.valueOf(txtNo.getText()), txtRue.getText(), Integer.valueOf(txtNPA.getText()), txtLocalite.getText(), ckbActif.isSelected(), Double.valueOf(txtSalaire.getText()), java.sql.Date.valueOf(LocalDate.now()));
                dbWrk.creer(p);
            } else {
                Personne p = new Personne(Integer.parseInt(txtPK.getText()), txtNom.getText(), txtPrenom.getText(), date, Integer.valueOf(txtNo.getText()), txtRue.getText(), Integer.valueOf(txtNPA.getText()), txtLocalite.getText(), ckbActif.isSelected(), Double.valueOf(txtSalaire.getText()), java.sql.Date.valueOf(LocalDate.now()));
                dbWrk.modifier(p);
            }

            pManager.setPersonnes(dbWrk.lirePersonnes());
            afficherPersonne(pManager.precedentPersonne());
            rendreVisibleBoutonsDepl(true);
        } catch (MyDBException e) {
            System.out.println("Erreur de sauverPersonne");
        }
    }

    public void quitter() {
        try {
            dbWrk.deconnecter(); // ne pas oublier !!!
        } catch (MyDBException ex) {
            System.out.println(ex.getMessage());
        }
        Platform.exit();
    }

    /*
   * METHODES PRIVEES 
     */
    private void afficherPersonne(Personne p) {
        modeAjout = true;
        if (modeAjout) {
            rendreVisibleBoutonsDepl(true);
        }
        if (p != null) {
            LocalDate date = dateLib.dateToLocalDate(p.getDateNaissance());
            txtPrenom.setText(p.getPrenom());
            txtNom.setText(p.getNom());
            txtPK.setText(p.getPkPers() + "");
            txtNo.setText(p.getNoRue() + "");
            txtRue.setText(p.getRue());
            txtNPA.setText(p.getNpa() + "");
            txtLocalite.setText(p.getLocalite());
            txtSalaire.setText(p.getSalaire() + "");
            dateNaissance.setValue(date);
            if (p.isActif() == true) {
                ckbActif.setSelected(true);
            } else {
                ckbActif.setSelected(false);
            }

        }
    }

    private void ouvrirDB() {
        try {
            switch (DB_TYPE) {
                case MYSQL:
                    dbWrk.connecterBdMySQL("223_personne_1table");
                    break;
                case HSQLDB:
                    dbWrk.connecterBdHSQLDB("../data" + File.separator + "223_personne_1table");
                    break;
                case ACCESS:
                    dbWrk.connecterBdAccess("../data" + File.separator + "223_Personne_1table.accdb");
                    break;
                default:
                    System.out.println("Base de données pas définie");
            }
            System.out.println("------- DB OK ----------");
            List<Personne> list = dbWrk.lirePersonnes();
            Personne pers = pManager.setPersonnes(list);
            afficherPersonne(pers);
        } catch (MyDBException ex) {
            JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
            System.exit(1);
        }
    }

    private void rendreVisibleBoutonsDepl(boolean b) {
        btnDebut.setVisible(b);
        btnPrevious.setVisible(b);
        btnNext.setVisible(b);
        btnEnd.setVisible(b);
        btnAnnuler.setVisible(!b);
        btnSauver.setVisible(!b);
    }

    private void effacerContenuChamps() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtPK.setText("");
        txtNo.setText("");
        txtRue.setText("");
        txtNPA.setText("");
        txtLocalite.setText("");
        txtSalaire.setText("");
        ckbActif.setSelected(false);
    }

}
