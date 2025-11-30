package file.xml;

import file.LevelWriter;
import model.Level;
import model.LevelComponent;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XmlLevelWriter implements LevelWriter {
    Document doc;
    File file;

    public XmlLevelWriter(String path) throws IOException {
        file = new File(path);
        file.createNewFile();
        doc = new Document();
    }

    @Override
    public void writeLevel(Level level) throws IOException {
        Element root = new Element("Level");
        root.setAttribute("width", String.valueOf(level.getWidth()));
        root.setAttribute("height", String.valueOf(level.getHeight()));
        root.setAttribute("name", file.getName().split("\\.")[0]);

        for (LevelComponent component : level.getComponents()){
            root.addContent(LevelComponentMarshaller.elementFrom(component));
        }

        doc.setRootElement(root);

        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        outputter.output(doc, new FileWriter(file));
    }
}
