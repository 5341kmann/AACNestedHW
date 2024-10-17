import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KVPair;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Creates a set of mappings of an AAC that has two levels, one for categories and then within each
 * category, it has images that have associated text to be spoken. This class provides the methods
 * for interacting with the categories and updating the set of images that would be shown and
 * handling an interactions.
 *
 * @author Catie Baker
 * @author Grant Sackmann
 */
public class AACMappings implements AACPage {

  AssociativeArray<String, AACCategory> categories;
  AACCategory currentCategory;
  AACCategory homeCategory;
  File file;

  /**
   * Creates a set of mappings fo r the AAC based on the provided file. The file is read in to
   * create categories and fill each of the categories with initial items. The file is formatted as
   * the text location of the category followed by the text name of the category and then one line
   * per item in the category that starts with
   * > and then has the file name and text of that image
   * <p>
   * for instance: img/food/plate.png food
   * >img/food/icons8-french-fries-96.png french fries
   * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png clothing
   * >img/clothing/collaredshirt.png collared shirt
   * <p>
   * represents the file with two categories, food and clothing and food has french fries and
   * watermelon and clothing has a collared shirt
   *
   * @param filename the name of the file that stores the mapping information
   */
  public AACMappings(String filename) throws FileNotFoundException {
    this.categories = new AssociativeArray<>();
    this.file = new File(filename);
    this.homeCategory = new AACCategory("");
    try {
      this.categories.set("", homeCategory);
    } catch (NullKeyException e) {
//      do nothing
    }

    this.readFile();
  }

  private void readFile() throws FileNotFoundException {
    try {
      Scanner scan = new Scanner(this.file);
      while (scan.hasNextLine()) {
        String line = scan.nextLine();

        String[] tokens = line.split("\\s", 2);
        if (tokens.length >= 2) {
          if (tokens[0].charAt(0) == '>') {
            currentCategory.addItem(tokens[0].substring(1), tokens[1]);
          } else {
            try {
              currentCategory = new AACCategory(tokens[1]);
              categories.set(tokens[0], currentCategory);
              homeCategory.addItem(tokens[0], tokens[1]);
            } catch (NullKeyException e) {
//					Do Nothing
            } // try-catch
          } // if
        } // if
      } // while

      this.currentCategory = homeCategory;

    } catch (FileNotFoundException e) {
      throw new FileNotFoundException();
    }

  } // readFile()

  /**
   * Given the image location selected, it determines the action to be taken. This can be updating
   * the information that should be displayed or returning text to be spoken. If the image provided
   * is a category, it updates the AAC's current category to be the category associated with that
   * image and returns the empty string. If the AAC is currently in a category and the image
   * provided is in that category, it returns the text to be spoken.
   *
   * @param imageLoc the location where the image is stored
   * @return if there is text to be spoken, it returns that information, otherwise it returns the
   * empty string
   * @throws NoSuchElementException if the image provided is not in the current category
   */
  public String select(String imageLoc) {
    if (currentCategory.equals(homeCategory)) {
      try {
        currentCategory = categories.get(imageLoc);
        return "";
      } catch (KeyNotFoundException e) {
        //do nothing
      }
    } else if (currentCategory.hasImage(imageLoc)) {
      return currentCategory.select(imageLoc);
    }
    throw new NoSuchElementException();
  } // select()

  /**
   * Provides an array of all the images in the current category
   *
   * @return the array of images in the current category; if there are no images, it should return
   * an empty array
   */
  public String[] getImageLocs() {
    if (currentCategory != null) {
      return currentCategory.getImageLocs();
    }
    return new String[0];
  } // getImageLocs

  /**
   * Resets the current category of the AAC ≠back to the default category
   */
  public void reset() {
    this.currentCategory = homeCategory;
  }


  /**
   * Writes the ACC mappings stored to a file. The file is formatted as the text location of the
   * category followed by the text name of the category and then one line per item in the category
   * that starts with
   * > and then has the file name and text of that image
   * <p>
   * for instance: img/food/plate.png food
   * >img/food/icons8-french-fries-96.png french fries
   * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png clothing
   * >img/clothing/collaredshirt.png collared shirt
   * <p>
   * represents the file with two categories, food and clothing and food has french fries and
   * watermelon and clothing has a collared shirt
   *
   * @param filename the name of the file to write the AAC mapping to
   */
  public void writeToFile(String filename) {
    Iterator<KVPair<String, AACCategory>> categoriesIterator = categories.iterator();

    try {
      PrintWriter writer = new PrintWriter(filename);
//    Going through categories
      while (categoriesIterator.hasNext()) {
        KVPair<String, AACCategory> pair = categoriesIterator.next();
        String catImageLoc = pair.getKey();
        AACCategory aacCat = pair.getValue();
        if (!aacCat.equals(homeCategory)) {
          writer.println(catImageLoc + " " + aacCat.getCategory());
          //      Processing individual category
          for (KVPair<String, String> item : aacCat.aa) {
            String itemImageLoc = item.getKey();
            String itemName = item.getValue();
            writer.println(">" + itemImageLoc + " " + itemName);
          }
        }
      }
      writer.close();
    } catch (FileNotFoundException e) {
//        do nothing
    }
  }

  /**
   * Adds the mapping to the current category (or the default category if that is the current
   * category)
   *
   * @param imageLoc the location of the image
   * @param text     the text associated with the image
   */
  public void addItem(String imageLoc, String text) {
    currentCategory.addItem(imageLoc, text);
    if (currentCategory.equals(homeCategory)) {
      try {
        currentCategory.addItem(imageLoc, text);
        categories.set(imageLoc, new AACCategory(text));
      } catch (NullKeyException e) {
        // Do nothing
      }
    }

  }


  /**
   * Gets the name of the current category
   *
   * @return returns the current category or the empty string if on the default category
   */
  public String getCategory() {
    return currentCategory.getCategory();
  }


  /**
   * Determines if the provided image is in the set of images that can be displayed and false
   * otherwise
   *
   * @param imageLoc the location of the category
   * @return true if it is in the set of images that can be displayed, false otherwise
   */
  public boolean hasImage(String imageLoc) {
//    try {
//      String catImageLoc = getImageCat(imageLoc);
//      return categories.get(catImageLoc).hasImage(imageLoc);
//    } catch (Exception e) {
//      return false;
//    }
    return false;
  }

  private String getImageCat(String imageLoc) {
    return imageLoc.split("/")[1];
  }

}
