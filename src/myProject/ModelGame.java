package myProject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;

/**
 * This class is used for the functional part of the user
 * @version v.1.0.0 date:21/03/2022
 * @autor Juan Esteban Brand Tovar - Jose Miguel Becerra Casierra - Juan Pablo Pantoja Guitierrez
 */

public class ModelGame {

    int[][] tableroPos = new int[10][10];
    int fragatasF = 4, destructoresF = 3, submarinosF = 2, portaavionesF = 1, posicionando = 0, destruidos = 0;
    boolean fragatas = true, destructores = false, submarinos = false, portaaviones = false, etapaPosicion = true;
    int[] anterior = new int[4];
    String matrizFinal = "";
    ArrayList<Integer> posiblesD = new ArrayList<>();
    ArrayList<Integer> posiblesS = new ArrayList<>();
    ArrayList<Integer> posiblesP = new ArrayList<>();
    ArrayList<Integer> barcosEnUso = new ArrayList<>();
    ArrayList<Integer> ataquesPosibles = new ArrayList<>();


    /**
     * Initialize ataquesPosibles
     */

    public void llenarAtaque() {
        for (int i = 0; i <= 99; i++) {
            ataquesPosibles.add(i);
        }
    }

    /**
     * Select a random value for the movement of the opponet
     */
    public int seleccionarAtaque() {
        Random aleatorio = new Random();

        int numero = aleatorio.nextInt(ataquesPosibles.toArray().length);
        int eleccion = ataquesPosibles.get(numero);
        ataquesPosibles.remove(numero);

        return eleccion;
    }


    /**
     * Function that uses the seleccionarAtaque value to attack
     */
    public void ataqueCPU() {
        int eleccion = seleccionarAtaque();

        if (eleccion <= 9) {
            int equis = 0;
            int ye = eleccion;

            atacar(equis, ye);
        } else {
            String number = String.valueOf(eleccion);
            String[] digitos = number.split("(?<=.)");

            int equis = Integer.parseInt(digitos[0]);
            int ye = Integer.parseInt(digitos[1]);
            atacar(equis, ye);
        }
    }

    /**
     * Initialize tableroPos values to 1(Water)
     */
    public void tablerosIniciales() {
        for (int i = 0; i < tableroPos.length; i++) {
            for (int j = 0; j < tableroPos[0].length; j++) {
                tableroPos[i][j] = 1;
            }
        }
    }


