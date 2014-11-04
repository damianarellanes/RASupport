package RASupport.rasupport.ratoolkit.common;

import RASupport.myconet.MycoNode;

/**
 * RAToolkit: representation of an agent in the toolkit
 * @author damianarellanes
 */
public interface Agent {
  
    public abstract void send(MycoNode sender, MycoNode receiver);
    
}
