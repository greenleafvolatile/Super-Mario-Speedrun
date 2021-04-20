package mario;

import mario.tiles.MarioTile;
import mario.enemies.Enemy;
import mario.enemies.FlyingTurtle;
import mario.enemies.Goomba;
import nl.han.ica.oopg.dashboard.Dashboard;
import nl.han.ica.oopg.objects.TextObject;
import nl.han.ica.oopg.tile.Tile;
import nl.han.ica.oopg.tile.TileMap;
import nl.han.ica.oopg.tile.TileType;
import nl.han.ica.oopg.view.CenterFollowingViewport;
import nl.han.ica.oopg.view.View;
import nl.han.ica.oopg.view.Viewport;
import org.w3c.dom.Text;

import java.io.File;

import static mario.TileTypeLoader.loadTileTypes;

public class Game {

    private MainApp app;
    private Player player;
    private int level = 1;
    private TileMap tileMap;

    private final File[] mapFiles = { new File(MainApp.MEDIA_URL.concat("maps/level1.csv"))};

    public Game(MainApp app) {
        this.app = app;
        this.tileMap = initMap();
        this.app.setTileMap(tileMap);
    }

    public void display() {
        createObjects();
        initMap();
        createView();
        createDashboard();
    }

    private void createObjects() {
        this.player = new Player(this.app);
        this.app.addGameObject(player, 0, 481);

        Goomba goomba = new Goomba(this.app);
        this.app.addGameObject(goomba, 1100, 513);

        Enemy flyingTurtle = new FlyingTurtle(this.app);
        this.app.addGameObject(flyingTurtle, 1100, 300);

        Enemy flyingTurtle2 = new FlyingTurtle(this.app);
        this.app.addGameObject(flyingTurtle2, 10, 300);
    }

    private void createDashboard() {
        int screenWidth = this.app.getWidth();
        int screenHeight = this.app.getHeight();

        final int xMargin = 40;
        final int yMargin = 20;

        Dashboard dashboard = new Dashboard(0, 0, screenWidth, screenHeight);

        // TEXT LEFT
        TextObject playerName = new TextObject("Player", 32);
        playerName.setForeColor(255, 255, 255, 255);
        dashboard.addGameObject(playerName, xMargin, yMargin);

        // TEXT MIDDLE
        TextObject time = new TextObject("00:00:00", 32);
        time.setForeColor(255, 255, 255, 255);
        dashboard.addGameObject(time, screenWidth / 2 - 50, yMargin);

        // TEXT RIGHT
        TextObject keysCollected = new TextObject("Keys", 32);
        keysCollected.setForeColor(255, 255, 255, 255);
        dashboard.addGameObject(keysCollected, screenWidth - 110, yMargin);

        this.app.addDashboard(dashboard);
    }

    private TileMap initMap() {

        @SuppressWarnings("unchecked")
        TileType<Tile>[] tileTypes =  loadTileTypes();

        int[][] tilesMap = MapLoader.loadMap(mapFiles[level - 1]);
        return new TileMap(MarioTile.getTileSize(), tileTypes, tilesMap);
    }

    private Viewport centerViewport() {
        int screenWidth = this.app.getWidth();
        int screenHeight = this.app.getHeight();

        CenterFollowingViewport viewPort = new CenterFollowingViewport(player, screenWidth, screenHeight, 0, 100);
        viewPort.setTolerance(50, 0, 50, 50);
        return viewPort;
    }

    private void createView() {
        int worldWidth = this.tileMap.getMapWidth();
        int worldHeight = this.tileMap.getMapHeight();

        View view = new View(centerViewport(), worldWidth, worldHeight);
        this.app.setView(view);
    }
}
