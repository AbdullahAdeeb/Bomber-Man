/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import GameServer.PlayerPawn;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class MapModel {
    
    MapView viewer;
    Entity mapGrid[][];
    boolean isExitSet = false;
    
    public MapModel(String mapFilePath) {
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

        int x = 0;
        for (int i = 0; i < rows.getLength(); i++) {
            Node row = rows.item(i);
            if (row.getNodeName().equals("row")) {
                String[] cells = row.getTextContent().trim().split("-");
                System.out.println("size>>>"+cells.length);
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
        mapGrid = new Entity[10][10];
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
        this.mapGrid[x][y] = new Entity(Entity.WALL);
    }
    
    private void putBoxIn(int x, int y) {
        this.mapGrid[x][y] = new Entity(Entity.BOX);
    }
    
    private void putPathIn(int x, int y) {
        this.mapGrid[x][y] = new Entity(Entity.PATH);
    }
    
    private void putExitIn(int x, int y) {
        if (!isExitSet) {
            System.out.println("exit is done");
            isExitSet = true;
            this.mapGrid[x][y] = new Entity(Entity.EXIT);
        } else {
            putBoxIn(x, y);
        }
    }
    
    private void putPlayerIn(int x, int y) {
    }
}
