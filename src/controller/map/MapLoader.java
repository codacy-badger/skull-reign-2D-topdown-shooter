package controller.map;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.shape.Rectangle;
import model.wall.Wall;
import view.GameViewManager;
import view.LevelManager;
import view.Main;

import java.util.ArrayList;
import java.util.Random;

public class MapLoader {
    private static final int MAP_BLOCKS_WIDTH = 25;
    private static final int MAP_BLOCKS_HEIGHT = 16;

    public static final double BLOCK_SIZE = 16 * 4;
    private static final int STARTING_X = (int) ((GameViewManager.WIDTH - (BLOCK_SIZE * MAP_BLOCKS_WIDTH)) / 2);
    private static final int STARTING_Y = (int) ((GameViewManager.HEIGHT - (BLOCK_SIZE * MAP_BLOCKS_HEIGHT)) / 2);

    private static final String PATH_RESOURCES_SPRITES_MAP = Main.PATH_RESOURCES_SPRITES + "map/";

    private static final String[] WALL_GROUNDS = {
            "Wall_Ground_1.png",
            "Wall_Ground_2.png",
            "Wall_Ground_3.png"
    };

    private final ArrayList<Node> nodes;
    private final Random random;

    public MapLoader(Map map) {
        nodes = new ArrayList<>();
        random = new Random();

        final PixelReader pixelReader = new Image(map.getPath()).getPixelReader();
        for (int i = 0; i < MAP_BLOCKS_HEIGHT; i++) {
            for (int j = 0; j < MAP_BLOCKS_WIDTH; j++) {
                switch (MapKey.getMapKeyFrom(pixelReader.getColor(j, i))) {
                    case FLAG:
                        addWallTiles("Flag_Red.png", j, i);
                        break;
                    case PILLAR:
                        addWall("Pillar_Center.png", j, i);
                        addTile("Pillar_Ground.png", j, i + 1);
                        addTile("Pillar_Top.png", j, i - 1);
                        break;
                    case SIDE_CORNER_LEFT:
                        addTile("Side_Corner.png", j, i);
                        break;
                    case SIDE_CORNER_RIGHT:
                        addTile("Side_Corner.png", j, i, true);
                        break;
                    case WALL_SIDE_LEFT:
                        addWall("Side_Wall.png", j, i);
                        break;
                    case WALL_SIDE_RIGHT:
                        addWall("Side_Wall.png", j, i, true);
                        break;
                    case WALL:
                        addWallTiles("Wall.png", j, i);
                        break;
                    case WALL_ALONE:
                        addWall("Wall_Alone.png", j, i);
                        addTile(getRandWallGround(), j, i + 1);
                        break;
                    case WALL_BARS_BOTTOM:
                        addWallTiles("Wall_Bars_Bottom.png", j, i);
                        break;
                    case WALL_BARS_MIDDLE:
                        addWallTiles("Wall_Bars_Middle.png", j, i);
                        break;
                    case WALL_BROKEN_1:
                        addWallTiles("Wall_Broken_1.png", j, i);
                        break;
                    case WALL_BROKEN_2:
                        addWallTiles("Wall_Broken_2.png", j, i);
                        break;
                    case WALL_END_LEFT:
                        addWall("Wall_End_Left.png", j, i);
                        addTile("Wall_End_Left_Top.png", j, i - 1);
                        addTile(getRandWallGround(), j, i + 1);
                        break;
                    case WALL_END_RIGHT:
                        addWall("Wall_End_Right.png", j, i);
                        addTile("Wall_End_Right_Top.png", j, i - 1);
                        addTile(getRandWallGround(), j, i + 1);
                        break;
                    case WALL_FRONT:
                        addTile("Wall_Top.png", j, i - 1);
                        addWall("Wall.png", j, i);
                        break;
                    case WALL_FRONT_END_LEFT:
                        addTile("Wall_Front_End.png", j, i);
                        break;
                    case WALL_FRONT_END_RIGHT:
                        addTile("Wall_Front_End.png", j, i, true);
                        break;
                    case SKULL:
                        addTile("Skull.png", j, i);
                    case GROUND:
                        final Rectangle rectangle = new Rectangle(BLOCK_SIZE, BLOCK_SIZE, MapKey.GROUND.getColor());
                        rectangle.setStroke(MapKey.GROUND.getColor());
                        rectangle.setStrokeWidth(5);
                        rectangle.setLayoutX(STARTING_X + j * BLOCK_SIZE);
                        rectangle.setLayoutY(STARTING_Y + i * BLOCK_SIZE);
                        nodes.add(rectangle);
                        break;
                    case EMPTY:
                        continue;
                    default:
                        throw new NullPointerException();
                }
            }
        }
    }

    private void addWallTiles(String wallFileName, int j, int i) {
        addTile("Wall_Top.png", j, i - 1);
        addWall(wallFileName, j, i, false);
        addTile(getRandWallGround(), j, i + 1);
    }

    private void addWall(String wallFileName, int j, int i) {
        addWall(wallFileName, j, i, false);
    }

    private void addWall(String wallFileName, int j, int i, boolean reverse) {
        Wall wall = new Wall(new Image(PATH_RESOURCES_SPRITES_MAP + wallFileName,
                BLOCK_SIZE, BLOCK_SIZE, true, false));
        wall.setLayoutX(STARTING_X + j * BLOCK_SIZE);
        wall.setLayoutY(STARTING_Y + i * BLOCK_SIZE);
        if (reverse)
            wall.setScaleX(-1);
        nodes.add(wall);
        LevelManager.getWallArrayList().add(wall);
    }

    private String getRandWallGround() {
        return WALL_GROUNDS[random.nextInt(WALL_GROUNDS.length)];
    }

    private void addTile(String fileName, int j, int i) {
        addTile(fileName, j, i, false);
    }

    private void addTile(String fileName, int j, int i, boolean reverse) {
        ImageView imageView = new ImageView(new Image(PATH_RESOURCES_SPRITES_MAP + fileName,
                BLOCK_SIZE, BLOCK_SIZE, true, false));
        imageView.setLayoutX(STARTING_X + j * BLOCK_SIZE);
        imageView.setLayoutY(STARTING_Y + i * BLOCK_SIZE);
        if (reverse)
            imageView.setScaleX(-1);
        nodes.add(imageView);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }
}
