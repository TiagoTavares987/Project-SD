package rmi.advancedWars.server;

import java.io.Serializable;

/**
 *
 * @author rmoreira
 */
public class Game implements Serializable {

    private int id = 0;
    private int nPlayers;
    private int totalPlayers;
    private String mapLvl;
    private SubjectRI subjectRI;

    public Game(String mapLvl, int totalPlayers, SubjectRI subjectRI) {
        id++;
        this.nPlayers = 0;
        this.mapLvl = mapLvl;
        this.totalPlayers = totalPlayers;
        this.subjectRI = subjectRI;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getnPlayers() {
        return nPlayers;
    }

    public void setnPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }
    public String getMapLvl() {
        return mapLvl;
    }
    public SubjectRI getSubjectRI() {
        return subjectRI;
    }

    public void setSubjectRI(SubjectRI subjectRI) {
        this.subjectRI = subjectRI;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", nPlayers=" + nPlayers +
                ", mapLvl='" + mapLvl + '\'' +
                ", subjectRI=" + subjectRI +
                '}';
    }
}