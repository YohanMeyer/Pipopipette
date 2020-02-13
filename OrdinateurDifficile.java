import java.awt.Color;

public class OrdinateurDifficile extends Joueur
{
  private String nom;
  private int score = 0;
  private int level;
  private boolean premier;
  private Color couleur;

  private final int LARGEUR;
  private final int LONGUEUR; //Taille de la plateforme de jeu
  private int[][][] Vision; //Tableau qui permet a l'IA de "Voir" le tableau
  private boolean[][] lignesHor;
  private boolean[][] lignesVert;
  boolean Haut;
  boolean Droite;
  boolean Bas;
  boolean Gauche;
  int[] tabVide={5,5,5};

  public OrdinateurDifficile(String nom, int largeur, int longueur, boolean[][] hor, boolean[][] ver)
  {
    this.nom = nom;
    level = 2;
    LONGUEUR=longueur;
    LARGEUR = largeur;
    this.lignesHor = hor;
    this.lignesVert = ver;
    System.out.println(LONGUEUR + " "+ LARGEUR);

    this.Vision = new int[LONGUEUR][LARGEUR][5];
//    balayer();
  }
  //fin du constructeur OrdinateurDifficile

  public int[] jouer(int[] dernierJeu, boolean ordiCommence)
  {
    System.out.println("dJ : "+dernierJeu[0]+" "+dernierJeu[1]);

    //System.out.println("Ordinateur va jouer");
    int[] res = new int[3]; //tableau a rendre
    if (!ordiCommence)
    {
      actualiser(dernierJeu);
    }

    res = chercher(dernierJeu[0], dernierJeu[1], 3);
    if (res == tabVide)
    {
      res = chercher(dernierJeu[0], dernierJeu[1], 1);
      if (res == tabVide)
      {
        res = chercher(dernierJeu[0], dernierJeu[1], 0);
        if (res == tabVide)
        {
          res = chercher(dernierJeu[0], dernierJeu[1], 2);
        }
      }
    }
    return res;
  }
  //fin de la methode jouer

  public void balayer()
  {
    int tot=0;
    for (int i=0;i<LONGUEUR;i++ )
    {
      for (int j=0; j<LARGEUR; j++ )
      {
        tot=0;
        Haut = lignesHor[i][j];
        Droite = lignesVert[i+1][j];
        Bas = lignesHor[i][j+1];
        Gauche = lignesVert[i][j];

        if (Haut)
        {
          tot++;
          Vision[i][j][1]=1;
        }

        if (Droite)
        {
          tot++;
          Vision[i][j][2]=1;
        }

        if (Bas)
        {
          tot++;
          Vision[i][j][3]=1;
        }


        if (Gauche)
        {
          tot++;
          Vision[i][j][4]=1;
        }

        Vision[i][j][0]=Vision[i][j][1]+Vision[i][j][2]+Vision[i][j][3]+Vision[i][j][4];
      }
    }
  }
  //fin de la methode balayer

