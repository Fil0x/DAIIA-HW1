import jade.core.Agent;
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
