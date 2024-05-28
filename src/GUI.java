import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class Box
{
	
	boolean selected;
	boolean hasMine;
	int mineCount;
	
	Box()
	{
		selected=false;
		hasMine=false;
		mineCount=0;
	}
}

public class GUI extends JFrame
{
    
	char gameState;
	
	public char difficulty;
    public int a;//columns
	public int b;//rows
	public int l;//size of cell
	public int spacing;//spacing
	
	public int fontSize;
	public int cellPadding;
	
	public int titleBar=31;
	public int gap=57;
	
	int mx=-100;
    int my=-100;
    
    public int msgX;
    public int msgY;
    
    public int smileyX;
    public int smileyY;
    
    Date startDate;
    public int sec;
    public int timeX;
    public int timeY;
    
    
    Box[][] box;
    
    public GUI(char diff)
    {
        
        difficulty=diff;
    	switch(difficulty)
        {
        	case 'e':
        		a=10;
        		b=10;
        		break;
        	case 'm':
        		a=15;
        		b=15;
        		break;
        	case 'h':
        		a=20;
        		b=20;
        		break;
        	default:
        		this.dispose();
        }
    	
    	a++;
    	b++;
    	
		l=30;
		spacing=2;
		fontSize=20;
		cellPadding=7;
        
        box=new Box[a][b];
        for(int i=0;i<a;i++)
        {
            for(int j=0;j<b;j++)
            {
            	box[i][j]=new Box();
            }
        }
        
        msgX=15;
        msgY=-100;
        
        smileyX=(a*l)/2-25;
        smileyY=2;
        
        timeX=a*l-105;
        timeY=4;
       
        restart();
    	setMines();
        
        this.setTitle("Nonagram");
        this.setSize(a*l+16,b*l+95);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        
        Board board=new Board();
        this.setContentPane(board);
        
        Move move=new Move();
        this.addMouseMotionListener(move);
        
        Click click=new Click();
        this.addMouseListener(click);
    }
    
    public class Board extends JPanel
    {
        @Override
        public void paintComponent(Graphics g)
        {
            //game board
        	g.setColor(Color.DARK_GRAY);
            g.fillRect(0,0,a*l,b*l+gap);
            
            //msgbox
            String msg="";
            if(gameState=='w')
            {
            	msg="YOU WIN!";
            	g.setColor(Color.green);
            	msgY=40;
            }
            g.setFont(new Font("Tahoma",Font.BOLD,30));
            g.drawString(msg,msgX,msgY);
            
            
            //smiley painting
            g.setColor(Color.yellow);
            g.fillOval(smileyX,smileyY,50,50);
            g.setColor(Color.black);
            g.fillOval(smileyX+10,smileyY+12,10,10);
            g.fillOval(smileyX+30,smileyY+12,10,10);
            
            if(gameState=='o')
            {
                g.fillRect(smileyX+12,smileyY+32,26,5);
                
                g.fillRect(smileyX+11,smileyY+34,5,5);
                g.fillRect(smileyX+34,smileyY+34,5,5);
                
                g.fillRect(smileyX+10,smileyY+36,5,5);
                g.fillRect(smileyX+35,smileyY+36,5,5);
            }
            else if(gameState=='w')
            {
                g.fillRect(smileyX+12,smileyY+36,26,5);

                g.fillRect(smileyX+11,smileyY+34,5,5);
                g.fillRect(smileyX+34,smileyY+34,5,5);
                
                g.fillRect(smileyX+10,smileyY+32,5,5);
                g.fillRect(smileyX+35,smileyY+32,5,5);
            }
            
            
            //timer display
            g.setColor(Color.black);
            g.fillRect(timeX,timeY,100,50);
            if(gameState=='o') sec=(int)((new Date().getTime()-startDate.getTime())/1000);
            
            String time=sec+"";
            if(sec<10) time="00"+time;
            if(sec<100 && sec>=10) time="0"+time;
            if(sec>999) time="999";
            
            g.setColor(Color.white);
            if(gameState=='w') g.setColor(Color.green);
            g.setFont(new Font("Tahoma",Font.BOLD,40));
            g.drawString(time,timeX+16,timeY+40);
            
            //divider line
            g.setColor(Color.gray);
            g.fillRect(0,gap-1,a*l,1);
            
            //a by b will be the grid...each box l*l pixels with 'spacing' padding within 
            //cells
            for(int i=0;i<a;i++)
            {
                for(int j=0;j<b;j++)
                {
                    
                    //background
                    g.setColor(new Color(10,110,210));
                    
                    if(box[i][j].selected)
                        g.setColor(Color.white);
                    
                    if(gameState=='w' && box[i][j].hasMine)
                    	g.setColor(Color.red);
                    
                    //background when hover
                    if(mx>=l*i+spacing && mx<l*i+l-spacing && my>=j*l+spacing+gap+titleBar && my<j*l+l-spacing+gap+titleBar)
                        g.setColor(Color.LIGHT_GRAY);
                    
                    if(i==0||j==0)
                    	g.setColor(Color.YELLOW);
                    
                    g.fillRect(l*i+spacing,j*l+spacing+gap, l-2*spacing, l-2*spacing);
                    
                    if((i==0 && j!=0)||((i!=0 && j==0)))
                    {
                    	g.setColor(Color.BLACK);
                    	g.setFont(new Font("Tahoma",Font.BOLD,fontSize));
                    	
                    	if(box[i][j].mineCount<10)
                    		g.drawString(" "+box[i][j].mineCount,l*i+spacing,j*l+spacing+gap+3*cellPadding);
                    	else
                    		g.drawString(""+box[i][j].mineCount,l*i+spacing,j*l+spacing+gap+3*cellPadding);
                    }
                }
            }
        }
    }
    
