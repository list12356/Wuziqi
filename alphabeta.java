import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;
import java.lang.Math;

/* TopLevelDemo.java requires no other files. */
public class alphabeta {
    static private JFrame frame;
	static private JMenuBar menuBar;
	static private JPanel mainPane;
	static private myButton buttons[][];
	static private ImageIcon OO;
	static private ImageIcon XX;
	static private boolean First;

	
	
	static int[][] Table;
	static int[][] Value;
	static int table_size=15;
	static int MAX_STEP=4;
	
	static class myButton extends JButton
	{
		public int i;
		public int j;
		myButton(int a,int b)
		{
			i=a;
			j=b;
		}
	}
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Min-Max");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		frame.setPreferredSize(new Dimension(500,500));

        //Create the menu bar.  Make it have a green background.
        menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(200, 200, 200));
        menuBar.setPreferredSize(new Dimension(200, 20));
		frame.setJMenuBar(menuBar);
		
        
		mainPane = new JPanel();
		mainPane.setBounds(20, 20, 460, 500);	
		frame.getContentPane().add(mainPane);
		mainPane.setLayout(null);		
	
		
		JLabel Background = new JLabel();
        Background.setOpaque(true);
        Background.setBackground(new Color(255, 255, 255));
        Background.setPreferredSize(new Dimension(640, 480));
        mainPane.add(Background);
		
		buttons=new myButton[table_size][table_size];
		
		class ButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				int i=((myButton)e.getSource()).i;
				int j=((myButton)e.getSource()).j;
				if(Table[i][j]==0)
				{
					if(First==false)
					{
					Table[i][j]=-1;
					buttons[i][j].setIcon(XX);
					decision(Table,1);
					}
					if(First==true)
					{
					Table[i][j]=1;
					buttons[i][j].setIcon(OO);
					decision(Table,-1);
					}
				}
			}
        }
		ButtonListener listen=new ButtonListener();
		OO = new ImageIcon("OO.jpg");
		XX  = new ImageIcon("XX.jpg");
		for(int i=0;i<table_size;i++)
		{
			for(int j=0;j<table_size;j++)
			{ 
				buttons[i][j]=new myButton(i,j);
				buttons[i][j].addActionListener(listen);
				buttons[i][j].setBounds(30 * j, 30 * i, 25, 25);
				mainPane.add(buttons[i][j]);
			}
		}
		First=true;
		if(First==false)
		{
			int a=(int)(1+Math.random()*2);
			int b=(int)(1+Math.random()*2);
			buttons[a][b].setIcon(OO);
			Table[a][b]=1;
		}//Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	static private void initiate()
	{
		Table=new int[table_size][table_size];
		Value=new int[table_size][table_size];
		for(int i=0;i<table_size;i++)
		{
			for(int j=0;j<table_size;j++)
				Table[i][j]=0;
		}
		for(int i=0;i<=table_size/2;i++)
		{
			for(int j=i;j<=table_size/2;j++)
			{
				Value[i][j]=i;
				Value[i][table_size-1-j]=i;
				Value[table_size-1-i][j]=i;
				Value[table_size-1-i][table_size-1-j]=i;
			}
		}
	}
	static private void decision(int [][]state,int turn)
	{
		int[] action=new int[]{0,0};
		int result=utility(state,turn);
		if(result==2000||result==-2000)
		{
			System.out.printf("%d",result);
			return;
		}
		//if(turn==1) //max
			int v=-20000*turn;
			int tmp=0;
			for(int i=0;i<table_size;i++)
			{
				for(int j=0;j<table_size;j++)
				{
					if(state[i][j]==0&&neighbor(i,j)==true)
					{
						state[i][j]=turn;
						if(turn==1)
							tmp=MIN(state,2000,-2000,turn);
						else
							tmp=MAX(state,2000,-2000,turn);
						if(v<tmp&&turn==1||v>tmp&&turn==-1)
						{
							v=tmp;
							action[0]=i;
							action[1]=j;
						}
						else if(v==tmp&&Math.random()>0.6)
						{
							v=tmp;
							action[0]=i;
							action[1]=j;
						}
						state[i][j]=0;
						System.out.printf("%d,%d,%d\n",i,j,tmp);
					}
				}
			}
			state[action[0]][action[1]]=turn;
			if(turn==1)
				buttons[action[0]][action[1]].setIcon(OO);
			else
				buttons[action[0]][action[1]].setIcon(XX);
		// if(turn==-1) //min
		// {
			// int v=20000;
			// for(int i=0;i<table_size;i++)
			// {
				// for(int j=0;j<table_size;j++)
				// {
					// if(state[i][j]==0&&neighbor(i,j)==true)
					// {
						// state[i][j]=-1;
						// int tmp=MAX(state,2000,-2000,1);
						// alpha=Math.min(tmp,alpha);
						// if(v>tmp)
						// {
							// v=tmp;
							// action[0]=i;
							// action[1]=j;
						// }
						// else if(v==tmp&&Math.random()>0.6)
						// {
							// v=tmp;
							// action[0]=i;
							// action[1]=j;
						// }
						// state[i][j]=0;
						// System.out.printf("%d,%d,%d\n",i,j,tmp);
					// }
				// }
			// }
			// if(v==2000)
			// {
				// JOptionPane.showInternalMessageDialog(frame.getContentPane(), "AI surrender!","Information", JOptionPane.INFORMATION_MESSAGE);	
				// return;
			// }
			// state[action[0]][action[1]]=-1;
			// buttons[action[0]][action[1]].setIcon(XX);
			// buttons[action[0]][action[1]].requestFocus();
		System.out.printf("decide, %d,%d\n",action[0],action[1]);
	}
	static boolean neighbor(int a,int b)
	{
		for(int i=-1;i<2;i+=1)
			for(int j=-1;j<2;j+=1)
				if(!(i==0&&j==0)&&a+i>=0&&a+i<table_size&&b+j>=0&&b+j<table_size&&Table[a+i][b+j]!=0)
					return true; 
		return false;
	}
	static private int MAX(int[][] state,int alpha,int beta,int step)
	{
		int up=utility(state,1);
		if(up==2000)
			return up;
		int low=utility(state,-1);
		if(low==-2000)
			return low;
		if(step>=MAX_STEP)
			return up-low;
		int v=-2000;
		for(int i=0;i<table_size;i++)
		{
			for(int j=0;j<table_size;j++)
			{
				
				if(state[i][j]==0&&neighbor(i,j)==true)
				{
					state[i][j]=1;
					v=Math.max(v,MIN(state,alpha,beta,step+1));
					state[i][j]=0;
					if(v>=alpha)
						return v;
					beta=Math.max(beta,v);
				}
			}
		}
		return v;
	}
	static private int MIN(int[][] state,int alpha,int beta,int step)
	{
		int up=utility(state,1);
		if(up==2000)
			return up;
		int low=utility(state,-1);
		if(low==-2000)
			return low;
		if(step>=MAX_STEP)
			return up-low;
		int v=2000;
		for(int i=0;i<table_size;i++)
		{
			for(int j=0;j<table_size;j++)
			{
				if(state[i][j]==0&&neighbor(i,j)==true)
				{
					state[i][j]=-1;
					v=Math.min(v,MAX(state,alpha,beta,step+1));
					state[i][j]=0;
					if(v<=beta)
						return v;
					alpha=Math.min(v,alpha);
				}
			}
		}
		return v;
	}
	static private boolean terminal(int[][] state,int alpha,int beta,int step)
	{
		if(step==10)
			return true;
		else return false;
	}
	static private int utility(int[][] state,int side)
	{
		int max_count=0;
		for(int i=0;i<table_size;i++)
		{
			//横
			int j=0;
			int count=0;
			while(count<5&&j<table_size)
			{
				if(state[i][j]==side)
					count++;
				if(count==5)
					return side*2000;
				if(count>=max_count)
					max_count=count;
				if(state[i][j]!=side)
					count=0;
				j++;
			}
			//竖
			j=0;
			count=0;
			while(count<5&&j<table_size)
			{
				if(state[j][i]==side)
					count++;
				if(count==5)
					return side*2000;
				if(count>=max_count)
					max_count=count;
				if(state[j][i]!=side)
					count=0;
				j++;
			}
			//左上-右下
			j=0;
			int k=i;
			count=0;
			while(count<5&&k<table_size)
			{
				if(state[k][j]==side)
					count++;
				if(count==5)
					return side*2000;
				if(count>=max_count)
					max_count=count;
				if(state[k][j]!=side)
					count=0;
				j++;
				k++;
			}
			//左上-右下
			j=0;
			k=i;
			count=0;
			while(count<5&&k<table_size)
			{
				if(state[j][k]==side)
					count++;
				if(count==5)
					return side*2000;
				if(count>=max_count)
					max_count=count;
				if(state[j][k]!=side)
					count=0;
				j++;
				k++;
			}
			//右上-左下
			j=0;
			k=i;
			count=0;
			while(count<5&&k>=0)
			{
				if(state[j][k]==side)
					count++;
				if(count==5)
					return side*2000;
				if(count>=max_count)
					max_count=count;
				if(state[j][k]!=side)
					count=0;
				j++;
				k--;
			}
			//右上-左下
			j=i;
			k=table_size-1;
			count=0;
			while(count<5&&k>=0&&j<table_size)
			{
				if(state[j][k]==side)
					count++;
				if(count==5)
					return side*2000;
				if(count>=max_count)
					max_count=count;
				if(state[j][k]!=side)
					count=0;
				j++;
				k--;
			}
		}
		return max_count*100;
	}
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				initiate();
                createAndShowGUI();
            }
        });
    }
}