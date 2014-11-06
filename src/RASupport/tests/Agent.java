package RASupport.tests;

import static java.lang.System.exit;

public class Agent implements Cloneable {
        
        private String owner = "";
        private int transport = 100;
        
        public Agent(String owner) {
            this.owner = owner;
        }
        
        public Agent(String owner, int transport) {
            this.owner = owner;
            this.transport =transport;
        }
       
        /**
         * @param transport the transport to set
         */
        public void setTransport(int transport) {
            this.transport = transport;
        }
        
        public void printTransport() {
            System.out.println("Value=" + this.transport);
        }
        
        public void killAgent(Agent a) {
            owner = null;
            a = null;
        }
                
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
        
        @Override
        protected void finalize() {
            
            System.out.println("Killed!");
            //exit(1);
        }
    }