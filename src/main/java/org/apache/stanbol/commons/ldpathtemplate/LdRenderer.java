package org.apache.stanbol.commons.ldpathtemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import javax.servlet.ServletOutputStream;

import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.commons.ldpath.clerezza.ClerezzaBackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.template.engine.TemplateEngine;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
@Service(LdRenderer.class)
public class LdRenderer {
	
	private static final Logger log = LoggerFactory.getLogger(LdRenderer.class);

	public void render(GraphNode node, final URL template, Writer out) {
		//cfg.setTemplateLoader(new ClassTemplateLoader(getClass(),""));
		//Configuration cfg = new Configuration();
		TemplateLoader templateLoader = new TemplateLoader() {
			
			@Override
			public Reader getReader(Object templateSource, String encoding)
					throws IOException {
				URL templateUrl = (URL) templateSource;
				return new InputStreamReader(templateUrl.openStream(), encoding);
			}
			
			@Override
			public long getLastModified(Object templateSource) {
				// not known
				return -1;
			}
			
			@Override
			public Object findTemplateSource(String name) throws IOException {
				if ("root".equals(name)) {
					return template;
				}
				log.warn("Template "+name+" not known");
				return null;
			}
			
			@Override
			public void closeTemplateSource(Object templateSource) throws IOException {
	
				
			}
		};
		RDFBackend<Resource> backend = new ClerezzaBackend(node.getGraph());
		Resource context = node.getNode();
		TemplateEngine<Resource> engine = new TemplateEngine<Resource>(backend);
		engine.setTemplateLoader(templateLoader);
		try {
			engine.processFileTemplate(context, "root", null, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}
}
