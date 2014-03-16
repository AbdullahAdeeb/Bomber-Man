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
import java.util.Scanner;

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
                System.out.println("--RECEIVED CMD--\nNew Player: " + p.getId() + "\n");
                com.sendID(p.getId());

            } else if (cmd == 6) { // disconnect command
                playerPawns.remove(id);
                System.out.println("--RECEIVED CMD--\nPlayer Left: " + id + "\n");
            } else if (cmd == 7) { // list current players
                System.out.println(playerPawns.toArray());
            } else {
                if (id >= playerPawns.size()) {
                    // TODO log it using logger
                    System.out.println("OHH SHITTTT");
                    return;
                }
                playerPawns.get(id).action(cmd);
                System.out.println("--RECEIVED CMD--\nPlayer: " + id + "\tCMD>>" + cmd);
            }
            com.send(id, 1);  // send ACK
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

    private void testPlayer() {
        this.playerPawns.add(new PlayerPawn(212, this.mapModel));

        PlayerPawn p = this.playerPawns.get(0);
        while (true) {
            Scanner scan = new Scanner(System.in);
            String text = scan.nextLine();
            if (text.equals("w")) {
                System.out.println("f");
                p.action(PlayerPawn.FORWARD);
            } else if (text.equals("s")) {
                System.out.println("back");
                p.action(PlayerPawn.BACKWARD);
            } else if (text.equals("a")) {
                System.out.println("left");
                p.action(PlayerPawn.LEFT);
            } else if (text.equals("d")) {
                System.out.println("right");
                p.action(PlayerPawn.RIGHT);
            } else {
                System.out.println("not implemented");
            }
        }

    }
}
