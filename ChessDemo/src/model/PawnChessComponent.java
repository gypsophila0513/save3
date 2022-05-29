package model;

import controller.ClickController;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PawnChessComponent extends ChessComponent{
    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;
    private Image pawnImage;
    @Override
    public String getName(){
        return "0";
    }
    public void loadResource() throws IOException {
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("ChessDemo./images/pawn-white.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("ChessDemo./images/pawn-black.png"));
        }
    }
    private void initiatePawnImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (color == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiatePawnImage(color);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(pawnImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) {
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination){
        ChessboardPoint source=getChessboardPoint();
        ChessColor color=getChessColor();
        if(color==ChessColor.BLACK&&source.getX()==1&&destination.getY()==source.getY()&&destination.getX()-source.getX()==2){
            if(!(chessComponents[source.getX()+1][source.getY()]instanceof EmptySlotComponent)&&!(chessComponents[destination.getX()+2][destination.getY()]instanceof EmptySlotComponent)){
                return false;
            }
        }
        else if(color==ChessColor.WHITE&&source.getX()==6&&destination.getY()==source.getY()&&destination.getX()-source.getX()==-2){
            if(!(chessComponents[source.getX()-1][source.getY()]instanceof EmptySlotComponent)&&!(chessComponents[destination.getX()-2][destination.getY()]instanceof EmptySlotComponent)){
                return false;
            }
        }
        else if(source.getY()==destination.getY()&&source.getX()-destination.getX()==1&&color==ChessColor.WHITE){
            if(!(chessComponents[destination.getX()][destination.getY()]instanceof EmptySlotComponent)){return false;}
        }
        else if(source.getY()==destination.getY()&&source.getX()-destination.getX()==-1&&color==ChessColor.BLACK){
            if(!(chessComponents[destination.getX()][destination.getY()]instanceof EmptySlotComponent)){return false;}
        }
        else if(Math.abs(source.getY()-destination.getY())==1&&source.getX()-destination.getX()==-1&&color==ChessColor.BLACK){
            if(source.getX()==4&&chessComponents[destination.getX()][destination.getY()]instanceof EmptySlotComponent){
                if(chessComponents[4][destination.getY()]instanceof PawnChessComponent&&chessComponents[4][destination.getY()].getChessColor()==ChessColor.WHITE){
                    List<List<ChessComponent>> steps= Chessboard.getSteps();
                    List<ChessComponent> step1=new ArrayList<>();List<ChessComponent> step2=steps.get(steps.size()-1);
                    if(!(step2.get(48+destination.getY())instanceof EmptySlotComponent)||!(step2.get(32+destination.getY())instanceof PawnChessComponent)){return false;}
                    else {
                        for (int i=0;i<steps.size()-1;i++){
                            step1=steps.get(i);
                            if((!(step1.get(48+destination.getY())instanceof PawnChessComponent)||step1.get(48+destination.getY()).getChessColor()!=ChessColor.WHITE)&&
                                    (!(step1.get(32+destination.getY())instanceof PawnChessComponent)||step1.get(32+destination.getY()).getChessColor()!=ChessColor.WHITE)){
                                return false;
                            }
                        }
                    }
                }else {return false;}
            }
            else if(chessComponents[destination.getX()][destination.getY()].getChessColor()!=ChessColor.WHITE){return false;}
        }
        else if(Math.abs(source.getY()-destination.getY())==1&&source.getX()-destination.getX()==1&&color==ChessColor.WHITE){
            if(source.getX()==3&&chessComponents[destination.getX()][destination.getY()]instanceof EmptySlotComponent){
                if(chessComponents[3][destination.getY()]instanceof PawnChessComponent&&chessComponents[3][destination.getY()].getChessColor()==ChessColor.BLACK){
                    List<List<ChessComponent>> steps= Chessboard.getSteps();
                    List<ChessComponent> step1=new ArrayList<>();List<ChessComponent> step2=steps.get(steps.size()-1);
                    if(!(step2.get(8+destination.getY())instanceof EmptySlotComponent)||!(step2.get(24+destination.getY())instanceof PawnChessComponent)){return false;}
                    else {
                        for (int i=0;i<steps.size()-1;i++){
                            step1=steps.get(i);
                            if((!(step1.get(8+destination.getY())instanceof PawnChessComponent)||step1.get(8+destination.getY()).getChessColor()!=ChessColor.BLACK)&&
                                    (!(step1.get(24+destination.getY())instanceof PawnChessComponent)||step1.get(24+destination.getY()).getChessColor()!=ChessColor.BLACK)){
                                return false;
                            }
                        }
                    }
                }else {return false;}
            }
            else if(chessComponents[destination.getX()][destination.getY()].getChessColor()!=ChessColor.BLACK){return false;}
        }
        else {
            return false;
        }
        return true;
    }
}
