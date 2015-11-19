import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.SimpleAchieveREInitiator;
import jade.proto.SimpleAchieveREResponder;
import jade.proto.states.MsgReceiver;

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
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            request.addReceiver(new AID("curator", AID.ISLOCALNAME));
            request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
            request.setOntology(FIPANames.Ontology.SL0_ONTOLOGY);

            return request;
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

























