package org.apache.stanbol.commons.ldpathtemplate;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.commons.ldpath.clerezza.ClerezzaBackend;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.template.engine.TemplateEngine;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
@Service(LdRenderer.class)
public class LdRenderer {

	public void render(GraphNode node, Template temp, Writer out) {
		RDFBackend<Resource> backend = new ClerezzaBackend(node.getGraph());
		Resource context = node.getNode();
		TemplateEngine<Resource> engine = new TemplateEngine<Resource>(backend);
		try {
			engine.processTemplate(context, temp, null, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}
}
