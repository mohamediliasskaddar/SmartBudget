# Mini‑projet Android  — SmartBudget

<aside>
🎯

**Objectif métier**

Créer une application Android *offline-first* de gestion de budget personnel qui permet de **suivre ses dépenses**, **comprendre où part l’argent** (par catégorie / période), et **exporter** ses données pour un usage externe.

</aside>

### 1) Contexte & besoin

De nombreux étudiants gèrent des dépenses récurrentes (transport, repas, loisirs) sans visibilité claire.

L’application **SmartBudget** sert de carnet de dépenses structuré : on enregistre une dépense en quelques secondes, puis on consulte des totaux et répartitions par mois.

### 2) Périmètre fonctionnel (côté métier)

#### Fonctionnalités principales (obligatoires)

- **Gestion des dépenses (CRUD)**
    - Ajouter une dépense
    - Modifier une dépense
    - Supprimer une dépense (avec confirmation)
- **Catégorisation**
    - Associer chaque dépense à une catégorie métier (ex : Alimentation, Transport, Logement, Santé, Loisirs, Études, Autre)
- **Filtrage temporel**
    - Vue par **mois** (ex : mars 2026)
    - Navigation mois précédent/suivant
- **Synthèse**
    - Total des dépenses du mois
    - Total par catégorie (répartition)
    - Top catégories du mois
- **Offline-first**
    - Toutes les opérations fonctionnent **sans internet**

#### Options métier (bonus)

- **Budgets mensuels par catégorie** (ex : Transport = 300 MAD)
- **Dépenses récurrentes** (abonnement, loyer)
- **Export CSV** du mois
- **Import** (rejouer un CSV simple)

### 3) Modèles de données (proposition)

> Les modèles sont décrits “métier” d’abord, puis vous pouvez les mapper en Room (Entities + Relations).
>

#### A) Dépense (Expense)

- **id** : identifiant
- **amount** : montant (ex : 45.50)
- **currency** : devise (par défaut MAD)
- **date** : date de la dépense
- **categoryId** : référence catégorie
- **note** : note libre (facultatif)
- **paymentMethod** : espèce / carte / virement (optionnel)
- **createdAt** : date de création
- **updatedAt** : date de dernière modification

#### B) Catégorie (Category)

- **id**
- **name** (unique) : Alimentation, Transport…
- **icon** : emoji ou nom d’icône
- **color** : couleur UI (string)
- **isActive** : bool (permet d’archiver)

#### C) (Bonus) Budget mensuel par catégorie (MonthlyBudget)

- **id**
- **month** : AAAA-MM
- **categoryId**
- **limitAmount** : montant limite

### 4) Règles métier & validations

- Montant **strictement positif**
- Date obligatoire
- Catégorie obligatoire
- Une catégorie a un nom **unique**
- Suppression d’une catégorie :
    - soit interdite si des dépenses existent
    - soit autorisée avec bascule des dépenses vers “Autre”

### 5) Vue globale de l’UI (maquette fonctionnelle)

<aside>
🧭

**Navigation proposée (simple et pédagogique)**

- Onglets (Bottom bar) : **Dépenses** | **Stats** | **Paramètres**
- Écrans modaux : **Ajouter / Modifier dépense**
</aside>

#### Écran 1 — Dépenses (liste)

- En-tête : Mois courant + boutons ◀ ▶
- Carte “Total du mois”
- Filtres : catégorie (dropdown/chips) + tri (date / montant)
- Liste des dépenses : item = montant + catégorie + date + note
- Action principale : bouton “+” (ajouter)
- États UX :
    - vide (aucune dépense)
    - recherche/filtre sans résultat

#### Écran 2 — Ajouter / Modifier une dépense (formulaire)

- Champs : Montant, Catégorie, Date, Note, (Méthode paiement)
- Bouton : Enregistrer
- Erreurs inline (montant, date)

#### Écran 3 — Statistiques

- Total du mois
- Répartition par catégorie (liste triée décroissante)
- Top catégories
- (Bonus) comparaison mois N vs mois N-1

#### Écran 4 — Paramètres

- Gestion des catégories (activer/désactiver)
- Choix devise
- Export CSV (bonus)

### 6) Jeux de données de test (pour la démo)

- Au moins 30 dépenses réparties sur 2 mois
- 6 à 8 catégories actives

### 7) Critères de réussite (résultat attendu)

- L’app permet de suivre un mois complet, filtrer, modifier, supprimer sans crash
- Les stats donnent une lecture claire des “postes de dépense”
- L’app reste fonctionnelle hors connexion