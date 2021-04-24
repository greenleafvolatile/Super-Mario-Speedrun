package mario;

import nl.han.ica.oopg.engine.GameEngine;

import java.util.List;

public class MainApp extends GameEngine {

    public static String MEDIA_URL = "src/main/java/mario/media/";


    public static void main(String[] args) {

        MainApp app = new MainApp();
        app.runSketch();
    }

    @Override
    public void setupGame() {

        int screenWidth = 1024;
        int screenHeight = 768;

        size(screenWidth, screenHeight);

        //new StateManager(this);

        new StartMenu(this);



    }

    @Override
    public void update() {}
}
