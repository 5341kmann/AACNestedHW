import edu.grinnell.csc207.util.AssociativeArray;
import java.util.NoSuchElementException;

import edu.grinnell.csc207.util.AssociativeArray;

/**
 * Represents the mappings for a single category of items that should be displayed
 *
 * @author Catie Baker
 * @author Grant Sackmann
 */
public class AACCategory implements AACPage {
  String catName;
  AssociativeArray<String, String> aa;

  /**
   * Creates a new empty category with the given name
   *
   * @param name the name of the category
   */
  public AACCategory(String name) {
    this.catName = name;
    this.aa = new AssociativeArray<>();
  } // AACCategory

  /**
   * Adds the image location, text pairing to the category
   *
   * @param imageLoc the location of the image
   * @param text     the text that image should speak
   */
  public void addItem(String imageLoc, String text) {
    try {
      aa.set(imageLoc, text);
    } catch (Exception e) {
//		do nothing
    } // try-catch
  } // addItem()

  /**
   * Returns an array of all the images in the category
   *
   * @return the array of image locations; if there are no images, it should return an empty array
   */
  public String[] getImageLocs() {
    String readOut = aa.toString().substring(1, aa.toString().length() - 1);
    return readOut.replaceAll(":[^\\s]+,\\s", " ").replaceAll(":.+$", "").split(",");
  } // getImageLocs()

  /**
   * Returns the name of the category
   *
   * @return the name of the category
   */
  public String getCategory() {
    return catName;
  } // getCategory()

  /**
   * Returns the text associated with the given image in this category
   *
   * @param imageLoc the location of the image
   * @return the text associated with the image
   * @throws NoSuchElementException if the image provided is not in the current category
   */
  public String select(String imageLoc) {
    try{
      return aa.get(imageLoc);
    } catch (Exception e) {
      throw new NoSuchElementException(e);
    } // try-catch
  } // select()

  /**
   * Determines if the provided images is stored in the category
   *
   * @param imageLoc the location of the category
   * @return true if it is in the category, false otherwise
   */
  public boolean hasImage(String imageLoc) {
      return aa.hasKey(imageLoc);
  } // hasImage()

  public int size(){
    return aa.size();
  }
} // ACCCategory
