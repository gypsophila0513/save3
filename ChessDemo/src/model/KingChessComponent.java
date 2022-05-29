package model;

import controller.ClickController;
import view.ChessGameFrame;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class KingChessComponent extends ChessComponent{
    private String name="KING";
    private static Image KING_WHITE;
    private static Image KING_BLACK;
    private Image kingImage;
    @Override
    public String getName(){
        return name;
    }
    public void loadResource() throws IOException {
        if (KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("ChessDemo./images/king-white.png"));
        }

        if (KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("ChessDemo./images/king-black.png"));
        }
    }
    private void initiateKingImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLACK) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateKingImage(color);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(kingImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) {
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination){
        ChessboardPoint source=getChessboardPoint();
        if(Math.abs(source.getX()-destination.getX())>1){
            return false;
        }
        else if(Math.abs(source.getY()-destination.getY())>2){
            return false;
        }
        else if(source.getY()-destination.getY()==-2&&getChessColor()==ChessColor.BLACK){
            System.out.println(100);
            if(destination.getX()!=0){
                return false;
            }
            List<List<ChessComponent>> steps= Chessboard.getSteps();
            System.out.println(steps.size());
            for (int i=0;i<steps.size();i++){
                List<ChessComponent> step=steps.get(i);
                if(!(step.get(4)instanceof KingChessComponent)||!(step.get(7) instanceof RookChessComponent)){return false;}
                else if(i==steps.size()-1&&(!(chessComponents[0][5] instanceof EmptySlotComponent)||!(chessComponents[0][6] instanceof EmptySlotComponent))){return false;}
            }
        }
        else if(getChessColor()==ChessColor.BLACK&&source.getY()-destination.getY()==2){
            if(destination.getX()!=0){
                return false;
            }
            List<List<ChessComponent>> steps=Chessboard.getSteps();
            for (int i=0;i<steps.size();i++){
                List<ChessComponent> step=steps.get(i);
                if(!(step.get(4)instanceof KingChessComponent)||!(step.get(0) instanceof RookChessComponent)){return false;}
                else if(i==steps.size()-1&&(!(chessComponents[0][1] instanceof EmptySlotComponent)||!(chessComponents[0][2] instanceof EmptySlotComponent)
                        ||!(chessComponents[0][3] instanceof EmptySlotComponent))){return false;}
            }
        }
        else if(getChessColor()==ChessColor.WHITE&&source.getY()-destination.getY()==-2){
            if(destination.getX()!=7){
                return false;
            }
            List<List<ChessComponent>> steps=Chessboard.getSteps();
            for (int i=0;i<steps.size();i++){
                List<ChessComponent> step=steps.get(i);
                if(!(step.get(60)instanceof KingChessComponent)||!(step.get(63) instanceof RookChessComponent)){return false;}
                else if(i==steps.size()-1&&(!(chessComponents[7][5] instanceof EmptySlotComponent)||!(chessComponents[7][6] instanceof EmptySlotComponent))){return false;}
            }
        }
        else if(getChessColor()==ChessColor.WHITE&&source.getY()-destination.getY()==2){
            if(destination.getX()!=7){
                return false;
            }
            List<List<ChessComponent>> steps=Chessboard.getSteps();
            for (int i=0;i<steps.size();i++){
                List<ChessComponent> step=steps.get(i);
                if(!(step.get(60)instanceof KingChessComponent)||!(step.get(56) instanceof RookChessComponent)){return false;}
                else if(i==steps.size()-1&&(!(chessComponents[7][1] instanceof EmptySlotComponent)||!(chessComponents[7][2] instanceof EmptySlotComponent)
                        ||!(chessComponents[7][3] instanceof EmptySlotComponent))){return false;}
            }
        }
        return true;
    }
}
