package mario.view.game;

import mario.MainApp;
import mario.model.Player;
import mario.model.Timer;
import mario.view.game.sprites.HeartSprite;
import mario.view.game.sprites.KeySprite;
import nl.han.ica.oopg.dashboard.Dashboard;
import nl.han.ica.oopg.objects.TextObject;
import nl.han.ica.oopg.view.PGraphicsCreator;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;

public class GameDashboard extends Dashboard {

    private final int xMargin = 40;
    private final int yMargin = 20;

    private final List<HeartSprite> hearts = new ArrayList<>();

    private Player player;

    private int fontSize;

    private MainApp app;
    private Timer timer;
    private TextObject time;
    private PGraphics graphics;

    public GameDashboard(MainApp app, float x, float y, float width, float height, Player player) {

        super(x, y, width, height);
        this.player = player;
        this.graphics = new PGraphicsCreator().createPGraphics((int) width, (int) height);
        this.app = app;
        this.timer = new Timer();
        initHearts();
        this.init();
    }

    private void init() {

        this.fontSize = 32;
        this.graphics.textSize(fontSize);

        String keysLabelText = "keys:";

        this.addLabel("Player:" + this.player.getName(), xMargin, yMargin, fontSize, 255, 255, 255);
        this.addLabel(keysLabelText, (int) (this.width - xMargin - this.graphics.textWidth(keysLabelText)), yMargin, fontSize, 255, 255, 255);

        this.createTimeLabel();

        this.addHearts();

        this.timer.startTimer();

    }

    private void initHearts() {
        for (int i = 0; i < this.player.getHealth(); i++) {
            hearts.add(new HeartSprite());
        }
    }

    private void createTimeLabel() {
        this.time = new TextObject(this.timer.formatToString(timer.getElapsedTime()), 32);
        this.time.setForeColor(255, 255, 255, 255);
        this.addGameObject(time, (int) (this.width / 2 - graphics.textWidth(this.timer.formatToString(timer.getElapsedTime())) / 2), yMargin);
    }

    @Override
    public void update() {
        removeHeart();
        addkey();
        this.time.setText(this.timer.formatToString(timer.getElapsedTime()));
    }


    private void addLabel(String text, int xPos, int yPos, int fontSize, int red, int green, int blue) {

        final int alpha = 255;

        TextObject textObject = new TextObject(text, fontSize);
        textObject.setForeColor(red, green, blue, alpha);

        this.addGameObject(textObject, xPos, yPos);
    }

    private void addHearts() {

        final int y = yMargin + fontSize;

        int x = xMargin;

        for (HeartSprite heartSprite : this.hearts) {

            this.addGameObject(heartSprite, x, y + (int) (heartSprite.getHeight() / 2f));
            x += heartSprite.getWidth();
        }
    }

    public void removeHeart() {
        if (this.player.getHealth() != this.hearts.size()) {
            this.deleteGameObject(hearts.get(hearts.size() - 1));
            this.hearts.remove(this.hearts.size() - 1);
        }
    }

    public int getNumberOfHearts() {
        return this.hearts.size();
    }


    public void addkey() {
        if (this.player.getKeysCollected() > 0) {
            KeySprite key = new KeySprite();
            this.addGameObject(key, (int) (this.width - xMargin - key.getWidth() * this.player.getKeysCollected()), (int) (yMargin + fontSize + key.getHeight() / 2f));
        }
    }
}
