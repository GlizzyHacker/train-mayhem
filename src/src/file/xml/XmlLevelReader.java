package file.xml;

import file.LevelReader;
import model.Level;
import model.LevelComponent;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the {@link LevelReader} interface that reads from an XML file
 * For the required format see {@link file.xml.XmlLevelWriter}
 */
public class XmlLevelReader implements LevelReader {
    Document doc;

    /**
     * Creates a reader associated with an XML file
     * @param path The path to the XML file
     * @throws IOException If there is a problem building the XML document
     */
    public XmlLevelReader(String path) throws IOException{
        SAXBuilder b = new SAXBuilder();
        File f = new File(path);
        try {
            doc = b.build(f);
        } catch (JDOMException e) {
            throw new IOException(e);
        }
    }

    /**
     * Creates a reader associated with an Input stream
     * @param stream The Input stream containing the XML
     * @throws IOException If there is a problem building the XML document
     */
    public XmlLevelReader(InputStream stream) throws IOException {
        SAXBuilder b = new SAXBuilder();
        try {
            doc = b.build(stream);
        } catch (JDOMException e) {
            throw new IOException(e);
        }
    }

    /**
     * Reads the level in the XML file
     * @return A new level constructed from the XML document
     * @throws IOException If the document's format is not valid
     */
    @Override
    public Level readLevel() throws IOException {
        Element root = doc.getRootElement();
        if (!root.getQualifiedName().equals("Level")){
            throw new IOException();
        }
        try {
            int width = root.getAttribute("width").getIntValue();
            int height = root.getAttribute("height").getIntValue();
            String name = root.getAttribute("name").getValue();

            List<LevelComponent> components = new LinkedList<>();
            for (Element element : root.getChildren()){
                components.add(LevelComponentFactory.createLevelComponentFrom(element));
            }

            return new Level(name, width, height, components);
        } catch (DataConversionException e) {
            throw new IOException(e);
        }
    }
}
