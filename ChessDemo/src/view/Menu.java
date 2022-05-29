package view;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;




import com.sun.tools.javac.Main;
import controller.GameController;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@SuppressWarnings("deprecation")
public class Menu extends JFrame {
    JLayeredPane layeredPane;
    JPanel panel1 = new JPanel();
    JPanel jp;
    JLabel jl;
    ImageIcon image;

    private int WIDTH ;
    private int HEIGTH ;


    private static GameController gameController;
    JButton button = new JButton("New Game");
    JButton button3 = new JButton("Theme");
    JButton jb;




    public Menu(int width,int height) {
        setTitle("Chess menu");
        this.WIDTH = width;
        this.HEIGTH = height;

        panel1.setLayout(null);
        ImageIcon image = new ImageIcon("ChessDemo./images/bb(1).png");
        int width1 = image.getIconWidth();
        int height1 = image.getIconHeight();

        setContentPane(panel1);
        JLabel background = new JLabel(image);
        background.setSize(width1,height1);
        getLayeredPane().add(background,new Integer(Integer.MIN_VALUE));
        ((JPanel)this.getContentPane()).setOpaque(false);
        background.setFont(new Font("Rockwell",Font .BOLD,20));



        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        addNewButton();
        addThemeButton();

    }public static void main(String[] args) {
        new Menu();
    }
    public Menu() {
        layeredPane = new JLayeredPane();
        image = new ImageIcon("ChessDemo./images/background.png");
        jp = new JPanel();
        jp.setBounds(0,0,image.getIconWidth(),image.getIconHeight());
        jl=new JLabel(image);
        jp.add(jl);
        jb=new JButton("测试按钮");
        jb.setBounds(100,100,100,100);
        layeredPane.add(jp,JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(jb,JLayeredPane.MODAL_LAYER);


        this.setLayeredPane(layeredPane);
        this.setSize(image.getIconWidth(),image.getIconHeight());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(image.getIconWidth(),image.getIconHeight());
        this.setVisible(true);
    }


    private void addNewButton() {
        button.setLocation(HEIGTH, HEIGTH / 10 + 160);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e ->{
            Click(e);
        });
    }

    private void addThemeButton(){

        button3.setLocation(HEIGTH, HEIGTH / 10 + 320);
        button3.setSize(200, 60);
        button3.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button3);

    }


    public void Click(ActionEvent e){
        if(e.getSource() == button) {
            setVisible(false);
            ChessGameFrame mainFrame = new ChessGameFrame(1000,760);
            mainFrame.setVisible(true);
            new Thread(()->{while(true) {playMusic();}
            }).start();
        }
    }
    static void playMusic() {// 背景音乐播放

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("ChessDemo./music/music.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {
                nByte = ais.read(buffer, 0, SIZE);
                sdl.write(buffer, 0, nByte);
            }
            sdl.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
