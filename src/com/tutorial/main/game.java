package com.tutorial.main;

/**https://www.youtube.com/watch?v=0T1U0kbu1Sk
 * ^tutorial used
 * Created by Davey McGinnis on 10/8/2016.
 */
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class game extends Canvas implements Runnable{

    public static final long serialVersionUID = 15523453245L;

    public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
    private Thread thread;
    private boolean running = false;
    private Random r;
    private Handler handler;

    public game() {
        handler = new Handler();
        this.addKeyListener(new KeyInput(handler));

        new Window(WIDTH, HEIGHT, "Box Game", this);

        r = new Random();

        handler.addObject(new Player(WIDTH/2-32, HEIGHT/2-32, ID.Player));
        for(int i=0;i<150;i++){
            handler.addObject(new Player(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.Player2));}
            //handler.addObject(new Player(0, 0, ID.Player));

    }

    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    public synchronized void stop(){
        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                delta--;
            }
            if(running)
                render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                //System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick(){
        handler.tick();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.gray);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        handler.render(g);


        g.dispose();
        bs.show();
    }

    public static void main(String args[]){
        new game();
    }
}
