package mario;

import mario.dashboard.GameDashboard;
import mario.enemies.Enemy;
import mario.tiles.DoorTile;
import mario.tiles.FloorTile;
import mario.tiles.KeyTile;
import mario.tiles.LavaTile;
import nl.han.ica.oopg.collision.CollidedTile;
import nl.han.ica.oopg.collision.ICollidableWithGameObjects;
import nl.han.ica.oopg.collision.ICollidableWithTiles;
import nl.han.ica.oopg.exceptions.TileNotFoundException;
import nl.han.ica.oopg.objects.AnimatedSpriteObject;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.sound.Sound;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.List;

public final class Player extends AnimatedSpriteObject implements ICollidableWithTiles, ICollidableWithGameObjects {

    private static final int SPACE_BAR = 32;

    private final Sound jumpSound;
    private final MainApp app;
    private final List<Key> keys = new ArrayList<>();
    private final GameDashboard gameDashboard;

    private final int walkingSpeed = 5;
    private final int jumpingSpeed = 8;
    private int keysCollected = 0;

    private boolean jump;
    private boolean onFloorTile;

    public Player(MainApp app) {
        super(new Sprite(MainApp.MEDIA_URL.concat("sprites/characters/mario.png")), 7);
        this.jumpSound = new Sound(app, MainApp.MEDIA_URL.concat(("sounds/jump_11.wav")));
        this.app = app;
        this.gameDashboard = (GameDashboard) app.getDashboards().get(0);
        this.keys.add(new Key(LEFT));
        this.keys.add(new Key(RIGHT));
        initPlayer();
    }

    private void initPlayer() {
        this.setSpeed(this.walkingSpeed);
        this.setGravity(0.2f);
    }


    @Override
    public void keyPressed(int intValue, char charValue) {

        this.setKeyPressed(intValue, true);


        switch (intValue) {

            case LEFT:
                setDirectionSpeed(270, this.walkingSpeed);

                /*if (onFloorTile) {
                    nextFrame();
                }*/

                break;

            case RIGHT:
                setDirectionSpeed(90, this.walkingSpeed);

                /*if (onFloorTile) {
                    nextFrame();
                }*/
                break;

            case SPACE_BAR:
                if (onFloorTile) {
                    jump();
                }
                break;

        }
    }

    private void setKeyPressed(int intValue, boolean pressed) {

        for (Key key : keys) {
            if (key.getKeyValue() == intValue) {
                key.setPressed(pressed);
            }
        }
    }

    private boolean isKeyPressed() {

        for (Key key : keys) {
            if (key.isPressed()) {
                return true;
            }
        }
        return false;
    }

    private void jump() {

        this.jump = true;

        if (this.isKeyPressed()) {
            doDirectionalJump();
        } else {
            doVerticalJump();
        }

        this.setCurrentFrameIndex(3); // Change sprite index to jump motion.
        this.onFloorTile = false;
        this.jumpSound.cue(0);
        this.jumpSound.play();
    }

    private void doVerticalJump() {
        setDirectionSpeed(360, jumpingSpeed);
    }


    private void doDirectionalJump() {
        for (Key key : keys) {
            if (key.getKeyValue() == RIGHT && key.isPressed()) {
                setDirectionSpeed(30, jumpingSpeed);
            } else if (key.getKeyValue() == LEFT && key.isPressed()) {
                setDirectionSpeed(320, jumpingSpeed);
            }
        }
    }

    @Override
    public void keyReleased(int intValue, char charValue) {

        this.setKeyPressed(intValue, false);

        if (intValue != Player.SPACE_BAR) {
            this.setSpeed(0);
        }

    }

    private void resetPlayer() {

        if (this.gameDashboard.getNumberOfHearts() == 0) {
            // Show endgame screen.
        }
        this.setSpeed(0);
        this.setX(508);
        this.setY(802);

    }

    @Override
    public void gameObjectCollisionOccurred(List<GameObject> list) {

        for (GameObject object : list) {

            if (object instanceof Enemy) {

                if (this.getY() + this.getHeight() <= object.getCenterY()) {
                    this.app.deleteGameObject(object);
                } else {
                    this.gameDashboard.removeHeart();
                    resetPlayer();
                }

            }

        }

    }

    @Override
    public void tileCollisionOccurred(List<CollidedTile> collidedTiles) {

        for (CollidedTile tile : collidedTiles) {

            PVector tilePixelLocation = this.app.getTileMap().getTilePixelLocation(tile.getTile());
            PVector tileIndexLocation = this.app.getTileMap().getTileIndex(tile.getTile());

            if (tile.getTile() instanceof FloorTile || tile.getTile() instanceof LavaTile) {

                this.onFloorTile = true;

                if (jump) {
                    this.setCurrentFrameIndex(0);
                    jump = false;
                }

                if (tile.getTile() instanceof LavaTile) {
                    this.gameDashboard.removeHeart();
                    resetPlayer();
                }

                switch (tile.getCollisionSide()) {

                    case LEFT:
                        this.setX(tilePixelLocation.x - this.width);
                        break;

                    case RIGHT:
                        this.setX(tilePixelLocation.x + this.width);
                        break;

                    case TOP:
                        this.setY(tilePixelLocation.y - this.height);
                        this.setySpeed(0);
                        break;

                    case BOTTOM:
                        this.setY(tilePixelLocation.y + this.height);
                        break;

                }

            } else if (tile.getTile() instanceof KeyTile) {

                try {

                    this.app.getTileMap().setTile((int) tileIndexLocation.x, (int) tileIndexLocation.y, -1);
                    gameDashboard.addkey();
                    this.keysCollected++;
                } catch (TileNotFoundException e) {
                    e.printStackTrace();
                }

            } else if (tile.getTile() instanceof DoorTile) {

                if (this.keysCollected == 5)  {
                    // show end game menu.
                }
            }
        }
    }


    public void update() {
        if (isKeyPressed() && onFloorTile && this.app.frameCount % 4 == 0) {
           this.nextFrame();
        }
    }
}
