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
import java.util.LinkedList;
import java.util.List;

public class XmlLevelReader implements LevelReader {
    Document doc;

    public XmlLevelReader(String path) throws IOException{
        SAXBuilder b = new SAXBuilder();
        File f = new File(path);
        try {
            doc = b.build(f);
        } catch (JDOMException e) {
            throw new IOException(e);
        }
    }

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
