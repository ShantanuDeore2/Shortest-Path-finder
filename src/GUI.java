//package myPackage;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;

import java.lang.System;
import java.util.PriorityQueue;
import java.util.Stack;

public class GUI implements ActionListener{
	
	public static int SCREENSIZE = 100;
	public static int x1=-1 ,y1= -1,x2= -1,y2= -1;
    public static Stack<String> stack = new Stack<String>();
	
	GUI(){
		
	}
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
        SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.add(new MyPanel());
        f.pack();
        f.setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("button");
		
	} 
}

class MyPanel extends JPanel implements ActionListener{

    private int squareX = 50;
    private int squareY = 50;
    private int squareW = 20;
    private int squareH = 20;
    private JButton button;
    
    public MyPanel() {

        setBorder(BorderFactory.createLineBorder(Color.black));
        button = new JButton();
        button.addActionListener(this);
        this.add(button);
        

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
            }
        });
        
    }
    
    private void moveSquare(int x, int y) {
    	if(GUI.x1==-1 && GUI.y1==-1)
    	{
    		GUI.x1=x;
    		GUI.y1=y;
        	//System.out.println("gonna paint");
        	paintComponent(this.getGraphics(),x,y,1);
    	}
    	else if (GUI.x2==-1 && GUI.y2==-1)
    	{
    		GUI.x2=x;
    		GUI.y2=y;
        	//System.out.println("gonna paint");
        	paintComponent(this.getGraphics(),x,y,1);
    	}
    	else
    	{
    		String black = String.valueOf(x) + "_" + String.valueOf(y);
    		GUI.stack.push(black);
        	//System.out.println("gonna paint");
        	paintComponent(this.getGraphics(),x,y,2);
    	}
    	
    	System.out.println("painted " + x + " " + y);
    	//System.out.println("x1 y1 x2 y2 " + GUI.x1 + " " + GUI.y1 + " "+  GUI.x2 + " " +GUI.y2);
    }
    

    public Dimension getPreferredSize() {
        return new Dimension(GUI.SCREENSIZE,GUI.SCREENSIZE);
    }
    
    protected void paintComponent(Graphics g,int x, int y,int c)
    {
    	if(c==1)
    		g.setColor(Color.RED);
    	else if (c==2)
    		g.setColor(Color.BLACK);
    	else if (c==3)
    		g.setColor(Color.GREEN);
    	//System.out.println("pc");
    	g.fillRect(x,y,1,1);
    }
    

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == button)
		{
			System.out.println("button");
			ShortestPath s = new ShortestPath(this);
		}
	}  	
}

class pqElements implements Comparable<pqElements>{
	int value, x, y;

	public pqElements(int value, int x, int y) {
		this.value = value;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int compareTo(pqElements o) {
		// TODO Auto-generated method stub
		if(value > o.value) {
            return 1;
        } else if (value < o.value) {
            return -1;
        } else {
            return 0;
        }
	}
}

class ShortestPath{
	
	private int[][] iValue = new int[GUI.SCREENSIZE][GUI.SCREENSIZE];
	private String[][] iParent = new String[GUI.SCREENSIZE][GUI.SCREENSIZE];
	private PriorityQueue<pqElements> pq = new PriorityQueue<pqElements>();
	private MyPanel Mypanel;
	public ShortestPath(MyPanel panel) {
		Mypanel = panel;
		for(int i=0;i<GUI.SCREENSIZE;i++) {
			for(int j=0;j<GUI.SCREENSIZE;j++)
			{
				iValue[i][j] = Integer.MAX_VALUE;
			}
		}
		
		while(!GUI.stack.empty())
		{
			String temp = GUI.stack.peek();
			String[] parts = temp.split("_");
			int x1 = Integer.parseInt(parts[0]);
			int y1 = Integer.parseInt(parts[1]);
			iValue[x1][y1] = -1;
			//System.out.println(x1  + " " + y1);
			GUI.stack.pop();
		}
		
		iValue[GUI.x1][GUI.y1] = 0;
		pqElements e = new pqElements(0, GUI.x1, GUI.y1);
		pq.add(e);
		findsp();
	}
	
