package chess_game.gui;

import ClientSide.Client;
import Messages.ChatMessage;
import Messages.GameState;
import Messages.Message;
import Messages.PlayerInfo;
import Messages.PlayRequest;
import chess_game.Boards.Board;
import chess_game.Pieces.Team;
import chess_game.Resources.GUI_Configurations;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Table {

    private JFrame gameFrame;
    private BoardPanel boardPanel;
    private Board chessBoard;
    private MainMenu mainMenu;
    private PlayerSelectionPanel playerSelectionPanel;
    private InGameBottomMenu bottomGameMenu;
    private Client client;
    private String playerName;
    private String opponentName;

    public Table() {
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setSize(GUI_Configurations.OUTER_FRAME_DIMENSION);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainMenu = new MainMenu();
        this.playerSelectionPanel = new PlayerSelectionPanel();
        this.client = new Client(this);
        this.client.Connect("172.20.32.65", 4000);
        if (this.client.socket == null) {
            JOptionPane.showMessageDialog(null, "Servera bağlanılamadı");
            System.exit(0);
        }
        createMainMenu();
        this.gameFrame.setVisible(true);
    }

    public void createMainMenu() {
        this.gameFrame.getContentPane().removeAll();
        this.mainMenu.getInfoLBL().setText("");
        this.mainMenu.getInfoLBL().setVisible(false);
        
        // Remove all existing action listeners to prevent duplicates
        for (ActionListener al : this.mainMenu.getPlayBTN().getActionListeners()) {
            this.mainMenu.getPlayBTN().removeActionListener(al);
        }
        for (ActionListener al : this.mainMenu.getSelectPlayerBTN().getActionListeners()) {
            this.mainMenu.getSelectPlayerBTN().removeActionListener(al);
        }
        
        // Quick match button handler
        this.mainMenu.getPlayBTN().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = mainMenu.getPlayerName();
                
                if (client.isPaired == false) {
                    mainMenu.getInfoLBL().setVisible(true);
                    mainMenu.getInfoLBL().setText("Matching...");
                    mainMenu.getPlayBTN().setEnabled(false);
                    
                    Message msg = new Message(Message.MessageTypes.PAIRING);
                    msg.content = playerName;
                    client.Send(msg);
                }
                if (client.isPaired == true) {
                    mainMenu.getInfoLBL().setText("Matched");
                    mainMenu.getInfoLBL().setText("Game is starting...");
                    mainMenu.getPlayBTN().setEnabled(true);
                    mainMenu.getInfoLBL().setText("");
                    mainMenu.getInfoLBL().setVisible(false);
                    createGamePanel();
                }
            }
        });
        
        // Select player button handler
        this.mainMenu.getSelectPlayerBTN().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = mainMenu.getPlayerName();
                createPlayerSelectionPanel();
            }
        });
        
        this.mainMenu.getLoadGameBTN().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = mainMenu.getPlayerName();
                // For now, we'll show a simple message. In a full implementation,
                // this would show a list of saved games to choose from.
                JOptionPane.showMessageDialog(
                    gameFrame,
                    "Load Game functionality will be implemented in the next version.\n" +
                    "This would show a list of saved games to choose from.",
                    "Load Game",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        this.gameFrame.add(mainMenu, BorderLayout.CENTER);
        this.gameFrame.revalidate();
        this.gameFrame.repaint();
    }

    public void createPlayerSelectionPanel() {
        this.gameFrame.getContentPane().removeAll();
        
        // Setup event handlers
        setupPlayerSelectionHandlers();
        
        // Request player list from server
        Message msg = new Message(Message.MessageTypes.PLAYER_LIST);
        msg.content = playerName;
        client.Send(msg);
        
        this.gameFrame.add(playerSelectionPanel, BorderLayout.CENTER);
        this.gameFrame.revalidate();
        this.gameFrame.repaint();
    }

    private void setupPlayerSelectionHandlers() {
        // Remove existing listeners
        for (ActionListener al : this.playerSelectionPanel.getSendRequestBTN().getActionListeners()) {
            this.playerSelectionPanel.getSendRequestBTN().removeActionListener(al);
        }
        for (ActionListener al : this.playerSelectionPanel.getRefreshBTN().getActionListeners()) {
            this.playerSelectionPanel.getRefreshBTN().removeActionListener(al);
        }
        for (ActionListener al : this.playerSelectionPanel.getBackBTN().getActionListeners()) {
            this.playerSelectionPanel.getBackBTN().removeActionListener(al);
        }
        
        // Send request button
        this.playerSelectionPanel.getSendRequestBTN().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlayerInfo selectedPlayer = playerSelectionPanel.getSelectedPlayer();
                if (selectedPlayer != null) {
                    PlayRequest request = new PlayRequest(playerName, client.getClientId(), 
                                                        selectedPlayer.playerName, selectedPlayer.playerId);
                    Message msg = new Message(Message.MessageTypes.PLAY_REQUEST);
                    msg.content = request;
                    client.Send(msg);
                    
                    playerSelectionPanel.getStatusLBL().setText("Play request sent to " + selectedPlayer.playerName);
                    playerSelectionPanel.getSendRequestBTN().setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(gameFrame, "Please select a player first!");
                }
            }
        });
        
        // Refresh button
        this.playerSelectionPanel.getRefreshBTN().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Message msg = new Message(Message.MessageTypes.PLAYER_LIST);
                msg.content = playerName;
                client.Send(msg);
            }
        });
        
        // Back button
        this.playerSelectionPanel.getBackBTN().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMainMenu();
            }
        });
    }

    public void createGamePanel() {
        this.gameFrame.getContentPane().removeAll();
        this.chessBoard = new Board();
        this.boardPanel = new BoardPanel(this.chessBoard, this.client);
        this.bottomGameMenu = new InGameBottomMenu();
        
        this.bottomGameMenu.getPlayersColorLBL().setText("Your color is " + this.client.getTeam().toString());
        this.bottomGameMenu.setPlayerName(playerName);
        
        if (opponentName != null) {
            this.bottomGameMenu.setOpponentName(opponentName);
        }
        
        setupChatHandlers();
        
        if(this.client.getTeam() == Team.WHITE) {
            this.bottomGameMenu.getTurnLBL().setText("Your Turn");
            this.bottomGameMenu.getTurnLBL().setForeground(Color.GREEN);
        } else {
            this.bottomGameMenu.getTurnLBL().setText("Enemy Turn");
            this.bottomGameMenu.getTurnLBL().setForeground(Color.RED);
        }    
        
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.bottomGameMenu, BorderLayout.PAGE_END);
        this.gameFrame.revalidate();
        this.gameFrame.repaint();
        this.gameFrame.setVisible(true);
    }

    private void setupChatHandlers() {
        this.bottomGameMenu.getSendBTN().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
            }
        });
        
        this.bottomGameMenu.getChatInputTXT().addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendChatMessage();
                }
            }
            
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        // Quit game button handler
        this.bottomGameMenu.getQuitGameBTN().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    gameFrame,
                    "Are you sure you want to quit the game?",
                    "Quit Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    // Send leave message to server
                    Message leaveMsg = new Message(Message.MessageTypes.LEAVE);
                    client.Send(leaveMsg);
                    
                    // Reset client state
                    client.isPaired = false;
                    
                    // Return to main menu
                    createMainMenu();
                }
            }
        });
        
        // Save game button handler
        this.bottomGameMenu.getSaveGameBTN().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String saveName = JOptionPane.showInputDialog(
                    gameFrame,
                    "Enter a name for this save:",
                    "Save Game",
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (saveName != null && !saveName.trim().isEmpty()) {
                    // Create game state
                    GameState gameState = new GameState(
                        chessBoard,
                        playerName,
                        opponentName,
                        chessBoard.getCurrentPlayer().getTeam(),
                        saveName.trim()
                    );
                    
                    // Send save request to server
                    Message saveMsg = new Message(Message.MessageTypes.SAVE_GAME);
                    saveMsg.content = gameState;
                    client.Send(saveMsg);
                    
                    JOptionPane.showMessageDialog(
                        gameFrame,
                        "Game saved successfully as: " + saveName.trim(),
                        "Save Game",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
    }

    private void sendChatMessage() {
        String messageText = this.bottomGameMenu.getChatInputTXT().getText().trim();
        if (!messageText.isEmpty()) {
            ChatMessage chatMsg = new ChatMessage(playerName, messageText);
            
            Message msg = new Message(Message.MessageTypes.CHAT);
            msg.content = chatMsg;
            client.Send(msg);
            
            this.bottomGameMenu.addChatMessage("You: " + messageText);
            this.bottomGameMenu.getChatInputTXT().setText("");
        }
    }

    public void updatePlayersList(ArrayList<PlayerInfo> players) {
        if (playerSelectionPanel != null) {
            playerSelectionPanel.updatePlayersList(players);
        }
    }

    public void showPlayRequest(PlayRequest request) {
        int response = JOptionPane.showConfirmDialog(
            gameFrame,
            request.fromPlayerName + " wants to play with you. Accept?",
            "Play Request",
            JOptionPane.YES_NO_OPTION
        );
        
        request.isAccepted = (response == JOptionPane.YES_OPTION);
        request.isRejected = (response == JOptionPane.NO_OPTION);
        
        Message msg = new Message(Message.MessageTypes.PLAY_RESPONSE);
        msg.content = request;
        client.Send(msg);
    }

    // Getters and setters
    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public PlayerSelectionPanel getPlayerSelectionPanel() {
        return playerSelectionPanel;
    }

    public InGameBottomMenu getBottomGameMenu() {
        return bottomGameMenu;
    }

    public JFrame getGameFrame() {
        return gameFrame;
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public Board getChessBoard() {
        return chessBoard;
    }

    public Client getClient() {
        return client;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
        if (bottomGameMenu != null) {
            bottomGameMenu.setOpponentName(opponentName);
        }
    }

    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    public void setChessBoard(Board chessBoard) {
        this.chessBoard = chessBoard;
    }

    public void setGameFrame(JFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public void setBottomGameMenu(InGameBottomMenu bottomGameMenu) {
        this.bottomGameMenu = bottomGameMenu;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