    public class Move implements MouseMotionListener
    {
    	@Override
        public void mouseMoved(MouseEvent e) {
            mx=e.getX();
            my=e.getY();
        }
    	@Override
        public void mouseDragged(MouseEvent e) {
    		mx=e.getX();
            my=e.getY();
    	}
    }
    
    public class Click implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e)
        {
        	mx=e.getX();
            my=e.getY();
            
            if(inSmiley() && gameState=='o')
            	newGame();
                        	
            int x=inBoxX();
            int y=inBoxY();
            if(x!=-1 && y!=-1 && gameState=='o')
            {
            	select(x,y);
            }
        }
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
    
    void select(int x,int y)
    {
    	if(x!=0 && y!=0)
    	{
    		if(box[x][y].selected)
    			box[x][y].selected=false;
    		else
    			box[x][y].selected=true;
    	}
    	if(checkVictory())
    	{
    		gameState='w';
    		new GameOver(this).setVisible(true);
    	}
    }
    
    boolean checkVictory()
    {
    	/*for(int i=1;i<a;i++)
        {
    		int count=0;
    		for(int j=1;j<b;j++)
    		{
	    		if(box[i][j].selected)
	    			count++;
	        }
    		if(count!=box[i][0].mineCount)
    			return false;
        }
    	for(int j=1;j<b;j++)
        {
    		int count=0;
    		for(int i=1;i<a;i++)
    		{
	    		if(box[i][j].selected)
	    			count++;
	        }
    		if(count!=box[0][j].mineCount)
    			return false;
        }
    	return true;*/
    	
    	for(int i=1;i<a;i++)
        {
    		for(int j=1;j<b;j++)
    		{
	    		if(box[i][j].selected!=box[i][j].hasMine)
	    			return false;
	        }
        }
    	return true;
    }
    
    public void newGame()
    {
    	this.dispose();
    	new LevelChoser().setVisible(true);
    }
    
    public void restart()
    {
    	gameState='o';
    	startDate=new Date();
    	msgY=-100;
    	
    	for(int i=0;i<a;i++)
    	{
            for(int j=0;j<b;j++)
            	box[i][j].selected=false;
        }
    }
    
    void setMines()
    {
    	for(int i=1;i<a;i++)
        {
    		for(int j=1;j<b;j++)
            {
	    		box[i][j].hasMine=false;
            }
        }
    	
    	Random rand=new Random();
    	
    	for(int i=1;i<a;i++)
        {
    		for(int j=1;j<b;j++)
            {
	    		if(rand.nextDouble()>0.5)
	    		{
	    			box[i][j].hasMine=true;
	    			box[i][0].mineCount++;
	    			box[0][j].mineCount++;
	    			
	    			if(rand.nextDouble()>0.1)
	    				box[i][j].selected=true;
	    		}
            }
        }
    }
    
    public boolean inSmiley()
    {
    	double diff=Math.sqrt((mx-(smileyX+25))*(mx-(smileyX+25))+(my-titleBar-(smileyY+25))*(my-titleBar-(smileyY+25)));
    	if(diff<=25)
    		return true;
    	else
    		return false;
    }
    
    public int inBoxX(){
        for(int i=0;i<a;i++){
                for(int j=0;j<b;j++){
                    if(mx>=l*i+spacing && mx<l*i+l-spacing && my>=j*l+spacing+gap+titleBar && my<j*l+l-spacing+gap+titleBar)
                        return i;
                }
            }
        return -1;
    }
    public int inBoxY(){
        for(int i=0;i<a;i++){
                for(int j=0;j<b;j++){
                    if(mx>=l*i+spacing && mx<l*i+l-spacing && my>=j*l+spacing+gap+titleBar && my<j*l+l-spacing+gap+titleBar)
                        return j;
                }
            }
        return -1;
    }
    
}
