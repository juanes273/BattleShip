package myProject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * This class is used for the functional part of the opponent(CPU)
 * @version v.1.0.0 date:21/03/2022
 * @autor Juan Esteban Brand Tovar - Jose Miguel Becerra Casierra - Juan Pablo Pantoja Guitierrez
 */

public class CPU {
    int[][] tableroPpal = new int[10][10];
    int[][] tableroPpalInicial = new int[10][10];
    int fragatasF = 4, destructoresF = 3, submarinosF = 2, portaavionesF = 1, posicionando = 0, seleccionCPU, destruidos;
    boolean fragatas = true, destructores = false, submarinos = false, portaaviones = false, etapaPosicion = true;
    int[] anterior = new int[4];
    String matrizFinal = " 1 = agua \n 2 = fragata \n 3 = destructor \n 4 = submarino \n 5 = portaaviones \n";
    ArrayList<Integer> posiblesD = new ArrayList<>();
    ArrayList<Integer> posiblesS = new ArrayList<>();
    ArrayList<Integer> posiblesP = new ArrayList<>();
    ArrayList<Integer> barcosEnUso = new ArrayList<>();


    /**
     * Saves the first matrix of the position of the enemy ships
     */
    public void igualarTablero(){
            for(int i=0;i<tableroPpal.length;i++) {
                for (int j = 0; j < tableroPpal.length; j++) {
                    tableroPpalInicial[i][j] = tableroPpal[i][j];
                }
            }
    }

