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
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abdullahadeeb
 */
public class Game {

    MapModel mapModel;
    MapView mapView;
    ArrayList<PlayerPawn> playerPawns;
    Connection com;
    Timer timer;
    //ArrayList<ActionEvent> commandsQ;
    BlockingQueue<ActionEvent> commandsQ;

    public Game(String mapFilePath) {
        //this.commandsQ = new ArrayList<ActionEvent>();
    	this.commandsQ =  new ArrayBlockingQueue<ActionEvent>(1024);
        timer = new Timer();
        this.mapModel = new MapModel(mapFilePath);
        playerPawns = new ArrayList();
        com = new Connection(new IncomingActionListener());

        long updateX = 1000;
        long updateY = 1000;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAllPlayers();
            }
        }, updateX, updateY);

        while (true) {
        	/**
            synchronized (commandsQ) {
                if (!commandsQ.isEmpty()) {
                    processCommand(commandsQ.remove(commandsQ.size() - 1));
                }
//                try {
//                    //Thread.sleep(100);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
            **/
        	ActionEvent currentCommand;
			try {
				currentCommand = this.commandsQ.take();
				processCommand(currentCommand);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
    }

    private void processCommand(ActionEvent e) {
        synchronized (playerPawns) {
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
            } else if (cmd == 5) { //do an action with the player
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
    }

    class IncomingActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        	/**
            synchronized (commandsQ) {
                commandsQ.add(e);

            }
            **/
           commandsQ.add(e);
        }
    }

    public void updateAllPlayers() {
        synchronized (playerPawns) {
            if (playerPawns.isEmpty()) {
                return;
            }

            System.out.println("updating all players >>>");
            String serialize = mapModel.serialize();
            for (int i = 0; i < playerPawns.size(); i++) {
                com.send(playerPawns.get(i).getId(), 5, serialize);
            }
        }
    }

    public ArrayList<PlayerPawn> getPlayerPawns() {
        return playerPawns;
    }

    public MapModel getMapModel() {
        return mapModel;
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
