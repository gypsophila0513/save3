package model;
import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
public class BishopChessComponent extends ChessComponent {
    private static Image BISHOP_WHITE;
    private static Image BISHOP_BLACK;
    private Image bishopImage;
    @Override
    public String getName(){
        return "0";
    }
    public void loadResource() throws IOException{
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("ChessDemo./images/bishop-white.png"));
        }

        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("ChessDemo./images/bishop-black.png"));
        }
    }
    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                bishopImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLACK) {
                bishopImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateBishopImage(color);
    }
    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if(source.getX()-destination.getX()==source.getY()-destination.getY()&&source.getX()!=destination.getX()&&source.getY()!=destination.getY()){
            int row=Math.min(source.getX(),destination.getX())+1;
            int col=Math.min(source.getY(),destination.getY())+1;
            while (row<Math.max(source.getX(),destination.getX())&&col<Math.max(source.getY(),destination.getY())){
                if(!(chessComponents[row][col] instanceof EmptySlotComponent)){return false;}
                row++;col++;
            }
        }
        else if(source.getX()-destination.getX()+source.getY()-destination.getY()==0&&source.getX()!=destination.getX()&&source.getY()!=destination.getY()){
            int row=Math.max(source.getX(),destination.getX())-1;
            int col=Math.min(source.getY(),destination.getY())+1;
            while (row>Math.min(source.getX(),destination.getX())&&col<Math.max(source.getY(),destination.getY())){
                if(!(chessComponents[row][col] instanceof EmptySlotComponent)){return false;}
                row--;col++;
            }
        }
        else {
            return false;
        }
        return true;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(bishopImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
