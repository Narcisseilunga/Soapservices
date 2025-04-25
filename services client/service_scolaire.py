from flask import Flask, render_template, request, redirect, url_for
from zeep import Client

app = Flask(__name__)

# Créer un client pour le service SOAP
client = Client('http://localhost:8081/inscription?wsdl')

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/creer_compte', methods=['POST'])
def creer_compte():
    nom = request.form['nom']
    email = request.form['email']
    compte = client.service.creerCompte(nom, email)
    return render_template('result.html', message=compte)

@app.route('/soumettre_demande', methods=['POST'])
def soumettre_demande():
    dossier_id = request.form['dossier_id']
    formulaire = request.form['formulaire']
    pieces = request.form['pieces']
    demande = client.service.soumettreDemande(dossier_id, formulaire, pieces)
    return render_template('result.html', message=demande)

@app.route('/verifier_dossier', methods=['POST'])
def verifier_dossier():
    dossier_id = request.form['dossier_id']
    verification = client.service.verifierDossier(dossier_id)
    return render_template('result.html', message=verification)

@app.route('/valider_dossier', methods=['POST'])
def valider_dossier():
    dossier_id = request.form['dossier_id']
    validation = client.service.validerDossier(dossier_id)
    return render_template('result.html', message=validation)

@app.route('/confirmer_admission', methods=['POST'])
def confirmer_admission():
    dossier_id = request.form['dossier_id']
    confirmation = client.service.confirmerAdmission(dossier_id)
    return render_template('result.html', message=confirmation)

@app.route('/televerser_recu', methods=['POST'])
def televerser_recu():
    dossier_id = request.form['dossier_id']
    recu = request.form['recu']
    televersement = client.service.televerserRecuPaiement(dossier_id, recu)
    return render_template('result.html', message=televersement)

@app.route('/afficher_demandes', methods=['GET'])
def afficher_demandes():
    demandes = client.service.afficherDemandes()
    return render_template('demandes.html', demandes=demandes)

@app.route('/afficher_utilisateurs', methods=['GET'])
def afficher_utilisateurs():
    utilisateurs = client.service.afficherUtilisateurs()
    return render_template('utilisateurs.html', utilisateurs=utilisateurs)

if __name__ == '__main__':
    app.run(debug=True)