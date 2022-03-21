package myProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * This class is used for the visual part of the project
 * @autor Juan Esteban Brand Tovar - Jose Miguel Becerra Casierra - Juan Pablo Pantoja Guitierrez
 * @version v.1.0.0 date:21/03/2022
 */

public class GUIGridBagLayout extends JFrame {
    public static final String MENSAJE_INICIO = "Bienvenido a BattleShip \n"
            + "Oprime el boton 'Jugar' para iniciar el juego"
            + "\n-El juego consiste en que deberás acabar con la flota enemiga sin verla"
            + "\n-Posiciona tus barcos pulsando las casillas de tu territorio, teniendo en cuenta que:"
            + "\n Deberás posicionar 4 fragatas, serán pintadas de color gris (1 casilla cada una)"
            + "\n Deberás posicionar 3 destructores, serán pintadas de color verde (2 casilla cada una)"
            + "\n Deberás posicionar 2 submarinos, serán pintadas de color magenta (3 casilla cada una)"
            + "\n Deberás posicionar 1 portaaviones, serán pintadas de color carne  (4 casilla cada una)"
            + "\n NOTA: para posicionar submarinos y portaaviones deberás hacerlo de forma ordenada (una tras otra)"
            + "\n-Para hundir los barcos enemigos deberás usar las casillas del territorio enemigo"
            + "\n Segun el numero de casillas del barco, podrás dañarlo o hundirlo"
            + "\n Si se pinta de amarillo, faltan partes por hundir"
            + "\n Si se pinta de rojo, has hundido ese barco"
            + "\n-El juego termina cuando uno de los dos hunde todos los barcos del oponente"
            + "\n-Podrás ver la matriz enemiga (territorio) si pulsas el boton 'Barcos enemigos'";

    private Header headerProject;
    private JButton jugar, ayuda, salir;
    private JPanel panelAliado, panelEnemigo;
    private JTextArea mensajeSalida;
    private Escucha escucha;
    private int lugar = 0;
    private int lugare = 0;
    private int parar = 0;
    private int equis = 45;
    private int equiz = 330;
    private int ye = 1;
    private int ge = 1;
    private int lugar1 = 0;
    private int lugar0 = 0;
    private int lugar2 = 0;
    private int lugar3 = 0;
    private JLabel[] label = new JLabel[100];
    private JLabel[] label2 = new JLabel[100];
    private ModelGame modelGame;
    public CPU cpu;

    public GUIGridBagLayout() {
        initGUI();

        //Default JFrame configuration
        this.setTitle("Battleship");
        this.setSize(800, 600);
        //this.setUndecorated(true);
        this.setBackground(new Color(255, 255, 255, 255));
        this.pack();
        this.setResizable(true);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initGUI() {
        //Set up JFrame Container's Layout
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        //Create Listener Object or Control Object
        escucha = new Escucha();
        //Set up JComponents
        headerProject = new Header("BattleShip", Color.BLACK);
        modelGame = new ModelGame();
        cpu = new CPU();
        panelAliado = new JPanel();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(headerProject, constraints);

        Border border = BorderFactory.createLineBorder(Color.black, 1);

        int[][] tableroPos = modelGame.getTableroPos();
        int[][] tableroPpal = cpu.getTableroPpal();
        modelGame.tablerosIniciales();
        cpu.tablerosIniciales();


        //Crear labels
        for (int i = 0; i < (label.length); i++) {
            label[i] = new JLabel();
        }

        //Crear labels2
        for (int i = 0; i < (label2.length); i++) {
            label2[i] = new JLabel();
        }

        //darle color a label
        for (int x = 0; x < tableroPpal.length; x++) {
            for (int y = 0; y < tableroPpal[0].length; y++) {
                if (tableroPpal[x][y] == 1) {
                    label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.CYAN);
                    label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                }
                if (tableroPpal[x][y] == 2) {
                    label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.RED);
                    label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                }
            }
        }

