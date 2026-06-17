# 9emePRO Android

Application Android de preparation pour la **9eme annee fondamentale** en Haiti.

> WebView native chargeant la plateforme 9emePRO (Google Apps Script).

---

## Matieres couvertes (11)

| Matiere | | Matiere |
|---|---|---|
| Maths | | Anglais |
| Comm. Creole | | Espagnol |
| Comm. Francaise | | Env. et Agriculture |
| Sciences Experimentales | | EPS |
| Sciences Sociales | | Education Citoyenne |
| | | ETAP |

---

## Installation de l'APK

1. Aller dans l'onglet **Releases** de ce repo
2. Telecharger le dernier fichier `.apk`
3. Sur Android : **Parametres → Securite → Sources inconnues** → Activer
4. Ouvrir le `.apk` et installer

---

## Configuration avant build

### 1. Remplacer l'URL GAS

Dans `app/src/main/java/com/widger/neuvemepro/MainActivity.java`, ligne 17 :

```java
private static final String GAS_URL = "https://script.google.com/macros/s/TON_DEPLOYMENT_ID/exec";
```

Remplacer `TON_DEPLOYMENT_ID` par l'ID de deploiement de ton projet GAS.

### 2. Generer un keystore de signature

```bash
keytool -genkey -v -keystore keystore.jks \
  -alias 9emepro \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

### 3. Ajouter les Secrets GitHub

Dans **Settings → Secrets and variables → Actions** de ton repo :

| Secret | Valeur |
|---|---|
| `KEYSTORE_BASE64` | `base64 -w 0 keystore.jks` |
| `KEYSTORE_PASSWORD` | Mot de passe du keystore |
| `KEY_ALIAS` | `9emepro` |
| `KEY_PASSWORD` | Mot de passe de la cle |

### 4. Publier une release

```bash
git tag v1.0
git push origin v1.0
```

GitHub Actions compile automatiquement et publie l'APK dans les Releases.

---

## Structure du projet

```
9emePRO-android/
├── .github/workflows/build.yml     <- Build automatique
├── app/
│   └── src/main/
│       ├── java/com/widger/neuvemepro/
│       │   └── MainActivity.java   <- WebView principale
│       ├── res/
│       │   ├── layout/activity_main.xml
│       │   ├── values/
│       │   └── xml/network_security_config.xml
│       └── AndroidManifest.xml
├── build.gradle
├── settings.gradle
└── gradlew
```

---

## Prerequis techniques

- Android 5.0+ (API 21+)
- Connexion internet requise (l'app charge GAS en temps reel)

---

*Developpe par Widger — Miragoane, Haiti*
