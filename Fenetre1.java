import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Fenetre1 extends JFrame // première fenêtre où l'utilisateur rentre les paramètres qu'il veut
{
  private JPanel container;

  private JTextField jtfNom1; // JTextField où le joueur 1 rentre son nom
  private JLabel labelNom1;
  private JTextField jtfNom2; // de même pour le joueur 2
  private JLabel labelNom2;

  private boolean fini = false; //à renvoyer au main par la méthode getFini(), permet de récupérer les paramètres actualisées des JTextField de cette fenêtre
  private boolean param = false; // pareil, mais pour les paramètres de la fenêtre FenetreParam
  private int[] joueurLevel = new int[2]; // tableau contenant les niveaux des Joueurs (0 pour Humain, 1 ou 2 pour Ordinateur)

  private Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
  private double jFrameWidth = screenDimension.width/1440;// permet d'adapter les tailles de tous les composants des fenêtres à la taille de l'écran
  private double jFrameHeight = screenDimension.height/800; // ici, jFrameHeight et jFrameWidth correspondent à "une unité" de taille calculée en fonction de la taille de l'écran

  private JPanel top;
  private JPanel bot;
  private JPanel mid;

  private JButton bouton = new JButton("Ok"); // bouton permettant de passer à la suite
  private JButton boutonParam = new JButton("Paramètres avancés"); // bouton permettant d'accéder à la fenêtre FenetreParam

  private String[] choixStrings = {"Humain", "Ordinateur"};
  private String[] choixLevel = {"Facile", "Difficile"}; // facile -> niveau == 1, difficile -> niveau == 2

  private JComboBox<String> choix1 = new JComboBox<String>(choixStrings); // menus déroulants pour choisir si les Joueurs sont des Humains ou des Ordinateurs
  private JComboBox<String> choix2 = new JComboBox<String>(choixStrings);
  private JComboBox<String> level1 = new JComboBox<String>(choixLevel); //menus déroulants pour choisir le niveau de l'ordinateur
  private JComboBox<String> level2 = new JComboBox<String>(choixLevel);

  public Fenetre1() // constructeur de la fenêtre
  {
    this.setTitle("Pipopipette");
    this.setSize((int)(jFrameWidth*1000), (int)(jFrameHeight*200));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de quitter le programme si on clique sur la croix
    this.setLocationRelativeTo(null); // permet de déplacer la fenêtre sur l'écran

    setFont(new Font("Arial", Font.PLAIN, (int)jFrameHeight*14)); // on initialise une police par défaut
    // on initialise les différents JPanel
    top = new JPanel(); // JPanel du haut
    bot = new JPanel(); // du bas
    mid = new JPanel(); // du milieu
    container = new JPanel(); // celui qui contient tous les autres

    container.setBackground(Color.white);
    container.setLayout(new BorderLayout());

    jtfNom1  = new JTextField("Joueur1"); // nom par défaut
    jtfNom1.setFont(jtfNom1.getFont().deriveFont((float)(jFrameHeight*14))); // on adapte la taille de la police à l'écran
    labelNom1  = new JLabel("Nom du joueur 1 :"); // permet de faire comprendre à l'utilisateur de rentrer son nom
    labelNom1.setFont(labelNom1.getFont().deriveFont((float)(jFrameHeight*14)));
    jtfNom2  = new JTextField("Joueur2");
    jtfNom2.setFont(jtfNom2.getFont().deriveFont((float)(jFrameHeight*14)));
    labelNom2  = new JLabel("Nom du joueur 2 :");
    labelNom2.setFont(labelNom2.getFont().deriveFont((float)(jFrameHeight*14)));

    choix1.setFont(choix1.getFont().deriveFont((float)(jFrameHeight*14))); // on adapte les polices de tous les composants
    choix2.setFont(choix2.getFont().deriveFont((float)(jFrameHeight*14))); // à la taille de l'écran
    level1.setFont(level1.getFont().deriveFont((float)(jFrameHeight*14)));
    level2.setFont(level2.getFont().deriveFont((float)(jFrameHeight*14)));
    bouton.setFont(bouton.getFont().deriveFont((float)(jFrameHeight*14)));
    boutonParam.setFont(boutonParam.getFont().deriveFont((float)(jFrameHeight*14)));

    choix1.setPreferredSize(new Dimension((int)(jFrameWidth*130), (int)(jFrameHeight*26))); // on adapte également les tailles
    choix2.setPreferredSize(new Dimension((int)(jFrameWidth*130), (int)(jFrameHeight*26))); // de tous les composants sauf des
    level1.setPreferredSize(new Dimension((int)(jFrameWidth*110), (int)(jFrameHeight*26))); // JLabel, dont la taille s'adapte
    level2.setPreferredSize(new Dimension((int)(jFrameWidth*110), (int)(jFrameHeight*26))); // au texte qui ne change pas
    jtfNom1.setPreferredSize(new Dimension((int)(jFrameWidth*150), (int)(jFrameHeight*30)));
    jtfNom2.setPreferredSize(new Dimension((int)(jFrameWidth*150), (int)(jFrameHeight*30)));


    choix1.addActionListener(new ItemAction1()); // on associe les composants à leur Listener correspondants
    choix2.addActionListener(new ItemAction2());
    level1.addActionListener(new LevelItemAction1());
    level2.addActionListener(new LevelItemAction2());
    bouton.addActionListener(new BoutonListener());
    boutonParam.addActionListener(new ParamListener());

    choix1.setForeground(Color.blue); // on initialise la couleur du texte des JComboBox
    choix2.setForeground(Color.blue);
    level1.setForeground(Color.blue);
    level2.setForeground(Color.blue);

    top.add(choix1);
    top.add(labelNom1);
    top.add(jtfNom1);
    top.add(level1);
    top.add(choix2);
    top.add(labelNom2);
    top.add(jtfNom2);
    top.add(level2); // on place les composants pour les noms en haut de la fenêtre
    bot.add(bouton); //on place le bouton tout en bas
    mid.add(boutonParam); // et le bouton pour ouvrir la fenêtre FenetreParam au milieu

    container.add(top, BorderLayout.NORTH);
    container.add(bot, BorderLayout.SOUTH);
    container.add(mid, BorderLayout.CENTER);

    this.setContentPane(container);
    level1.setVisible(false); // on "cache" les JComboBox des niveaux pour l'ordinateur
    level2.setVisible(false);
    this.setVisible(true);
  }

  public String[] getNoms() // méthode permettant de récupérer les noms des deux Joueurs
  {
    String[] noms = {"",""};
    noms[0] = jtfNom1.getText();
    noms[1] = jtfNom2.getText();
    return noms;
  }
  public int[] getLevel() // méthode permettant de récupérer les niveaux des deux Joueurs
  {
    return joueurLevel;
  }
  public boolean getFini() // permet de renvoyer au main si l'utilisateur a fini de rentrer ses paramètres ou non
  {
    return fini;
  }
  public boolean getParam() // pareil, mais pour la fenêtre FenetreParam
  {
    return param;
  }
  public void setParam(boolean param) // permet de pouvoir rouvrir la fenetre FenetreParam après l'avoir ouverte et fermée
  {
    this.param = param;
  }
  public double[] getScreenSize() // évite de recalculer jFrameWidth et jFrameHeight dans le main
  {
    double ret[] = {jFrameWidth, jFrameHeight};
    return(ret);
  }

  class ItemAction1 implements ActionListener // classe associée à la JComboBox choix1, permettant de choisir "Humain" ou
  {                                           // "Ordinateur" pour le Joueur1
    public void actionPerformed(ActionEvent event)
    {
      if (choix1.getSelectedItem() == "Ordinateur") // si l'utilisateur choisit que Joueur1 sera un Ordinateur
      {
        jtfNom1.setText("Ordi1"); // on lui donne automatiquement le nom "Ordi1"
        labelNom1.setText("Niveau de l'ordinateur 1 :"); // on fait comprendre à l'utilisateur de choisir le niveau de l'ordinateur
        joueurLevel[0] = 1; // par défaut, l'Ordinateur est "Facile"
        jtfNom1.setVisible(false); // on "cache" la JTextField qui permettait de rentrer le nom du Joueur1
        level1.setVisible(true); // on affiche la JComboBox pour choisir le niveau
      }
      else
      {
        labelNom1.setText("Nom du joueur 1 :"); // si l'utilisateur choisit que Joueur1 sera un Humain
        jtfNom1.setText("Joueur1"); // nom par défaut
        level1.setVisible(false); // on "cache" la JComboBox pour choisir le niveau de l'Ordinateur
        jtfNom1.setVisible(true); // et on réaffiche la JTextField qui permet de rentrer le nom du Joueur1
      }
    }
  }
  class ItemAction2 implements ActionListener //même classe que ci-dessus pour le Joueur2
  {
    public void actionPerformed(ActionEvent event)
    {
      if (choix2.getSelectedItem() == "Ordinateur")
      {
        jtfNom2.setText("Ordi2");
        labelNom2.setText("Niveau de l'ordinateur 2 :");
        joueurLevel[1] = 1;
        jtfNom2.setVisible(false);
        level2.setVisible(true);
      }
      else
      {
        labelNom2.setText("Nom du joueur 2 :");
        jtfNom2.setText("Joueur2");
        level2.setVisible(false);
        jtfNom2.setVisible(true);
      }
    }
  }
  class LevelItemAction1 implements ActionListener // classe associée à la JComboBox level1, qui permet de choisir le niveau
  {                                                // de l'Ordinateur pour le Joueur1
    public void actionPerformed(ActionEvent event)
    {
      if (level1.getSelectedItem() == "Facile") // si l'utilisateur choisit que le niveau sera "Facile"
      {
        joueurLevel[0] = 1; // le niveau de l'Ordinateur est égal à 1
      }
      else if (level1.getSelectedItem() == "Difficile") // si c'est "Difficile"
      {
        joueurLevel[0] = 2; // la variable sera égale à 2
      }
    }
  }
  class LevelItemAction2 implements ActionListener // même classe que ci-dessus pour le Joueur2
  {
    public void actionPerformed(ActionEvent event)
    {
      if (level2.getSelectedItem() == "Facile")
      {
        joueurLevel[1] = 1;
      }
      else if (level2.getSelectedItem() == "Difficile")
      {
        joueurLevel[1] = 2;
      }
    }
  }

  class BoutonListener implements ActionListener // classe associée au bouton 'bouton' en bas de la fenêtre
  {
    public void actionPerformed(ActionEvent event) // si on clique dessus
    {
      fini = true; // renvoie au main que l'utilisateur a fini d'utiliser la fenêtre via la méthode getFini()
      dispose(); // ferme cette fenêtre
    }
  }
  class ParamListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event) // si l'utilisateur clique sur le bouton boutonParam
    {
      param = true; // renvoie au main que l'utilisateur veut utiliser la fenêtre FenetreParam
    }
  }
}
