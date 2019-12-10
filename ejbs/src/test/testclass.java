import org.junit.Before;
import org.junit.Test;

import javax.ejb.EJB;

public class testclass{

    private googleService service;

    @Before
    public void init(){
        service = new googleService();
    }

    @Test
    public void testMethod() throws Exception
    {
       // service.testUnmarshall();
        System.out.println("sdfsfd");
    }
}