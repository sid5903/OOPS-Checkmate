package ClientSide;

import Messages.ChatMessage;
import Messages.Message;
import Messages.PlayerInfo;
import Messages.PlayRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ClientSide.*;
import Messages.MovementMessage;
import chess_game.Boards.Board;
import chess_game.Move.Move;
import chess_game.Pieces.PieceTypes;
import chess_game.Pieces.Team;
import chess_game.Player.Player;
import java.awt.Color;
import javax.swing.JOptionPane;
import Messages.GameState;

public class ClientListenThread extends Thread {

    Client client;

    public ClientListenThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (!this.client.socket.isClosed()) {
            try {
                Message msg = (Message) (this.client.sInput.readObject());
                switch (msg.type) {
                    case START:
                        Team serverChosenTeam = (Team) msg.content;
                        this.client.setTeam(serverChosenTeam);
                        break;
                        
                    case PAIRING:
                        if (msg.content instanceof String) {
                            String opponentName = (String) msg.content;
                            this.client.game.setOpponentName(opponentName);
                        }
                        this.client.isPaired = true;
                        this.client.game.getMainMenu().getPlayBTN().setEnabled(true);
                        this.client.game.getMainMenu().getPlayBTN().setText("Start Game");
                        this.client.game.getMainMenu().getInfoLBL().setText("Matched. Click To Start Game");
                        break;
                        
                    case MATCHED:
                        this.client.isPaired = true;
                        this.client.game.getMainMenu().getPlayBTN().setEnabled(true);
                        this.client.game.getMainMenu().getPlayBTN().setText("Start Game");
                        this.client.game.getMainMenu().getInfoLBL().setText("Matched. Click To Start Game");
                        break;
                        
                    case PLAYER_LIST:
                        ArrayList<PlayerInfo> players = (ArrayList<PlayerInfo>) msg.content;
                        this.client.game.updatePlayersList(players);
                        break;
                        
                    case PLAY_REQUEST:
                        PlayRequest request = (PlayRequest) msg.content;
                        this.client.game.showPlayRequest(request);
                        break;
                        
                    case PLAY_RESPONSE:
                        PlayRequest response = (PlayRequest) msg.content;
                        if (response.isAccepted) {
                            this.client.isPaired = true;
                            this.client.game.setOpponentName(response.fromPlayerName);
                            JOptionPane.showMessageDialog(null, response.fromPlayerName + " accepted your request! Starting game...");
                            this.client.game.createGamePanel();
                        } else {
                            JOptionPane.showMessageDialog(null, response.fromPlayerName + " declined your request.");
                            this.client.game.getPlayerSelectionPanel().getSendRequestBTN().setEnabled(true);
                            this.client.game.getPlayerSelectionPanel().getStatusLBL().setText("Request declined. Try another player.");
                        }
                        break;
                        
                    case REQUEST_DENIED:
                        String requestMessage = (String) msg.content;  // Changed variable name
                        JOptionPane.showMessageDialog(null, requestMessage);
                        this.client.game.getPlayerSelectionPanel().getSendRequestBTN().setEnabled(true);
                        break;
                        
                    case CHAT:
                        ChatMessage chatMessage = (ChatMessage) msg.content;
                        String displayMessage = chatMessage.playerName + ": " + chatMessage.message;
                        if (this.client.game.getBottomGameMenu() != null) {
                            this.client.game.getBottomGameMenu().addChatMessage(displayMessage);
                        }
                        break;
                        
                    case MOVE:
                        MovementMessage movement = (MovementMessage) msg.content;
                        Board board = this.client.game.getChessBoard();
                        Player player = board.getCurrentPlayer();
                        Move move;
                        if (movement.isCastling) {
                            move = new Move(
                                board,
                                board.getTile(movement.currentCoordinate),
                                board.getTile(movement.destinationCoordinate),
                                board.getTile(movement.rookStartCoordinate),
                                board.getTile(movement.rookEndCoordinate)
                            );
                        } else if (movement.isEnPassant) {
                            move = new Move(
                                board,
                                board.getTile(movement.currentCoordinate),
                                board.getTile(movement.destinationCoordinate),
                                board.getTile(movement.enPassantCapturedPawnCoordinate)
                            );
                        } else {
                            move = new Move(board, board.getTile(movement.currentCoordinate), board.getTile(movement.destinationCoordinate));
                        }
                        player.makeMove(board, move);
                        this.client.game.getBoardPanel().updateBoardGUI(this.client.game.getChessBoard());
                        if (move.isEnPassantMove()) {
                            if (this.client.game.getBottomGameMenu() != null && move.getKilledPiece() != null) {
                                this.client.game.getBottomGameMenu().killedPiecesListModel.addElement(move.getKilledPiece().toString());
                            }
                        } else if (move.hasKilledPiece()) {
                            if (this.client.game.getBottomGameMenu() != null) {
                                this.client.game.getBottomGameMenu().killedPiecesListModel.addElement(move.getKilledPiece().toString());
                            }
                            if (move.getKilledPiece().getType() == PieceTypes.KING) {
                                Team winnerTeam;
                                winnerTeam = (move.getKilledPiece().getTeam() == Team.BLACK) ? Team.WHITE : Team.BLACK;
                                JOptionPane.showMessageDialog(null, "Winner: " + winnerTeam.toString());
                                Message endMessage = new Message(Message.MessageTypes.END);  // Changed variable name
                                endMessage.content = null;
                                client.Send(endMessage);
                                break;
                            }
                        }
                        board.changeCurrentPlayer();
                        if (this.client.game.getBottomGameMenu() != null) {
                            this.client.game.getBottomGameMenu().getTurnLBL().setText("Your Turn");
                            this.client.game.getBottomGameMenu().getTurnLBL().setForeground(Color.GREEN);
                        }
                        break;
                        
                    case CHECK:
                        Team checkStateTeam = (Team) msg.content;
                        JOptionPane.showMessageDialog(null, "Check state for team: " + checkStateTeam.toString());
                        break;
                        
                    case SAVE_GAME:
                        // Handle save game confirmation from server
                        if (msg.content instanceof GameState) {
                            GameState savedGame = (GameState) msg.content;
                            System.out.println("Game saved successfully: " + savedGame.getSaveName());
                        }
                        break;
                        
                    case LOAD_GAME:
                        // Handle load game request
                        if (msg.content instanceof GameState) {
                            GameState loadedGame = (GameState) msg.content;
                            // Load the game state
                            loadGameState(loadedGame);
                        } else if (msg.content instanceof String) {
                            // Handle error message
                            String errorMsg = (String) msg.content;
                            JOptionPane.showMessageDialog(null, 
                                errorMsg,
                                "Load Error", 
                                JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                        
                    case GET_SAVED_GAMES:
                        // Handle list of saved games
                        if (msg.content instanceof ArrayList) {
                            ArrayList<String> savedGameNames = (ArrayList<String>) msg.content;
                            this.client.game.showLoadGameDialog(savedGameNames);
                        }
                        break;
                        
                    case LOAD_GAME_WITH_PAIRING:
                        // Handle load game with pairing confirmation
                        if (msg.content instanceof String) {
                            String message = (String) msg.content;
                            JOptionPane.showMessageDialog(null, 
                                message,
                                "Load Game with Pairing", 
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;

                    case LEAVE:
                        JOptionPane.showMessageDialog(null, "Enemy left. Returning to the Menu.");
                        this.client.isPaired = false;
                        this.client.game.createMainMenu();
                        break;
                        
                    case END:
                        JOptionPane.showMessageDialog(null, "Game ended. Returning to menu.");
                        this.client.isPaired = false;
                        this.client.game.createMainMenu();
                        break;
                        
                    default:
                        System.out.println("Unknown message type received: " + msg.type);
                        break;
                }

            } catch (IOException ex) {
                Logger.getLogger(ClientListenThread.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Connection lost to server");
                break;
            } catch (ClassNotFoundException ex) {
                System.out.println("Girilen class bulunamadı: " + ex.getMessage());
                break;
            } catch (Exception ex) {
                System.out.println("Unexpected error in ClientListenThread: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
        
        try {
            if (this.client.socket != null && !this.client.socket.isClosed()) {
                this.client.socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientListenThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadGameState(GameState gameState) {
        try {
            // Set client as paired since we're loading a saved game
            this.client.isPaired = true;
            
            // Update player names first
            this.client.game.setPlayerName(gameState.getPlayer1Name());
            this.client.game.setOpponentName(gameState.getPlayer2Name());
            
            // Create game panel with the loaded board
            this.client.game.createGamePanelWithBoard(gameState.getBoard());
            
            JOptionPane.showMessageDialog(null, 
                "Game loaded successfully: " + gameState.getSaveName(),
                "Load Game", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception ex) {
            System.out.println("Error loading game state: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error loading game: " + ex.getMessage(),
                "Load Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
