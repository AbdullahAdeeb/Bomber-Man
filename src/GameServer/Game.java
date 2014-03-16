/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameServer;

import GameView.MapModel;
import GameView.MapView;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author abdullahadeeb
 */
public class Game {

    MapModel mapModel;
    MapView mapView;
    ArrayList<PlayerPawn> playersPawns;

    Game(String mapFilePath) {
        this.mapModel = new MapModel(mapFilePath);
        playersPawns = new ArrayList();

        addPlayer();
        testPlayer();
    }

    private void addPlayer() {
        this.playersPawns.add(new PlayerPawn(212,this.mapModel));
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
        PlayerPawn p = this.playersPawns.get(0);
        while(true){
            Scanner scan = new Scanner(System.in);
            String text = scan.nextLine();
            if (text.equals("w")) {
                System.out.println("f");
                p.move(PlayerPawn.FORWARD);
            }else if (text.equals("s")) {
                System.out.println("back");
                p.move(PlayerPawn.BACKWARD);
            }else if (text.equals("a")) {
                System.out.println("left");
                p.move(PlayerPawn.LEFT);
            }else if (text.equals("d")) {
                System.out.println("right");
                p.move(PlayerPawn.RIGHT);
            }else{
                System.out.println("not implemented");
            }
        }
        
    }
}
