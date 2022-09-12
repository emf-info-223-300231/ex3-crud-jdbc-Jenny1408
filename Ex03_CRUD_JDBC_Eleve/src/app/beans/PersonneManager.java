/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.beans;

import java.util.List;

/**
 *
 * @author AymonJ
 */
public class PersonneManager {

    private List<Personne> listePersonnes;
    private int index = 0;

    public Personne setPersonnes(List<Personne> listePersonnes) {
        this.listePersonnes = listePersonnes;
        return listePersonnes.get(index);
    }

    public Personne precedentPersonne() {
        Personne p = new Personne();
        if (listePersonnes != null) {
            if (index > 0) {
                index--;
            }
            p = listePersonnes.get(index);
        } else {
            p = listePersonnes.get(0);
        }
        return p;
    }

    public Personne suivantPersonne() {
        Personne p = new Personne();
        if (listePersonnes != null) {
            if (index < listePersonnes.size() - 1) {
                index++;
                p = listePersonnes.get(index);
            } else {
                p = listePersonnes.get(index);
            }

        }
        return p;
    }

    public Personne debutPersonne() {
        Personne p = new Personne();
        if (listePersonnes != null) {
            p = listePersonnes.get(0);
        }
        return p;
    }

    public Personne finPersonne() {
        Personne p = new Personne();
        if (listePersonnes != null) {
            p = listePersonnes.get(listePersonnes.size() - 1);
        }
        return p;
    }

    public Personne courantPersonne() {
        Personne p = new Personne();
        if (listePersonnes == null || listePersonnes.isEmpty()) {
            return null;
        }
        if (index >= listePersonnes.size()) {
            index = listePersonnes.size() - 1;
        }
        return listePersonnes.get(index);

    }

}
