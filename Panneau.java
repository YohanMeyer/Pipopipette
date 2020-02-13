import javax.swing.*;
import java.awt.*;
public class Panneau extends JPanel // JPanel permettant de tracer les points, lignes et carrés du plateau
{
  private int dernierX;
  private int dernierY; // coordonnées du dernier coup qui a été joué
  private boolean dernierHorizontal = false; // true si la dernière ligne tracée était horizontale
  private int XMAX;
  private int YMAX;
  private int ZERO;
  private int COTE;
  private double jFrameWidth;
  private double jFrameHeight; //constantes de Fenetre2 qu'on récupère ici
  private Color[] couleurs; // couleurs des joueurs

  // méthode permettant de mettre à jour l'affichage
  public void paintComponent(Graphics g, int x, int y, boolean estHorizontal, boolean[][]lignesHor, boolean[][]lignesVert, int joueurCourant)
  {
    int xPoint = (dernierX - ZERO)/(2*COTE);
    int yPoint = (dernierY - ZERO)/(2*COTE); // xPoint et yPoint sont les coordonnées en points du dernier coup
    g.setColor(Color.white);

    if (dernierHorizontal && !lignesHor[xPoint][yPoint]) // si le joueur est passé sur une ligne horizontale sans cliquer
    {
      g.drawLine(dernierX, dernierY, dernierX + 2*COTE, dernierY); // on efface la ligne
      g.setColor(Color.black);
      g.drawOval(dernierX - (int)(jFrameWidth*1), dernierY - (int)(jFrameHeight*1), (int)(jFrameWidth*2), (int)(jFrameHeight*2));
      g.drawOval(dernierX- (int)(jFrameWidth*1)+2*COTE,dernierY- (int)(jFrameHeight*1), (int)(jFrameWidth*2), (int)(jFrameHeight*2)); // et on retrace les deux points à ses extrémités
      dernierHorizontal = true;
    }
    else if (!dernierHorizontal && !lignesVert[xPoint][yPoint]) // pareil pour une ligne verticale
    {
      g.drawLine(dernierX, dernierY, dernierX, dernierY + 2*COTE);
      g.setColor(Color.black);
      g.drawOval(dernierX-(int)(jFrameWidth*1), dernierY-(int)(jFrameHeight*1), (int)(jFrameWidth*2), (int)(jFrameHeight*2));
      g.drawOval(dernierX-(int)(jFrameWidth*1), dernierY-(int)(jFrameHeight*1)+2*COTE, (int)(jFrameWidth*2), (int)(jFrameHeight*2));
      dernierHorizontal = false;
    }
    g.setColor(couleurs[joueurCourant]);
    // on trace la nouvelle ligne
    if (estHorizontal)
    {
      g.drawLine(x, y, x+2*COTE, y);
      dernierHorizontal = true;
    }
    else
    {
      g.drawLine(x, y, x, y+2*COTE);
      dernierHorizontal = false;
    }
    dernierX = x;
    dernierY = y;
  }
  //initialisation des constantes de Panneau
  public void paintComponent(Graphics g, int COTE, int XMAX, int YMAX, int ZERO, double jFrameHeight, double jFrameWidth, Color[] couleurs)
  {
    this.XMAX = XMAX;
    this.YMAX = YMAX;
    this.COTE = COTE;
    this.ZERO = ZERO;
    this.jFrameWidth = jFrameWidth;
    this.jFrameHeight = jFrameHeight;
    dernierX = ZERO;
    dernierY = ZERO; // initialisation des constantes
    this.couleurs = couleurs; // initialisation des couleurs des joueurs

    g.setColor(Color.black);
    for(int loop = ZERO ; loop <= XMAX+ZERO ; loop += 2*COTE) // tracé des points du plateau
    {
      for(int loop2 = ZERO ; loop2 <= YMAX+ZERO ; loop2 += 2*COTE)
      {
        g.drawOval(loop-(int)(jFrameWidth*1), loop2-(int)(jFrameHeight*1), (int)(jFrameWidth*2), (int)(jFrameHeight*2));
      }
    }
  }

  public void paintCarre(Graphics g, int xPoint, int yPoint, int joueurCourant) // méthode pour remplir un carré
  {
    int x = ZERO + 2*COTE*xPoint;
    int y = ZERO + 2*COTE*yPoint;
    g.setColor(couleurs[joueurCourant]);
    g.fillRect(x, y, 2*COTE, 2*COTE);
  }

  public void paintComponent(Graphics g) // méthode pour effacer le contenu de la JFrame à la fin
  {
    super.paintComponent(g);
  }
}
