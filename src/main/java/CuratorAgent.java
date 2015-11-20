import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;


public class CuratorAgent extends Agent {
    private static final String NAME = "curator";

    public CuratorAgent(){
        super();
        MessageTemplate mt = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        addBehaviour(new MySimpleAchieveREResponder(this, mt));
    }

    protected void setup(){
        // Artifact search
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("artifact-search");
        sd1.setName("provide-artifact-search");
        sd1.addOntologies("request-ids");

        // Artifact lookup based on item ID
        ServiceDescription sd2 = new ServiceDescription();
        sd2.setType("artifact-lookup");
        sd2.setName("provide-artifact-lookup");
        sd2.addOntologies("request-iteminfo");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd1);
        dfd.addServices(sd2);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private class MySimpleAchieveREResponder extends SimpleAchieveREResponder {

        public MySimpleAchieveREResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        protected ACLMessage prepareResponse(ACLMessage request){
            System.out.println("(Curator) Message received from " + request.getSender().getLocalName());
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.AGREE);
            return reply;
        }

        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response){
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);

            return reply;
        }
    }
}
