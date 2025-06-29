package Messages;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Enes Kızılcın <nazifenes.kizilcin@stu.fsm.edu.tr>
 */

//This class' purpose is providing a generic communication object for us to
// send any serializable objects to the server.
public class Message implements java.io.Serializable {

    public static enum MessageTypes {
        MATCHED, START, MOVE, END, PAIRING, CHECK, LEAVE, CHAT, 
        PLAYER_LIST, PLAY_REQUEST, PLAY_RESPONSE, REQUEST_DENIED,
        SAVE_GAME, LOAD_GAME, GET_SAVED_GAMES, LOAD_GAME_WITH_PAIRING
    };

    public MessageTypes type;
    public Object content;

    public Message(MessageTypes type) {
        this.type = type;
    }
}
