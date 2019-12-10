import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import javax.ejb.EJB;

public class messageTest{

    private message Message;
    private messageEncoder MessageEncoder;
    private messageDecoder MessageDecoder;

    @Before
    public void init(){
        Message = new message();
        Message.setOperation("sharePosition");
        messageOrder MessageOrder = new messageOrder();
        MessageOrder.setAddress("testing address");
        Message.setOrder(MessageOrder);
        MessageEncoder = new messageEncoder();
        MessageDecoder = new messageDecoder();
    }

    @Test
    public void testMessageEncoding() throws Exception
    {
        System.out.println(MessageEncoder.encode(this.Message));

    }

    @Test
    public void testMessageDecoding() throws Exception
    {
        message decodedMessage = MessageDecoder.decode("{\"operation\":\"sharePosition\",\"order\":{\"address\":\"testing address\"}}");
        System.out.println(decodedMessage.getOrder().getAddress());
    }
}