package org.exemple;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String COMPTES_FILE = "comptes.json";
    private static final String DEMANDES_FILE = "demandes.json";
    private Gson gson = new Gson();

    public DatabaseManager() {
        // Assurez-vous que les fichiers existent
        if (!Files.exists(Paths.get(COMPTES_FILE))) {
            try {
                new File(COMPTES_FILE).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(Paths.get(DEMANDES_FILE))) {
            try {
                new File(DEMANDES_FILE).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void creerCompte(String nom, String email) {
        List<Compte> comptes = getComptes();
        int id = comptes.size() + 1; // Simple ID auto-incrémenté
        comptes.add(new Compte(id, nom, email));
        saveComptes(comptes);
    }

    public void soumettreDemande(int idEtudiant, String formulaire, String piecesJointes) {
        List<Demande> demandes = getDemandes();
        int id = demandes.size() + 1; // Simple ID auto-incrémenté
        demandes.add(new Demande(id, idEtudiant, formulaire, piecesJointes, "En attente"));
        saveDemandes(demandes);
    }

    public String verifierDossier(int idDossier) {
        List<Demande> demandes = getDemandes();
        for (Demande demande : demandes) {
            if (demande.getId() == idDossier) {
                return "Statut du dossier ID " + idDossier + ": " + demande.getStatut();
            }
        }
        return "Dossier non trouvé.";
    }

    public void validerDossier(int idDossier) {
        List<Demande> demandes = getDemandes();
        for (Demande demande : demandes) {
            if (demande.getId() == idDossier) {
                demande.setStatut("Validé");
                saveDemandes(demandes);
                return;
            }
        }
    }

    public void confirmerAdmission(int idDossier) {
        List<Demande> demandes = getDemandes();
        for (Demande demande : demandes) {
            if (demande.getId() == idDossier) {
                demande.setStatut("Admission confirmée");
                saveDemandes(demandes);
                return;
            }
        }
    }

    public void televerserRecuPaiement(int idEtudiant, String reçu) {
        System.out.println("Reçu de paiement téléversé pour l'étudiant ID " + idEtudiant + ": " + reçu);
    }

    public List<String> afficherDemandes() {
        List<String> demandes = new ArrayList<>();
        for (Demande demande : getDemandes()) {
            demandes.add("ID: " + demande.getId() + ", ID Étudiant: " + demande.getIdEtudiant() +
                    ", Formulaire: " + demande.getFormulaire() +
                    ", Pièces jointes: " + demande.getPiecesJointes() +
                    ", Statut: " + demande.getStatut());
        }
        return demandes;
    }

    public List<String> afficherUtilisateurs() {
        List<String> utilisateurs = new ArrayList<>();
        List<Compte> comptes = getComptes(); // Vérifiez que cette liste n'est pas null
        if (comptes == null) {
            System.out.println("La liste des comptes est null.");
            return utilisateurs;
        }
        for (Compte compte : comptes) {
            if (compte != null) { // Vérifiez que compte n'est pas null
                utilisateurs.add("ID: " + compte.getId() + ", Nom: " + compte.getNom());
            }
        }
        return utilisateurs;
    }
    private List<Compte> getComptes() {
        try (Reader reader = new FileReader(COMPTES_FILE)) {
            return gson.fromJson(reader, new TypeToken<List<Compte>>() {}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void saveComptes(List<Compte> comptes) {
        try (Writer writer = new FileWriter(COMPTES_FILE)) {
            gson.toJson(comptes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Demande> getDemandes() {
        try (Reader reader = new FileReader(DEMANDES_FILE)) {
            return gson.fromJson(reader, new TypeToken<List<Demande>>() {}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void saveDemandes(List<Demande> demandes) {
        try (Writer writer = new FileWriter(DEMANDES_FILE)) {
            gson.toJson(demandes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classes internes pour gérer les comptes et les demandes
    class Compte {
        private int id;
        private String nom;
        private String email;

        public Compte(int id, String nom, String email) {
            this.id = id;
            this.nom = nom;
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public String getNom() {
            return nom;
        }
    }

    class Demande {
        private int id;
        private int idEtudiant;
        private String formulaire;
        private String piecesJointes;
        private String statut;

        public Demande(int id, int idEtudiant, String formulaire, String piecesJointes, String statut) {
            this.id = id;
            this.idEtudiant = idEtudiant;
            this.formulaire = formulaire;
            this.piecesJointes = piecesJointes;
            this.statut = statut;
        }

        public int getId() {
            return id;
        }

        public int getIdEtudiant() {
            return idEtudiant;
        }

        public String getFormulaire() {
            return formulaire;
        }

        public String getPiecesJointes() {
            return piecesJointes;
        }

        public String getStatut() {
            return statut;
        }

        public void setStatut(String statut) {
            this.statut = statut;
        }
    }
}