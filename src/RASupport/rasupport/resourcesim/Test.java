package RASupport.rasupport.resourcesim;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportmain.RASupportMain;
import RASupport.rasupport.ratoolkit.RAToolkit;
import RASupport.rasupport.resourcesim.resourcesim.ResourceSim;
import RASupport.rasupport.resourcesim.rsimmonitor.RSimMonitor;

/**
 *
 * @author damianarellanes
 */
public class Test {
    
    public TestNode tn = new TestNode();
    
    public class TestNode implements RASupportTopologyNode {
        
        RASupportTopologyNode sp = new RASupportTopologyNode() {

            @Override
            public void createRASupport() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public long getIdNode() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getAlias() {
                return "superpeer100";
            }

            @Override
            public RASupportMain getRASupport() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        @Override
        public void createRASupport() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public long getIdNode() {
            return 10;
        }

        @Override
        public String getAlias() {
            return "testNode";
        }

        @Override
        public RASupportMain getRASupport() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Test t = new Test();
        RASupportNode node = new RASupportNode(t.tn);
        ResourceSim rs = new ResourceSim();
        RAToolkit toolkit = new RAToolkit(node);
        
        node.setDynamicAttributes(rs.obtainDynamicAttributes());
        node.setStaticAttributes(rs.obtainStaticAttributes());
        node.printAttributes(RASupportCommon.AttributeCategories.STATIC_ATTRIBUTE);
        node.printAttributes(RASupportCommon.AttributeCategories.DYNAMIC_ATTRIBUTE);
        
        RSimMonitor monitor = new RSimMonitor(node, toolkit);
        monitor.setUpdatingInterval(0, 1);
        monitor.startMonitor();
        
        System.out.println("OK");
        
    }

}
