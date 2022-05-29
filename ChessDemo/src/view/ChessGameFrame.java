package view;

import controller.GameController;
import model.ChessColor;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.util.List;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    private static GameController gameController;
    private static JLabel statusLabel = new JLabel("White Turn");
    JButton button = new JButton("Restart");
    boolean judge = true;
    JPanel panel = new JPanel();

    public ChessGameFrame(int width, int height) {
        setTitle("Chess"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;
        panel.setLayout(null);
        ImageIcon image = new ImageIcon("ChessDemo./images/background(1).png");
        int width1 = image.getIconWidth();
        int height1 = image.getIconHeight();
        setContentPane(panel);
        JLabel background = new JLabel(image);
        background.setSize(width1,height1);
        getLayeredPane().add(background,new Integer(Integer.MIN_VALUE));
        ((JPanel)this.getContentPane()).setOpaque(false);
        background.setFont(new Font("Rockwell",Font .BOLD,20));

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        addChessboard();
        addLabel();
        addHelloButton();
        addLoadButton();
        addSaveButton();
        addUndoButton();
        addReStartButton();
    }


    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        add(chessboard);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {

        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addHelloButton() {
        JButton button = new JButton("Show Hello Here");
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGTH, HEIGTH / 10 + 160);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this,"Input Path here");
            if(judge == false){
                JOptionPane.showConfirmDialog(this,"Wrong format save");
            }
            gameController.loadGameFromFile(path);
        });
    }
    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click save");
            String path = JOptionPane.showInputDialog(this,"Name this save");
            List<String> chessData=gameController.saveGameFile();
            StringBuilder save = new StringBuilder();
            for (int i=0;i<chessData.size();i++){
                save.append(chessData.get(i));
                save.append("\n");
            }
            File file = new File("ChessDemo./save/"+path+".txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            DataOutputStream os = null;
            try {
                os = new DataOutputStream(new FileOutputStream(file));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            try {
                os.writeBytes(save.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    private void addUndoButton() {
        JButton button = new JButton("Undo");
        button.setLocation(HEIGTH, HEIGTH / 10 + 320);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
    private void addReStartButton() {
        JButton button2 = new JButton("Restart");
        button2.setLocation(HEIGTH, HEIGTH / 10 + 400);
        button2.setSize(200, 60);
        button2.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button2);
        button2.addActionListener(e -> {
            System.out.println("Click Restart");
            String path = ("ChessDemo./save/initial.txt");
            gameController.loadGameFromFile(path);
        });

    }
    public static void click(){

        if(gameController.getChessboard().getCurrentColor() == ChessColor.WHITE){
            statusLabel.setText("White Turn");
        }else if(gameController.getChessboard().getCurrentColor() == ChessColor.BLACK){
            statusLabel.setText("Black Turn");
        }
    }
    public static void playClickMusic() throws MalformedURLException {
        File file = new File("ChessDemo./music/eat.wav");
        AudioClip ac = Applet.newAudioClip(file.toURL());
        ac.play();
    }

}
