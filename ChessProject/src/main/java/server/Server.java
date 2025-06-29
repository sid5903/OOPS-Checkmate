/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*Shivansh pull request again*/
package server;

import Messages.Message;
import Messages.GameState;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Enes Kızılcın <nazifenes.kizilcin@stu.fsm.edu.tr>
 */

//This is a TCP protocol connection based server.
public class Server {

    public ServerSocket socket;
    public int port;
    public ListenConnectionRequestThread listenConnectionRequestThread;
    public ClientRemovingControlThread removingControlThread;
    public static ArrayList<SClient> clients;
    
    // Storage for saved games
    public static HashMap<String, GameState> savedGames = new HashMap<>();

    //lock mechanism for pairing thread. One client can match with one client at the same time. So we use the lock mechanism to provide
    //other clients not try to pair this client at the same time.
    public static Semaphore pairingLockForTwoPair = new Semaphore(1, true);

    public Server(int port) {
        try {
            this.port = port;
            this.socket = new ServerSocket(this.port);
            this.listenConnectionRequestThread = new ListenConnectionRequestThread(this);
            removingControlThread = new ClientRemovingControlThread(this);
            this.clients = new ArrayList<SClient>();
            
        } catch (IOException ex) {
            System.out.println("There is an error occured when opening the server on port:" + this.port);
        }
    }

    // starts the acceptance
    public void ListenClientConnectionRequests() {
        this.listenConnectionRequestThread.start();
        this.removingControlThread.start(); // Start the removing control thread
    }
    
    // Method to save a game
    public static void saveGame(GameState gameState) {
        savedGames.put(gameState.getSaveName(), gameState);
        System.out.println("Game saved: " + gameState.getSaveName());
    }
    
    // Method to get a saved game
    public static GameState getSavedGame(String saveName) {
        return savedGames.get(saveName);
    }
    
    // Method to get all saved game names
    public static ArrayList<String> getSavedGameNames() {
        return new ArrayList<>(savedGames.keySet());
    }

    public static void SendMessage(SClient client, Message message) {
        try {
            if (client != null && !client.socket.isClosed()) {
                client.cOutput.writeObject(message);
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            // Remove client from list if send fails
            clients.remove(client);
        }
    }

    public static void SendMessage(SClient client, String message) {
        try {
            if (client != null && !client.socket.isClosed()) {
                client.cOutput.writeObject(message);
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            // Remove client from list if send fails
            clients.remove(client);
        }
    }
    
    public static void SendMessage(SClient client, Object object) {
        try {
            if (client != null && !client.socket.isClosed()) {
                client.cOutput.writeObject(object);
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            // Remove client from list if send fails
            clients.remove(client);
        }
    }
}
