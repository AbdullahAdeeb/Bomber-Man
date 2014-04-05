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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.Random;

/**
 *
 * @author abdullahadeeb
 */
public class Game {

    MapModel mapModel;
    MapView mapView;
    ArrayList<PlayerPawn> playerPawns;
    ArrayList<PlayerPawn> AIPawns;
    ArrayList<Enemy> enemyList;
    Connection com;
    Timer timer;
    BlockingQueue<ActionEvent> commandsQ;
    JFrame frame;

    public Game(String mapFilePath) {
        
        this.commandsQ = new ArrayBlockingQueue<ActionEvent>(1024);
        timer = new Timer();
        this.mapModel = new MapModel(mapFilePath);
        playerPawns = new ArrayList();
        com = new Connection(new IncomingActionListener());
        
        long updateDelay = 100;  //TODO revert the delay to 100
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                
            	try{
            		checkInteractions();
            	}catch(Exception e){
            		System.out.println("Couldn't check interactions");
            	}
                
                //TODO uncomment updateAllplayers
            	try{
            		updateAllPlayers();
            	}catch(Exception e){
            		System.out.println("Couldn't update players");
            	}
            	
            	try{		
            		checkPowerup();
            	}catch(Exception e){
            		System.out.println("Couldn't check win");
            	}
            	
            	try{
            		checkWin();
            	}catch(Exception e){
            		System.out.println("Couldn't check win");
            	}

            }
        }, updateDelay, updateDelay);
        
        while (true) {
            /**
             * synchronized (commandsQ) { if (!commandsQ.isEmpty()) {
             * processCommand(commandsQ.remove(commandsQ.size() - 1)); } // try
             * { // //Thread.sleep(100); // } catch (InterruptedException ex) {
             * // Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null,
             * ex); // } }
             *
             */
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

    private PlayerPawn findPlayerPawnById(int id){
        for (int i = 0; i < playerPawns.size(); i++) {
            if (playerPawns.get(i).getId() == id) {
                return playerPawns.get(i);     
            }
        }
        return null;
    }
    
    private void processCommand(ActionEvent e) {
        synchronized (playerPawns) {
            String actionCmd = e.getActionCommand();
            int id = Character.getNumericValue(actionCmd.charAt(0));
            int cmd = Character.getNumericValue(actionCmd.charAt(1)); //get the command

            logEvent("RECEIVED CMD <" + actionCmd + "> id = " + id + "|| cmd = " + cmd);
            if (id < 0) { // add a new player .. when id is less than zero means id is not yet assigned
                try {
                    String playerIP = actionCmd.substring(3);
                    InetAddress ipAddr = InetAddress.getByName(playerIP);
                    PlayerPawn newPawn = new PlayerPawn(playerPawns.size(), ipAddr, mapModel);
                    Logger.writeLogFile("++++New player:" + newPawn.getId() + " @" + playerIP + " +++");
                    com.sendID(newPawn);
                    playerPawns.add(newPawn);
                    id = newPawn.getId();

                } catch (UnknownHostException ex) {
                    System.err.println(ex);
                    return;
                }
            } else if (cmd == 6) { // disconnect command
            	
            	try{	
	            	synchronized (playerPawns){
	            		
	            		playerPawns.get(id).setDeath();
	            		//playerPawns.get(id).
		                //playerPawns.remove(id);
		               // System.out.println("\tPlayer Left: " + id + "\n");
                                logEvent("---Player Left: " + id + " ---");        
	            	}
            	}catch(Exception z){
            		System.out.println("Couldn't disconnect player");
            	}
            	
            } else if (cmd == 7) { // list current players
                System.out.println(playerPawns.toArray());
            } else if (cmd == 5) { //do an action with the player
            	synchronized (playerPawns){
            		if (playerPawns.get(id).isDead()) {
                        logEvent("Player: " + id + " died");
                    } else {
                        playerPawns.get(id).action(cmd);
                    }
            	}
            } else {
            	synchronized (playerPawns){
            		 if (id >= playerPawns.size()) {
                         // TODO log it using logger
                         logEvent("***ID ERROR: " + id + " ***");
                         return;
                     }
                     playerPawns.get(id).action(cmd);
                     System.out.println("\n--RECEIVED CMD--\nPlayer: " + id + "\tCMD: " + cmd);
            	}     
            }
            com.send(findPlayerPawnById(id), 1, "8");  // send ACK

            //Currently there's an issue where if you move over to where someone died, you also die
            //this is due to the players x and y remaining on the board even after death, need to
            //change these to something like -1,-1, but this causes out of bounds errors

            //Code that checks for AI, player, interactions
            checkInteractions();
            updateAllPlayers();       
            checkDisconnections();
         
        }
    }
   
    public void checkWin(){
    	
    	for (PlayerPawn v : playerPawns){
    		if (v.checking){
    			//Only shows if it's the only player left
    			if (playerPawns.size()==1){
    				JOptionPane.showMessageDialog(frame, "Player " + v.getId() + " wins!, game exiting");
    				
    				//Sends the client the exit command, and closes
    				com.send(findPlayerPawnById(playerPawns.get(0).getId()), 9, "9");
    				System.exit(0);
    			}   			
    			v.checking = false;
    		}
    	}
    	
    	
    	
    	
    }
    
    public void checkPowerup(){
    	for (PlayerPawn v : playerPawns){
    		if (v.thePower){
    			
    			//Gets a random powerup
    			Random randomGenerator = new Random();
    			int randomInt = randomGenerator.nextInt(2);
    			
    			if (randomInt ==0){
    				//Powerup for increasing range
        			v.incRange();
    			}else{
    				//Powerup for increasing bomb amount
    				v.incBombs();
    			}
    			
    			
    			v.thePower = false;
    		}
    	}
    }
    
    public void checkDisconnections(){
    	
    	for (PlayerPawn v : playerPawns){
        	
        }
        
        //Iterator used for removing players from the game when they're dead/disconnected.
        //Using an interator because otherwise shifting indexes will cause issues
    	synchronized (playerPawns){
	    			
	        Iterator it = playerPawns.iterator(); 
	        while(it.hasNext()){
	            PlayerPawn o = (PlayerPawn) it.next();
	            if (o.isDead()){	
	            	
	            	//Causes game to kind of freeze after enemy is killed
	                //it.remove();
	            }
	        }
	        
	      //If no players left, server will close
	        if (playerPawns.isEmpty()){
	        	JOptionPane.showMessageDialog(frame, "All players have disconnected, exiting game");
                        logEvent("All Players have died. Disconnnecting game");
	        	System.exit(0);
	        }
    	}
    }
    
    //Code that checks for AI, player, interactions
    public void checkInteractions() {

        synchronized (playerPawns) {
            //Code that checks for AI, player, interactions
            if (playerPawns.isEmpty()) {
                return;
            }
            PlayerPawn previousPlayer = playerPawns.get(0);
            for (PlayerPawn v : playerPawns) {

                //Checks to see if the two players have collided, if so, they both die
                if (v.checkPlayerCollision(previousPlayer)) {
                    v.setDeath();
                    previousPlayer.setDeath();

                }
                previousPlayer = v;
            }

            //Checks to see if any players died from explosions
            for (Integer v : mapModel.explosionDeaths) {
                playerPawns.get(v).setDeath();
            }
            //Clears the list of deaths since they've been checked
            mapModel.explosionDeaths.clear();
        }

    }
    
    private void logEvent(String e){
        
        String time = new SimpleDateFormat("yyyy/MM/dd_h:mm:ss").format(Calendar.getInstance().getTime());
        String log = e + "\t\t" + time;
        Logger.writeLogFile(log);
    }

    class IncomingActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            /**
             * synchronized (commandsQ) { commandsQ.add(e);
             *
             * }
             *
             */
            commandsQ.add(e);
        }
    }

    public void updateAllPlayers() {
        synchronized (playerPawns) {
            if (playerPawns.isEmpty()) {
                return;
            }

            //System.out.println("updating all players with game state >>>");
            String serialize = mapModel.serialize();
            for (int i = 0; i < playerPawns.size(); i++) {
                com.send(playerPawns.get(i), 5, serialize);
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
        
//        DatagramSocket socket;
//        DatagramPacket packet;
//        
//        String msg = "hello";
//        try {
//            packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length,
//                    InetAddress.getByName("192.168.212.14"),8000);
//       socket = new DatagramSocket();
//       socket.send(packet);
//        } catch (UnknownHostException ex) {
//            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SocketException ex) {
//            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
        
        
        
        
        Game game;
        Logger.writeLogFile("GAME SERVER STARTED <" + new SimpleDateFormat("yyyy/MM/dd_h:mm:ss").format(Calendar.getInstance().getTime()) + ">");
        if (args.length > 0) {
            game = new Game(args[0]);
        } else {
            game = new Game(null);
        }

    }
}
