package com.TETOSOFT.test;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.ImageIcon;

import com.TETOSOFT.graphics.ScreenManager;

/**
    Simple abstract class used for testing. Subclasses should
    implement the draw() method.
 */
public abstract class GameCore {

	protected static final int FONT_SIZE = 18;

//	private static final DisplayMode POSSIBLE_MODES[] = {
//			new DisplayMode(800, 600, 32, 0),
//			new DisplayMode(800, 600, 16, 0),
//			new DisplayMode(800, 600, 24, 0),
//			new DisplayMode(640, 480, 16, 0),
//			new DisplayMode(640, 480, 32, 0),
//			new DisplayMode(640, 480, 24, 0),
//			new DisplayMode(1024, 768, 16, 0),
//			new DisplayMode(1024, 768, 32, 0),
//			new DisplayMode(1024, 768, 24, 0),
//	};

	private boolean isRunning;
	private boolean isPause;
	protected boolean hasDied;
	protected ScreenManager screen;


	/**
        Signals the game loop that it's time to quit
	 */
	public void stop(boolean esc) {
		if(esc) {
			if(isPause) { 
				isPause = false;
				hasDied = false; //if game is paused and esc is pressed then the game was restarted
			}
			else isPause = true;
		}
		else 	//space pressed
			if(isPause) {
				isPause = false;
				isRunning = false; //exit the game
			}
	}


	/**
        Calls init() and gameLoop()
	 */
	public void run() {
		try {
			init();
			gameLoop();
		}
		finally {
			screen.restoreScreen();
			lazilyExit();
		}
	}


	/**
        Exits the VM from a daemon thread. The daemon thread waits
        2 seconds then calls System.exit(0). Since the VM should
        exit when only daemon threads are running, this makes sure
        System.exit(0) is only called if neccesary. It's neccesary
        if the Java Sound system is running.
	 */
	public void lazilyExit() {
		Thread thread = new Thread() {
			public void run() {
				// first, wait for the VM exit on its own.
				try {
					Thread.sleep(2000);
				}
				catch (InterruptedException ex) { }
				// system is still running, so force an exit
				System.exit(0);
			}
		};
		thread.setDaemon(true);
		thread.start();
	}


	/**
        Sets full screen mode and initiates and objects.
	 */
	public void init() 
	{
		screen = new ScreenManager();
//		DisplayMode displayMode =
//				screen.findFirstCompatibleMode(POSSIBLE_MODES);
		screen.init();

		Window window = screen.getWindow();
		window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
		window.setBackground(Color.BLACK);
		window.setForeground(Color.WHITE);

		isRunning = true;
		isPause = false;
		hasDied = false;
	}


	public Image loadImage(String fileName) {
		return new ImageIcon(fileName).getImage();
	}


	/**
        Runs through the game loop until stop() is called.
	 */
	public void gameLoop() {
		long startTime = System.currentTimeMillis();
		long currTime = startTime;

		while (isRunning) {
			long elapsedTime =
					System.currentTimeMillis() - currTime;
			currTime += elapsedTime;

			// update
			update(elapsedTime);

			// draw the screen
			Graphics2D g = screen.getGraphics();
			draw(g);
			g.dispose();
			screen.update();

			while(isPause) {				
				currTime = System.currentTimeMillis(); //time moves on but nothing gets updated
				update(0);
				Graphics2D g1 = screen.getGraphics();
				drawPause(g1);
				g1.dispose();
				screen.update();
			}

			// don't take a nap! run as fast as possible
			/*try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }*/
		}
	}


	/**
        Updates the state of the game/animation based on the
        amount of elapsed time that has passed.
	 */
	public void update(long elapsedTime) {
		// do nothing
	}


	/**
        Draws to the screen. Subclasses must override this
        method.
	 */
	public abstract void draw(Graphics2D g);
	public void drawPause(Graphics2D g) {
		AttributedString styledText1 = null;
		if(hasDied) {
			AttributedString styledText = new AttributedString("YOU DIED !!");
	        styledText.addAttribute(TextAttribute.FAMILY, "serif");
	        styledText.addAttribute(TextAttribute.SIZE, 100);
	        styledText.addAttribute(TextAttribute.FOREGROUND, Color.red);        
	        AttributedCharacterIterator i = styledText.getIterator();
	        g.drawString(i, 100.0f,200.0f);
	        
	        styledText1 = new AttributedString("Press ESC to Restart the Game");
		}
		else styledText1 = new AttributedString("Press ESC to return to the Game");
		
        styledText1.addAttribute(TextAttribute.FAMILY, "serif");
        styledText1.addAttribute(TextAttribute.SIZE, 50);
        styledText1.addAttribute(TextAttribute.FOREGROUND, Color.yellow);        
        AttributedCharacterIterator i1 = styledText1.getIterator();
        AttributedString styledText2 = new AttributedString("Or Press SPACE to Exit !");
        styledText2.addAttribute(TextAttribute.FAMILY, "serif");
        styledText2.addAttribute(TextAttribute.SIZE, 50);
        styledText2.addAttribute(TextAttribute.FOREGROUND, Color.yellow);        
        AttributedCharacterIterator i2 = styledText2.getIterator();
        g.drawString(i1, 80.0f,270.0f);
        g.drawString(i2, 130.0f,350.0f);
        
	}
}
