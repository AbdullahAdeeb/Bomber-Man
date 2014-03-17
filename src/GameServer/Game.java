/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameServer;

import GameView.MapModel;
import GameView.MapView;
import UDPCommunication.Connection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author abdullahadeeb
 */
public class Game {

    MapModel mapModel;
    MapView mapView;
    ArrayList<PlayerPawn> playerPawns;
    Connection com;

    Game(String mapFilePath) {
        this.mapModel = new MapModel(mapFilePath);
        playerPawns = new ArrayList();
        com = new Connection(new IncomingActionListener());

    }

    class IncomingActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String actionCmd = e.getActionCommand();
            int id = Character.getNumericValue(actionCmd.charAt(0));
            int cmd = Character.getNumericValue(actionCmd.charAt(1)); //get the command


            if (id < 0) { // add a new player .. when id is less than zero means id is not yet assigned
                PlayerPawn p = new PlayerPawn(playerPawns.size(), mapModel);
                playerPawns.add(p);
                System.out.println("\n--RECEIVED CMD--\nNew Player: " + p.getId() + "\n");
                com.sendID(String.valueOf(p.getId()));
                id = p.getId();
            } else if (cmd == 6) { // disconnect command
                playerPawns.remove(id);
                System.out.println("\n--RECEIVED CMD--\nPlayer Left: " + id + "\n");
            } else if (cmd == 7) { // list current players
                System.out.println(playerPawns.toArray());
            } else if (cmd == 5) {
                playerPawns.get(id).action(cmd);
            } else {
                if (id >= playerPawns.size()) {
                    // TODO log it using logger
                    System.out.println("ID Error: " + id);
                    return;
                }
                playerPawns.get(id).action(cmd);
                System.out.println("\n--RECEIVED CMD--\nPlayer: " + id + "\tCMD: " + cmd);
            }
            com.send(id, 1, "8");  // send ACK
            updateAllPlayers();
        }

        public void updateAllPlayers() {
            System.out.println("updating all players >>>");
            String serialize = mapModel.serialize();
            for (int i = 0; i < playerPawns.size(); i++) {
                com.send(playerPawns.get(i).getId(), 5, serialize);
            }
        }

    
    }

    public static void main(String[] args) {
        Game game;
        if (args.length > 0) {
            game = new Game(args[0]);
        } else {
            game = new Game(null);
        }


    }
}
