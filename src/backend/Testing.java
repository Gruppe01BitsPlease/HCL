package backend;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

class FileTest {

    @Test
    public void getFile(){
        File file = new File();
        boolean exists = file.fileExists();
        assertEquals(true,exists);
    }

}
class UserTest{
    @Test
    public void get(){
        assertEquals(1,1);
    }
}