        //darle color a label2
        for (int x = 0; x < tableroPos.length; x++) {
            for (int y = 0; y < tableroPos[0].length; y++) {
                if (tableroPos[x][y] == 1) {
                    label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.CYAN);
                    label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                }
                if (tableroPos[x][y] == 2) {
                    label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.RED);
                    label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                }
            }
        }

        setVisible(true);


        ayuda = new JButton("?");
        ayuda.addActionListener(escucha);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.LINE_START;
        this.add(ayuda, constraints);


        salir = new JButton("Barcos enemigos");
        salir.addActionListener(escucha);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.LINE_END;
        this.add(salir, constraints);

        panelAliado = new JPanel();
        panelAliado.setPreferredSize(new Dimension(300, 300));
        panelAliado.setBorder(BorderFactory.createTitledBorder("Tu territorio (Tablero de posición)"));
        panelAliado.setLayout(null);
        for (int i = 0; i < (label.length); i++) {
            //label[i].setText("" + (i));
            label[i].setBorder(border);
            label[i].setHorizontalAlignment(SwingConstants.CENTER);
            label[i].setBounds(new Rectangle(equis - 20, (ye) * 25, 25, 25));
            lugar++;
            ye++;
            if (lugar == 10) {
                equis = equis + 25;
                ye = 1;
                lugar = 0;
            }
            panelAliado.add(label[i], null);
        }

        panelEnemigo = new JPanel();
        panelEnemigo.setPreferredSize(new Dimension(300, 300));
        panelEnemigo.setBorder(BorderFactory.createTitledBorder("Territorio enemigo (Tablero principal)"));
        panelEnemigo.setLayout(null);
        for (int i = 0; i < (label2.length); i++) {
            //label2[i].setText("" + (i));
            label2[i].setBorder(border);
            label2[i].setHorizontalAlignment(SwingConstants.CENTER);
            label2[i].setBounds(new Rectangle(equiz - 300, (ge) * 25, 25, 25));
            lugare++;
            ge++;
            if (lugare == 10) {
                equiz = equiz + 25;
                ge = 1;
                lugare = 0;
            }
            panelEnemigo.add(label2[i], null);
        }

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;

        add(panelAliado, constraints);


        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        add(panelEnemigo, constraints);

        jugar = new JButton("Jugar");
        jugar.addActionListener(escucha);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        add(jugar, constraints);

        mensajeSalida = new JTextArea(2, 10);
        mensajeSalida.setText("Presiona Jugar para iniciar \n Usa el botón (?) para ver las reglas del juego");
        mensajeSalida.setBorder(BorderFactory.createTitledBorder("Mensajes"));
        mensajeSalida.setEditable(false);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        add(mensajeSalida, constraints);


    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GUIGridBagLayout miProjectGUI = new GUIGridBagLayout();
        });
    }

    private class Escucha implements ActionListener, MouseListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == jugar) {
                for (int i = 0; i < (label.length); i++) {
                    label[i].addMouseListener(escucha);
                }

                for (int i = 0; i < (label.length); i++) {
                    label2[i].addMouseListener(escucha);
                }

                modelGame.llenarAtaque();
                cpu.posicionarCPU();
                cpu.igualarTablero();
            }
            if (e.getSource() == salir) {
                cpu.imprimir();
            }
            if (e.getSource() == ayuda) {
                JOptionPane.showMessageDialog(null, MENSAJE_INICIO,"Instrucciones",1);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int[][] tableroPos = modelGame.getTableroPos();
            int[][] tableroPpal = cpu.getTableroPpal();

            for (int i = 0; i < (label.length); i++) {
                if (e.getSource() == label[i]) {
                    if (i < 10) {
                        lugar0 = i;
                        lugar1 = 0;
                    }
                    if (i >= 10 && i < 20) {
                        lugar0 = i - 10;
                        lugar1 = 1;
                    }
                    if (i >= 20 && i < 30) {
                        lugar0 = i - 20;
                        lugar1 = 2;
                    }
                    if (i >= 30 && i < 40) {
                        lugar0 = i - 30;
                        lugar1 = 3;
                    }
                    if (i >= 40 && i < 50) {
                        lugar0 = i - 40;
                        lugar1 = 4;
                    }
                    if (i >= 50 && i < 60) {
                        lugar0 = i - 50;
                        lugar1 = 5;
                    }
                    if (i >= 60 && i < 70) {
                        lugar0 = i - 60;
                        lugar1 = 6;
                    }
                    if (i >= 70 && i < 80) {
                        lugar0 = i - 70;
                        lugar1 = 7;
                    }
                    if (i >= 80 && i < 90) {
                        lugar0 = i - 80;
                        lugar1 = 8;
                    }
                    if (i >= 90 && i < 100) {
                        lugar0 = i - 90;
                        lugar1 = 9;
                    }
                    modelGame.posicionar(lugar1, lugar0);
                    //JOptionPane.showMessageDialog(null, "" + lugar1);
                    for (int x = 0; x < tableroPos.length; x++) {
                        for (int y = 0; y < tableroPos[0].length; y++) {
                            if (tableroPos[x][y] == 1) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.CYAN);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPos[x][y] == 2) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.GRAY);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPos[x][y] == 3) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.GREEN);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPos[x][y] == 4) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.MAGENTA);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPos[x][y] == 5) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.PINK);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < (label2.length); i++) {
                if (e.getSource() == label2[i]) {
                    if (i < 10) {
                        lugar2 = i;
                        lugar3 = 0;
                    }
                    if (i >= 10 && i < 20) {
                        lugar2 = i - 10;
                        lugar3 = 1;
                    }
                    if (i >= 20 && i < 30) {
                        lugar2 = i - 20;
                        lugar3 = 2;
                    }
                    if (i >= 30 && i < 40) {
                        lugar2 = i - 30;
                        lugar3 = 3;
                    }
                    if (i >= 40 && i < 50) {
                        lugar2 = i - 40;
                        lugar3 = 4;
                    }
                    if (i >= 50 && i < 60) {
                        lugar2 = i - 50;
                        lugar3 = 5;
                    }
                    if (i >= 60 && i < 70) {
                        lugar2 = i - 60;
                        lugar3 = 6;
                    }
                    if (i >= 70 && i < 80) {
                        lugar2 = i - 70;
                        lugar3 = 7;
                    }
                    if (i >= 80 && i < 90) {
                        lugar2 = i - 80;
                        lugar3 = 8;
                    }
                    if (i >= 90 && i < 100) {
                        lugar2 = i - 90;
                        lugar3 = 9;
                    }
                    modelGame.ataqueCPU();
                    cpu.atacar(lugar3, lugar2);
                    for (int x = 0; x < tableroPpal.length; x++) {
                        for (int y = 0; y < tableroPpal[0].length; y++) {
                            if (tableroPpal[x][y] < 9) {
                                //Barcos tocados = num+5, Barcos hundidos = num+6 , Agua tocada = num+0
                                label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.CYAN);
                                label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPpal[x][y] == 15) {
                                  label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setForeground(Color.RED);
                                  label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setText("X");
                            }
                            if (tableroPpal[x][y] > 15 && tableroPpal[x][y] < 100) {
                                label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.YELLOW);
                                label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPpal[x][y] >= 100) {
                                label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.RED);
                                label2[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                        }
                    }

                    for (int x = 0; x < tableroPos.length; x++) {
                        for (int y = 0; y < tableroPos[0].length; y++) {
                            if (tableroPos[x][y] < 9) {
                                //Barcos tocados = num+5, Barcos hundidos = num+6 , Agua tocada = num+0
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.CYAN);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPos[x][y] == 15) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setForeground(Color.RED);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setText("X");
                            }
                            if (tableroPos[x][y] > 15 && tableroPpal[x][y] < 100) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.YELLOW);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPos[x][y] >= 100) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.RED);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                        }
                    }

                    for (int x = 0; x < tableroPos.length; x++) {
                        for (int y = 0; y < tableroPos[0].length; y++) {
                            if (tableroPos[x][y] == 1) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.CYAN);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPos[x][y] == 2) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.GRAY);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPos[x][y] == 3) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.GREEN);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPos[x][y] == 4) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.MAGENTA);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                            if (tableroPos[x][y] == 5) {
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setBackground(Color.PINK);
                                label[Integer.parseInt(String.valueOf(x) + String.valueOf(y))].setOpaque(true);
                            }
                        }
                    }
                }
            }

            modelGame.verDestruidos();
            cpu.verDestruidos();
            modelGame.ganadorCPU();
            cpu.ganadorJugador();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
