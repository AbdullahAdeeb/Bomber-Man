/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author abdullahadeeb02
 */
public class MapModel extends DefaultTableModel {

    private URL GRASS_ICON_DIR = this.getClass().getClassLoader().getResource("res/PATH1.JPG");
    private URL WALL_ICON_DIR = this.getClass().getClassLoader().getResource("res/WALL1.JPG");
    private URL BOX_ICON_DIR = this.getClass().getClassLoader().getResource("res/BOX1.JPG");
    private URL EXIT_ICON_DIR = this.getClass().getClassLoader().getResource("res/EXIT1.JPG");
    private URL BOMB_ICON_DIR = this.getClass().getClassLoader().getResource("res/BOMB1.JPG");
    private URL ENEMY_ICON_DIR = this.getClass().getClassLoader().getResource("res/ENEMY1.JPG");
    private URL PLAYER_ICON_DIR = this.getClass().getClassLoader().getResource("res/PLAYER1.PNG");
    private ImageIcon pathIcon;
    private ImageIcon wallIcon;
    private ImageIcon boxIcon;
    private ImageIcon exitIcon;
    private ImageIcon playerIcon;
    MapView viewer;
    boolean isExitHidden = true;
    int width = 10;
    int height = 10;
    Entity mapGrid[][];
    boolean isExitSet = false;
    private ImageIcon enemyIcon;
    private ImageIcon bombIcon;
    int doorX=0;
    int doorY=0;
    boolean doorHidden = true;
    

