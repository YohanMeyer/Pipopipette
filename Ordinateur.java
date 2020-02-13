import java.awt.Color;

public class Ordinateur extends Joueur // Ordinateur facile
{
  private String nom;
  private int score = 0;
  private int level;
  private final int LARGEUR;
  private final int LONGUEUR;
  private Color couleur;

  public Ordinateur(String nom, int level, int largeur, int longueur, Color couleur) // initialisation des paramètres donnés
  {
    this.nom = nom;
    this.level = level;
    LARGEUR = largeur;
    LONGUEUR = longueur; // correspondent au nombre de carrés sur le plateau
    this.couleur = couleur;

  }
  public int getScore() // renvoie le score
  {
    return score;
  }
  public void scorePlus(int scoreEnPlus) // permet d'augmenter le score de l'Ordinateur
  {
    this.score += scoreEnPlus;
  }
  public String getNom() // renvoie le nom
  {
    return nom;
  }
  public int getLevel() // renvoie le level (1)
  {
    return level;
  }
  public Color getCouleur() // renvoie la couleur
  {
    return couleur;
  }

  public int[] jouer(boolean[][] lignesHor, boolean[][] lignesVert) // méthode de jeu de l'Ordinateur Facile
  {
    boolean fini = false;
    int x = 0;
    int y = 0; // coordonnées de la ligne à tracer
    int orientation = 0; // 0 pour une ligne horizontale, 1 pour verticale
    while(!fini) // la boucle ne s'arrête pas tant qu'on teste des lignes déjà tracées
    {
      orientation = (int)(Math.random()*2); // choix aléatoire de l'orientation
      if (orientation == 0) // si la ligne serait horizontale
      {
        x = (int)(Math.random()*LONGUEUR);
        y = (int)(Math.random()*(LARGEUR+1)); //choix aléatoire des coordonnées
        fini = !lignesHor[x][y]; // on vérifie que la ligne n'est pas déjà tracée
      }
      else // pareil si la ligne serait verticale
      {
        x = (int)(Math.random()*(LONGUEUR+1));
        y = (int)(Math.random()*LARGEUR);
        fini = !lignesVert[x][y];
      }
    }
    int[] ret = {x,y,orientation}; // on return les coordonnées de la ligne ainsi que son orientation
    return(ret);
  }

  public int[] jouer(int[] a, boolean b){return(new int[3]);} // inutile ici
}
