package protocoles.anglaisesscellees.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * tools to help the use of the Directory Facilitator (registration to a
 * service, find agents that have declared a service)
 * 
 * @author revised by Emmanuel ADAM
 */
public final class AgentToolsEA {

	/**
	 * create an Agent Description (model of a service)
	 *
	 * @param typeService
	 *            type of the service
	 * @param nameService
	 *            name of the service (can  be null)
	 * @return the model of the service
	 */
	public static DFAgentDescription createAgentDescription(final String typeService, final String nameService) {
		var model = new DFAgentDescription();
		var service = new ServiceDescription();
		service.setType(typeService);
		service.setName(nameService);
		model.addServices(service);
		return model;
	}

	/**
	 * register an agent to a service with the Directory Facilitator
	 * 
	 * @param myAgent
	 *            agent that have to be registered
	 * @param typeService
	 *            type of the service
	 * @param nameService
	 *            name of the service (could be null but it is prefered to use a
	 *            name (like the agent name)
	 */
	public static void register(final Agent myAgent, final String typeService, final String nameService) {
		var model = createAgentDescription(typeService, nameService);
		try { DFService.register(myAgent, model); }
		catch (FIPAException fe) { fe.printStackTrace(); }
	}

	/***
	 * search agents that are registered to a typical service
	 * 
	 * @param myAgent
	 *            agent that asks for the search (it will be omitted from the
	 *            result)
	 * @param typeService
	 *            type of the service
	 * @param nameService
	 *            name of the service (can be null)
	 * @return AIDs of the agents that are registered to (_typeService, _nameService),
	 *			do not include the AID of myAgent
	 */
	public static AID[] searchAgents(final Agent myAgent, final String typeService, final String nameService) {
		var model = createAgentDescription(typeService, nameService);
		AID[] result = null;
		try {
            DFAgentDescription[] agentsDescription = DFService.search(myAgent, model);
			if (agentsDescription != null) {
			    ArrayList<DFAgentDescription> list = new ArrayList<>(Arrays.asList(agentsDescription));
			    AID myAID = myAgent.getAID();
			    list.removeIf(e->e.getName().equals(myAID));
             	result = list.stream().map(DFAgentDescription::getName).toArray(AID[]::new);
			}
		}
		catch (FIPAException fe) {  fe.printStackTrace(); }
		return result;
	}

}
