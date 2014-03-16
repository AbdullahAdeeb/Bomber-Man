/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameClient;

import GameView.MapModel;
import GameView.MapView;

/**
 *
 * @author abdullahadeeb
 */
public class Game {

    MapModel mapModel;
    MapView mapView;
//    ArrayList<PlayerPawn> playersPawns;

    Game(String mapFilePath) {
        this.mapModel = new MapModel(mapFilePath);
//        playersPawns = new ArrayList();

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
