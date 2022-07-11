/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matchgame;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;



import javax.swing.*;




public class MatchGame implements ActionListener {
	JPanel p,mainMenu;
	JFrame f,menuFrame;
        JLabel score1, score2,winner,blank1,blank2;
        JButton button4x4,button5x5,button6x6,menu;
        
	MyButton[][] buttons;
        MyButton button1, button2;

        ImageIcon icon, defaultIcon;   // icon used for face-up image (BUGs) defaultIcon used for face-down image (PUCCA)
        BufferedImage image,imageA,imageF,imageV;
        
        int state = 0;               // 0=initial , 1=select 1 picture, 2=select 2 pictures
        int matchScore=0, missedScore=0,pic=0;
        boolean matched = false;    // keep previous state of player's card matching.

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread
		//to create application and display its GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MatchGame app = new MatchGame();
				app.mainMenu(); 
			}
		});
	}
        
        //// add parameter dimention, pic and select. Dimention is parameter to calculate how to generate cards each dimention. 
        ////Pic is number of the random card each dimention. select is variable for sent to method addButton .
	public void generateCards(JPanel panel,int dimention,int pic,int select) {

                //// create vocCards array to keep the number of image Vocabulary usage.
                int [] vocCards = new int[18];
                int [] picCards = new int[18];// Keep the number of image usage. Where the index of the array is smaller than the number used as the image name. 
                                              // if 1.png was used as an IconImage of and single button, the cards[0] will be set as 1 
                                              // Later, after 1.png was selected to be another button's IconImage, thus the cards[0] will be set as 2
                                              // And then 1.png file cannot be used as the other buttons. 
                int x,v;

		buttons = new MyButton[6][6]; 
                Random rand = new Random();
                
                for(int i=0; i < 18; i++){
                    picCards[i]=0;   // initial all array's member to be 0
                    vocCards[i]=0;
                }
		for (int i= 0; i< 5-dimention; i++) {   ////reference from question(1), it start i<5 and j<6 , now question(2) has many dimentions so i have to change reasonably calculation.
                    for(int j=0; j<6-dimention; j++){
                       do {
                           v = rand.nextInt(2); //// random to create picture or vocab.
                           x = rand.nextInt(pic);   //// random for each dimentions. ex.)4x4 --> pic = 8   random 1-8 bag card
                           
                           if(picCards[x]<2) 
                           {
                             if(v==0&&picCards[x]<1){picCards[x]++;break;}
                             if(v==1&&vocCards[x]<1){vocCards[x]++;break;}      
                           }
                       }while(true);     
                       //***** a loop to find a random integer x within a range of 0 to numberOfImage - 1 
                       //***** and check if it is available (the image usage times is less than 2)than use it 
                       //***** and increase the usage time of the image by one.
                         
                       addButton(panel,i,j,x+1,select,v);                       
                    }   
                }
                
                x = 0;
                for(int j=0; j<6-dimention; j++){  // This separated loop could solve the slow initialization problem of the last row buttons caused by the random process.
                    
                    //// codition in the last row button.
                   while(true)
                   {if(picCards[x]==1 && vocCards[x]==1) x++;
                    if(picCards[x]==0 && vocCards[x]==0){addButton(panel,5,j,x+1,select,0); picCards[x]++; break;}
                    if(picCards[x]==0 && vocCards[x]==1){addButton(panel,5,j,x+1,select,0); picCards[x]++; break;}
                    if(picCards[x]==1 && vocCards[x]==0){addButton(panel,5,j,x+1,select,1); vocCards[x]++; break;}
                    if(x==pic) break;
                   }
                   
                   
                }               
        }
        //// create button by codition parameter int select for select file load image category .
        //// parameter vocab is codition to choose loadImage into button picture or vocab
        public void addButton(JPanel panel ,int i, int j,int val,int select,int vocab){
                        if(select == 1)   { 
                            if(vocab < 1){String iconFileAnimal =  "animal/" + val + ".png";
                                           imageA = loadImage(iconFileAnimal);}
                            if(vocab >= 1){String iconFileAnimal =  "animalV/" + val + ".png";//// folder vocab
                                           imageA = loadImage(iconFileAnimal);}
                        }
                        
                        if(select == 2)   { 
                            if(vocab < 1){String iconFileAnimal =  "foods/" + val + ".png";
                                           imageA = loadImage(iconFileAnimal);}
                            if(vocab >= 1){String iconFileAnimal =  "foodsV/" + val + ".png";
                                           imageA = loadImage(iconFileAnimal);}
                        }
                        
                        if(select == 3)   {
                            if(vocab < 1){String iconFileAnimal =  "Vegetable/" + val + ".png";
                                           imageA = loadImage(iconFileAnimal);}
                            if(vocab >= 1){String iconFileAnimal =  "VegetableV/" + val + ".png";
                                           imageA = loadImage(iconFileAnimal);}}
                        
                        ImageIcon bIcon1 = new ImageIcon(imageA);
                        //*****  create a new MyButton object and store it at buttons[i][j]
                        MyButton b = new MyButton(); 
                        buttons[i][j] = b;           
                        
                        //*****  call a method of MyButton to initial its icon images and value
                        b.set(defaultIcon ,bIcon1 ,val); 
                         
			buttons[i][j].addActionListener(this);
			panel.add(buttons[i][j]);
        }
        
        //// input parameter to create button for frame "mainMenu" set image to button, add button to contain panel ,and set when click button use method modeMenu to create modeMenu window and close mainMenu window.
         public  void mainMenuButton(JPanel m,String s,JFrame jf,int select){
                JButton b = new JButton();
                ImageIcon ImIc = new ImageIcon(loadImage(s));
                b.setIcon(ImIc);
                m.add(b);
                b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                modeMenu(select);jf.dispose(); //// sent variable int select to method modeMenu and close window.
                     }});    
        }
       //// input parameter to create button for frame "modeButton" , when click the button on modeButton window,the game gennerate and sent parameter to method makeGUI(int i , int j); and set variable pic in actionPerformed(ActionEvent e)
        public  void modeButton(JPanel m,String s,JFrame jf,int i,int j,int k,int select){
                JButton b = new JButton();
                ImageIcon ImIc = new ImageIcon(loadImage(s));
                b.setIcon(ImIc);
                m.add(b);
                b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                makeGUI(i,j,select);pic=k;jf.dispose();; //// sent variable int i,j,select to method makeGUI ,set pic in actionPerformed = int put parameter k  and close window.
                     }});    
        }
        ////input parameter to create button for frame "makeGUI" ,when click the button 
        public  void GUIButton(JPanel m,String s,JFrame jf){
                JButton b = new JButton();
                ImageIcon ImIc = new ImageIcon(loadImage(s));
                b.setIcon(ImIc);
                m.add(b);
                b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                mainMenu();state=0;missedScore=0;matchScore=0;jf.dispose(); //// return to main menu and reset score of game
                ;}});
        }
        
        //// start running programe create menu frame and menu button 
        //// create mainMenu window for select category to play game
        public  void mainMenu(){
                    String defaultIconFile =  "bug/pucca.png";
                    defaultIcon = new ImageIcon(loadImage(defaultIconFile));
                    
                ////create JFrame
                JFrame menuFrame = new JFrame(" Matching Vocabulary Game For Kids");
                menuFrame.setIconImage(loadImage(defaultIconFile));
		menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menuFrame.setPreferredSize(new Dimension(800, 800));   
		menuFrame.setSize(800, 800);    
                
                
                ////create JPanel
                JPanel mainMenu = new JPanel(new GridLayout(4,1,10,10));
                
                JLabel Title = new JLabel(" Category ");  
                Title.setFont(new Font("Calibri", Font.BOLD, 128));
                Title.setHorizontalAlignment(JLabel.CENTER);
                Title.setVerticalAlignment(JLabel.CENTER);
                mainMenu.add(Title);
                
                //// use method mainMenuButton to create button
                mainMenuButton(mainMenu,"bug/foodsButton.png",menuFrame,2);
                mainMenuButton(mainMenu,"bug/animalButton.png",menuFrame,1);
                mainMenuButton(mainMenu,"bug/vegetableButton.png",menuFrame,3);
                
                //// contain panel to show on JFrame
                menuFrame.getContentPane().setLayout(new BorderLayout());
                menuFrame.getContentPane().add(mainMenu);
                
                menuFrame.setResizable(false);
                menuFrame.setEnabled(true);    
                menuFrame.pack();               
                menuFrame.setVisible(true);
                menuFrame.setLocationRelativeTo(null);
                
        }
        
        //// create modeMenu window for select mode of game
        public  void modeMenu(int select){
                    String defaultIconFile =  "bug/pucca.png";
                    defaultIcon = new ImageIcon(loadImage(defaultIconFile));
                    
                JFrame modeFrame = new JFrame(" Select mode ");
                modeFrame.setIconImage(loadImage(defaultIconFile));
		modeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                modeFrame.setPreferredSize(new Dimension(800, 800));   
		modeFrame.setSize(800, 800);                           
                
                ////set layout of button
                JPanel mode = new JPanel(new GridLayout(1,3));
                
		//// use method modeButton to create button
                modeButton(mode,"bug/EasyButton.png",modeFrame,2,8,8,select);
                modeButton(mode,"bug/MediumButton.png",modeFrame,1,12,12,select);
                modeButton(mode,"bug/HardButton.png",modeFrame,0,18,18,select);
               
                //// contrain all objective in JPanel mainMenu to display on JFrame mainMenu
                modeFrame.getContentPane().setLayout(new BorderLayout());
                modeFrame.getContentPane().add(mode);

                modeFrame.setResizable(false);
                modeFrame.setEnabled(true);    
                modeFrame.pack();               
                modeFrame.setVisible(true);
                modeFrame.setLocationRelativeTo(null);
        }
        
        
	//  Creates the JFrame and its UI components.
	//// add parameter int i ,int j int select for sent to method generateCards(...int i,int j,int select)
        //// create game window and generate button
	public  void makeGUI(int i,int j,int select) {	

                        String defaultIconFile =  "bug/pucca.png";
                        image = loadImage(defaultIconFile);

                        //***** create an ImageIcon referenced as defaultIcon using "bug/pucca.png"
                        
                        defaultIcon = new ImageIcon(image); 
                        
		JFrame frame = new JFrame("Match vocab and pictue");
                frame.setIconImage(image);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        
                        //// set layuot for each dimention
                        p = new JPanel(new GridLayout(7-i,6-i)); //// why 7-i,6-i? beacuse i referecne question(1) that 6x6 dimention so i have to change layout of 4x4,5x5 dimention
                        p.setPreferredSize(new Dimension(200, 200));

                        generateCards(p,i,j,select); ////   add parameter i for calculate dimention , j for number of picture 
                        score1 = new JLabel("  Matched Score: 0  ");
                        score2 = new JLabel("Missed Score: 0  ");
                        winner = new JLabel();
                        blank1 = new JLabel(" "); ////space area
                        blank2 = new JLabel(" ");
                        
                        //// set label at center
                        score1.setHorizontalAlignment(JLabel.CENTER);
                        score1.setVerticalAlignment(JLabel.CENTER);
                        
                        score2.setHorizontalAlignment(JLabel.CENTER);
                        score2.setVerticalAlignment(JLabel.CENTER);
                        
                        winner.setHorizontalAlignment(JLabel.CENTER);
                        winner.setVerticalAlignment(JLabel.CENTER);
                        
                        p.add(score1);
                        p.add(score2);
                        p.add(winner);
                        
                        ////make home button stay right corner by space area of each dimentions.
                        if(i==0){p.add(blank1);p.add(blank2);}
                        if(i==1){p.add(blank1);}
                        //// create home button and set picture into button.
                        GUIButton(p,"bug/HomeButton2.png",frame);
                        
                        //// contrain all objective in JPanal p to display on JFrame frame
                        frame.getContentPane().setLayout(new BorderLayout());
                        frame.getContentPane().add(p,BorderLayout.CENTER);
                
		// Display the window.

                //***** resize the contained component proportionally to its preferenced size or current adjusted size (by player at the runtime.
                frame.setPreferredSize(new Dimension(790, 800));   
		frame.setSize(790, 800);                           

                //***** make the application window visible
                frame.setEnabled(true);     
                frame.pack();               
                frame.setVisible(true);     
                frame.setLocationRelativeTo(null); //// start window at center of screen.
                frame.setResizable(false); //// can not Resizable window.
	}
        
        public BufferedImage loadImage(String path){
            BufferedImage image = null;
                            
                try {
			image = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
                
                //***** return the result
                return image; 
        }

	public void actionPerformed(ActionEvent e) {
		int r,c;
               
                 // reset the state and flip the card to the back-face (PUCCA) 
                if (state == 2){              
                    state = 0;
                    if (matched == false) {
                        button1.resetIcon();
                        button2.resetIcon();
                    }
                }
                for(r=0;r<6;r++){
                    for(c=0;c<6;c++){
                        if (e.getSource()== buttons[r][c])
                        {
                           
                            if (state < 1)       // if this is the first card flipping, reference the card as button1 and show its hidden face and change game's state
                            {
                                state++;
                                button1 =  buttons[r][c];
                                button1.setIcon();
                            }
                            else if (state == 1){  
                                if(buttons[r][c] == button1)     // if player click on the same selected button, do nothing
                                    return;                               
                                
                                //*****    reference the card as button2 and show its hidden face and change game's state
                                if(buttons[r][c] != button1) {        
                                    button2 =  buttons[r][c];   
                                    button2.setIcon();         
                                    state++;                  
                                }
                                
                                //***** check if a matched cards were found, increase the corresponding score and check for end of game (all matched cards were found)
                                if (button1.value() == button2.value()) 
                                {   
                                    matchScore++;           
                                    
                                    button1.setBackground(Color.gray);
                                    button2.setBackground(Color.gray);
                                    button1.setEnabled(false);
                                    button2.setEnabled(false);
                                    matched = true;
                                    if (matchScore == pic) {  //// use variable "pic" to check how manay matchScore of each dimention to win.  ex.) 4x4 --> pic = 8 pairing picture. 
                                        System.out.println("You are the winner!!!!");
                                        winner.setText("You are the WINNER!!!");
                                        JOptionPane.showMessageDialog(null, "You are the WINNER!!!"); ////show pop up when you win the game.
                                    }
                                }
                                else {
                                    missedScore++;
                                    matched = false;
                                }                        
                                System.out.println("Matched Score : " + matchScore + "       Missed Score : " + missedScore);
                                
                                //***** update scores shown in JLabel
                                score1.setText("Matched Score = " + String.valueOf(matchScore)); 
                                score2.setText("Missed Score = " + String.valueOf(missedScore)); 
                              
                            }
                        }
                        
                    }
                }
                        
	}
}

