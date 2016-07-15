public class TestProzess extends Thread{

    public TestProzess(){
    }

    public void run(){
        for(int i = 0; i<10; i++){
            try{
                sleep(500);
            }
            catch(InterruptedException e){
            }
            System.out.println("Prozess1:"+500);
        }
    }

}