/**
 *
 */
package core.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import content.Tile;
import content.Tile.TileType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import levels.Level;

/**
 * @author matthias
 *
 */
public class LevelFileParser {

	private static String theme;

	private static char[][] level_text;

	public LevelFileParser(String file) throws IOException {
		/*
		 * open the file
		 * https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java#4716623
		 */
		Path file_path = FileSystems.getDefault().getPath(file);
		System.out.println("Path to the level: " + file_path);
		List<String> stringArray;
		stringArray = Files.readAllLines(file_path);
		System.out.println(stringArray);

		/*
		 * Now the parsing begins
		 * What do we do ?
		 * we read each line
		 * if it begins with #, we ignore it
		 * if it begins with tilesize: we add the tilesize
		 * the level is 'level:' and then enclosed in brackets
		 */
		// The size of the ttributes recognized
		int levellength = "level:".length();
		int themelength = "theme:".length();

		/*
		 * false if not between the brackets,
		 * true if in
		 */
		boolean inLevel = false;
		int line_nbr = 0;
		List<char[]> tiles_list = new ArrayList<char[]>();

		System.out.println("Beginning level parsing...");
		for (String line: stringArray) {
			if (line.trim().charAt(0) != '#') {
				/*
				 * System.out.println("-------------------");
				 * System.out.println(line);
				 * System.out.println("In level: " + inLevel);
				 */

				if (!inLevel && line.substring(0, Math.min(themelength, line.length())).equals("theme:")) {
					/*
					 *  the end of the substring should be the end of the line
					 *  and after, we strip the blanks from it with trim()
					 *
					 *  As we can see, the last 'theme:' in the file wins !
					 */
					System.out.println("Adding theme...");
					theme = line.substring(themelength).trim();
					//TODO We need to check whether the theme is 'right' (=it exists)
				}
				else if (!inLevel && line.substring(0, Math.min(levellength, line.length())).equals("level:")) {
					/*
					 *  now, we search for the '{'
					 *  which will be on the same line
					 */
					System.out.println("Level text found");
					if (line.substring(levellength).trim().equals("{")) {
						inLevel = true;
					}

				}
				else if (inLevel && line.trim().equals("}")) { // we are in the level
					System.out.println("Finished treating level");
					inLevel = false;
					}
				else if (inLevel) {
					char[] line_chars = new char[line.length()];
					for (int i = 0; i < line.length(); i++) {
						/* System.out.println("char " + line.charAt(i) + " (" +
					* line_nbr + "," + i + ")");
					*/
						// Problem : we *must* define level_text beforehand
						// I guess we should make a list of array, and then convert it ?
						line_chars[i] = line.charAt(i);
						// case
					}

					tiles_list.add(line_nbr, line_chars);
					line_nbr ++;
				}
				else {
					System.out.println("Line not treated :" + line);
				}
			}
		}
		/*
		 * Now, we have to convert tiles_list, an ArrayList<char[]>
		 * to level_text, a char[][]
		 * And the following doesn't work :
		 * level_text = (char[][]) tiles_list.toArray();
		 * So either I make it work
		 * Or I code a dedicated conversion function
		 *
		 * Create a char[][] of size tiles_list.size() × max(tiles_list[i].length)
		 */
		int max = 0;
		for (char[] line: tiles_list) {
			max = Math.max(line.length, max);
		}
		level_text = new char[tiles_list.size()][max];
		// then we fill level_text !
		int j;
		for (int i = 0; i < tiles_list.size(); i++) {
			j = 0;
			for (char c: tiles_list.get(i)) {
				level_text[i][j] = c;
				j++;
			}
		}
	}

