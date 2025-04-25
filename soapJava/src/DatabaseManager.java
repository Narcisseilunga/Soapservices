import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:inscription.db";

    public DatabaseManager() {
        try {
            // Charger le driver SQLite
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(URL)) {
                String sqlComptes = "CREATE TABLE IF NOT EXISTS comptes (id INTEGER PRIMARY KEY, nom TEXT, email TEXT)";
                String sqlDemandes = "CREATE TABLE IF NOT EXISTS demandes (id INTEGER PRIMARY KEY, id_etudiant INTEGER, formulaire TEXT, pieces_jointes TEXT, statut TEXT)";
                conn.createStatement().execute(sqlComptes);
                conn.createStatement().execute(sqlDemandes);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void creerCompte(String nom, String email) {
        String sql = "INSERT INTO comptes(nom, email) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void soumettreDemande(int idEtudiant, String formulaire, String piecesJointes) {
        String sql = "INSERT INTO demandes(id_etudiant, formulaire, pieces_jointes, statut) VALUES(?, ?, ?, 'En attente')";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEtudiant);
            pstmt.setString(2, formulaire);
            pstmt.setString(3, piecesJointes);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String verifierDossier(int idDossier) {
        String sql = "SELECT statut FROM demandes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idDossier);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return "Statut du dossier ID " + idDossier + ": " + rs.getString("statut");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Dossier non trouvé.";
    }

    public void validerDossier(int idDossier) {
        String sql = "UPDATE demandes SET statut = 'Validé' WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idDossier);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void confirmerAdmission(int idDossier) {
        String sql = "UPDATE demandes SET statut = 'Admission confirmée' WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idDossier);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void televerserRecuPaiement(int idEtudiant, String reçu) {
        // Cette méthode peut être développée pour gérer le stockage des reçus
        // Par exemple, ajouter un lien vers le reçu dans une table ou un champ
        System.out.println("Reçu de paiement téléversé pour l'étudiant ID " + idEtudiant + ": " + reçu);
    }
    public List<String> afficherDemandes() {
        List<String> demandes = new ArrayList<>();
        String sql = "SELECT d.id, c.nom, d.formulaire, d.pieces_jointes, d.statut " +
                "FROM demandes d JOIN comptes c ON d.id_etudiant = c.id";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String demande = "ID: " + rs.getInt("id") + ", Nom: " + rs.getString("nom") +
                        ", Formulaire: " + rs.getString("formulaire") +
                        ", Pièces jointes: " + rs.getString("pieces_jointes") +
                        ", Statut: " + rs.getString("statut");
                demandes.add(demande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demandes;
    }

    public List<String> afficherUtilisateurs() {
        List<String> utilisateurs = new ArrayList<>();
        String sql = "SELECT id, nom FROM comptes";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                utilisateurs.add("ID: " + rs.getInt("id") + ", Nom: " + rs.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }
}