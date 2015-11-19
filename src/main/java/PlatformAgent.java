import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class PlatformAgent extends Agent {

    public PlatformAgent(){
        super();
        MessageTemplate mt = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        addBehaviour(new MySimpleAchieveREResponder(this, mt));
    }

    private class MySimpleAchieveREResponder extends SimpleAchieveREResponder {

        public MySimpleAchieveREResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response){
            //don't care
            return null;
        }
        protected ACLMessage prepareResponse(ACLMessage request){
            System.out.println("Message received from " + request.getSender().getLocalName());
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.AGREE);
            return reply;
        }
    }
}
