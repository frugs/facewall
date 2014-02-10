package uk.co.o2.facewall.view;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.AbstractTemplateProcessor;
import org.glassfish.jersey.server.mvc.spi.TemplateProcessor;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerTemplateProcessor implements TemplateProcessor<Template> {

    private final static Configuration configuration = initConfig();

    private static Configuration initConfig() {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(FreemarkerTemplateProcessor.class, "/views");

        DefaultObjectWrapper objectWrapper = new DefaultObjectWrapper();
        objectWrapper.setExposeFields(true);

        configuration.setObjectWrapper(objectWrapper);
        configuration.setDefaultEncoding("UTF-8");

        return configuration;
    }

    @Override public Template resolve(String name, MediaType mediaType) {
        try {
            return configuration.getTemplate(name);
        } catch (IOException e) {
            throw new RuntimeException("Could not load template: " + name, e);
        }
    }

    @Override
    public void writeTo(Template template, final Viewable viewable, MediaType mediaType, OutputStream out) throws IOException {
        try {
            Object model = viewable.getModel();
            if (!(model instanceof Map)) {
                model = new HashMap<String, Object>() {{
                    put("model", viewable.getModel());
                }};
            }
            template.process(model, new OutputStreamWriter(out));
        } catch (TemplateException te) {
            throw new ContainerException(te);
        }
    }
}