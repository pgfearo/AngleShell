package angleshell;

import javax.xml.transform.SourceLocator;
import net.sf.saxon.s9api.MessageListener;
import net.sf.saxon.s9api.XdmNode;

/**
 *
 * @author Philip
 */
public class TransformMessageListener implements MessageListener  {
    
    private FXListener listener;
    
    public TransformMessageListener(FXListener listener){        
        this.listener = listener;
    }

    public void message(XdmNode xn, boolean bln, SourceLocator sl) {
        String msg = xn.getStringValue();
        listener.callback(msg);
    }
    
}
