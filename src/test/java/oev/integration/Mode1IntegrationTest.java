package oev.integration;

import oev.mvc.Controller;
import oev.mvc.Model;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

public class Mode1IntegrationTest {

  @Test
  public void shouldExecuteModeOne() {

    Properties properties = new Properties();
    properties.setProperty("oevVersion","1.2.3.4");
    Controller controller = new Controller(properties);

    Model model = controller.getModel();
    model.setSourceFiles(new File[]{
            new File("classpath://ovmr-seq2/10.png"),
            new File("classpath://ovmr-seq2/11.png")
    });
    model.setResPath(new File("resFolder/"));
    model.setFkt(Model.MODE_SUMIMAGE);

  }

}
