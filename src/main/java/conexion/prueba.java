/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import Entidades.Carro;
import Entidades.Persona;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author yalle
 */
public class prueba {
    public static void main(String[] args){
         EntityManager entity= JPAUtil.getEntityManagerFactory().createEntityManager();
      EntityManager jpa= JPAUtil.getEntityManagerFactory().createEntityManager();
        Carro carro=(Carro) jpa.createQuery("SELECT p FROM Carro p where idcarro=2").getSingleResult();
            Persona persona=(Persona) jpa.createQuery("SELECT p FROM Persona p where idpersona=1").getSingleResult();
             System.out.println(persona.getNombre());
            
             //Persona oP=new Persona("sebastian"); 
             //Carro oC=new Carro(persona,"hyundai");
             carro.setPersona(persona);
              jpa.getTransaction().begin();           
                jpa.persist(carro);
                jpa.getTransaction().commit();

    }
    
}
