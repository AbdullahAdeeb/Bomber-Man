/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import GameServer.PlayerPawn;
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
    private URL FACE_ICON_DIR = this.getClass().getClassLoader().getResource("res/FACE1.JPG");
    private ImageIcon grassIcon;
    private ImageIcon wallIcon;
    private ImageIcon boxIcon;
    private ImageIcon exitIcon;
    private ImageIcon faceIcon;
    MapView viewer;
    int width = 10;
    int height = 10;
    Entity mapGrid[][];
    boolean isExitSet = false;

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

        int x = 0;
        for (int i = 0; i < rows.getLength(); i++) {
            Node row = rows.item(i);
            if (row.getNodeName().equals("row")) {
                String[] cells = row.getTextContent().trim().split("-");
                for (int y = 0; y < size; y++) {

                    if (cells[y].equals("b")) {
                        putBoxIn(x, y);
                    } else if (cells[y].equals("w")) {   // 20% of map is boxes
                        putWallIn(x, y);
                    } else if (cells[y].equals("e")) {   // 20% of map is boxes
                        putExitIn(x, y);
                    } else if (cells[y].equals("p")) {   // 20% of map is boxes
                        putPathIn(x, y);
                    } else {
                        // TODO investigate wether fall back to a wall or a box or should throw an error 
                    }
                }
                x++;
            }

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
                    putExitIn(x, y);  // only one door will be created at a path 
                } else {
                    putPathIn(x, y);
                }
            }
        }


    }

    public void addPawn(PlayerPawn p) {
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

    private void putWallIn(int x, int y) {
        this.mapGrid[x][y] = new Entity(Entity.WALL,this.wallIcon);
        setValueAt(this.wallIcon, y, x);
    }

    private void putBoxIn(int x, int y) {
        this.mapGrid[x][y] = new Entity(Entity.BOX,this.boxIcon);
        setValueAt(this.boxIcon, y, x);
    }

    private void putPathIn(int x, int y) {
        this.mapGrid[x][y] = new Entity(Entity.PATH,this.grassIcon);
        setValueAt(this.grassIcon, y, x);
    }

    private void putExitIn(int x, int y) {
        if (!isExitSet) {
            isExitSet = true;
            this.mapGrid[x][y] = new Entity(Entity.EXIT,this.exitIcon);
            setValueAt(this.exitIcon, y, x);
        } else {
            putBoxIn(x, y);
            setValueAt(this.boxIcon, y, x);
        }
    }

    public boolean isCellPath(int x, int y) {
        System.out.println(this.mapGrid[x][y].getType() == Entity.PATH);
        return this.mapGrid[x][y].getType() == Entity.PATH;
    }

    public void setPlayerOnEntity(int x, int y) {
        System.out.println("player on entity "+x+"><"+y);
        this.mapGrid[x][y].setIsPlayerPawnOn(true);
        setValueAt(faceIcon, y, x);
    }

    public void setPlayerOffEntity(int x, int y) {
        this.mapGrid[x][y].setIsPlayerPawnOn(false);
        setValueAt(this.mapGrid[x][y].getIcon(), y, x);
    }

    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void initImages() {
        grassIcon = new ImageIcon(GRASS_ICON_DIR);
        wallIcon = new ImageIcon(WALL_ICON_DIR);
        boxIcon = new ImageIcon(BOX_ICON_DIR);
        exitIcon = new ImageIcon(EXIT_ICON_DIR);
        faceIcon = new ImageIcon(FACE_ICON_DIR);
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return Icon.class;
    }
}
