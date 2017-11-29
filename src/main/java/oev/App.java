package oev;

import oev.mvc.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App {

  public static void main(String[] args) {
    Properties prop = new Properties();

    try {
      // version 1: read file from jar root
      prop.load(App.class.getResourceAsStream("/gradle.properties"));
    } catch (Exception e) {
      // if 1 does not work, try 2: read from project root
      try {
        prop.load(new FileInputStream("gradle.properties"));
      } catch(Exception e2) {
        throw new RuntimeException("could not find gradle.properties",e2);
      }
    }

    new Controller(prop);
  }
}
