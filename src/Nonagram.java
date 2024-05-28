public class Nonagram implements Runnable{
    
    GUI gui;
    
    public static void main(String[] args)
    {
    	new LevelChoser().setVisible(true);
    }
    
    public Nonagram(char difficulty)
    {
    	gui=new GUI(difficulty);
    }
    
    @Override
    public void run() {
        while(true){
            gui.repaint();
        }
    }
    
}