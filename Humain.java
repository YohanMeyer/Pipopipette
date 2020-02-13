import java.awt.Color;

public class Humain extends Joueur
{
  private String nom;
  private int score = 0;
  private int level = 0;
  private Color couleur;

  public Humain(String nom, Color couleur) // initialisation des paramètres donnés
  {
    this.nom = nom;
    this.couleur = couleur;
  }

  public int getScore() // renvoie le score de l'Humain
  {
    return score;
  }

  public void scorePlus(int scoreEnPlus) // sert à augmenter le score
  {
    this.score += scoreEnPlus;
  }

  public String getNom() // renvoie le nom
  {
    return nom;
  }

  public int getLevel() // renvoie le niveau (0)
  {
    return level;
  }
  public Color getCouleur() // renvoie la couleur
  {
    return couleur;
  }

  public int[] jouer(boolean[][] lignesHor, boolean[][] lignesVert) // inutile pour cette classe
  {return(new int[3]);}

  public int[] jouer(int[] a, boolean b) // inutile pour cette classe
  {return(new int[3]);}
}
