

import GameServer.*;
import java.net.URL;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JTestCases {

//g.com.sendID(String.valueOf(p.getId()));
//g.id = p.getId();
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    //Player attempts to walk on a box, Player 1 attempts to walk on grass. 
    public void test1() {
        URL resource = this.getClass().getClassLoader().getResource("res/map1.xml");
        Game g = new Game(resource.getPath());
        //PlayerPawn player = new PlayerPawn(1, g.getMapModel());
       // PlayerPawn p = new PlayerPawn(g.getPlayerPawns().size(), g.getMapModel());
       // g.getPlayerPawns().add(p);
        //g.id = p.getId();
       // PlayerPawn z = new PlayerPawn(g.getPlayerPawns().size(), g.getMapModel());
      //  g.getPlayerPawns().add(z);
        //fail("Not yet implemented");
        //System.out.println("Test 1: ");
        //System.out.println(g.getMapModel().getMapGrid()[0][0].getType());
        System.out.println("Player " + g.getPlayerPawns().get(0).getId() + " is at position (" + g.getPlayerPawns().get(0).getX() + ", " + g.getPlayerPawns().get(0).getY() + ")");
        //System.out.println(g.playerPawns.get(1).getId());
        //System.out.println(g.playerPawns.size());

        //System.out.println(g.playerPawns.get(0).x);
        //System.out.println(g.playerPawns.get(0).y);
        g.getPlayerPawns().get(0).action(2);
        System.out.println("Player " + g.getPlayerPawns().get(0).getId() + " is at position (" + g.getPlayerPawns().get(0).getX() + ", " + g.getPlayerPawns().get(0).getY() + ")");

        //System.out.println("Player " + g.getPlayerPawns().get(1).getId() + " is at position (" + g.getPlayerPawns().get(1).getX() + ", " + g.getPlayerPawns().get(1).getY() + ")" );
        //g.getPlayerPawns().get(1).action(1);
        //System.out.println("Player " + g.getPlayerPawns().get(1).getId() + " is at position (" + g.getPlayerPawns().get(1).getX() + ", " + g.getPlayerPawns().get(1).getY() + ")" );
        //g.getPlayerPawns().get(1).action(2);
        //System.out.println("Player " + g.getPlayerPawns().get(0).getId() + " is at position (" + g.getPlayerPawns().get(0).getX() + ", " + g.getPlayerPawns().get(0).getY() + ")" );

    }

    @Test
    public void test2() {
    }
}