    /**
     * Shows the ally territory as a matrix
     */
    public void imprimir() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matrizFinal += tableroPos[i][j];
                matrizFinal += "    ";
            }
            matrizFinal += "\n";
        }
        JOptionPane.showMessageDialog(null, matrizFinal);
        matrizFinal = "";
    }

    /**
     * Function that puts the ships on the ally territory
     */
    public void posicionar(int X, int Y) {
        if (etapaPosicion) {
            verificarBarco();
            if (fragatas) {
                if (verificarAgua(X, Y)) {
                    tableroPos[X][Y] = 2;
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
                            tableroPos[X][Y] = 3;
                            barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                            anterior[0] = X;
                            anterior[1] = Y;
                            posicionando = 1;
                        }
                        if (verificarAgua(X, Y) && posicionando == 1) {
                            if (verificarDestructor2(X, Y)) {
                                tableroPos[X][Y] = 3;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                                posicionando = 0;
                                destructoresF--;
                                if (destructoresF == 0) {
                                    destructores = false;
                                    submarinos = true;
                                }
                                posiblesD.clear();
                            } else {
                                JOptionPane.showMessageDialog(null, "No puedes posicionar aqu??");
                            }
                        }
                    }
                }
            }
            if (submarinos) { // submarinos
                if (verificarAgua(X, Y)) {
                    if (verificarSubmarino(X, Y)) {
                        if (verificarAgua(X, Y) && posicionando == 0) {
                            tableroPos[X][Y] = 4;
                            barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                            anterior[0] = X;
                            anterior[1] = Y;
                            posicionando = 1;
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
                                tableroPos[X][Y] = 4;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                                posicionando = 2;
                                anterior[2] = X;
                                anterior[3] = Y;
                            } else {
                                JOptionPane.showMessageDialog(null, "No puedes posicionar aqu??");
                            }
                        }
                        if (verificarAgua(X, Y) && posicionando == 2) {
                            if (verificarSubmarino2(X, Y)) {
                                tableroPos[X][Y] = 4;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                                posicionando = 0;
                                submarinosF--;
                                if (submarinosF == 0) {
                                    submarinos = false;
                                    portaaviones = true;
                                }
                                posiblesS.clear();
                            } else {
                                JOptionPane.showMessageDialog(null, "No puedes posicionar aqu??");
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
                            tableroPos[X][Y] = 5;
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
                                tableroPos[X][Y] = 5;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                                posicionando = 2;
                                anterior[2] = X;
                                anterior[3] = Y;
                            } else {
                                JOptionPane.showMessageDialog(null, "No puedes posicionar aqu??");
                            }
                        }
                        if (verificarAgua(X, Y) && posicionando == 2) {
                            if (verificarPortaaviones2(X, Y)) {
                                tableroPos[X][Y] = 5;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                                posicionando = 3;
                            } else {
                                JOptionPane.showMessageDialog(null, "No puedes posicionar aqu??");
                            }
                        }
                        if (verificarAgua(X, Y) && posicionando == 3) {
                            if (verificarPortaaviones2(X, Y)) {
                                tableroPos[X][Y] = 5;
                                posicionando = 0;
                                portaavionesF--;
                                if (portaavionesF == 0) {
                                    portaaviones = false;
                                }
                                //posiblesP.clear();
                                etapaPosicion = false;
                                barcosEnUso.add(Integer.parseInt(String.valueOf(X) + String.valueOf(Y)));
                            } else {
                                JOptionPane.showMessageDialog(null, "No puedes posicionar aqu??");
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
        if (tableroPos[X][Y] == 1) {
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
                if (tableroPos[X + 1][Y] == 1) {
                    posiblesD.add(((X + 1) * 10) + Y);
                }
            }
            if (((X - 1) >= 0)) {
                if (tableroPos[X - 1][Y] == 1) {
                    posiblesD.add(((X - 1) * 10) + Y);
                }
            }
            if (((Y + 1) <= 9)) {
                if (tableroPos[X][Y + 1] == 1) {
                    posiblesD.add(((X) * 10) + (Y + 1));
                }
            }
            if (((Y - 1) >= 0)) {
                if (tableroPos[X][Y - 1] == 1) {
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
                if (tableroPos[X + 1][Y] == 1 && tableroPos[X + 2][Y] == 1) {
                    posiblesS.add(((X + 1) * 10) + Y);
                    posiblesS.add(((X + 2) * 10) + Y);
                }
            }
            if (((X - 1) >= 0 && ((X - 2) >= 0))) {
                if (tableroPos[X - 1][Y] == 1 && tableroPos[X - 2][Y] == 1) {
                    posiblesS.add(((X - 1) * 10) + Y);
                    posiblesS.add(((X - 2) * 10) + Y);
                }
            }
            if (((Y + 1) <= 9 && (Y + 2) <= 9)) {
                if (tableroPos[X][Y + 1] == 1 && tableroPos[X][Y + 2] == 1) {
                    posiblesS.add(((X) * 10) + (Y + 1));
                    posiblesS.add(((X) * 10) + (Y + 2));
                }
            }
            if (((Y - 1) >= 0 && (Y - 2) >= 0)) {
                if (tableroPos[X][Y - 1] == 1 && tableroPos[X][Y - 2] == 1) {
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
                if (tableroPos[X + 1][Y] == 1 && tableroPos[X + 2][Y] == 1 && tableroPos[X + 3][Y] == 1) {
                    posiblesP.add(((X + 1) * 10) + Y);
                }
            }
            if ((X - 1) >= 0 && (X - 2) >= 0 && (X - 3) >= 0) {
                if (tableroPos[X - 1][Y] == 1 && tableroPos[X - 2][Y] == 1 && tableroPos[X - 3][Y] == 1) {
                    posiblesP.add(((X - 1) * 10) + Y);
                }
            }
            if ((Y + 1) <= 9 && (Y + 2) <= 9 && (Y + 3) <= 9) {
                if (tableroPos[X][Y + 1] == 1 && tableroPos[X][Y + 2] == 1 && tableroPos[X][Y + 3] == 1) {
                    posiblesP.add(((X) * 10) + (Y + 1));
                }
            }
            if ((Y - 1) >= 0 && (Y - 2) >= 0 && (Y - 3) >= 0) {
                if (tableroPos[X][Y - 1] == 1 && tableroPos[X][Y - 2] == 1 && tableroPos[X][Y - 3] == 1) {
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
        if (tableroPos[X][Y] == 1) {
            tableroPos[X][Y] = 15;
        }
        if (tableroPos[X][Y] == 3) {
            tableroPos[X][Y] = 35;
        }
        if (tableroPos[X][Y] == 2) {
            tableroPos[X][Y] = 125;
        }
        if (tableroPos[X][Y] == 4) {
            tableroPos[X][Y] = 45;
        }
        if (tableroPos[X][Y] == 5) {
            tableroPos[X][Y] = 55;
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
            int compa??ero = barcosEnUso.get(1);

            String number = String.valueOf(compa??ero);

            String[] digitos = number.split("(?<=.)");

            int equis = Integer.parseInt(digitos[0]);
            int ye = Integer.parseInt(digitos[1]);

            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
            }
        }
        if (posicion == 1) {
            int compa??ero = barcosEnUso.get(0);

            String number = String.valueOf(compa??ero);

            String[] digitos = number.split("(?<=.)");

            int equis = Integer.parseInt(digitos[0]);
            int ye = Integer.parseInt(digitos[1]);

            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
            }
        }
        if (posicion == 2) {
            int compa??ero = barcosEnUso.get(3);

            String number = String.valueOf(compa??ero);

            String[] digitos = number.split("(?<=.)");

            int equis = Integer.parseInt(digitos[0]);
            int ye = Integer.parseInt(digitos[1]);

            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
            }
        }
        if (posicion == 3) {
            int compa??ero = barcosEnUso.get(2);

            String number = String.valueOf(compa??ero);

            String[] digitos = number.split("(?<=.)");

            int equis = Integer.parseInt(digitos[0]);
            int ye = Integer.parseInt(digitos[1]);

            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
            }
        }
        if (posicion == 4) {
            int compa??ero = barcosEnUso.get(5);

            String number = String.valueOf(compa??ero);

            String[] digitos = number.split("(?<=.)");

            int equis = Integer.parseInt(digitos[0]);
            int ye = Integer.parseInt(digitos[1]);

            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
            }
        }
        if (posicion == 5) {
            int compa??ero = barcosEnUso.get(4);

            String number = String.valueOf(compa??ero);

            String[] digitos = number.split("(?<=.)");

            int equis = Integer.parseInt(digitos[0]);
            int ye = Integer.parseInt(digitos[1]);

            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
            }
        }
        if (posicion == 6) {

            /**PRUEBA**/

            int compa??ero = barcosEnUso.get(7);
            int compa??ero1 = barcosEnUso.get(8);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compa??ero < 10) {
                equis = 0;
                ye = compa??ero;
            }
            if (compa??ero1 < 10) {
                equis1 = 0;
                ye1 = compa??ero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compa??ero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compa??ero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100 &&
                    tableroPos[equis1][ye1] > 10 && tableroPos[equis1][ye1] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
                tableroPos[equis1][ye1] = 100 + compa??ero1;
            }
        }
        if (posicion == 7) {
            int compa??ero = barcosEnUso.get(6);
            int compa??ero1 = barcosEnUso.get(8);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compa??ero < 10) {
                equis = 0;
                ye = compa??ero;
            }
            if (compa??ero1 < 10) {
                equis1 = 0;
                ye1 = compa??ero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compa??ero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compa??ero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100 &&
                    tableroPos[equis1][ye1] > 10 && tableroPos[equis1][ye1] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
                tableroPos[equis1][ye1] = 100 + compa??ero1;
            }


        }
        if (posicion == 8) {
            int compa??ero = barcosEnUso.get(7);
            int compa??ero1 = barcosEnUso.get(6);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compa??ero < 10) {
                equis = 0;
                ye = compa??ero;
            }
            if (compa??ero1 < 10) {
                equis1 = 0;
                ye1 = compa??ero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compa??ero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compa??ero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100 &&
                    tableroPos[equis1][ye1] > 10 && tableroPos[equis1][ye1] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
                tableroPos[equis1][ye1] = 100 + compa??ero1;
            }
        }
        if (posicion == 9) {
            int compa??ero = barcosEnUso.get(10);
            int compa??ero1 = barcosEnUso.get(11);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compa??ero < 10) {
                equis = 0;
                ye = compa??ero;
            }
            if (compa??ero1 < 10) {
                equis1 = 0;
                ye1 = compa??ero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compa??ero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compa??ero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100 &&
                    tableroPos[equis1][ye1] > 10 && tableroPos[equis1][ye1] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
                tableroPos[equis1][ye1] = 100 + compa??ero1;
            }
        }
        if (posicion == 10) {
            int compa??ero = barcosEnUso.get(9);
            int compa??ero1 = barcosEnUso.get(11);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compa??ero < 10) {
            }
            if (compa??ero1 < 10) {
                equis1 = 0;
                ye1 = compa??ero1;
                equis = 0;
                ye = compa??ero;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compa??ero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compa??ero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100 &&
                    tableroPos[equis1][ye1] > 10 && tableroPos[equis1][ye1] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
                tableroPos[equis1][ye1] = 100 + compa??ero1;
            }
        }
        if (posicion == 11) {
            int compa??ero = barcosEnUso.get(9);
            int compa??ero1 = barcosEnUso.get(10);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;

            if (compa??ero < 10) {
                equis = 0;
                ye = compa??ero;
            }
            if (compa??ero1 < 10) {
                equis1 = 0;
                ye1 = compa??ero1;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compa??ero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compa??ero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }


            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100 &&
                    tableroPos[equis1][ye1] > 10 && tableroPos[equis1][ye1] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
                tableroPos[equis1][ye1] = 100 + compa??ero1;
            }
        }
        if (posicion == 12) {

            int compa??ero = barcosEnUso.get(13);
            int compa??ero1 = barcosEnUso.get(14);
            int compa??ero2 = barcosEnUso.get(15);

            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;
            int equis2 = 12;
            int ye2 = 12;


            if (compa??ero < 10) {
                equis = 0;
                ye = compa??ero;
            }
            if (compa??ero1 < 10) {
                equis1 = 0;
                ye1 = compa??ero1;
            }
            if (compa??ero2 < 10) {
                equis2 = 0;
                ye2 = compa??ero2;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compa??ero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compa??ero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }
            if (equis2 == 12 && ye2 == 12) {

                String number2 = String.valueOf(compa??ero2);
                String[] digitos2 = number2.split("(?<=.)");

                equis2 = Integer.parseInt(digitos2[0]);
                ye2 = Integer.parseInt(digitos2[1]);
            }


            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100 &&
                    tableroPos[equis1][ye1] > 10 && tableroPos[equis1][ye1] < 100 &&
                    tableroPos[equis2][ye2] > 10 && tableroPos[equis2][ye2] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
                tableroPos[equis1][ye1] = 100 + compa??ero1;
                tableroPos[equis2][ye2] = 100 + compa??ero2;
            }
        }
        if (posicion == 13) {


            int compa??ero = barcosEnUso.get(12);
            int compa??ero1 = barcosEnUso.get(14);
            int compa??ero2 = barcosEnUso.get(15);


            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;
            int equis2 = 12;
            int ye2 = 12;


            if (compa??ero < 10) {
                equis = 0;
                ye = compa??ero;
            }
            if (compa??ero1 < 10) {
                equis1 = 0;
                ye1 = compa??ero1;
            }
            if (compa??ero2 < 10) {
                equis2 = 0;
                ye2 = compa??ero2;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compa??ero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compa??ero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }
            if (equis2 == 12 && ye2 == 12) {

                String number2 = String.valueOf(compa??ero2);
                String[] digitos2 = number2.split("(?<=.)");

                equis2 = Integer.parseInt(digitos2[0]);
                ye2 = Integer.parseInt(digitos2[1]);
            }


            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100 &&
                    tableroPos[equis1][ye1] > 10 && tableroPos[equis1][ye1] < 100 &&
                    tableroPos[equis2][ye2] > 10 && tableroPos[equis2][ye2] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
                tableroPos[equis1][ye1] = 100 + compa??ero1;
                tableroPos[equis2][ye2] = 100 + compa??ero2;
            }

        }
        if (posicion == 14) {
            int compa??ero = barcosEnUso.get(13);
            int compa??ero1 = barcosEnUso.get(12);
            int compa??ero2 = barcosEnUso.get(15);


            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;
            int equis2 = 12;
            int ye2 = 12;


            if (compa??ero < 10) {
                equis = 0;
                ye = compa??ero;
            }
            if (compa??ero1 < 10) {
                equis1 = 0;
                ye1 = compa??ero1;
            }
            if (compa??ero2 < 10) {
                equis2 = 0;
                ye2 = compa??ero2;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compa??ero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compa??ero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }
            if (equis2 == 12 && ye2 == 12) {

                String number2 = String.valueOf(compa??ero2);
                String[] digitos2 = number2.split("(?<=.)");

                equis2 = Integer.parseInt(digitos2[0]);
                ye2 = Integer.parseInt(digitos2[1]);
            }


            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100 &&
                    tableroPos[equis1][ye1] > 10 && tableroPos[equis1][ye1] < 100 &&
                    tableroPos[equis2][ye2] > 10 && tableroPos[equis2][ye2] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
                tableroPos[equis1][ye1] = 100 + compa??ero1;
                tableroPos[equis2][ye2] = 100 + compa??ero2;
            }
        }
        if (posicion == 15) {
            int compa??ero = barcosEnUso.get(13);
            int compa??ero1 = barcosEnUso.get(14);
            int compa??ero2 = barcosEnUso.get(12);


            int equis = 12;
            int ye = 12;
            int equis1 = 12;
            int ye1 = 12;
            int equis2 = 12;
            int ye2 = 12;


            if (compa??ero < 10) {
                equis = 0;
                ye = compa??ero;
            }
            if (compa??ero1 < 10) {
                equis1 = 0;
                ye1 = compa??ero1;
            }
            if (compa??ero2 < 10) {
                equis2 = 0;
                ye2 = compa??ero2;
            }

            if (equis == 12 && ye == 12) {
                String number = String.valueOf(compa??ero);
                String[] digitos = number.split("(?<=.)");

                equis = Integer.parseInt(digitos[0]);
                ye = Integer.parseInt(digitos[1]);

            }
            if (equis1 == 12 && ye1 == 12) {
                String number1 = String.valueOf(compa??ero1);

                String[] digitos1 = number1.split("(?<=.)");

                equis1 = Integer.parseInt(digitos1[0]);
                ye1 = Integer.parseInt(digitos1[1]);
            }
            if (equis2 == 12 && ye2 == 12) {

                String number2 = String.valueOf(compa??ero2);
                String[] digitos2 = number2.split("(?<=.)");

                equis2 = Integer.parseInt(digitos2[0]);
                ye2 = Integer.parseInt(digitos2[1]);
            }


            if (tableroPos[X][Y] > 10 && tableroPos[X][Y] < 100 && tableroPos[equis][ye] > 10 && tableroPos[equis][ye] < 100 &&
                    tableroPos[equis1][ye1] > 10 && tableroPos[equis1][ye1] < 100 &&
                    tableroPos[equis2][ye2] > 10 && tableroPos[equis2][ye2] < 100) {
                tableroPos[X][Y] = 100 + numero;
                tableroPos[equis][ye] = 100 + compa??ero;
                tableroPos[equis1][ye1] = 100 + compa??ero1;
                tableroPos[equis2][ye2] = 100 + compa??ero2;
            }
        }
    }

    /**
     * Check how many ships has been sunken
     */
    public void verDestruidos() {
        destruidos = 0;
        for (int i = 0; i < tableroPos.length; i++) {
            for (int j = 0; j < tableroPos[0].length; j++) {
                if (tableroPos[i][j] > 99) {
                    destruidos++;
                }
            }
        }
    }

    /**
     * Show us if the CPU has won
     */
    public void ganadorCPU() {
        if (destruidos == 20) {
            JOptionPane.showMessageDialog(null, "Gan?? la maquina");
        }
    }

    /**
     * Getters
     */
    public int[][] getTableroPos() {
        return tableroPos;
    }
}

