//package test;

import backend.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class testCustomerManager {

    @Test
    public void testCustomerManager1(){

        CustomerManager manager = new CustomerManager(new SQL());

        int generated = manager.generate("Test","Test",123456789);
        assertEquals(1,generated);

        int edited = manager.edit("Test","Test2",987654321);
        assertEquals(1,edited);

        int deleted = manager.delete("Test");
        assertEquals(1,deleted);
    }
    @Test
    public void string(){
        assertEquals("t","t");
    }
}