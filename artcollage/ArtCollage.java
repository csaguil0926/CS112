/*************************************************************************
 *  Compilation:  javac ArtCollage.java
 *  Execution:    java ArtCollage Flo2.jpeg
 *
 *  @author: Carolette Saguil, cas699, cas699@rutgers.edu
 *
 *************************************************************************/

import java.awt.Color;

public class ArtCollage {

    // The orginal picture
    private Picture original;

    // The collage picture
    private Picture collage;

    // The collage Picture consists of collageDimension X collageDimension tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    private int tileDimension;
    
    /*
     * One-argument Constructor
     * 1. set default values of collageDimension to 4 and tileDimension to 100
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename) {

	    // WRITE YOUR CODE HERE
        collageDimension = 4;
        tileDimension = 100;

        int w = tileDimension*collageDimension;
        int h = tileDimension*collageDimension;

        original = new Picture(filename);
        collage = new Picture(w, h);

        for (int ccol = 0; ccol < w; ccol++){
            for (int crow = 0; crow < h; crow++){
                int ocol = ccol * original.width() / w;
                int orow = crow * original.height() / h;
                Color color = original.get(ocol, orow);
                collage.set(ccol, crow, color);
            }
        }
    }

    /*
     * Three-arguments Constructor
     * 1. set default values of collageDimension to cd and tileDimension to td
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename, int td, int cd) {

	    // WRITE YOUR CODE HERE

        this.collageDimension = cd;
        this.tileDimension = td;

        int w = tileDimension*collageDimension;
        int h = tileDimension*collageDimension;

        this.original = new Picture(filename);
        this.collage = new Picture(w, h);

        for (int ccol = 0; ccol < w; ccol++){
            for (int crow = 0; crow < h; crow++){
                int ocol = ccol * original.width() / w;
                int orow = crow * original.height() / h;
                Color color = original.get(ocol, orow);
                collage.set(ccol, crow, color);
            }
        }
    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {

	    // WRITE YOUR CODE HERE
        return collageDimension;
    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {

	    // WRITE YOUR CODE HERE
        return tileDimension;
    }

    /*
     * Returns original instance variable
     *
     * @return original
     */
    public Picture getOriginalPicture() {

	    // WRITE YOUR CODE HERE
        return original;
    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */
    public Picture getCollagePicture() {

	    // WRITE YOUR CODE HERE
        return collage;
    }
    
    /*
     * Display the original image
     * Assumes that original has been initialized
     */
    public void showOriginalPicture() {

	    // WRITE YOUR CODE HERE
        original.show();
    }

    /*
     * Display the collage image
     * Assumes that collage has been initialized
     */
    public void showCollagePicture() {

	    // WRITE YOUR CODE HERE
        collage.show();
    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename
     * Tile (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void replaceTile (String filename,  int collageCol, int collageRow) {

	    // WRITE YOUR CODE HERE
        Picture source = new Picture(filename);
        Picture targetSource = new Picture (tileDimension, tileDimension);
        Picture replaceCollage = new Picture (tileDimension*collageDimension, tileDimension*collageDimension);

        for (int tcol = 0;  tcol< tileDimension; tcol++){
            for (int trow = 0; trow < tileDimension; trow++){
                int scol = tcol * source.width() / tileDimension;
                int srow = trow * source.height() / tileDimension;
                Color color = source.get(scol, srow);
                targetSource.set(tcol, trow, color);
            }
        }

        for (int col = 0; col < replaceCollage.width(); col++){
            for (int row = 0; row < replaceCollage.height(); row++){
                Color color = targetSource.get(col%tileDimension, row%tileDimension);
                replaceCollage.set(col, row, color);
            }
        }

        for(int tcol = tileDimension * collageCol; tcol < tileDimension * (collageCol+1); tcol++){
            for(int trow = tileDimension * collageRow; trow < tileDimension * (collageRow+1); trow++){
                Color color = replaceCollage.get(tcol, trow);
                collage.set(tcol,trow,color);
            }
        }
    }
    
    /*
     * Makes a collage of tiles from original Picture
     * original will have collageDimension x collageDimension tiles, each tile
     * has tileDimension X tileDimension pixels
     */
    public void makeCollage () {

	    // WRITE YOUR CODE HERE

        Picture newOriginal = new Picture(tileDimension, tileDimension);

        for (int ncol = 0;  ncol< tileDimension; ncol++){
            for (int nrow = 0; nrow < tileDimension; nrow++){
                int ocol = ncol * original.width() / tileDimension;
                int orow = nrow * original.height() / tileDimension;
                Color color = original.get(ocol, orow);
                newOriginal.set(ncol, nrow, color);
            }
        }

        for (int col = 0; col < collage.width(); col++){
            for (int row = 0; row < collage.height(); row++){
                Color color = newOriginal.get(col%tileDimension, row%tileDimension);
                collage.set(col, row, color);
            }
        }

    }

    /*
     * Colorizes the tile at (collageCol, collageRow) with component 
     * (see CS111 Week 9 slides, the code for color separation is at the 
     *  book's website)
     *
     * @param component is either red, blue or green
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void colorizeTile (String component,  int collageCol, int collageRow) {

	    // WRITE YOUR CODE HERE

        for(int tcol = tileDimension * collageCol; tcol < tileDimension * (collageCol+1); tcol++){
            for(int trow = tileDimension * collageRow; trow < tileDimension * (collageRow+1); trow++){
                Color color = collage.get(tcol, trow);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                if (component == "red"){
                    collage.set(tcol, trow, new Color (red, 0, 0));
                } else if (component == "green"){
                    collage.set(tcol, trow, new Color (0, green, 0));
                } else if (component == "blue"){
                    collage.set(tcol, trow, new Color (0, 0, blue));
                }
            }
        }

    }

    /*
     * Grayscale tile at (collageCol, collageRow)
     * (see CS111 Week 9 slides, the code for luminance is at the book's website)
     *
     * @param collageCol tile column
     * @param collageRow tile row
     */

    public void grayscaleTile (int collageCol, int collageRow) {

        for(int tcol = tileDimension * collageCol; tcol < tileDimension * (collageCol+1); tcol++){
            for(int trow = tileDimension * collageRow; trow < tileDimension * (collageRow+1); trow++){
                Color color = collage.get(tcol, trow);
                Color gray = Luminance.toGray(color);
                collage.set(tcol,trow,gray);
            }
        }   

    }


    /*
     *
     *  Test client: use the examples given on the assignment description to test your ArtCollage
     */
    public static void main (String[] args) {

        ArtCollage art = new ArtCollage(args[0], 200, 3);
        art.makeCollage();
        art.colorizeTile("red",2,2);
        art.colorizeTile("blue",2,1);
        art.colorizeTile("green",0,0);
        art.grayscaleTile(1, 0);
        art.replaceTile(args[1],1,1);
        art.showCollagePicture();

        // ArtCollage art = new ArtCollage(args[0], 200, 2);
        // art.makeCollage();

        // // Replace 3 tiles 
        // art.replaceTile(args[1],0,1);
        // art.replaceTile(args[2],1,0);
        // art.replaceTile(args[3],1,1);
        // art.colorizeTile("green",0,0);
        // art.showCollagePicture();

    }
}