  public void actualiser(int[] dernierJeu)
  {
    if (dernierJeu[2] == 0)
    {
      lignesHor[dernierJeu[0]][dernierJeu[1]] = true;
    }
    else
    {
      lignesVert[dernierJeu[0]][dernierJeu[1]] = true;
    }
    balayer();
  }
/*  public String actualiser(int[] dT)
  {
    if (dT[2]==0)
    {
      Vision[dT[0]][dT[1]][1]=1;
    }
    else
    {
      Vision[dT[0]][dT[1]][4]=1;
    }
    Vision[dT[0]][dT[1]][0]=Vision[dT[0]][dT[1]][1]+Vision[dT[0]][dT[1]][2]+Vision[dT[0]][dT[1]][3]+Vision[dT[0]][dT[1]][4];
    return "actualisation finie";
  }
  //fin de la methode actualiser
*/
  public int[] chercher(int x, int y, int valCherchee)
  {
    int[] choix = new int[2];//tableau contenant les coord de la case à tester pour des choix de traits
    int[] res = new int[3];//Résultat
    int k = 0; //Variable de changement de direction
    int val = 5; //Variable du nombre de traits dans la case en question, initialisée à 5 car c'est différent de toutes les valeurs réellement possibles pour une case
    int count = 1; //Compteur de fin de test
    boolean ChercheEncore = true; //Deviendra false une fois qu'un résultat est trouvé
    int j = 1; //Compteur de pas dans chaque direction

    //on teste la dernière case jouée
    System.out.println("je cherche la valeur "+valCherchee+" !");
    val=getVal(x,y);
    if (val == valCherchee)
    {
      System.out.println("Valeur trouvee en "+x+" "+y+" !");
      choix[0] = x;
      choix[1] = y;
      res = choisir(choix);
      if (res != tabVide) {
        return res;
      }
    }

    while (ChercheEncore)
    {
      for (int p = 0; p < 2 ; p++) //un for a 2 repetitions necessaire pour la lecture en tourbillon
      {
        for (int i = 0 ; i < j ; i++)
        { //Responsable des lectures dans une direction precise
          k%=4;
          switch (k) //selon la valeur de k, on lit la valeur de la case dans une certaine direction
          {
            case 0: //lire vers le Haut
              val=getValHaut(x,y);
              System.out.println("val : "+val+" coord : "+x+" "+y);
              if (val == 5)
              {
                count--; //On ne compte pas ce tour car c'est une case hors tableau
              }
              y--;
              count++;
              if (count==LONGUEUR*LARGEUR)
              {
                System.out.println("full pour "+valCherchee);
                return tabVide;
              }
              //System.out.println("Cases testees "+count+" direction : "+k+" !");
              break;

            case 1: //lire vers la Droite
              val=getValDroite(x,y);
              System.out.println("val : "+val+" coord : "+x+" "+y);
              if (val == 5)
              {
                count--; //On ne compte pas ce tour car c'est une case hors tableau
              }
              x++;
              count++;
              if (count==LONGUEUR*LARGEUR)
              {
                System.out.println("full pour "+valCherchee);
                return tabVide;
              }
            //System.out.println("Cases testees "+count+" direction : "+k+" !");
              break;

            case 2: //lire vers le Bas
              val=getValBas(x,y);
              System.out.println("val : "+val+" coord : "+x+" "+y);
              if (val == 5)
              {
                count-=1; //On ne compte pas ce tour car c'est une case hors tableau
              }
              y++;
              count++;
              if (count==LONGUEUR*LARGEUR)
              {
                System.out.println("full pour "+valCherchee);
                return tabVide;
              }
            //System.out.println("Cases testees "+count+" direction : "+k+" !");
              break;

            case 3: //lire vers la Gauche
              val=getValGauche(x,y);
              System.out.println("val : "+val+" coord : "+x+" "+y);
              if (val == 5)
              {
                count--; //On ne compte pas ce tour car c'est une case hors tableau
              }
              x--;
              count++;
              if (count==LONGUEUR*LARGEUR)
              {
                System.out.println("full pour "+valCherchee);
                return tabVide;
              }
            //System.out.println("Cases testees "+count+" direction : "+k+" !");
              break;
          }
          //Fin du Switch sur k

          if (val==valCherchee) { //Si c'est la valeur cherchee qui renvoie les coordonnes
            choix[0]=x;
            choix[1]=y;
            res=choisir(choix);
            if (res != tabVide) {
              return res;
            }
          }
        }
        //Fin du for de lecture dans une direction precise
        if (count==LONGUEUR*LARGEUR)
        {
          ChercheEncore=false;
          return tabVide;
        }
        //Si on a lu tout le tableau, on sort et on cherche autre chose

        k++; //Changement de direction
      }
      //fin du for a 2 repetitions
      j++; //Pour faire un pas de plus
    }
      //fin du while(ChercheEncore), sortira ssi on a scanne toutes les cases
    return tabVide;
  }
  //Fin de la methode chercher

