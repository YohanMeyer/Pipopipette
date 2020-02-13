import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FenetreParam extends JFrame // fenêtre des paramètres avancés accessible par un bouton de Fenêtre1
{
  private boolean fini = false; // renvoie au main si l'utilisateur a fini d'utiliser cette fenêtre (true) ou non (false)
  private JLabel message;
  private JLabel labelCouleur1;
  private JLabel labelCouleur2;
  private JLabel labelTaille;
  private JLabel labelLongueur;
  private JLabel labelLargeur;
  private JTextField jtfLong;
  private JTextField jtfLarg;

  private String[] listeCouleur = {"Par défaut", "Aléatoire", "Noir", "Bleu", "Cyan", "Gris foncé", "Gris", "Gris clair", "Vert", "Magenta", "Orange", "Rose", "Rouge", "Jaune"}; // liste des couleurs disponibles
  private Color[] couleurs = {Color.red, Color.darkGray}; // couleurs par défaut

  private JComboBox<String> choixCouleur1 = new JComboBox<String>(listeCouleur);
  private JComboBox<String> choixCouleur2 = new JComboBox<String>(listeCouleur);

  private double jFrameWidth;
  private double jFrameHeight;

  private JPanel top;
  private JPanel mid;
  private JPanel bot;
  private JPanel container;

  private JButton bouton = new JButton("Ok");

  public FenetreParam(double jFrameWidth, double jFrameHeight)
  {
    this.setTitle("Pipopipette");
    this.setSize((int)(jFrameWidth*1000), (int)(jFrameHeight*200));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);

    setFont(new Font("Arial", Font.PLAIN, (int)jFrameHeight*14));

    top = new JPanel();
    bot = new JPanel();
    mid = new JPanel();
    container = new JPanel();

    container.setBackground(Color.white);
    container.setLayout(new BorderLayout()); // même initialisation de fenêtre que Fenetre1

    bouton.setFont(bouton.getFont().deriveFont((float)(jFrameHeight*14)));

    message = new JLabel("Attention ! Si un joueur choisit la même couleur que l'autre, sa couleur sera choisie alétoirement à la place");
    message.setForeground(Color.red); // on affiche le message en rouge
    labelCouleur1 = new JLabel("Couleur du joueur 1 : "); // initialisation des différents composants
    labelCouleur2 = new JLabel("Couleur du joueur 2 : ");
    labelTaille = new JLabel("Taille du plateau :");
    labelLongueur = new JLabel("Longeur :");
    labelLargeur = new JLabel("Largeur :");
    jtfLong = new JTextField("3");
    jtfLarg = new JTextField("3");

    message.setFont(message.getFont().deriveFont((float)(jFrameHeight*16))); // initialisation de la taille de leur police
    labelCouleur1.setFont(labelCouleur1.getFont().deriveFont((float)(jFrameHeight*14)));
    labelCouleur2.setFont(labelCouleur2.getFont().deriveFont((float)(jFrameHeight*14)));
    labelTaille.setFont(labelTaille.getFont().deriveFont((float)(jFrameHeight*14)));
    labelLongueur.setFont(labelLongueur.getFont().deriveFont((float)(jFrameHeight*14)));
    labelLargeur.setFont(labelLargeur.getFont().deriveFont((float)(jFrameHeight*14)));
    jtfLong.setFont(jtfLong.getFont().deriveFont((float)(jFrameHeight*14)));
    jtfLarg.setFont(jtfLarg.getFont().deriveFont((float)(jFrameHeight*14)));
    choixCouleur1.setFont(choixCouleur1.getFont().deriveFont((float)(jFrameHeight*14)));
    choixCouleur2.setFont(choixCouleur1.getFont().deriveFont((float)(jFrameHeight*14)));

    jtfLong.setPreferredSize(new Dimension((int)(jFrameWidth*60), (int)(jFrameHeight*30))); // initialisation des tailles des
    jtfLarg.setPreferredSize(new Dimension((int)(jFrameWidth*60), (int)(jFrameHeight*30))); // composants
    choixCouleur1.setPreferredSize(new Dimension((int)(jFrameWidth*130), (int)(jFrameHeight*26)));
    choixCouleur2.setPreferredSize(new Dimension((int)(jFrameWidth*130), (int)(jFrameHeight*26)));

    bouton.addActionListener(new BoutonListener());
    choixCouleur1.addActionListener(new ItemAction1());
    choixCouleur2.addActionListener(new ItemAction2());

    top.add(labelTaille);
    top.add(labelLongueur);
    top.add(jtfLong);
    top.add(labelLargeur);
    top.add(jtfLarg); //composants pour la taille en haut
    mid.add(labelCouleur1);
    mid.add(choixCouleur1);
    mid.add(labelCouleur2);
    mid.add(choixCouleur2); // couleur au milieu
    mid.add(message); // message juste en dessous de la couleur car le texte ne rentre pas sur la même ligne
    bot.add(bouton); // le bouton en bas, même bouton que pour Fenetre1

    container.add(top, BorderLayout.NORTH);
    container.add(bot, BorderLayout.SOUTH);
    container.add(mid, BorderLayout.CENTER);
    this.setContentPane(container);
    this.setVisible(true);
  }

  public boolean getFini() // pareil que Fenetre1
  {
    return fini;
  }
  public int[] getTaille() // permet de renvoyer au main la taille rentrée par l'utilisateur en nombre de points
  {
    String longu = jtfLong.getText();
    String larg = jtfLarg.getText();
    int longueur;
    int largeur;
    // permet d'empêcher l'utilisateur d'écrire autre chose qu'un chiffre dans la JTextField
    try
    { //pour la longueur
      longueur = Integer.parseInt(longu);
    }
    catch(Exception e)
    {
      if (longu.equals(""))
      {
        longueur = 0;
      }
      else
      {
        char dernierCar = longu.charAt(longu.length() -1);
        while (dernierCar != '0' && dernierCar != '1' && dernierCar != '2' && dernierCar != '3' && dernierCar != '4' && dernierCar != '5' && dernierCar != '6' && dernierCar != '7' && dernierCar != '8' && dernierCar != '9' && !longu.equals(""))
        {
          jtfLong.setText(longu.substring(0, longu.length()-1));
          longu = jtfLong.getText();
        }
        if (longu.equals(""))
        {
          longueur = 0;
        }
        else
        {
          longueur = Integer.parseInt(longu);
        }
      }
    }

    try
    { // et pour la largeur
      largeur = Integer.parseInt(larg);
    }
    catch(Exception e)
    {
      if (larg.equals(""))
      {
        largeur = 0;
      }
      else
      {
        char dernierCar = larg.charAt(larg.length() -1);
        while (dernierCar != '0' && dernierCar != '1' && dernierCar != '2' && dernierCar != '3' && dernierCar != '4' && dernierCar != '5' && dernierCar != '6' && dernierCar != '7' && dernierCar != '8' && dernierCar != '9' && !larg.equals(""))
        {
          jtfLarg.setText(larg.substring(0, larg.length()-1));
          larg = jtfLarg.getText();
        }
        if (larg.equals(""))
        {
          largeur = 0;
        }
        else
        {
          largeur = Integer.parseInt(larg);
        }
      }
    }

    if (longueur > 20) // empêche l'utilisateur de rentrer une taille trop grande
    {
      longueur = 20;
    }
    if (largeur > 10)
    {
      largeur = 10;
    }

    int[] taille = {longueur,largeur};
    return taille;
  }
  public Color[] getCouleurs() // renvoie au main les couleurs choisies
  {
    return(couleurs);
  }

  class BoutonListener implements ActionListener //pareil que pour Fenetre1
  {
    public void actionPerformed(ActionEvent event)
    {
      fini = true;
      dispose();
    }
  }

  class ItemAction1 implements ActionListener // classe associée à la JComboBox choixCouleur1
  {
    public void actionPerformed(ActionEvent event)
    {
      if (choixCouleur1.getSelectedIndex() == 0) // si l'utilisateur choisit "Par défaut"
      {
        couleurs[0] = Color.red;
      }
      else if (choixCouleur1.getSelectedIndex() == 1) // si l'utilisateur choisit "Aléatoire"
      {
        couleurs[0] = maCouleur((int)(Math.random()*12));
      }
      else
      {
        couleurs[0] = maCouleur(choixCouleur1.getSelectedIndex() - 2); // si l'utilisateur choisit directement une couleur
      }
      while(couleurs[0].equals(couleurs[1])) // si la couleur choisie est la même que pour l'autre joueur
      {
        couleurs[0] = maCouleur((int)(Math.random()*12)); // on en prend une autre au hasard
      }
    }
  }
  class ItemAction2 implements ActionListener // pareil que ci-dessus pour le Joueur2
  {
    public void actionPerformed(ActionEvent event)
    {
      if (choixCouleur2.getSelectedIndex() == 0)
      {
        couleurs[1] = Color.red;
      }
      else if (choixCouleur2.getSelectedIndex() == 1)
      {
        couleurs[1] = maCouleur((int)(Math.random()*12));
      }
      else
      {
        couleurs[1] = maCouleur(choixCouleur2.getSelectedIndex() - 2);
      }
      while(couleurs[1].equals(couleurs[0]))
      {
        couleurs[1] = maCouleur((int)(Math.random()*12));
      }
    }
  }

  public Color maCouleur(int i)
  {
    Color mesCouleurs[] = {Color.black, Color.blue, Color.cyan, Color.darkGray, Color.gray, Color.lightGray, Color.green, Color.magenta, Color.orange, Color.pink, Color.red, Color.yellow};
    return mesCouleurs[i];
  }
}
