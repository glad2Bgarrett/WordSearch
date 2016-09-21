import java.io.*;
import java.util.*;

public class Search {
  public static void main(String[] args){
    final int MAX_SIZE = 500;
    Scanner scanner = null;
    WordSearch myWordSearch = new WordSearch(args[0]);
    myWordSearch.OpenFile();
    String[] lines = new String[MAX_SIZE];
    lines = myWordSearch.GetBoard();
    for (String l : lines){
      System.out.println(l);
    }
    myWordSearch.FindWords();
  }
}
