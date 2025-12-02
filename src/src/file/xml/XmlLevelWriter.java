package file.xml;

import file.LevelReader;
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

/**
 * Implementation of the {@link LevelWriter} interface that writes to an XML file
 */
public class XmlLevelWriter extends LevelComponentMarshaller implements LevelWriter {
    Document doc;
    File file;

    /**
     * Creates a writer associated with an XML file
     * @param path Path to the XML file, if a file doesn't exist there a new one will be created
     * @throws IOException If a new file cannot be created
     */
    public XmlLevelWriter(String path) throws IOException {
        file = new File(path);
        file.createNewFile();
        doc = new Document();
    }

    /** Writes a level to the XML file
     * @param level The level to write
     * @throws IOException if the file cannot be written into
     */
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
