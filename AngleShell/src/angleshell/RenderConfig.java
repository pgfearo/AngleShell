/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package angleshell;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.SaxonApiException;

/**
 *
 * @author philipfearon
 */
public class RenderConfig {
    
    private final HTMLRender hr= new HTMLRender();
    
    public RenderConfig() {
        InputStream is= loadResource("highlight-file.xsl");
        hr.init(is, new ClasspathResourceURIResolver());
    }
    
    class ClasspathResourceURIResolver implements URIResolver {
      @Override
      public Source resolve(String href, String base) throws TransformerException {
          
        return new StreamSource(getClass().getClassLoader().getResourceAsStream("angleshell/resources/xsl/" + href));
      }
    }
    
    private InputStream loadResource(String resourceName) {
        InputStream is= null;
        URL url= getClass().getClassLoader().getResource("angleshell/resources/xsl/" + resourceName);
        if (url == null) {
            throw new Error("Unable to find resource: " + resourceName + " ");
        }
        try {
            is= url.openStream();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("url: " + url.getFile());
        return is;
    }
    
    private void runRenderingTask(String inputURI, Boolean inputIsURI){
        resetStatusText();
        try {
            Map paramMap = getXslParameters(); 
            hr.run(paramMap, inputURI, inputIsURI, (String outPath) -> {
                String pfx = outPath.substring(0,1);
                if (pfx.equals("[")){
                    //statusField.appendText(outPath + "\n");
                } else {
                    String fullHTMLString = "";
                    try {

                        // br fix required because html output-method seems to be affected by xhtml namespace
                        fullHTMLString = HTMLRender.getFileContent("file:///" + outPath).replace("<br></br>", "<br />");
                        fullHTMLString = fullHTMLString.replace("<body>", "<body style=\"background-color:#454545;\">");
                        int initStart = fullHTMLString.indexOf("style=") + 1;
                        int start = fullHTMLString.indexOf("style=", initStart) + 7;
                        int end = fullHTMLString.lastIndexOf("</pre>") + 6;
                        String divString = "<pre style=\"white-space: nowrap; ";
                        String preString = divString + fullHTMLString.substring(start, end);
                    } catch (Exception e){
                        System.err.println("Error: " + e.getMessage());
                    }

                }
            });
        } catch (SaxonApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resetStatusText() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Map getXslParameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
