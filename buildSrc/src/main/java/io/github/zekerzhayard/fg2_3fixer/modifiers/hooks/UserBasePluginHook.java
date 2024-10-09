package io.github.zekerzhayard.fg2_3fixer.modifiers.hooks;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.gradle.common.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class UserBasePluginHook {
    public static File createWorkspaceXml(File file) throws Throwable {
        if (!file.exists() && file.getParentFile().mkdirs()) {
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<project version=\"4\">\n"
                + "    <component name=\"RunManager\">\n"
                + "    </component>\n"
                + "</project>";
            Files.write(file.toPath(), xml.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        }
        return file;
    }

    public static Element configureRunManagerElement(Document doc, Element root) {
        if (root == null) {
            root = doc.createElement("component");
            root.setAttribute("name", "RunManager");
            doc.appendChild(root);
        } else {
            Document component = root.getOwnerDocument();
            NodeList list = component.getElementsByTagName("configuration");
            for (int i = list.getLength(); i > 0; i--) {
                Element e = (Element) list.item(i - 1);
                String name = e.getAttribute("name");
                if ("Minecraft Client".equals(name) || "Minecraft Server".equals(name)) {
                    e.getParentNode().removeChild(e);
                }
            }
        }
        return root;
    }

    public static Element addGradleBeforeRunTask(Element child) {
        Element method = Constants.addXml(child, "method", ImmutableMap.of("v", "2"));
        Constants.addXml(method, "option", ImmutableMap.of("name", "Gradle.BeforeRunTask", "enabled", "true", "tasks", "classes", "externalProjectPath", "$PROJECT_DIR$"));
        return child;
    }
}
