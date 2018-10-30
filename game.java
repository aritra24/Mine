/*
Program Name: Mines
Author : Aritra Basu
Description : A game of Mines

Edited By : Shreyansh Murarka
*/

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.time.Duration;
import java.time.Instant;
import java.lang.*;


class Game extends JFrame
{
	//set variables
	static Instant startTime,endTime;
	boolean startedTime=false,wonCalled=false;
	int rows=10,columns=10;
	Random random = new Random();
	JButton[][] grids = new JButton[rows][columns];
	JButton start = new JButton("reset");
	final int CELL_HEIGHT = 40, CELL_LENGTH = 40, CELL_PADDING = 5, PANEL_BORDER = 25;
	
	Color backgroundColor = new Color(103,200,190);
	Color cellColor = new Color(134,134,134);
	Color startColor = new Color(146,142,202);
	Color numColor = new Color(0,0,0);
	Color postCellColor = new Color(224,224,224);
	
	Border mainBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
	ListenForGridButton buttonClicked =new ListenForGridButton();
	ListenForStartButton startButtonClicked = new ListenForStartButton();
	

	public Game()
	{
		this.setSize(CELL_LENGTH * (columns + 4) + CELL_PADDING * columns , CELL_HEIGHT * (rows + 1) + CELL_PADDING * rows + PANEL_BORDER * 2);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel= new JPanel();
		JPanel settingsPanel = new JPanel();
		JPanel outerPanel = new JPanel();
		FlowLayout outerLayout = new FlowLayout();
		
		outerPanel.setLayout(outerLayout);
		mainPanel.setBorder(mainBorder);
		GridLayout mainLayout = new GridLayout(rows, columns, CELL_PADDING, CELL_PADDING);
		mainPanel.setLayout(mainLayout);
		mainPanel.setBackground(backgroundColor);
		settingsPanel.setBackground(backgroundColor);
		outerPanel.setBackground(backgroundColor);
		
		//make buttons and set all to zero
		for(int i = 0;i < rows;i++)
			for(int j = 0;j < columns;j++)
			{
				grids[i][j] = new JButton("0");
				grids[i][j].setEnabled(true);
				grids[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				grids[i][j].setFont(new Font("Ariel",Font.PLAIN,25));
				grids[i][j].setFocusPainted(false);
				grids[i][j].setMargin(new Insets(0, 0, 0, 0));
				grids[i][j].setPreferredSize(new Dimension(CELL_LENGTH, CELL_HEIGHT));
				grids[i][j].setBackground(cellColor);
				grids[i][j].setForeground(cellColor);
				grids[i][j].setBorderPainted(false);
				grids[i][j].addActionListener(buttonClicked);
				mainPanel.add(grids[i][j]);
			}

		setMines();
		start.addActionListener(startButtonClicked);
		start.setBackground(startColor);
		start.setForeground(numColor);
		start.setBorderPainted(false);
		start.setFocusPainted(false);
		settingsPanel.add(start);
		outerPanel.add(mainPanel);
		outerPanel.add(settingsPanel);
		outerPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.add(outerPanel);
		this.setVisible(true);
	}

	//set the mines and increment the values around it
	void setMines()
	{
		int x=(int)Math.sqrt(rows*columns);
		// int x= 1;
		int mineCount=0;
		while(mineCount<x)
		{
			int p = random.nextInt(rows);
			int q = random.nextInt(columns);
			if(grids[p][q].getText()!="-1")
			{
				grids[p][q].setText("-1");
				mineCount++;
			}
		}
		for(int r = 0; r < rows; r++)
			for( int c = 0; c < columns; c++)
				if( grids[r][c].getText()=="-1")
					for(int i = r-1; i < r+2; i++)
						for(int j = c-1; j < c+2; j++)
							if(i > -1 && i < rows && j> -1 && j < columns && (i != r || j != c))
								if(!grids[i][j].getText().equals("-1"))
									grids[i][j].setText(""+(1+Integer.parseInt(grids[i][j].getText())));
	}

	//Set the Button with a Mine Icon
	void setMineIcon(JButton a)
	{
		ImageIcon icon = new ImageIcon("./icon.png");
		Image img = icon.getImage(); 
   		Image newimg = img.getScaledInstance( CELL_LENGTH, CELL_LENGTH,  java.awt.Image.SCALE_SMOOTH );  
   		icon = new ImageIcon( newimg);
   		a.setHorizontalAlignment(SwingConstants.LEFT);
   		a.setIcon(icon);
   		a.setDisabledIcon(icon);
	}

	//restart the game when start button is clicked
	void restartGame()
	{
		for(int i = 0;i < rows;i++)
			for(int j = 0;j < columns;j++)
			{
				grids[i][j].setText("0");
				grids[i][j].setIcon(null);
				grids[i][j].setEnabled(true);
				grids[i][j].setFont(new Font("Ariel",Font.PLAIN,25));
				grids[i][j].setFocusPainted(false);
				grids[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				grids[i][j].setMargin(new Insets(0, 0, 0, 0));
				grids[i][j].setPreferredSize(new Dimension(CELL_LENGTH, CELL_HEIGHT));
				grids[i][j].setBackground(cellColor);
				grids[i][j].setForeground(cellColor);
				grids[i][j].setBorderPainted(false);
			}
		setMines();
	}

	void won()
	{
		if(!wonCalled)
		{
			wonCalled=true;
			for(int i = 0; i < rows; i++)
						for(int j = 0; j < columns; j++)
							if(grids[i][j].getText()=="-1")
							{
								grids[i][j].setText("");
								grids[i][j].setEnabled(false);
							}
			Duration totalTime = Duration.between(startTime,endTime);
			try
			{
				//Smooth out the time taken to show the Game won Panel
				Thread.sleep(300);
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			String time=totalTime.toMinutes()+":"+((totalTime.toMillis()/1000)%60);
			JOptionPane.showMessageDialog(null,"Yay, you won the game\n Time Taken = "+time,"Winning Message",JOptionPane.INFORMATION_MESSAGE,null);
		}
	}

	public class clicker extends Thread
	{
		JButton tempButton;
		int row, column;
		clicker(JButton a, int r, int c)
		{
			tempButton=a;
			row=r;
			column=c;
			start();
		}
		public void run()
		{
			if(tempButton.isEnabled())
				tempButton.doClick();
		}
	}

	class ListenForGridButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(!startedTime)
			{
				startTime=Instant.now();
				startedTime=true;
			}
			JButton w = (JButton) e.getSource();
			for(int i = 0; i < rows; i++)
				for(int j = 0; j < columns; j++)
					if(w==grids[i][j])
					{
						this.recursiveClick(w,i,j);
					}
		}


		//function when a mine is clicked
		void recursiveClick(JButton a, int r, int c)
		{
			if(a.getText().equals("-1"))
			{
				a.setBackground(Color.red);
				a.setEnabled(false);
				setMineIcon(a);
				gameOver();
			}
			else if(a.getText().equals("0"))
			{
				a.setEnabled(false);
				a.setText("");
				a.setBackground(postCellColor);
				for(int i = r-1; i < r+2; i++)
					for(int j = c-1; j < c+2; j++)
						if(i > -1 && i < rows && j > -1 && j < columns)
							try
							{
								Thread.sleep(25);
								new clicker(grids[i][j],i,j);
								// this.recursiveClick(grids[i][j],i,j);
							}
							catch(InterruptedException e)
							{
								System.out.println(e);
							}
			}
			else
			{
				a.setBackground(postCellColor);
				a.setEnabled(false);
			}
			if(ifWon())
			{
				endTime=Instant.now();
				won();
			}
		}
		void gameOver()
		{
			for(int i = 0; i < rows; i++)
					for(int j = 0; j < columns; j++)
					{
						if(grids[i][j].getText()=="-1")
							setMineIcon(grids[i][j]);
						if(grids[i][j].getBackground()==cellColor)
							grids[i][j].setText("");
						grids[i][j].setEnabled(false);
					}
			JOptionPane.showMessageDialog(null,"Sorry, you've lost the game","Game Over",JOptionPane.INFORMATION_MESSAGE,null);
		}

		//check if the game has been won
		boolean ifWon()
		{
			int count=0;
			for( int i =0; i < rows; i++)
				for( int j = 0; j < columns; j++)
					if(grids[i][j].getBackground()==postCellColor)  //colour of the clicked grids are 
						count+=1;									//changed so check the colours
			if(count==rows*columns-(int)Math.sqrt(rows*columns))
				return true;
			return false;
		}
	}

	//Game is won

	class ListenForStartButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			wonCalled=false;
			startedTime=false;
			restartGame();
		}
	}

	//start the game
	public static void main(String[] args) 
	{
		Game game = new Game();
	}
}