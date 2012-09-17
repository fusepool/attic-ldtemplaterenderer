package org.apache.stanbol.commons.ldpathex;


import java.io.File;
import java.io.StringWriter;

import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.commons.ldpath.clerezza.ClerezzaBackend;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.template.engine.TemplateEngine;


@Component
@Service(LDPathTemplateEx.class)
public class LDPathTemplateEx {

	public String getResult(GraphNode node) throws Exception {
		RDFBackend<Resource> backend = new ClerezzaBackend(node.getGraph());
		Resource context = node.getNode();
		StringWriter out = new StringWriter();
		File template = new File("/home/reto/test.fml");
		if(backend != null && context != null && template != null) {
            TemplateEngine<Resource> engine = new TemplateEngine<Resource>(backend);
            engine.setDirectoryForTemplateLoading(template.getParentFile());
            engine.processFileTemplate(context,template.getName(),out);
            out.flush();
            out.close();
        }
		return out.toString();
	}
}
