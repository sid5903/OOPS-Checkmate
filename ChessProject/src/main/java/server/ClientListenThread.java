package server;

import Messages.ChatMessage;
import Messages.Message;
import Messages.PlayerInfo;
import Messages.PlayRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientListenThread extends Thread {

    SClient client;

    public ClientListenThread(SClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (!this.client.socket.isClosed()) {
            try {
                Message msg = (Message) (this.client.cInput.readObject());
                switch (msg.type) {
                    case PAIRING:
                        if (msg.content instanceof String) {
                            this.client.setPlayerName((String) msg.content);
                        }
                        this.client.isWantToPair = true;
                        if (!this.client.pairingThread.isAlive()) {
                            this.client.pairingThread = new ClientPairingThread(this.client);
                            this.client.pairingThread.start();
                        }
                        break;
                        
                    case PLAYER_LIST:
                        if (msg.content instanceof String) {
                            this.client.setPlayerName((String) msg.content);
                        }
                        this.client.isInPlayerSelection = true;
                        sendPlayerList();
                        break;
                        
                    case PLAY_REQUEST:
                        PlayRequest request = (PlayRequest) msg.content;
                        handlePlayRequest(request);
                        break;
                        
                    case PLAY_RESPONSE:
                        PlayRequest response = (PlayRequest) msg.content;
                        handlePlayResponse(response);
                        break;
                        
                    case CHAT:
                        if (this.client.isPaired && this.client.pair != null) {
                            ChatMessage chatMsg = (ChatMessage) msg.content;
                            Message forwardMsg = new Message(Message.MessageTypes.CHAT);
                            forwardMsg.content = chatMsg;
                            this.client.pair.Send(forwardMsg);
                        }
                        break;
                        
                    case MOVE:
                        if (this.client.isPaired && this.client.pair != null) {
                            this.client.pair.Send(msg);
                        }
                        break;
                        
                    case CHECK:
                        if (this.client.isPaired && this.client.pair != null) {
                            this.client.pair.Send(msg);
                        }
                        break;
                        
                    case SAVE_GAME:
                        if (this.client.isPaired && this.client.pair != null) {
                            // Forward the save request to the paired client
                            this.client.pair.Send(msg);
                        }
                        // Store the game state on the server
                        if (msg.content instanceof Messages.GameState) {
                            Messages.GameState gameState = (Messages.GameState) msg.content;
                            Server.saveGame(gameState);
                        }
                        break;
                        
                    case LOAD_GAME:
                        // Handle load game request
                        if (msg.content instanceof String) {
                            String saveName = (String) msg.content;
                            Messages.GameState loadedGame = Server.getSavedGame(saveName);
                            if (loadedGame != null) {
                                // Send the loaded game back to the client
                                Message loadResponse = new Message(Message.MessageTypes.LOAD_GAME);
                                loadResponse.content = loadedGame;
                                this.client.Send(loadResponse);
                                
                                // If client is paired, also send to the paired client
                                if (this.client.isPaired && this.client.pair != null) {
                                    this.client.pair.Send(loadResponse);
                                }
                            } else {
                                // Send error message if game not found
                                Message errorMsg = new Message(Message.MessageTypes.LOAD_GAME);
                                errorMsg.content = "Game not found: " + saveName;
                                this.client.Send(errorMsg);
                            }
                        }
                        break;
                        
                    case GET_SAVED_GAMES:
                        // Send list of saved games to client
                        Message savedGamesMsg = new Message(Message.MessageTypes.GET_SAVED_GAMES);
                        savedGamesMsg.content = Server.getSavedGameNames();
                        this.client.Send(savedGamesMsg);
                        break;
                        
                    case LOAD_GAME_WITH_PAIRING:
                        // Handle load game with pairing request
                        if (msg.content instanceof String) {
                            String saveName = (String) msg.content;
                            Messages.GameState loadedGame = Server.getSavedGame(saveName);
                            if (loadedGame != null) {
                                // Set this client to want to pair for loading
                                this.client.isWantToPair = true;
                                this.client.setPlayerName(loadedGame.getPlayer1Name());
                                
                                // Store the game name to load after pairing
                                this.client.loadGameName = saveName;
                                
                                // Start pairing thread if not already running
                                if (!this.client.pairingThread.isAlive()) {
                                    this.client.pairingThread = new ClientPairingThread(this.client);
                                    this.client.pairingThread.start();
                                }
                                
                                // Don't send confirmation message - user already saw instructions
                                System.out.println("Client " + this.client.getPlayerName() + " waiting to pair for game: " + saveName);
                            } else {
                                // Send error message if game not found
                                Message errorMsg = new Message(Message.MessageTypes.LOAD_GAME_WITH_PAIRING);
                                errorMsg.content = "Game not found: " + saveName;
                                this.client.Send(errorMsg);
                            }
                        }
                        break;
                        
                    case END:
                        this.client.isPaired = false;
                        this.client.isWantToPair = false;
                        if (this.client.pair != null) {
                            Message endMsg = new Message(Message.MessageTypes.END);
                            this.client.pair.Send(endMsg);
                            this.client.pair.isPaired = false;
                            this.client.pair.pair = null;
                        }
                        this.client.pair = null;
                        break;

                    case LEAVE:
                        this.client.isPaired = false;
                        this.client.isWantToPair = false;
                        this.client.isInPlayerSelection = false;
                        if (this.client.pair != null) {
                            Message leaveMsg = new Message(Message.MessageTypes.LEAVE);
                            this.client.pair.Send(leaveMsg);
                            this.client.pair.isWantToPair = false;
                            this.client.pair.isPaired = false;
                            this.client.pair.pair = null;
                        }
                        this.client.pair = null;
                        break;
                        
                    default:
                        System.out.println("Unknown message type received: " + msg.type);
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientListenThread.class.getName()).log(Level.SEVERE, null, ex);
                handleClientDisconnection();
                break;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientListenThread.class.getName()).log(Level.SEVERE, null, ex);
                break;
            } catch (Exception ex) {
                System.out.println("Unexpected error in server ClientListenThread: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
    
    private void sendPlayerList() {
        ArrayList<PlayerInfo> availablePlayers = new ArrayList<>();
        synchronized (Server.clients) {
            for (SClient client : Server.clients) {
                if (client != this.client && !client.isPaired && 
                    (client.isInPlayerSelection || client.isWantToPair)) {
                    PlayerInfo playerInfo = new PlayerInfo(client.getPlayerName(), client.getClientId());
                    availablePlayers.add(playerInfo);
                }
            }
        }
        
        Message response = new Message(Message.MessageTypes.PLAYER_LIST);
        response.content = availablePlayers;
        this.client.Send(response);
    }
    
    private void handlePlayRequest(PlayRequest request) {
        // Find the target client
        SClient targetClient = null;
        synchronized (Server.clients) {
            for (SClient client : Server.clients) {
                if (client.getClientId().equals(request.toPlayerId)) {
                    targetClient = client;
                    break;
                }
            }
        }
        
        if (targetClient != null && !targetClient.isPaired) {
            // Forward the request to target client
            Message msg = new Message(Message.MessageTypes.PLAY_REQUEST);
            msg.content = request;
            targetClient.Send(msg);
        } else {
            // Send denial message
            Message denial = new Message(Message.MessageTypes.REQUEST_DENIED);
            denial.content = "Player is no longer available.";
            this.client.Send(denial);
        }
    }
    
    private void handlePlayResponse(PlayRequest response) {
        // Find the requester client
        SClient requesterClient = null;
        synchronized (Server.clients) {
            for (SClient client : Server.clients) {
                if (client.getClientId().equals(response.fromPlayerId)) {
                    requesterClient = client;
                    break;
                }
            }
        }
        
        if (requesterClient != null) {
            if (response.isAccepted) {
                // Pair the clients
                this.client.pair = requesterClient;
                requesterClient.pair = this.client;
                this.client.isPaired = true;
                requesterClient.isPaired = true;
                this.client.isInPlayerSelection = false;
                requesterClient.isInPlayerSelection = false;
                
                // Send start messages with teams
                Message clientStartMessage = new Message(Message.MessageTypes.START);
                clientStartMessage.content = chess_game.Pieces.Team.WHITE;
                Message pairClientStartMessage = new Message(Message.MessageTypes.START);
                pairClientStartMessage.content = chess_game.Pieces.Team.BLACK;
                
                requesterClient.Send(clientStartMessage);
                this.client.Send(pairClientStartMessage);
                
                System.out.println("Custom paired: " + requesterClient.getPlayerName() + " (WHITE) vs " + this.client.getPlayerName() + " (BLACK)");
            }
            
            // Send response back to requester
            Message msg = new Message(Message.MessageTypes.PLAY_RESPONSE);
            msg.content = response;
            requesterClient.Send(msg);
        }
    }
    
    private void handleClientDisconnection() {
        this.client.isPaired = false;
        this.client.isWantToPair = false;
        this.client.isInPlayerSelection = false;
        if (this.client.pair != null) {
            Message leaveMsg = new Message(Message.MessageTypes.LEAVE);
            this.client.pair.Send(leaveMsg);
            this.client.pair.isPaired = false;
            this.client.pair.pair = null;
        }
        this.client.pair = null;
        Server.clients.remove(this.client);
    }
}
