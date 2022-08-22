package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 60;
    private static final Random RANDOM = new Random();
    private static void initBackground(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }
    /*
    * maxLen--- the longest line in hexagon
    * num   --- the number of tiles in a line
    * x     --- left side of a line
    * */
    private static void drawLine(int maxLen, int num, int x, int y, TETile tile, TETile[][] world) {
        int leftBlankNum = (maxLen - num) / 2;
        for (int i = leftBlankNum; i < leftBlankNum + num; i++) {
            world[x + i][y] = tile;
        }
    }
    /*
    * s --- size of hexagon
    * (x, y) --- the left-down corner of hexagon
    * */
    public static void addHexagon(int s, int x, int y, TETile tile, TETile[][] world) {
        int maxLen = 3 * s - 2;
        int num, lineY;
        for (int i = 0; i < s; i++) {
            num = s + 2 * i;
            lineY = y + i;
            drawLine(maxLen, num, x, lineY, tile, world);
        }
        for (int i = s - 1; i >= 0; i--) {
            num = s + 2 * i;
            lineY = y + 2 * s - i - 1;
            drawLine(maxLen, num, x, lineY, tile, world);
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.MOUNTAIN;
            case 3: return Tileset.AVATAR;
            case 4: return Tileset.SAND;
            default: return Tileset.MOUNTAIN;
        }
    }

    private static void drawHexagonLine(int maxNum, int num, int s, int x, int y, TETile[][] world) {
        int w = 3 * s - 2;
        int leftBlankNum = (maxNum - num) / 2 * w;
        for (int i = leftBlankNum; i < leftBlankNum + num * w; i += w) {
            addHexagon(s, x + i, y, randomTile(), world);
        }
    }

    public static void drawBigHexagon(int bigS, int s, int x, int y, TETile[][] world) {
        int h = 2 * s;
        int maxNum = 3 * bigS - 2;
        int num;
        int lineY;
        for (int i = 0; i < bigS; i++) {
            num = bigS + 2 * i;
            lineY = y + h * i;
            drawHexagonLine(maxNum, num, s, x, lineY, world);
        }
        for (int i = bigS - 1; i >= 0; i--) {
            num = bigS + 2 * i;
            lineY = y + h * (2 * bigS - i - 1);
            drawHexagonLine(maxNum, num, s, x, lineY, world);
        }
    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        initBackground(world);
        drawBigHexagon(5, 2, 0, 0, world);

        ter.renderFrame(world);
    }
}