	public Level toLevel() {
		// Variables used
		int max_i = level_text.length - 1; // height of the char[][]
		int max_j = level_text[0].length - 1; // width of the char[][]

		/*
		 * Now, the objects...
		 */
		// Images :
		/*
		 *  We've got the theme
		 *  So we load the corresponding images and make some imageViews
		 */
		// We get the TILE_SIZE
		float tilesize = Tile.TILE_SIZE;
		//System.out.println(theme);
		//SQUARE
		ImageView tilePlain = new ImageView(new Image("resources/tiles/"
				 + theme + "/tile-plain.png", tilesize, tilesize, false, false));
		// surface-simple
		ImageView tileSurfaceTop = new ImageView(new Image("resources/tiles/"
		 + theme + "/tile-surface-top.png", tilesize, tilesize, false, false));
		// surface-double
		ImageView tileSurfaceDoubleDownRight = new ImageView(new Image("resources/tiles/"
		 + theme + "/tile-surface-double-down-right.png", tilesize, tilesize, false, false));
		// surface-triple
		ImageView tileSurfaceTripleDown = new ImageView(new Image("resources/tiles/"
				 + theme + "/tile-surface-triple-down.png", tilesize, tilesize, false, false));
		ImageView tileSurfaceTripleTop = new ImageView(new Image("resources/tiles/"
				 + theme + "/tile-surface-triple-top.png", tilesize, tilesize, false, false));
		ImageView tileSurfaceTripleLeft = new ImageView(new Image("resources/tiles/"
				 + theme + "/tile-surface-triple-left.png", tilesize, tilesize, false, false));
		ImageView tileSurfaceTripleRight = new ImageView(new Image("resources/tiles/"
				 + theme + "/tile-surface-triple-right.png", tilesize, tilesize, false, false));
		// surface-quadruple
		ImageView tileSurfaceQuadruple = new ImageView(new Image("resources/tiles/"
		 + theme + "/tile-surface-quadruple.png", tilesize, tilesize, false, false));
		// TRIANGLE
		ImageView tileTriangleDownRight = new ImageView(new Image("resources/tiles/"
				 + theme + "/triangle-down-right.png", tilesize, tilesize, false, false));
		ImageView tileTriangleDownLeft= new ImageView(new Image("resources/tiles/"
				 + theme + "/triangle-down-left.png", tilesize, tilesize, false, false));
		ImageView tileTriangleTopLeft = new ImageView(new Image("resources/tiles/"
				 + theme + "/triangle-top-left.png", tilesize, tilesize, false, false));
		ImageView tileTriangleTopRight = new ImageView(new Image("resources/tiles/"
				 + theme + "/triangle-top-right.png", tilesize, tilesize, false, false));


		Level level = new Level();
		List<Tile> list_tiles = new ArrayList<Tile>();
		// the following tile won't be used, it's just so that the IDE won't yell
		Tile tile = new Tile(0, 0, tileSurfaceTop, TileType.SQUARE);
		for (int i = 0; i < max_i + 1; i++) {
			for (int j = 0; j< max_j + 1; j++) {
				char c = level_text[i][j];
				boolean tilefound = false;
				if (c =='x') { // square
					/*
					 * we need to check the surroundings, to see how it connects
					 * So we will consider all its nine surroundings :
					 * ...
					 * .x.
					 * ...
					 * And if we only use ifs, it will be a lot of them.
					 * So we need a more clever solution
					 * 1. We could generate a file in which every solution would be written
					 * 2. But if we can generate the file, then we can just not use a file !
					 *
					 * Method :
					 * first, count number of direct neighbours (not in diagonal)
					 * case 0: quadriple, easy
					 * case 1: triple, depends on the neighbour
					 * case 2: double
					 * case 3: surface
					 */
					// for now, all will be the same
					tile = new Tile(j, i, tileSurfaceTop, TileType.SQUARE);
					int nbrDirectNeighbours = 0;
					// So, we make a list/array of neighbours
					List<neighbourPosition> neighbours = new ArrayList<neighbourPosition>();
					/*
					 *  if we assume the tile not to be on an edge
					 *  Solutions :
					 *  https://stackoverflow.com/questions/4120609/more-efficient-way-to-check-neighbours-in-a-two-dimensional-array-in-java#5802694
					 */
					int rowStart  = Math.max( i - 1, 0   );
					int rowFinish = Math.min( i + 1, max_i);
					int colStart  = Math.max( j - 1, 0   );
					int colFinish = Math.min( j + 1, max_j);

					for ( int curRow = rowStart; curRow <= rowFinish; curRow++ ) {
					    for ( int curCol = colStart; curCol <= colFinish; curCol++ ) {

					    	/*
					    	 * The if condition is a bit messy
					    	 * First, we check that we are on a direct neighbour
					    	 * then, we check that the neighbour is not empty
					    	 */
					        if (((curRow == i && curCol != j) ||
					        		(curRow != i && curCol == j))
					        		&& (level_text[curRow][curCol] != ' ' &&
							        		level_text[curRow][curCol] != '\u0000')) {
					        	nbrDirectNeighbours ++;
					        	// then we detect where the neighbours are
					        	if (curRow > i) {
					        		neighbours.add(neighbourPosition.UP);
					        	}
					        	else if (curRow < i) {
					        		neighbours.add(neighbourPosition.DOWN);
					        	}
					        	else if (curRow < j) {
					        		neighbours.add(neighbourPosition.LEFT);
					        	}
					        	else if (curRow > j) {
					        		neighbours.add(neighbourPosition.RIGHT);
					        	}
					        }
					    }
					}
					switch (nbrDirectNeighbours) {
					case 0:
						tile = new Tile(j, i, tileSurfaceQuadruple, TileType.SQUARE);
						break;
					case 1:
						neighbourPosition n = neighbours.get(0);
						switch (n) {
						case UP:
							tile = new Tile(j, i, tileSurfaceTripleTop, TileType.SQUARE);
							break;
						case DOWN:
							tile = new Tile(j, i, tileSurfaceTripleDown, TileType.SQUARE);
							break;
						case LEFT:
							System.out.println("Left !");
							tile = new Tile(j, i, tileSurfaceTripleLeft, TileType.SQUARE);
							break;
						case RIGHT:
							tile = new Tile(j, i, tileSurfaceTripleRight, TileType.SQUARE);
							break;
						}
						break;
					case 2:
						tile = new Tile(j, i, tileSurfaceDoubleDownRight, TileType.SQUARE);
						break;
					case 3:
						tile = new Tile(j, i, tileSurfaceTop, TileType.SQUARE);
						break;
					case 4:
						tile = new Tile(j, i, tilePlain, TileType.SQUARE);
						// and we do not need to add a collider
						break;
					}
					tilefound = true;
				}
				else if (c=='/') {
					tile = new Tile(j, i, tileTriangleDownRight, TileType.TRIANGLE_DOWN_RIGHT);
					tilefound = true;
				}
				else if (c=='\\') {
					tile = new Tile(j, i, tileTriangleDownLeft, TileType.TRIANGLE_DOWN_LEFT);
					tilefound = true;
				}
				else if (c=='v') {
					tile = new Tile(j, i, tileTriangleTopLeft, TileType.TRIANGLE_TOP_LEFT);
					tilefound = true;
				}
				else if (c=='u') {
					tile = new Tile(j, i, tileTriangleTopRight, TileType.TRIANGLE_TOP_RIGHT);
					tilefound = true;
				}
				else {
					//System.out.println("Char '" + c + "' ignored");
				}

				if (tilefound) {
					//System.out.println("Char '" + c + "' not ignored");
					list_tiles.add(tile);
				}
			}
		}
		System.out.println(list_tiles.getClass().getName());
		System.out.println(list_tiles.toArray().getClass().getName());

		/*
		 * Ce serait bien de faire
		 * level.tiles = (Tile[]) list_tiles.toArray();
		 */

		level.tiles = new Tile[list_tiles.size()];
		for (int i = 0; i < list_tiles.size(); i++) {
			level.tiles[i] = list_tiles.get(i);
		}
		return level;
	}

	public String toString() {
		String level_parser_txt = "";
		String line;
		for (char[] charline: level_text) {
			line = "";
			for (char c: charline) {
				line = line + c;
			}
			level_parser_txt = level_parser_txt + "\n" + line;
		}
		return level_parser_txt;
	}

	// For now, no indirect (on a diagonal) neighbour
	enum neighbourPosition {
		UP, DOWN, RIGHT, LEFT
	}
}