	public void findsp()
	{
		int x = 0;
		while(!pq.isEmpty()){
			int val = pq.peek().value;
			int x1 = pq.peek().x;
			int y1 = pq.peek().y;
			if(x < 5) System.out.println("pq size = " + pq.size());
			pq.poll();
			if(x < 5) System.out.println("pq size = " + pq.size());
			
			x++;
			
			if(x1 == GUI.x2 && y1 == GUI.y2) {
				
				
				System.out.println(val + 1);
				
				while(iParent[x1][y1] != String.valueOf(GUI.x1) + "_" + String.valueOf(GUI.y1))
				{
					System.out.println("making path");
					System.out.println("x1 y1 x2 y2 " + GUI.x1 + " " + GUI.y1 + " "+  GUI.x2 + " " +GUI.y2);
					if (x1 != GUI.x1 || y1 != GUI.y1)
					{
						String[] parts = iParent[x1][y1].split("_");
						int px = Integer.parseInt(parts[0]);
						int py = Integer.parseInt(parts[1]);
						Mypanel.paintComponent(Mypanel.getGraphics(),px,py,1);
						System.out.println(px + " " + py);
						x1 = px;y1=py;
					}
					else {
						System.out.println("exit");
						return;
					}
				}
			}
			
			//right
			if(x1+1 <= GUI.SCREENSIZE && iValue[x1+1][y1] !=-1 && iValue[x1][y1] + 1 <= iValue[x1+1][y1])
			{
				//System.out.println("right");
				iValue[x1+1][y1] = iValue[x1][y1] + 1;
				pqElements newMember = new pqElements(iValue[x1+1][y1], x1+1, y1);
				pq.add(newMember);
				iParent[x1+1][y1] = String.valueOf(x1) + "_" + String.valueOf(y1);
				Mypanel.paintComponent(Mypanel.getGraphics(),x1+1,y1,3);
			}
			
			//left
			if(x1-1 >= 1 && iValue[x1-1][y1] !=-1 && iValue[x1][y1] + 1 <= iValue[x1-1][y1])
			{
				//System.out.println("left");
				iValue[x1-1][y1] = iValue[x1][y1] + 1;
				pqElements newMember = new pqElements(iValue[x1-1][y1], x1-1, y1);
				pq.add(newMember);
				iParent[x1-1][y1] = String.valueOf(x1) + "_" + String.valueOf(y1);
				Mypanel.paintComponent(Mypanel.getGraphics(),x1-1,y1,3);
			}
			
			//down
			if(y1+1 <= GUI.SCREENSIZE && iValue[x1][y1+1] !=-1 && iValue[x1][y1] + 1 <= iValue[x1][y1+1])
			{
				//System.out.println("down");
				iValue[x1][y1+1] = iValue[x1][y1] + 1;
				pqElements newMember = new pqElements(iValue[x1][y1+1], x1, y1+1);
				pq.add(newMember);
				iParent[x1][y1+1] = String.valueOf(x1) + "_" + String.valueOf(y1);
				Mypanel.paintComponent(Mypanel.getGraphics(),x1,y1+1,3);
			}
			
			//up
			if(y1-1 >= 1 && iValue[x1][y1-1] !=-1 && iValue[x1][y1] + 1 <= iValue[x1][y1-1])
			{
				//System.out.println("up");
				iValue[x1][y1-1] = iValue[x1][y1] + 1;
				pqElements newMember = new pqElements(iValue[x1][y1-1], x1, y1-1);
				pq.add(newMember);
				iParent[x1][y1-1] = String.valueOf(x1) + "_" + String.valueOf(y1);
				Mypanel.paintComponent(Mypanel.getGraphics(),x1,y1-1,3);
			}
			
		}
		if (iValue[GUI.x2][GUI.y2] != Integer.MAX_VALUE)
			System.out.println("value is"  + iValue[GUI.x2][GUI.y2]);
		else
			System.out.println("no path!");
		return;
	}
	
}