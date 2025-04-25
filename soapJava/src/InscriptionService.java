import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface InscriptionService {
    @WebMethod
    String creerCompte(String nom, String email);

    @WebMethod
    String soumettreDemande(String idEtudiant, String formulaire, String piecesJointes);

    @WebMethod
    String verifierDossier(String idDossier);

    @WebMethod
    String validerDossier(String idDossier);

    @WebMethod
    String confirmerAdmission(String idDossier);

    @WebMethod
    String televerserRecuPaiement(String idEtudiant, String reçu);

    @WebMethod
    List<String> afficherDemandes(); // Nouvelle méthode pour afficher les demandes
    @WebMethod
    List<String> afficherUtilisateurs(); // Nouvelle méthode pour afficher les utilisateurs
}