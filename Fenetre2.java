import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Fenetre2 extends JFrame // fenêtre principale de jeu, arrive à la fermeture de Fenetre1
{
  private JPanel top = new JPanel(); // JPanel des scores
  private JPanel mid = new JPanel();
  private JPanel bot = new JPanel(); // mid et bot servent pour la fin du jeu
  private JPanel container = new JPanel(); // container pour le jeu
  private JPanel containerFin = new JPanel(); // container pour la fin du jeu
  private Panneau pan = new Panneau(); // JPanel servant à tracer les figures pour le jeu
  private JLabel labelJoueur1;
  private JLabel labelJoueur2; // JLabels servant à afficher les scores
  private Color[] couleurs = new Color[2]; // couleurs des joueurs

  private Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
  private double jFrameWidth = screenDimension.width/1440;
  private double jFrameHeight = screenDimension.height/800; // permettent d'adapter la taille de tous les composants à l'écran

  private int x;
  private int y; // position de la souris
  private final int COTE = (int)(jFrameHeight*25); //moitié de la distance séparant deux points
  private final int ZERO; // abscisse et ordonnée du point en haut à gauche
  private final int XMAX;
  private final int YMAX; // coordonnées du point en bas à droite
  private final int SCOREMAX; // score cumulé maximum des deux joueurs
  private int joueurCourant = 0; // variable prenant la valeur 0 ou 1 selon le joueur qui joue

  private int[] coord = new int[2];
  private int[] dernierTour = new int[3]; // sert pour l'OrdinateurDifficile
  private boolean[][] lignesHor;// deux tableaux de boolean dont chaque case vaut true ou false selon que la ligne horizontale (lignesHor) ou verticale (lignesVert) est tracée ou non
  private boolean[][] lignesVert; // les positions des lignes sur le plateau correspondent à leur position dans le tableau
  private boolean horizontal = false; // true si la dernière ligne tracée était horizontale
  private boolean ordiCommence = false; // pour démarrer le jeu si un Ordinateur joue en premier
  private boolean fini = false; // arrête le jeu lorsque le plateau est rempli

  private Joueur[] lesJoueurs = new Joueur[2]; // tableau des joueurs

  public Fenetre2(int longueur, int largeur, String nom1, String nom2, int level1, int level2, Color[] couleurs)
  { // constructeur de la fenêtre
    int cote = COTE; // inutile, mais évite un bug d'affichage sur Atom
    if(cote <= (int)(jFrameHeight*25))
    {
      ZERO = 4*COTE; // pour que la première ligne de points ne soit pas cachée par la bordure du titre de la fenetre
    }
    else
    {
      ZERO = 2*COTE;
    }

    // il est impératif que ZERO soit un multiple de (2*COTE), sinon le programme ne marche pas en raison des '%(2*COTE)'
    lignesHor = new boolean[longueur*2][largeur*2];
    lignesVert = new boolean[longueur*2][largeur*2]; // initialisation large pour éviter des ArrayIndexOutOfBoundsException

    //Configure la taille du plateau
    XMAX = (longueur-1)*2*COTE; // attention : longueur et largeur correspondent au nombre de points
    YMAX = (largeur-1)*2*COTE;
    SCOREMAX = (largeur-1)*(longueur-1); //Score maximal à atteindre, donc si toutes les cases sont remplies
    //Configuration de la taille et le titre de la fenetre
    this.setTitle("Pipopipette");
    this.setSize(XMAX+2*ZERO,YMAX+2*ZERO);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);

    this.setFont(new Font("Arial", Font.PLAIN, (int)jFrameHeight*14));

    //initialise les couleurs de chaque joueur
    this.couleurs = couleurs;

    //Configure les joueurs / IA en fonction des données récupérées de la fenêtre 1
    if (!nom1.equals("Ordi1"))
    {
      lesJoueurs[0] = new Humain(nom1, couleurs[0]);
    }
    else if (level1 == 1)
    {
      lesJoueurs[0] = new Ordinateur(nom1, level1, largeur-1, longueur-1, couleurs[0]);
      ordiCommence = true;
    }
    else
    {
      lesJoueurs[0] = new OrdinateurDifficile(nom1, largeur -1, longueur -1, lignesHor, lignesVert);
      ordiCommence = true;
    }
    if (!nom2.equals("Ordi2"))
    {
    lesJoueurs[1] = new Humain(nom2, couleurs[1]);
    }
    else if(level2 == 1)
    {
      lesJoueurs[1] = new Ordinateur(nom2, level2, largeur-1, longueur-1, couleurs[1]);
    }
    else
    {
      lesJoueurs[1] = new OrdinateurDifficile(nom2, largeur -1, longueur -1, lignesHor, lignesVert);
    }

    //Configure l'affichage des scores en haut de la fenetre
    labelJoueur1 = new JLabel(updateScore(lesJoueurs[0], 0));
    labelJoueur2 = new JLabel(updateScore(lesJoueurs[1], 0));
    labelJoueur1.setForeground(couleurs[0]);
    labelJoueur2.setForeground(couleurs[1]);
    labelJoueur1.setFont(labelJoueur1.getFont().deriveFont((float)(jFrameHeight*14)));
    labelJoueur2.setFont(labelJoueur2.getFont().deriveFont((float)(jFrameHeight*14)));

    top.add(labelJoueur1);
    top.add(labelJoueur2);

    container.setLayout(new BorderLayout());

    pan.setBackground(Color.white);
    container.add(pan);
    container.add(top, BorderLayout.NORTH);

    this.setContentPane(container);
    this.setVisible(true);
    pan.paintComponent(this.getGraphics(), COTE, XMAX, YMAX, ZERO, jFrameHeight, jFrameWidth, couleurs); // affiche les points sur le plateau
    this.setResizable(false);

    //Configure le Listener souris
    this.addMouseListener(new SourisListener()); // gère les clics
    this.addMouseMotionListener(new SourisMotionListener()); // gère le déplacement

    if (ordiCommence) // si le joueur 1 est un ordi, son premier tour est automatiquement lancé
    {
      if (lesJoueurs[0].getLevel() == 1)
      {
        jouerTour();
      }
      else
      {
        jouerTourDifficile();
      }
    }
  }

  public void changerDeJoueur()
  {
    joueurCourant = 1 - joueurCourant; // revient à faire (joueurCourant+1)%2
    if (lesJoueurs[joueurCourant].getLevel() == 1)
    {
      jouerTour(); // fait jouer l'Ordinateur
    }
    else if (lesJoueurs[joueurCourant].getLevel() == 2 && !fini)
    {
      jouerTourDifficile(); // fait jouer l'OrdinateurDifficile
    }
  }

  public void jouerTour() // méthode de jeu de l'Ordinateur
  {
    try
    {
      Thread.sleep(500); // on laisse le temps à l'Humain de voir le coup
    }
    catch(Exception e)
    {
    }

    int[] tourOrdi = lesJoueurs[joueurCourant].jouer(lignesHor, lignesVert); // renvoie la position et l'orientation de la ligne à tracer
    if (tourOrdi[2] == 0) // si la ligne est horizontale
    {
      this.lignesHor[tourOrdi[0]][tourOrdi[1]] = true; // on "sauvegarde" la ligne tracée par l'ordi
      horizontal = true;
    }
    else
    {
      this.lignesVert[tourOrdi[0]][tourOrdi[1]] = true; // pareil si elle est verticale
      horizontal = false;
    }

    pan.paintComponent(this.getGraphics(), tourOrdi[0]*2*COTE+ZERO, tourOrdi[1]*2*COTE+ZERO, (tourOrdi[2] == 0), lignesHor, lignesVert, joueurCourant); // permet de tracer proprement la ligne

    if (carresRemplis(tourOrdi[0], tourOrdi[1]) != 0)// si l'ordi remplit un carré ou plus
    {
      joueurCourant = 1 - joueurCourant; // on le fait rejouer en faisant changer de joueur deux fois, mais sans passer par la fonction changerDeJoueur car la fonction serait annulée si deux ordi jouent
    }
    for (int loop = 0 ; loop <=2 ; loop++ )
    {
      dernierTour[loop] = tourOrdi[loop]; // utile si un OrdinateurDifficile joue
    }
    jeuFini(); // teste si le jeu est fini
    changerDeJoueur();
  }

  public void jouerTourDifficile() // méthode de jeu de l'OrdinateurDifficile
  {
    int[] aTracer = new int[3];
    try
    {
      Thread.sleep(500); // on laisse le temps à l'Humain de voir le coup
    }
    catch(Exception e)
    {
    }

    aTracer = lesJoueurs[joueurCourant].jouer(dernierTour, ordiCommence); // renvoie la position et l'orientation de la ligne à tracer

    if (ordiCommence)
    {
      ordiCommence = false; // évite un bug si deux OrdinateurDifficile jouent ensemble
    }

    if (aTracer[2] == 0) // si la ligne est horizontale
    {
      this.lignesHor[aTracer[0]][aTracer[1]] = true; // on "sauvegarde" la ligne tracée par l'ordi
      horizontal = true;
    }
    else
    {
      this.lignesVert[aTracer[0]][aTracer[1]] = true; // pareil si elle est verticale
      horizontal = false;
    }

    pan.paintComponent(this.getGraphics(), aTracer[0]*2*COTE+ZERO, aTracer[1]*2*COTE+ZERO, (aTracer[2] == 0), lignesHor, lignesVert, joueurCourant); //permet de tracer la ligne proprement

    if (carresRemplis(aTracer[0], aTracer[1]) != 0)// si l'ordi remplit un carré ou plus
    {
      joueurCourant=1-joueurCourant; // on le fait rejouer en faisant changer de joueur deux fois, mais sans passer par la fonction changerDeJoueur car la fonction serait annulée si deux ordi jouent
    }

    for (int loop = 0 ; loop <=2 ; loop++ )
    {
      dernierTour[loop] = aTracer[loop]; // utile si deux OrdinateurDifficile jouent
    }
    jeuFini(); // teste si le jeu est fini
    changerDeJoueur();
  }

  public boolean estOrdinateur() // renvoie true si le joueurCourant est un Ordinateur, false si Humain
  {
    if (lesJoueurs[joueurCourant].getLevel() != 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public String updateScore(Joueur joueur, int score) // actualise le score
  {
    joueur.scorePlus(score); // augmente le score si besoin

    String ret = new String();
    ret += joueur.getNom()+" : "+joueur.getScore()+"        ";
    return ret; // String à mettre en texte du JLabel du score
  }

  public int carresRemplis(int xPoint, int yPoint) // renvoie le nombre de carrés remplis par le dernier coup joué
  {
    int carresRemplis = 0;
    if (horizontal) // si la ligne est horizontale
    {
      try
      {
        if(lignesVert[xPoint][yPoint] && lignesVert[xPoint+1][yPoint] && lignesHor[xPoint][yPoint+1])
        { // on teste si les lignes entourant le carré en dessous de la dernière ligne est tracé
          carresRemplis++;
          pan.paintCarre(this.getGraphics(), xPoint, yPoint, joueurCourant); // remplit le carré rempli de la couleur du joueur
        }
      }
      catch(ArrayIndexOutOfBoundsException e)
      {
      }
      try
      {
        if (lignesVert[xPoint][yPoint-1] && lignesVert[xPoint+1][yPoint-1] && lignesHor[xPoint][yPoint-1])
        { // on teste si les lignes entourant le carré au dessus de la dernière ligne est tracé
          carresRemplis++;
          pan.paintCarre(this.getGraphics(), xPoint, yPoint - 1, joueurCourant);
        }
      }
      catch(ArrayIndexOutOfBoundsException e)
      {
      }
    }
    else // si la ligne est verticale
    {
      try
      {
        if (lignesHor[xPoint][yPoint] && lignesHor[xPoint][yPoint+1] && lignesVert[xPoint+1][yPoint])
        { // on teste si les lignes entourant le carré à droite de la ligne tracée est tracé
          carresRemplis++;
          pan.paintCarre(this.getGraphics(), xPoint, yPoint, joueurCourant); // remplit le carré rempli de la couleur du joueur
        }
      }
      catch(ArrayIndexOutOfBoundsException e)
      {
      }
      try
      {
        if (lignesHor[xPoint-1][yPoint] && lignesHor[xPoint-1][yPoint+1] && lignesVert[xPoint-1][yPoint])
        { // on teste si les lignes entourant le carré à gauche de la dernière ligne est tracé
          carresRemplis++;
          pan.paintCarre(this.getGraphics(), xPoint - 1, yPoint, joueurCourant);
        }
      }
      catch(ArrayIndexOutOfBoundsException e)
      {
      }
    }
    if (joueurCourant == 0) // met à jour le score
    {
      labelJoueur1.setText(updateScore(lesJoueurs[0], carresRemplis));
      labelJoueur1.repaint();
    }
    else
    {
      labelJoueur2.setText(updateScore(lesJoueurs[1], carresRemplis));
      labelJoueur2.repaint();
    }
    return carresRemplis;
  }

  public void jeuFini() // teste si le jeu est fini
  {
    if (lesJoueurs[0].getScore() + lesJoueurs[1].getScore() == SCOREMAX) // teste si c'est fini
    {
      try
      {
        labelJoueur1.setText(updateScore(lesJoueurs[0], 0));
        labelJoueur1.repaint();
        labelJoueur2.setText(updateScore(lesJoueurs[1], 0));
        labelJoueur2.repaint(); // actualise les scores
        Thread.sleep(2000); // laisse le temps aux joueurs d'admirer le plateau rempli

        pan.paintComponent(this.getGraphics()); // "réinitialise" la JFrame
        mid.setBackground(Color.white);
        bot.setBackground(Color.white);

        JButton boutonFini = new JButton("Ok"); // bouton permettant de quitter le jeu
        JLabel labelFin1 = new JLabel("<html> <br/><br/> La partie est finie ! Merci d'avoir joué ! <br/><br/><br/><br/> Score final :<br/><br/>");
        JLabel labelFin2 = new JLabel(lesJoueurs[0].getNom()+" : "+lesJoueurs[0].getScore()+"   "+lesJoueurs[1].getNom()+" : "+lesJoueurs[1].getScore()); // affiche les scores finaux
        JLabel labelFin3; // affiche le nom du vainqueur
        if (lesJoueurs[0].getScore() > lesJoueurs[1].getScore())
        {
          labelFin3 = new JLabel("<html><br/><br/>Le grand gagnant est donc "+lesJoueurs[0].getNom());
          labelFin3.setForeground(couleurs[0]);
        }
        else if (lesJoueurs[1].getScore() > lesJoueurs[0].getScore())
        {
          labelFin3 = new JLabel("<html><br/><br/>Le grand gagnant est donc "+lesJoueurs[1].getNom());
          labelFin3.setForeground(couleurs[1]);
        }
        else
        {
          labelFin3 = new JLabel("<html><br/><br/>Les deux joueurs sont à égalité...");
        }
        labelFin1.setFont(labelFin1.getFont().deriveFont((float)(jFrameHeight*14)));
        labelFin2.setFont(labelFin2.getFont().deriveFont((float)(jFrameHeight*14)));
        labelFin3.setFont(labelFin3.getFont().deriveFont((float)(jFrameHeight*17)));
        boutonFini.setFont(boutonFini.getFont().deriveFont((float)(jFrameHeight*14))); // adapte la taille des caractères à l'écran

        labelFin1.setVerticalAlignment(JLabel.CENTER);
        labelFin2.setVerticalAlignment(JLabel.CENTER);
        labelFin3.setVerticalAlignment(JLabel.CENTER); // place les JLabel au milieu
        boutonFini.addActionListener(new BoutonFiniListener()); // quitte si on clique dessus

        containerFin.setLayout(new BorderLayout());
        bot.add(boutonFini);
        mid.add(labelFin1);
        mid.add(labelFin2);
        mid.add(labelFin3);
        containerFin.add(bot, BorderLayout.SOUTH);
        containerFin.add(mid, BorderLayout.CENTER);
        this.setContentPane(containerFin);
        this.setVisible(true);
        fini = true;
      }
      catch(Exception e)
      {
        fini = true;
      }
    }
  }

  public boolean estHorizontal(int x, int y) // renvoie true si la ligne la plus proche de la souris est horizontale
  { //x et y représentent les coordonnées de la souris
    x += 2*COTE - ZERO;
    y += 2*COTE - ZERO;
    x %= 2*COTE;
    y %= 2*COTE; // permet de ramener les coordonnées dans un carré simple

    if ((y < x && y < -x+2*COTE) || (y > x && y > -x+2*COTE)) // voir le rapport
    {
      horizontal = true;
      return true;
    }
    else
    {
      return false;
    }
  }

  public boolean estVertical(int x, int y) // pareil qu'au-dessus, mais pour une ligne verticale
  {
    x += 2*COTE - ZERO;
    y += 2*COTE - ZERO;
    x %= 2*COTE;
    y %= 2*COTE;

    if ((y > x && y < -x+2*COTE) || (y < x && y > -x+2*COTE))
    {
      horizontal = false;
      return true;
    }
    else
    {
      return false;
    }
  }

  public boolean estSurLePlateau(int x, int y) // renvoie true si la souris est sur le plateau
  {
    x -= ZERO;
    y -= ZERO;
    // on teste toutes les possibilités où la souris pourrait se trouver dans le plateau
    if (x > 0 && x < XMAX && y > 0 && y < YMAX) // dans le "grand rectangle" (cf rapport)
    {
      return true;
    }
    else if (y < 0 && y > -COTE && x > 0 && x < XMAX)
    {
      x %= 2*COTE;

      if (y > -x && y > x-2*COTE) // dans les "triangles" au-dessus du grand rectangle
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if (x < 0 && x > -COTE && y > 0 && y < YMAX)
    {
      y %= 2*COTE;

      if (y > -x && y < x+2*COTE) // dans les triangles à gauche du grand rectangle
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if (x > XMAX && x < XMAX+COTE && y > 0 && y < YMAX) // dans les triangles à droite
    {
      y %= 2*COTE;
      x -= XMAX;

      if (y > x && y < -x+2*COTE)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if (y > YMAX && y < YMAX+2*COTE && x > 0 && x < XMAX)
    {
      x %= 2*COTE;
      y -= YMAX;

      if (y < x && y < -x+2*COTE) // dans les triangles au-dessous
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else
    {
      return false;
    }
  }

  public int[] calcCoor(int x, int y) // transforme les coordonnées "brutes" de la souris en les coordonnées du point correspondant à la ligne la plus proche
  {
    int xx = x;
    int yy = y;

    xx -= xx%(2*COTE);
    yy -= yy%(2*COTE);

    if (estHorizontal(x, y)) // si la ligne est horizontale
    {
      horizontal = true;
      if(y%(2*COTE) != (y%COTE))
      {
        yy += 2*COTE; // permet de ne pas avoir de coordonnées négatives dans les triangles autour du grand rectangle
      }
      int xPoint = (xx - ZERO)/(2*COTE);
      int yPoint = (yy - ZERO)/(2*COTE); // calcul des coordonnées de la ligne
      if (!lignesHor[xPoint][yPoint]) // si le joueur n'a pas cliqué
      {
        pan.paintComponent(this.getGraphics(), xx, yy, true, lignesHor, lignesVert, joueurCourant);
      } // on trace la ligne et on efface la dernière
    }
    else if (estVertical(x, y)) // pareil si la ligne est verticale
    {
      horizontal = false;
      if(x%(2*COTE) != x%COTE)
      {
        xx += 2*COTE;
      }
      int xPoint = (xx - ZERO)/(2*COTE);
      int yPoint = (yy - ZERO)/(2*COTE);
      if (!lignesVert[xPoint][yPoint])
      {
        pan.paintComponent(this.getGraphics(), xx, yy, false, lignesHor, lignesVert, joueurCourant);
      }
    }
    int [] ret = new int[2];
    ret[0] = xx;
    ret[1] = yy;
    return(ret); // on renvoie les coordonnées de la ligne la plus proche de la souris
  }

  public boolean changeLosange(int x, int y, int[] coord) // renvoie true si la souris a quitté une "zone" de ligne (cf rapport), false sinon
  {
    if (horizontal && x > coord[0] && x < (coord[0]+2*COTE)) // si la ligne est horizontale
    {
      x %= 2*COTE;
      if (y > coord[1]-x && y < coord[1]+x && y > coord[1]+x-2*COTE && y < coord[1]-x+2*COTE)
      { // on regarde si le curseur est compris dans la zone autour de la ligne horizontale
        return false; // le curseur est toujours dans la zone
      }
      else
      {
        return true; // le curseur n'est plus dans la zone
      }
    }
    else if (!horizontal && x > coord[0]-COTE && x < coord[0]+COTE) // pareil si la ligne est verticale
    {
      x %= 2*COTE;
      if (x > COTE)
      {
        x -= 2*COTE;
      }
      if (y > coord[1]-x && y < coord[1]+x+2*COTE && y > coord[1]+x && y < coord[1]-x+2*COTE)
      {
        return false;
      }
      else
      {
        return true;
      }
    }
    else
    {
      return true;
    }
  }

  class SourisListener implements MouseListener // gère le clic de la souris
  {
    public void mouseClicked(MouseEvent event)
    { // si l'utilisateur clique sur une ligne
      if (!fini)
      {
        int carres = 0;
        int xPoint = (coord[0] - ZERO)/(2*COTE);
        int yPoint = (coord[1] - ZERO)/(2*COTE); // calcul des coordonnées de la ligne
        if (horizontal && !lignesHor[xPoint][yPoint]) // si la ligne est horizontale et non définitivement tracée
        {
          lignesHor[xPoint][yPoint] = true; // on "sauvegarde" la ligne
          carres = carresRemplis(xPoint, yPoint); // on vérifie si on a rempli un/des carré(s)
          dernierTour[0] = xPoint;
          dernierTour[1] = yPoint;
          dernierTour[2] = 0; // sert si on joue contre un OrdinateurDifficile
        }
        else if(!lignesVert[xPoint][yPoint]) // pareil si la ligne est verticale
        {
          lignesVert[xPoint][yPoint] = true;
          carres = carresRemplis(xPoint, yPoint);
          dernierTour[0] = xPoint;
          dernierTour[1] = yPoint;
          dernierTour[2] = 1;
        }
        if (carres == 0) // si on ne remplit pas de carré
        {
          changerDeJoueur(); // on change de joueur
        }
        jeuFini(); // sinon on ne change pas de joueur et on regarde si le plateau est rempli ou non
      }
    }
    public void mousePressed(MouseEvent event)
    {
    }
    public void mouseReleased(MouseEvent event)
    {
    }
    public void mouseEntered(MouseEvent event)
    {
    }
    public void mouseExited(MouseEvent event)
    {
    }
  }
  class SourisMotionListener implements MouseMotionListener // gère les déplacements de la souris
  {
    public void mouseMoved(MouseEvent event)
    {
      if (!fini)
      {
        x = event.getX();
        y = event.getY(); // récupère les coordonnées du curseur
        if (estSurLePlateau(x, y)) // si le curseur est sur le plateau
        {
          if (changeLosange(x, y, coord)) // et s'il a changé de zone de ligne
          {
            coord = calcCoor(x, y); // on réaffiche la ligne la plus proche et on efface celle d'avant
          }
        }
        jeuFini();
      }
    }
    public void mouseDragged(MouseEvent event)
    {
    }
  }

  class BoutonFiniListener implements ActionListener // bouton de la fenêtre de fin
  {
    public void actionPerformed(ActionEvent event)
    { // si on clique dessus, quitte le jeu
      dispose();
      System.exit(0);
    }
  }
}
