import java.awt.Color;
public class Test
{
  public static void main(String[] args)
  { // main
    Fenetre1 maFenetreDebut = new Fenetre1(); // on instancie la première fenêtre
    String[] noms = new String[2]; // noms des joueurs
    int[] levels = new int[2]; // niveaux des joueurs
    int taille[] = {3, 3}; // taille du plateau instanciée par défaut
    Color couleurs[] = {Color.red, Color.darkGray}; // couleurs des joueurs instanciées par défaut
    boolean parametres = false; // permet l'affichage ou non de la fenêtre des paramètres avancés

    while (!maFenetreDebut.getFini()) // tant que l'utilisateur n'a pas quitté la fenêtre
    {
      noms = maFenetreDebut.getNoms(); // on récupère les noms des joueurs
      parametres = maFenetreDebut.getParam(); // true si l'utilisateur a cliqué sur le bouton "Paramètres avancés"
      if (parametres)// si l'utilisateur a cliqué sur le bouton "Paramètres avancés"
      {
        FenetreParam fenParam = new FenetreParam(maFenetreDebut.getScreenSize()[0], maFenetreDebut.getScreenSize()[1]); // on instancie la fenêtre correspondante
        while(!fenParam.getFini()) // tant qu'il n'a pas cliqué sur le bouton pour la quitter
        {
          taille = fenParam.getTaille();
          try
          {
            Thread.sleep(1000); // permet d'éviter que l'ordinateur "tourne dans le vide" pour rien
          }
          catch(Exception e)
          {
          }
        }
        taille = fenParam.getTaille(); // on récupère la taille de la fenêtre en carrés
        couleurs = fenParam.getCouleurs(); // et les couleurs des deux joueurs
        maFenetreDebut.setParam(false); // permet de pouvoir réafficher la fenêtre si l'utilisateur reclique sur le bouton "Paramètres avancés"
      }
    }
    levels = maFenetreDebut.getLevel(); // on récupère les niveaux des joueurs

    Fenetre2 maFenetre = new Fenetre2(taille[0]+1, taille[1]+1, noms[0], noms[1], levels[0], levels[1], couleurs);
    // on instancie la fenêtre de jeu avec tous les paramètres nécessaires, la taille est transformée en nombre de points
  }
}
