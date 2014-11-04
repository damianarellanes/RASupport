package RASupport.rasupport.ratoolkit.common;

import RASupport.rasupport.ratoolkit.common.Common.Agents;

/**
 * RAToolkit: factory of agents for the resource aggregation toolkit
 * @author damianarellanes
 */
public interface AgentsFactory {
    public Agent create(Agents agentType, Object...params);
}
