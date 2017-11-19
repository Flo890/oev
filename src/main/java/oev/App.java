package oev;

import oev.mvc.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App {

  public static void main(String[] args) {
    Properties prop = new Properties();
    try {
      InputStream input = new FileInputStream("gradle.properties");
      prop.load(input);

      new Controller(prop);
    } catch (IOException e) {
      throw new RuntimeException("could not find gradle.properties");
    }
  }
}
