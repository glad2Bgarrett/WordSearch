import java.io.*;
import java.util.*;

public class WordSearch
{
  final int MAX_WORDS = 200;
  private int maxRow = 0;
  private int maxColumn = 0;
  private String[] board;
  private String[] words;
  private String[][] grid;
  private String endIndex;
  private String currentWord;
  private String diagonalWord;
  private Scanner scanner = null;
  private String file;

  public WordSearch(String fileName)
  {
    file = fileName;
    board = new String[this.FirstLineLength()];
    grid = new String[this.FirstLineLength()][this.FirstLineLength()];
    words = new String[MAX_WORDS];
    currentWord = "";
    diagonalWord = "";
    maxRow = board.length - 1;
    maxColumn = board.length - 1;
  }

  private int FirstLineLength()
  {
    try {
      scanner = new Scanner(new File(file));
    } catch (FileNotFoundException x){
      System.out.println("File open failed");
      x.printStackTrace();
      System.exit(0);
    }
    String line;
    if(scanner.hasNextLine()){
      line = scanner.nextLine();
      return line.length();
    }
    else{
      return 0;
    }
  }

  private void SetGrid ()
  {
    for (int i = 0; i < board.length; i++){
      String[] parts = new String[board.length];
      parts = board[i].split("");
      for (int j = 0; j < board.length; j++){
        grid[i][j] = parts[j];
      }
    }
  }

  public void OpenFile()
  {
    try {
      scanner = new Scanner(new File(file));
    } catch (FileNotFoundException x){
      System.out.println("File open failed");
      x.printStackTrace();
      System.exit(0);
    }
    String line;
    int gridLineNumber = 1;
    int wordLineNumber = 1;
    while(scanner.hasNextLine()){
      line = scanner.nextLine();
      if(gridLineNumber <= board.length){
        board[gridLineNumber - 1] = line;
        gridLineNumber++;
      }
      else if(line.equals("")){
        continue;
      }
      else if(line != null){
        words[wordLineNumber - 1] = line;
        wordLineNumber++;
      }
    }
    this.SetGrid();
  }

  public String[] GetBoard()
  {
    return board;
  }

  public String[] GetWords()
  {
    return words;
  }

  public void FindWords()
  {
    String[] letters = {};
    String[] letterLocations = {};
    String firstLetter;
    boolean found = false;
    String foundAt = "";
    for (String w : words){
      if (w != null){
        found = false;
        letters = w.split("");
        currentWord = w;
        firstLetter = letters[0];
        letterLocations = this.SearchLetterOccurences(firstLetter);
        for (String location : letterLocations){
            found = this.Find(location, currentWord);
            if (found){
              foundAt = location;
              break;
            }
          }
          if (found){
            System.out.println(currentWord + " found at start: " + foundAt + " end: " + endIndex);
          }
          else{
            System.out.println(currentWord + " not found");
          }
        }
      }
  }

  private String[] SearchLetterOccurences (String letter)
  {
    List<String> locations = new ArrayList<String>();
    for (int i = 0; i < board.length; i++){
      for (int j = 0; j < board.length; j++){
        if (grid[i][j].equals(letter))
          locations.add(i + "," + j);
      }
    }
    String[] stringLocations = locations.toArray(new String[0]);
    return stringLocations;
  }

  private boolean Find(String location, String word)
  {
    String[] position = location.split(",");
    int row = Integer.parseInt(position[0]);
    int column = Integer.parseInt(position[1]);
    boolean found = false;

    if (column + word.length() <= board.length){
      String[] formedString = Arrays.asList(grid[row]).subList(column, column + word.length()).toArray(new String[0]);
      String simpleString = Build(formedString);
      if (simpleString.equals(word)){
        endIndex = row + "," + (column + word.length() - 1);
        found = true;
      }
    }
    if (column - word.length() >= 0){
      String[] formedString = Arrays.asList(grid[row]).subList(column - (word.length() - 1), column + 1).toArray(new String[0]);
      String simpleString = Build(formedString);
      if (simpleString.equals(new StringBuilder(word).reverse().toString())){
        endIndex = row + "," + (column - (word.length() - 1));
        found = true;
      }
    }
    if (row + word.length() <= board.length){
      String[] formedString = GetColumnContents(column, row, row + word.length());
      String simpleString = Build(formedString);
      if (simpleString.equals(word)){
        endIndex = (row + (word.length() - 1)) + "," + column;
        found = true;
      }
    }
   if (row - word.length() >= 0){
     String[] formedString = GetColumnContents(column, row - word.length() + 1, row + 1);
     String simpleString = Build(formedString);
     if (simpleString.equals(new StringBuilder(word).reverse().toString())){
        endIndex = (row - (word.length() - 1)) + "," + column;
        found = true;
      }
    }
    if (row + word.length() <= board.length && column + word.length() <= board.length){
      FindDiagonal(column, column + word.length(), row, row + word.length());
      if(diagonalWord.equals(word)){
        endIndex = (row + word.length() - 1) + "," + (column + word.length() - 1);
        found = true;
      }
      diagonalWord = "";
    }
    if (row - (word.length() - 1) >= 0 && column - (word.length() - 1) >= 0){
      FindDiagonal(column - (word.length() - 1), column + 1, row - (word.length() - 1), row + 1);
      if(diagonalWord.equals(new StringBuilder(word).reverse().toString())){
        endIndex = ((row - word.length()) + 1) + "," + ((column - word.length()) + 1);
        found = true;
      }
      diagonalWord = "";
    }
    if (row - (word.length() - 1) >= 0 && column + word.length() <= board.length){
      FindDiagonal(column, column + word.length(), row, row - (word.length() - 1), false);
      if(diagonalWord.equals(word)){
        endIndex = ((row - word.length()) + 1) + "," + (column + (word.length() - 1));
        found = true;
      }
      diagonalWord = "";
    }
    if (row + word.length() <= board.length && column - (word.length() - 1) >= 0){
      FindDiagonal(row, row + word.length(), column, column - (word.length() - 1), true);
      if(diagonalWord.equals(word)){
        endIndex = (row + word.length() - 1) + "," + (column - word.length() + 1);
        found = true;
      }
      diagonalWord = "";
    }
    return found;
  }

  private String Build(String[] arrayString)
  {
    StringBuilder builder = new StringBuilder();
    for (String s : arrayString){
      builder.append(s);
    }
    return builder.toString();
  }

  private String[] GetColumnContents(int column, int rowLow, int rowHigh)
  {
    String[] contents = new String[rowHigh - rowLow];
    int index = 0;
    for (; rowLow < rowHigh; rowLow++){
      contents[index] = grid[rowLow][column];
      index++;
    }
    return contents;
  }

  private void FindDiagonal(int columnLow, int columnHigh, int rowLow, int rowHigh)
  {
    if (columnLow < columnHigh && rowLow < rowHigh){
      diagonalWord += grid[rowLow][columnLow];
      FindDiagonal(columnLow + 1, columnHigh, rowLow + 1, rowHigh);
    }
    else{
      return;
    }
  }

  private void FindDiagonal(int inc, int max, int dec, int low, boolean row)
  {
    if (inc <= max && dec >= low){
      if(row)
        diagonalWord += grid[inc][dec];
      else
        diagonalWord += grid[dec][inc];
      FindDiagonal(inc + 1, max, dec - 1, low, row);
    }
    else{
      return;
    }
  }

}
