package controller;

import Entidades.Persona;
import conexion.JPAUtil;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javax.persistence.EntityManager;

public class PrimaryController implements Initializable{

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODOdsd
        System.out.println("bebe");
       
    }
    
    @FXML
    private void switchToSecondary() throws IOException {
        /* EntityManager jpa= JPAUtil.getEntityManagerFactory().createEntityManager();
            List<Persona> lista_persona=jpa.createQuery("SELECT p FROM Persona p").getResultList();
            for (Persona opersona : lista_persona) {
                System.err.println(opersona.getNombre());
               
        }
             Persona oP=new Persona("matilda"); 
                jpa.getTransaction().begin();    
                jpa.persist(oP);
                jpa.getTransaction().commit();*/
        App.setRoot("secondary");
    }
    
    
}