  public int[] choisir(int[] coord)
  {
    int[] res = new int[3]; //tableau a rendre
    int x = coord[0];
    int y = coord[1]; //récupération des coordonnées
    int[] voisinage= new int[4]; //tableau qui regardera les cases adjacentes à la case cherchée
    voisinage[0] = getValHaut(x,y);
    voisinage[1] = getValDroite(x,y);
    voisinage[2] = getValBas(x,y);
    voisinage[3] = getValGauche(x,y);
    System.out.println("je  choisis la case "+x+" "+y+"");
    for(int i = 0; i<voisinage.length; i++ ) //parcours des possibilités si on joue sans créer d'opportunités adverses
    {
      if (Vision[x][y][i+1] == 0) //si il n'y a pas de trait déja tracé
      {
        if (voisinage[i] != 2) //si la case adjacente n'a pas déja deux traits dessinés
        {
          switch (i) //switch pour réagir selon la case a jouer
          {
            case 0:
              res[0]=x;
              res[1]=y;
            break;

            case 1:
              res[0]=x+1;
              res[1]=y;
            break;

            case 2:
              res[0]=x;
              res[1]=y+1;
            break;

           case 3:
             res[0]=x;
             res[1]=y;
            break;
          }
          //fin du switch sur i

          res[2]=i%2;
          return res;
        }
        //fin du si la case adjacente a déja 2 traits dessinés
      }
      //si il n'y a pas  de trait déja tracé
    }
    //fin du for de parcours des possibilités de jouers sans créer d'opportunités adverses

    for(int i=0; i<voisinage.length ; i++ ) //Parcours des possibilités sans recherche d'opportunités
    {
      if (Vision[x][y][i+1] == 0) {
        switch (i) //switch pour réagir selon la case a jouer
        {
          case 0:
            res[0]=x;
            res[1]=y;
          break;

          case 1:
            res[0]=x+1;
            res[1]=y;
          break;

          case 2:
            res[0]=x;
            res[1]=y+1;
          break;

         case 3:
           res[0]=x;
           res[1]=y;
          break;
        }
        //fin du switch sur i

        res[2]=i%2;
        return res;
      }
    }
    //fin du parcours de possibilité sans recherche
    return tabVide;
  }
  //fin de la methode choisir

  public String showVision()
  {
    balayer();
    String res="Vision : +\n";
    for (int i=0; i<LONGUEUR ; i++) {
      res+=" | ";
      for (int j=0; j<LARGEUR; j++) {
        res+=Vision[i][j][0];
        res+=" | ";
      }
      res+="\n";
    }
    res += "\n";
    for (int i=0; i<4 ; i++) {
      res+=" | ";
      for (int j=0; j<4; j++) {
        res+=lignesHor[i][j];
        res+=" | ";
      }
      res+="\n";
    }
    res += "\n";
    for (int i=0; i<4 ; i++) {
      res+=" | ";
      for (int j=0; j<4; j++) {
        res+=lignesVert[i][j];
        res+=" | ";
      }
      res+="\n";
    }
    System.out.println(res);
    return res;
  }

//GETTERS
  public int getValHaut(int x,int y)
  {
    int res=0;
    try
    {
      res = Vision[x][y-1][0];
    }
    catch (Exception e)
    {
      return 5;
    }
    return res;
  }

  public int getValDroite(int x,int y)
  {
    int res=0;
    try
    {
      res =  Vision[x+1][y][0];
    }
    catch (Exception e)
    {
      return 5;
    }
    return res;
  }

  public int getValBas(int x,int y)
  {
    int res=0;
    try
    {
      res =  Vision[x][y+1][0];
    }
    catch (Exception e)
    {
      return 5;
    }
    return res;
  }

  public int getValGauche(int x,int y)
  {
    int res = 0;
    try
    {
      res =  Vision[x-1][y][0];
    }
    catch (Exception e)
    {
      return 5;
    }
    return res;
  }

  public int getVal(int x,int y)
  {
    try
    {
      return Vision[x][y][0];
    }
    catch(Exception e)
    {
      return 5;
    }
  }

    public int getScore()
    {
      return score;
    }
    public void scorePlus(int scoreEnPlus)
    {
      score += scoreEnPlus;
    }
    public String getNom()
    {
      return nom;
    }
    public int getLevel()
    {
      return level;
    }

    public Color getCouleur()
    {
      return couleur;
    }
    public int[] jouer(boolean[][] a, boolean[][] b){return tabVide;}

} //Fin classe