    /**
     * Shows the enemy territory as a matrix
     */
    public void imprimir() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matrizFinal += tableroPpalInicial[j][i];
                matrizFinal += "    ";
            }
            matrizFinal += "\n";
        }
        JOptionPane.showMessageDialog(null, matrizFinal, "Barcos enemigos inicial", 1);
        matrizFinal = " 1 = agua \n 2 = fragata \n 3 = destructor \n 4 = submarino \n 5 = portaaviones \n";
    }

    /**
     * Gets a random position to place the enemy ships
     */
    public int getSeleccionCPU() {
        Random aleatorio = new Random();
        if (posicionando != 0 && destructores) {
            int numero = aleatorio.nextInt(posiblesD.toArray().length);
            int eleccion = posiblesD.get(numero);

            return eleccion;
        }
        if (posicionando != 0 && submarinos) {
            int numero = aleatorio.nextInt(posiblesS.toArray().length);
            int eleccion = posiblesS.get(numero);

            return eleccion;
        }
        if (posicionando != 0 && portaaviones) {
            int numero = aleatorio.nextInt(posiblesP.toArray().length);
            int eleccion = posiblesP.get(numero);

            return eleccion;
        } else {
            seleccionCPU = aleatorio.nextInt(10);
            return seleccionCPU;
        }
    }

    /**
     * Place the enemy ships dependind of the getSeleccionCPU value
     */
    public void posicionarCPU() {
        if (!portaaviones) {
            while (!pararCPU()) {
                if (posicionando != 0 && (destructores || submarinos || portaaviones)) {
                    int seleccion = getSeleccionCPU();
                    if (seleccion <= 9) {
                        int equis = 0;
                        int ye = seleccion;
                        posicionar(equis, ye);
                    } else {
                        String number = String.valueOf(seleccion);
                        String[] digitos = number.split("(?<=.)");

                        int equis = Integer.parseInt(digitos[0]);
                        int ye = Integer.parseInt(digitos[1]);

                        posicionar(equis, ye);
                    }
                } else {
                    int equis = getSeleccionCPU();
                    int ye = getSeleccionCPU();
                    posicionar(equis, ye);
                }
            }
        }
    }

    /**
     * Function that stops posicionarCPU function when all the ships are positioned
     */
    public boolean pararCPU() {
        if (fragatasF == 0 && destructoresF == 0 && submarinosF == 0 && portaavionesF == 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Initialize tableroPpal values to 1(Water)
     */
    public void tablerosIniciales() {
        for (int i = 0; i < tableroPpal.length; i++) {
            for (int j = 0; j < tableroPpal[0].length; j++) {
                tableroPpal[i][j] = 1;
            }
        }
    }

    /**
     * Function that puts the ships on the enemy territory
     */
    public void posicionar(int X, int Y) {
        if (etapaPosicion) {
            verificarBarco();
            if (fragatas) {
                if (verificarAgua(X, Y)) {
                    tableroPpal[X][Y] = 2;
                    //JOptionPane.showMessageDialog(null,""+fragatasF);
                    fragatasF--;
                    if (fragatasF == 0) {
                        fragatas = false;
                        destructores = true;
                    }
                }
            }
            if (destructores) {
                if (verificarAgua(X, Y)) {
                    if (verificarDestructor(X, Y)) {
                        if (verificarAgua(X, Y) && posicionando == 0) {
                            //JOptionPane.showMessageDialog(null,"destr:"+destructoresF);
                            tableroPpal[X][Y] = 3;
                            barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                            anterior[0] = X;
                            anterior[1] = Y;
                            posicionando = 1;
                            //JOptionPane.showMessageDialog(null, "" + posiblesD);
                        }
                        if (verificarAgua(X, Y) && posicionando == 1) {
                            if (verificarDestructor2(X, Y)) {
                                //JOptionPane.showMessageDialog(null, "funciona");
                                tableroPpal[X][Y] = 3;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                                posicionando = 0;
                                destructoresF--;
                                if (destructoresF == 0) {
                                    destructores = false;
                                    submarinos = true;
                                }
                                posiblesD.clear();
                            } else {
                                //JOptionPane.showMessageDialog(null, "No puedes posicionar aquí");
                            }
                        }
                    }
                }
            }
            if (submarinos) { // submarinos
                if (verificarAgua(X, Y)) {
                    if (verificarSubmarino(X, Y)) {
                        if (verificarAgua(X, Y) && posicionando == 0) {
                            tableroPpal[X][Y] = 4;
                            barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                            anterior[0] = X;
                            anterior[1] = Y;
                            posicionando = 1;
                            //JOptionPane.showMessageDialog(null, "" + posiblesD);
                        }
                        if (verificarAgua(X, Y) && posicionando == 1) {
                            if (verificarSubmarino2(X, Y)) {
                                if (anterior[0] > X) {
                                    posiblesS.clear();
                                    if (X + 1 == anterior[0]) {
                                        posiblesS.add(Integer.parseInt(String.valueOf(X - 1) + String.valueOf(Y)));
                                        posiblesS.add(Integer.parseInt(String.valueOf(anterior[0] + 1) + String.valueOf(anterior[1])));
                                    } else {
                                        posiblesS.add(Integer.parseInt(String.valueOf(X + 1) + String.valueOf(Y)));
                                    }
                                }
                                if (anterior[0] < X) {
                                    posiblesS.clear();
                                    if (X - 1 == anterior[0]) {
                                        posiblesS.add(Integer.parseInt(String.valueOf(X + 1) + String.valueOf(Y)));
                                        posiblesS.add(Integer.parseInt(String.valueOf(anterior[0] - 1) + String.valueOf(anterior[1])));
                                    } else {
                                        posiblesS.add(Integer.parseInt(String.valueOf(X - 1) + String.valueOf(Y)));
                                    }
                                }
                                if (anterior[1] > Y) {
                                    posiblesS.clear();
                                    if (Y + 1 == anterior[1]) {
                                        posiblesS.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y - 1)));
                                        posiblesS.add(Integer.parseInt(String.valueOf(anterior[0]) + String.valueOf(anterior[1] + 1)));
                                    } else {
                                        posiblesS.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y + 1)));
                                    }
                                }
                                if (anterior[1] < Y) {
                                    posiblesS.clear();
                                    if (Y - 1 == anterior[1]) {
                                        posiblesS.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y + 1)));
                                        if (anterior[1] != 0) {
                                            posiblesS.add(Integer.parseInt(String.valueOf(anterior[0]) + String.valueOf(anterior[1] - 1)));
                                        }
                                    } else {
                                        posiblesS.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y - 1)));
                                    }
                                }
                                tableroPpal[X][Y] = 4;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                                posicionando = 2;
                                anterior[2] = X;
                                anterior[3] = Y;
                            } else {
                                //JOptionPane.showMessageDialog(null, "No puedes posicionar aquí");
                            }
                        }
                        if (verificarAgua(X, Y) && posicionando == 2) {
                            if (verificarSubmarino2(X, Y)) {
                                tableroPpal[X][Y] = 4;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                                posicionando = 0;
                                submarinosF--;
                                if (submarinosF == 0) {
                                    submarinos = false;
                                    portaaviones = true;
                                }
                                posiblesS.clear();
                            } else {
                                //JOptionPane.showMessageDialog(null, "No puedes posicionar aquí");
                            }
                        }
                    }
                }
            }
            if (portaaviones) { // portaaviones
                if (verificarAgua(X, Y)) {
                    if (verificarPortaaviones(X, Y)) {
                        if (verificarAgua(X, Y) && posicionando == 0) {
                            anterior[0] = X;
                            anterior[1] = Y;
                            tableroPpal[X][Y] = 5;
                            barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                            posicionando = 1;

                        }
                        if (verificarAgua(X, Y) && posicionando == 1) {
                            if (verificarPortaaviones2(X, Y)) {

                                posiblesP.clear();

                                if (anterior[0] + 1 == X) {
                                    posiblesP.add(Integer.parseInt(String.valueOf(X + 1) + String.valueOf(Y)));
                                    posiblesP.add(Integer.parseInt(String.valueOf(X + 2) + String.valueOf(Y)));
                                }
                                if (anterior[0] - 1 == X) {
                                    posiblesP.add(Integer.parseInt(String.valueOf(X - 1) + String.valueOf(Y)));
                                    posiblesP.add(Integer.parseInt(String.valueOf(X - 2) + String.valueOf(Y)));
                                }
                                if (anterior[1] + 1 == Y) {
                                    posiblesP.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y + 1)));
                                    posiblesP.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y + 2)));
                                }
                                if (anterior[1] - 1 == Y) {
                                    posiblesP.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y - 1)));
                                    posiblesP.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y - 2)));
                                }
                                tableroPpal[X][Y] = 5;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                                posicionando = 2;
                                anterior[2] = X;
                                anterior[3] = Y;
                            } else {
                                //JOptionPane.showMessageDialog(null, "No puedes posicionar aquí");
                            }
                        }
                        if (verificarAgua(X, Y) && posicionando == 2) {
                            if (verificarPortaaviones2(X, Y)) {
                                tableroPpal[X][Y] = 5;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                                posicionando = 3;
                            } else {
                                JOptionPane.showMessageDialog(null, "No puedes posicionar aquí");
                            }
                        }
                        if (verificarAgua(X, Y) && posicionando == 3) {
                            if (verificarPortaaviones2(X, Y)) {
                                tableroPpal[X][Y] = 5;
                                posicionando = 0;
                                portaavionesF--;
                                if (portaavionesF == 0) {
                                    portaaviones = false;
                                }
                                //posiblesP.clear();
                                etapaPosicion = false;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                            } else {
                                //JOptionPane.showMessageDialog(null, "No puedes posicionar aquí");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * verify if the selected position is a empty position to place a ship (Water)
     */
    public boolean verificarAgua(int X, int Y) {
        if (tableroPpal[X][Y] == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * verify which ship is being positioned
     */
    public void verificarBarco() {
        if (fragatasF == 0) {
            fragatas = false;
            destructores = true;
        }
        if (destructoresF == 0) {
            destructores = false;
            submarinos = true;
        }
        if (submarinosF == 0) {
            submarinos = false;
            portaaviones = true;
        }
        if (portaavionesF == 0) {
            portaaviones = false;
        }
    }


    /**
     * Verify possibilities to put the second part of the ship(destructores)
     */
    public boolean verificarDestructor(int X, int Y) { // 6 y 4
        if (posiblesD.isEmpty() == true) {
            if (((X + 1) <= 9)) {
                if (tableroPpal[X + 1][Y] == 1) {
                    posiblesD.add(((X + 1) * 10) + Y);
                }
            }
            if (((X - 1) >= 0)) {
                if (tableroPpal[X - 1][Y] == 1) {
                    posiblesD.add(((X - 1) * 10) + Y);
                }
            }
            if (((Y + 1) <= 9)) {
                if (tableroPpal[X][Y + 1] == 1) {
                    posiblesD.add(((X) * 10) + (Y + 1));
                }
            }
            if (((Y - 1) >= 0)) {
                if (tableroPpal[X][Y - 1] == 1) {
                    posiblesD.add(((X) * 10) + (Y - 1));
                }
            }
        }
        return !posiblesD.isEmpty();
    }

    /**
     * Verify possibilities to put the second and third part of the ship(submarinos)
     */
    public boolean verificarSubmarino(int X, int Y) {
        if (posiblesS.isEmpty() == true) {
            if (((X + 1) <= 9) && ((X + 2) <= 9)) {
                if (tableroPpal[X + 1][Y] == 1 && tableroPpal[X + 2][Y] == 1) {
                    posiblesS.add(((X + 1) * 10) + Y);
                    posiblesS.add(((X + 2) * 10) + Y);
                }
            }
            if (((X - 1) >= 0 && ((X - 2) >= 0))) {
                if (tableroPpal[X - 1][Y] == 1 && tableroPpal[X - 2][Y] == 1) {
                    posiblesS.add(((X - 1) * 10) + Y);
                    posiblesS.add(((X - 2) * 10) + Y);
                }
            }
            if (((Y + 1) <= 9 && (Y + 2) <= 9)) {
                if (tableroPpal[X][Y + 1] == 1 && tableroPpal[X][Y + 2] == 1) {
                    posiblesS.add(((X) * 10) + (Y + 1));
                    posiblesS.add(((X) * 10) + (Y + 2));
                }
            }
            if (((Y - 1) >= 0 && (Y - 2) >= 0)) {
                if (tableroPpal[X][Y - 1] == 1 && tableroPpal[X][Y - 2] == 1) {
                    posiblesS.add(((X) * 10) + (Y - 1));
                    posiblesS.add(((X) * 10) + (Y - 2));
                }
            }
        }
        return !posiblesS.isEmpty();
    }

    /**
     * Verify possibilities to put the second,third and four part of the ship(portaaviones)
     */
    public boolean verificarPortaaviones(int X, int Y) { // 6 y 4
        if (posiblesP.isEmpty() == true) {
            if ((X + 1) <= 9 && (X + 2) <= 9 && (X + 3) <= 9) {
                if (tableroPpal[X + 1][Y] == 1 && tableroPpal[X + 2][Y] == 1 && tableroPpal[X + 3][Y] == 1) {
                    posiblesP.add(((X + 1) * 10) + Y);
                }
            }
            if ((X - 1) >= 0 && (X - 2) >= 0 && (X - 3) >= 0) {
                if (tableroPpal[X - 1][Y] == 1 && tableroPpal[X - 2][Y] == 1 && tableroPpal[X - 3][Y] == 1) {
                    posiblesP.add(((X - 1) * 10) + Y);
                }
            }
            if ((Y + 1) <= 9 && (Y + 2) <= 9 && (Y + 3) <= 9) {
                if (tableroPpal[X][Y + 1] == 1 && tableroPpal[X][Y + 2] == 1 && tableroPpal[X][Y + 3] == 1) {
                    posiblesP.add(((X) * 10) + (Y + 1));
                }
            }
            if ((Y - 1) >= 0 && (Y - 2) >= 0 && (Y - 3) >= 0) {
                if (tableroPpal[X][Y - 1] == 1 && tableroPpal[X][Y - 2] == 1 && tableroPpal[X][Y - 3] == 1) {
                    posiblesP.add(((X) * 10) + (Y - 1));
                }
            }
        }
        return !posiblesP.isEmpty();
    }

    /**
     * Verify possibilities to put the part of the ship(destructor)
     */
    public boolean verificarDestructor2(int X, int Y) {
        int indice = posiblesD.indexOf(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
        if (indice != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verify possibilities to put the part of the ship(submarino)
     */
    public boolean verificarSubmarino2(int X, int Y) {
        int indice = posiblesS.indexOf(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
        if (indice != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verify possibilities to put the part of the ship(destructor)
     */
    public boolean verificarPortaaviones2(int X, int Y) {
        int indice = posiblesP.indexOf(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
        if (indice != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Change the values of the matrix to makes us know if the ship has been shot
     */
    public void atacar(int X, int Y) {
        verificarHundido(X, Y);
        int numero = Integer.parseInt(String.valueOf(X) + String.valueOf(Y));
        if (tableroPpal[X][Y] == 1) {
            tableroPpal[X][Y] = 15;
        }
        if (tableroPpal[X][Y] == 3) {
            tableroPpal[X][Y] = 35;
        }
        if (tableroPpal[X][Y] == 2) {
            tableroPpal[X][Y] = 125;
        }
        if (tableroPpal[X][Y] == 4) {
            tableroPpal[X][Y] = 45;
        }
        if (tableroPpal[X][Y] == 5) {
            tableroPpal[X][Y] = 55;
        }
        verificarHundido(X, Y);
    }

    /**
     * Change the values of the matrix to makes us know if the ship has been sunken
     */
    public void verificarHundido(int X, int Y) {
        int numero = Integer.parseInt(String.valueOf(X) + String.valueOf(Y));
        int posicion = barcosEnUso.indexOf(numero);

        if (posicion == 0) {
            int compañero = barcosEnUso.get(1);

            if (compañero < 10) {
                int equis = 0;
                int ye = compañero;

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            } else {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                int equis = Integer.parseInt(digitos[0]);
                int ye = Integer.parseInt(digitos[1]);

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            }
        }
        if (posicion == 1) {
            int compañero = barcosEnUso.get(0);

            if (compañero < 10) {
                int equis = 0;
                int ye = compañero;

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            } else {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                int equis = Integer.parseInt(digitos[0]);
                int ye = Integer.parseInt(digitos[1]);

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            }
        }
        if (posicion == 2) {
            int compañero = barcosEnUso.get(3);

            if (compañero < 10) {
                int equis = 0;
                int ye = compañero;

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            } else {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                int equis = Integer.parseInt(digitos[0]);
                int ye = Integer.parseInt(digitos[1]);

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            }
        }
        if (posicion == 3) {
            int compañero = barcosEnUso.get(2);
            if (compañero < 10) {
                int equis = 0;
                int ye = compañero;

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            } else {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                int equis = Integer.parseInt(digitos[0]);
                int ye = Integer.parseInt(digitos[1]);

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            }
        }
        if (posicion == 4) {
            int compañero = barcosEnUso.get(5);

            if (compañero < 10) {
                int equis = 0;
                int ye = compañero;

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            } else {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                int equis = Integer.parseInt(digitos[0]);
                int ye = Integer.parseInt(digitos[1]);

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            }
        }
        if (posicion == 5) {
            int compañero = barcosEnUso.get(4);

            if (compañero < 10) {
                int equis = 0;
                int ye = compañero;

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            } else {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                int equis = Integer.parseInt(digitos[0]);
                int ye = Integer.parseInt(digitos[1]);

                if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100) {
                    tableroPpal[X][Y] = 100 + numero;
                    tableroPpal[equis][ye] = 100 + compañero;
                }
            }
        }
        if (posicion == 6) {

            /**PRUEBA**/

            int compañero = barcosEnUso.get(7);
            int compañero1 = barcosEnUso.get(8);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compañero < 10) {
                equis = 0;
                ye = compañero;
            }
            if (compañero1 < 10) {
                equis1 = 0;
                ye1 = compañero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compañero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100 &&
                    tableroPpal[equis1][ye1] > 10 && tableroPpal[equis1][ye1] < 100) {
                tableroPpal[X][Y] = 100 + numero;
                tableroPpal[equis][ye] = 100 + compañero;
                tableroPpal[equis1][ye1] = 100 + compañero1;
            }
        }
        if (posicion == 7) {
            int compañero = barcosEnUso.get(6);
            int compañero1 = barcosEnUso.get(8);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compañero < 10) {
                equis = 0;
                ye = compañero;
            }
            if (compañero1 < 10) {
                equis1 = 0;
                ye1 = compañero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compañero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100 &&
                    tableroPpal[equis1][ye1] > 10 && tableroPpal[equis1][ye1] < 100) {
                tableroPpal[X][Y] = 100 + numero;
                tableroPpal[equis][ye] = 100 + compañero;
                tableroPpal[equis1][ye1] = 100 + compañero1;
            }


        }
        if (posicion == 8) {
            int compañero = barcosEnUso.get(7);
            int compañero1 = barcosEnUso.get(6);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compañero < 10) {
                equis = 0;
                ye = compañero;
            }
            if (compañero1 < 10) {
                equis1 = 0;
                ye1 = compañero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compañero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100 &&
                    tableroPpal[equis1][ye1] > 10 && tableroPpal[equis1][ye1] < 100) {
                tableroPpal[X][Y] = 100 + numero;
                tableroPpal[equis][ye] = 100 + compañero;
                tableroPpal[equis1][ye1] = 100 + compañero1;
            }
        }
        if (posicion == 9) {
            int compañero = barcosEnUso.get(10);
            int compañero1 = barcosEnUso.get(11);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compañero < 10) {
                equis = 0;
                ye = compañero;
            }
            if (compañero1 < 10) {
                equis1 = 0;
                ye1 = compañero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compañero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100 &&
                    tableroPpal[equis1][ye1] > 10 && tableroPpal[equis1][ye1] < 100) {
                tableroPpal[X][Y] = 100 + numero;
                tableroPpal[equis][ye] = 100 + compañero;
                tableroPpal[equis1][ye1] = 100 + compañero1;
            }
        }
        if (posicion == 10) {
            int compañero = barcosEnUso.get(9);
            int compañero1 = barcosEnUso.get(11);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compañero < 10) {
                equis = 0;
                ye = compañero;
            }
            if (compañero1 < 10) {
                equis1 = 0;
                ye1 = compañero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compañero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100 &&
                    tableroPpal[equis1][ye1] > 10 && tableroPpal[equis1][ye1] < 100) {
                tableroPpal[X][Y] = 100 + numero;
                tableroPpal[equis][ye] = 100 + compañero;
                tableroPpal[equis1][ye1] = 100 + compañero1;
            }
        }
        if (posicion == 11) {
            int compañero = barcosEnUso.get(9);
            int compañero1 = barcosEnUso.get(10);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compañero < 10) {
                equis = 0;
                ye = compañero;
            }
            if (compañero1 < 10) {
                equis1 = 0;
                ye1 = compañero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compañero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100 &&
                    tableroPpal[equis1][ye1] > 10 && tableroPpal[equis1][ye1] < 100) {
                tableroPpal[X][Y] = 100 + numero;
                tableroPpal[equis][ye] = 100 + compañero;
                tableroPpal[equis1][ye1] = 100 + compañero1;
            }
        }
        if (posicion == 12) {
            int compañero = barcosEnUso.get(13);
            int compañero1 = barcosEnUso.get(14);
            int compañero2 = barcosEnUso.get(15);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;
            int equis2 = 12;
            int ye2 = 12;


            if (compañero < 10) {
                equis = 0;
                ye = compañero;
            }
            if (compañero1 < 10) {
                equis1 = 0;
                ye1 = compañero1;
            }
            if (compañero2 < 10) {
                equis2 = 0;
                ye2 = compañero2;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compañero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }
            if (equis2 == 12 && ye2 == 12) {

                String number2 = String.valueOf(compañero2);
                String[] digitos2 = number2.split("(?<=.)");

                equis2 = Integer.parseInt(digitos2[0]);
                ye2 = Integer.parseInt(digitos2[1]);
            }


            if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100 &&
                    tableroPpal[equis1][ye1] > 10 && tableroPpal[equis1][ye1] < 100 &&
                    tableroPpal[equis2][ye2] > 10 && tableroPpal[equis2][ye2] < 100) {
                tableroPpal[X][Y] = 100 + numero;
                tableroPpal[equis][ye] = 100 + compañero;
                tableroPpal[equis1][ye1] = 100 + compañero1;
                tableroPpal[equis2][ye2] = 100 + compañero2;
            }
        }
        if (posicion == 13) {
            int compañero = barcosEnUso.get(12);
            int compañero1 = barcosEnUso.get(14);
            int compañero2 = barcosEnUso.get(15);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;
            int equis2 = 12;
            int ye2 = 12;


            if (compañero < 10) {
                equis = 0;
                ye = compañero;
            }
            if (compañero1 < 10) {
                equis1 = 0;
                ye1 = compañero1;
            }
            if (compañero2 < 10) {
                equis2 = 0;
                ye2 = compañero2;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compañero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }
            if (equis2 == 12 && ye2 == 12) {

                String number2 = String.valueOf(compañero2);
                String[] digitos2 = number2.split("(?<=.)");

                equis2 = Integer.parseInt(digitos2[0]);
                ye2 = Integer.parseInt(digitos2[1]);
            }


            if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100 &&
                    tableroPpal[equis1][ye1] > 10 && tableroPpal[equis1][ye1] < 100 &&
                    tableroPpal[equis2][ye2] > 10 && tableroPpal[equis2][ye2] < 100) {
                tableroPpal[X][Y] = 100 + numero;
                tableroPpal[equis][ye] = 100 + compañero;
                tableroPpal[equis1][ye1] = 100 + compañero1;
                tableroPpal[equis2][ye2] = 100 + compañero2;
            }

        }
        if (posicion == 14) {
            int compañero = barcosEnUso.get(13);
            int compañero1 = barcosEnUso.get(12);
            int compañero2 = barcosEnUso.get(15);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;
            int equis2 = 12;
            int ye2 = 12;


            if (compañero < 10) {
                equis = 0;
                ye = compañero;
            }
            if (compañero1 < 10) {
                equis1 = 0;
                ye1 = compañero1;
            }
            if (compañero2 < 10) {
                equis2 = 0;
                ye2 = compañero2;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compañero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }
            if (equis2 == 12 && ye2 == 12) {

                String number2 = String.valueOf(compañero2);
                String[] digitos2 = number2.split("(?<=.)");

                equis2 = Integer.parseInt(digitos2[0]);
                ye2 = Integer.parseInt(digitos2[1]);
            }


            if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100 &&
                    tableroPpal[equis1][ye1] > 10 && tableroPpal[equis1][ye1] < 100 &&
                    tableroPpal[equis2][ye2] > 10 && tableroPpal[equis2][ye2] < 100) {
                tableroPpal[X][Y] = 100 + numero;
                tableroPpal[equis][ye] = 100 + compañero;
                tableroPpal[equis1][ye1] = 100 + compañero1;
                tableroPpal[equis2][ye2] = 100 + compañero2;
            }
        }
        if (posicion == 15) {
            int compañero = barcosEnUso.get(13);
            int compañero1 = barcosEnUso.get(14);
            int compañero2 = barcosEnUso.get(12);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;
            int equis2 = 12;
            int ye2 = 12;


            if (compañero < 10) {
                equis = 0;
                ye = compañero;
            }
            if (compañero1 < 10) {
                equis1 = 0;
                ye1 = compañero1;
            }
            if (compañero2 < 10) {
                equis2 = 0;
                ye2 = compañero2;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compañero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compañero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }
            if (equis2 == 12 && ye2 == 12) {

                String number2 = String.valueOf(compañero2);
                String[] digitos2 = number2.split("(?<=.)");

                equis2 = Integer.parseInt(digitos2[0]);
                ye2 = Integer.parseInt(digitos2[1]);
            }


            if (tableroPpal[X][Y] > 10 && tableroPpal[X][Y] < 100 && tableroPpal[equis][ye] > 10 && tableroPpal[equis][ye] < 100 &&
                    tableroPpal[equis1][ye1] > 10 && tableroPpal[equis1][ye1] < 100 &&
                    tableroPpal[equis2][ye2] > 10 && tableroPpal[equis2][ye2] < 100) {
                tableroPpal[X][Y] = 100 + numero;
                tableroPpal[equis][ye] = 100 + compañero;
                tableroPpal[equis1][ye1] = 100 + compañero1;
                tableroPpal[equis2][ye2] = 100 + compañero2;
            }
        }
    }

    /**
     * Check how many ships has been sunken
     */
    public void verDestruidos() {
        destruidos = 0;
        for (int i = 0; i < tableroPpal.length; i++) {
            for (int j = 0; j < tableroPpal[0].length; j++) {
                if (tableroPpal[i][j] > 99) {
                    destruidos++;
                }
            }
        }
    }

    /**
     * Show us if the player has won
     */
    public void ganadorJugador() {
        if (destruidos == 20) {
            JOptionPane.showMessageDialog(null, "Ganaste");
        }
    }

    /**
     * Getters
     */
    public int[][] getTableroPpal() {
        return tableroPpal;
    }
}