    public MapModel(String mapFilePath) {
        initImages();

        if (mapFilePath == null) {
            generateRandomMap();
        } else {
            try {
                loadMapFromFile(mapFilePath);
            } catch (Exception ex) {
                Logger.getLogger(MapModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        startMapView();
    }

    private void startMapView() {
        try {
            viewer = new MapView(this);
            viewer.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(MapModel.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    private void loadMapFromFile(String mapFilePath) throws ParserConfigurationException, IOException, SAXException {


        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(mapFilePath);
        NodeList docNodes = doc.getChildNodes();

        Integer size = Integer.parseInt(docNodes.item(0).getAttributes().item(0).getNodeValue());
        NodeList rows = docNodes.item(0).getChildNodes();  // get the rows elements
        mapGrid = new Entity[size][size]; // the shape of the map must be square
        height = size;
        width = size;
        setColumnCount(width);
        setRowCount(height);
        
        int j=0;
        int i =0;
        
        
        for (int y=0;y<height;y++){
        	Node row = rows.item(j);
        	String[] cells = row.getTextContent().trim().split("-");
        	if (row.getNodeName().equals("row")) {
        		for (int x=0;x<width;x++){
        			if (cells[x].equals("b")) {
                        putBoxIn(x, y);
                    } else if (cells[x].equals("w")) {   // 20% of map is boxes
                        putWallIn(x, y);
                    } else if (cells[x].equals("eb")) {   // 20% of map is boxes
                        hideExitIn(x, y, boxIcon);
                    } else if (cells[x].equals("ep")) {   // 20% of map is boxes
                        hideExitIn(x, y, pathIcon);
                    } else if (cells[x].equals("p")) {   // 20% of map is boxes
                        putPathIn(x, y);
                    } else {
                        // TODO investigate wether fall back to a wall or a box or should throw an error 
                    }
        		}
    			
    			
        	}else{
        		y--;
        		
        	}
        	j++;
        	
        }
       
    }

    private void generateRandomMap() {
        mapGrid = new Entity[width][height];
        setColumnCount(width);
        setRowCount(height);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int rand = (int) (Math.random() * 100);

                if (rand < 15) {             //15% of map is walls
                    putWallIn(x, y);
                } else if (rand >= 15 && rand < 50) {   // 20% of map is boxes
                    putBoxIn(x, y);
                } else if (rand >= 50 && rand <= 100) {  // 50% of the map will be path
                    putPathIn(x, y);
                    hideExitIn(x, y, pathIcon);  // only one door will be created at a path 
                } else {
                    putPathIn(x, y);
                }
            }
        }


    }

    public Entity[][] getGrid() {
        return this.mapGrid;
    }

    public int getGridSize() {
        return this.mapGrid.length;
    }

    public void setMapView(MapView v) {
        this.viewer = v;
    }

    public void placeBomb(int x, int y) {
        this.mapGrid[x][x] = new Entity(Entity.BOMB, this.bombIcon);
        setValueAt(this.bombIcon, y, x);
    }

    private void putWallIn(int x, int y) {
        this.mapGrid[x][y] = new Entity(Entity.WALL, this.wallIcon);
        setValueAt(this.wallIcon, y, x);
    }

    private void putBoxIn(int x, int y) {
        this.mapGrid[x][y] = new Entity(Entity.BOX, this.boxIcon);
        setValueAt(this.boxIcon, y, x);
    }

    private void putPathIn(int x, int y) {
        this.mapGrid[x][y] = new Entity(Entity.PATH, this.pathIcon);
        setValueAt(this.pathIcon, y, x);
    }

    private void hideExitIn(int x, int y, ImageIcon icon) {
        if (!isExitSet) {
            isExitSet = true;
            this.mapGrid[x][y] = new Entity(Entity.EXIT, icon); //create and exit entity
            // hide the entity by displaying the path icon
            setValueAt(exitIcon, y, x);
            doorX =x;
            doorY=y;
        } else {
            putBoxIn(x, y);
        }
    }

    public void putBombOn(int x, int y) {
        this.mapGrid[x][y] = new Entity(Entity.BOMB, bombIcon);
        setValueAt(bombIcon, y, x);
    }

    public boolean isCellHavePawnOn(int x, int y) {
        return this.mapGrid[x][y].isPawnOn();
    }

    public boolean isCellPath(int x, int y) {
        return this.mapGrid[x][y].getType() == Entity.PATH;
    }

    public void setPlayerOnEntity(int id, int x, int y) {
        System.out.println("player on entity " + x + "><" + y);
        this.mapGrid[x][y].setPawnOn(id);
        setValueAt(playerIcon, y, x);
    }

    public int[] findPosForNewPlayer() {
        //TODO make sure to handle when the map is full and there is no space for new players
        // maybe place then on a waiting queue but will still need to listen for other players because this will pause.
        System.out.println("Finding a place for a new player...");
        int loc[] = new int[2];
        boolean done = false;
        do {
            int xrand = (int) (Math.random() * width);
            int yrand = (int) (Math.random() * height);
            if (isCellPath(xrand, yrand) && !isCellHavePawnOn(xrand, yrand)) {
                loc[0] = xrand;
                loc[1] = yrand;
                done = true;
            }
        } while (!done);
        return loc;
    }

    public void setPlayerOffEntity(int x, int y) {
        this.mapGrid[x][y].setPawnOn(-1);
        if (this.mapGrid[x][y].getType() == Entity.EXIT) {
            setValueAt(exitIcon, y, x);
            this.isExitHidden = false;
        } else {
            setValueAt(this.mapGrid[x][y].getIcon(), y, x);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void initImages() {
        pathIcon = new ImageIcon(GRASS_ICON_DIR);
        wallIcon = new ImageIcon(WALL_ICON_DIR);
        boxIcon = new ImageIcon(BOX_ICON_DIR);
        exitIcon = new ImageIcon(EXIT_ICON_DIR);
        playerIcon = new ImageIcon(PLAYER_ICON_DIR);
        enemyIcon = new ImageIcon(ENEMY_ICON_DIR);
        bombIcon = new ImageIcon(BOMB_ICON_DIR);
    }

    public String serialize() {
        String str = "";
        for (int i = 0; i < this.mapGrid.length; i++) {
            for (int j = 0; j < this.mapGrid[0].length; j++) {
                if (mapGrid[i][j].isPawnOn()) { // set the id if there is pawn on the cell

                    str = str + "-" + mapGrid[i][j].getPlayerPawnOn();
                } else { // otherwise no player is on the entity and send the entity
                    // if the entity is an exit and it's hidden then will need to send the hiding object
                    if (mapGrid[i][j].getType() == Entity.EXIT && this.doorHidden) {
                        if (mapGrid[i][j].getIcon() == boxIcon) {
                            str = str + "-" + Entity.BOX;
                        } else {
                            str = str + "-" + Entity.PATH;
                        }
                    } else { // put a normal entity with no player on and no hiddent exit
                        str = str + "-" + String.valueOf(mapGrid[i][j].getType());
                    }


                }
            }
        }
        return str;
    }

// not needed in the server, needed in the player
//    public void parseString(String ser) {
//        String[] split = ser.split("-");
//        int colCount = 0;
//        int rowSize = this.mapGrid.length;
//        for (int i = 0; i < split.length; i++) {
//            if (colCount < this.mapGrid[0].length) {
//                switch (Integer.getInteger(split[colCount])) {
//                    case Entity.BOX:
//                        putBoxIn(i, colCount);
//                        break;
//                    case Entity.EXIT:
//                        hideExitIn(i, colCount);
//                        break;
//                    case Entity.PATH:
//                        putPathIn(i, colCount);
//                        break;
//                    case Entity.WALL:
//                        putWallIn(i, colCount);
//                        break;
//                    default:
//                        setPlayerOnEntity(Integer.getInteger(split[colCount]), i, colCount);
//                }
//                colCount++;
//            } else {
//                colCount = 0;
//            }
//
//        }
//
//    }
    @Override
    public Class<?> getColumnClass(int i) {
        return Icon.class;
    }

    public void setBombOff(int x, int y, int range) {
        try {
            changeToPath(x, y);
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            // there is explosion outside the map because range is outside the border
        }
        try {
            changeToPath(x + 1, y);
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            // there is explosion outside the map because range is outside the border
        }

        try {
            changeToPath(x - 1, y);
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            // there is explosion outside the map because range is outside the border
        }
        try {
            changeToPath(x, y - 1);
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            // there is explosion outside the map because range is outside the border
        }
        try {
            changeToPath(x, y + 1);
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            // there is explosion outside the map because range is outside the border
        }


    }

    private void changeToPath(int x, int y) {

        if (this.mapGrid[x][y].getType() != Entity.WALL && this.isCellHavePawnOn(x, y)==false) {
            putPathIn(x, y);
        }
        
        //Checks to see if the position blown up is that of the door
        if (x == doorX && y == doorY){
        	//setValueAt(exitIcon, y, x);
        	doorHidden = false;
        	this.mapGrid[x][y] = new Entity(Entity.EXIT, this.exitIcon);
            setValueAt(this.exitIcon, y, x);
        	//this.mapGrid[x][y].getType()
        }
        // TODO might wanna check if the player is there and kill it
    }
}
