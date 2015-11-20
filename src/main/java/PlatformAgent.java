import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.SimpleAchieveREInitiator;
import jade.proto.states.MsgReceiver;

import java.io.IOException;

public class PlatformAgent extends Agent {

    private static final String NAME = "platform";
    private FSMBehaviour fsm;

    private AID profiler;
    private String conversationID;
    private User user;

    public PlatformAgent(){
        super();

        fsm = new FSMBehaviour(this);
        fsm.registerFirstState(new WaitRequestsFromProfiler(this), "A");
        fsm.registerState(new AskCurator(this), "B");
        fsm.registerState(new SendItemIDS(this), "C");

        fsm.registerDefaultTransition("A", "B");
        fsm.registerDefaultTransition("B", "C");
        fsm.registerDefaultTransition("C", "A");

        addBehaviour(fsm);
    }

    protected void setup(){
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("provide-tour");
        sd.addOntologies("get-tour-guide");
        register(sd);
    }

    private void register(ServiceDescription sd) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private class WaitRequestsFromProfiler extends MsgReceiver {

        public WaitRequestsFromProfiler(Agent a) {
            super(a, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                    MsgReceiver.INFINITE, new DataStore(), "key");
        }

        @Override
        protected void handleMessage(ACLMessage msg) {
            System.out.println("(Platform) Received msg");
            try {
                profiler = msg.getSender();
                conversationID = msg.getConversationId();
                user = (User) msg.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
    }

    private class AskCurator extends SimpleAchieveREInitiator {

        public AskCurator(Agent a) {
            super(a, new ACLMessage(ACLMessage.INFORM));
        }

        @Override
        protected ACLMessage prepareRequest(ACLMessage msg) {
            try {
                // Get the platform AID
                AID curator;
                DFAgentDescription dfd = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("artifact-search");
                dfd.addServices(sd);
                DFAgentDescription[] result = DFService.search(getAgent(), dfd);
                if (result.length>0) {
                    curator = result[0].getName();

                    // send the first message to the platform to ask for interesting artifacts
                    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                    request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                    request.addReceiver(curator);
                    request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                    request.setOntology(FIPANames.Ontology.SL0_ONTOLOGY);
//                    request.setContentObject(u);
                    return request;
                }
                return null;
            } catch (FIPAException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void handleAgree(ACLMessage msg) {
            System.out.println("Platform: Received AGREE");
        }

        @Override
        protected void handleInform(ACLMessage msg) {
            System.out.println("Platform: Received INFORM");
        }

    }

    private class SendItemIDS extends OneShotBehaviour {

        public SendItemIDS(Agent agent) {
            super(agent);
        }

        @Override
        public void action() {

        }
    }
}

























