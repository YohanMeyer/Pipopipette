import java.awt.Color;

abstract class Joueur // sert pour Humain.java, Ordinateur.java et OrdinateurDifficile.java, car permet de créer un tableau de Joueur dans la Fenetre2
{
  private String nom; // nom du joueur
  private int score; // score du joueur
  private int niveau; // 0 si humain, 1 ou 2 pour Ordinateur
  private boolean premier; // true si joue en premier
  private Color couleur; // couleur du joueur pour l'affichage

  abstract String getNom();
  abstract int getScore();
  abstract void scorePlus(int scoreEnPlus);
  abstract int getLevel();
  abstract Color getCouleur();
  abstract int[] jouer(boolean[][] a, boolean[][] b);
  abstract int[] jouer(int[] a, boolean b);
}
