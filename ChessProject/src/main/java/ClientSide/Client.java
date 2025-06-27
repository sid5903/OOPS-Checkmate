package ClientSide;

import Messages.Message;
import chess_game.Pieces.Team;
import chess_game.gui.Table;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    
    public Socket socket;
    public ObjectInputStream sInput;
    public ObjectOutputStream sOutput;
    public boolean isPaired;
    public Table game;
    public ClientListenThread clientListenThread;
    private Team team;
    private String clientId;
    
    public Client(Table game) {
        this.game = game;
        this.isPaired = false;
        this.clientId = UUID.randomUUID().toString(); // Generate unique ID
    }
    
    public void Connect(String serverIP, int serverPort) {
        try {
            this.socket = new Socket(serverIP, serverPort);
            this.sOutput = new ObjectOutputStream(this.socket.getOutputStream());
            this.sInput = new ObjectInputStream(this.socket.getInputStream());
            this.clientListenThread = new ClientListenThread(this);
            this.clientListenThread.start();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            this.socket = null;
        }
    }
    
    public void Send(Message message) {
        try {
            this.sOutput.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Team getTeam() {
        return team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }
    
    public String getClientId() {
        return clientId;
    }
}
