package view;


import model.*;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent {
    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */
    private static final int CHESSBOARD_SIZE = 8;

    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor = ChessColor.WHITE;
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    private static List<List<ChessComponent>> steps=new ArrayList<>();
    private static OutputStream out;
    private static InputStream in;

    public static List<List<ChessComponent>> getSteps() {
        return steps;
    }


    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);

        initiateEmptyChessboard();

        // FIXME: Initialize chessboard for testing only.
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);
        initBishopOnBoard(0, 2, ChessColor.BLACK);
        initBishopOnBoard(0, CHESSBOARD_SIZE - 3, ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);
        initQueenOnBoard(0, CHESSBOARD_SIZE - 5, ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 5, ChessColor.WHITE);
        initKingOnBoard(0, CHESSBOARD_SIZE - 4, ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 4, ChessColor.WHITE);
        initPawnOnBoard(1,0,ChessColor.BLACK);initPawnOnBoard(1,1,ChessColor.BLACK);initPawnOnBoard(1,2,ChessColor.BLACK);initPawnOnBoard(1,3,ChessColor.BLACK);
        initPawnOnBoard(1,4,ChessColor.BLACK);initPawnOnBoard(1,5,ChessColor.BLACK);initPawnOnBoard(1,6,ChessColor.BLACK);initPawnOnBoard(1,7,ChessColor.BLACK);
        initPawnOnBoard(CHESSBOARD_SIZE-2,0,ChessColor.WHITE);initPawnOnBoard(CHESSBOARD_SIZE-2,1,ChessColor.WHITE);initPawnOnBoard(CHESSBOARD_SIZE-2,2,ChessColor.WHITE);initPawnOnBoard(CHESSBOARD_SIZE-2,3,ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE-2,4,ChessColor.WHITE);initPawnOnBoard(CHESSBOARD_SIZE-2,5,ChessColor.WHITE);initPawnOnBoard(CHESSBOARD_SIZE-2,6,ChessColor.WHITE);initPawnOnBoard(CHESSBOARD_SIZE-2,7,ChessColor.WHITE);
        initKnightOnBoard(0,1,ChessColor.BLACK);initKnightOnBoard(0,CHESSBOARD_SIZE-2,ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE-1,1,ChessColor.WHITE);initKnightOnBoard(CHESSBOARD_SIZE-1,CHESSBOARD_SIZE-2,ChessColor.WHITE);
        List<ChessComponent> step0=new ArrayList<>();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                step0.add(chessComponents[i][j]);
            }
        }
        steps.add(step0);
    }
    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }
    public ChessColor getCurrentColor() {
        return currentColor;
    }
    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }
    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if(chess1 instanceof KingChessComponent&&Math.abs(chess1.getChessboardPoint().getY()-chess2.getChessboardPoint().getY())==2){
            if(chess1.getChessboardPoint().getY()-chess2.getChessboardPoint().getY()<0){
                ChessComponent chess3=chessComponents[chess1.getChessboardPoint().getX()][7];ChessComponent chess4=chessComponents[chess1.getChessboardPoint().getX()][5];
                chess3.swapLocation(chess4);
                int row3 = chess3.getChessboardPoint().getX(), col3 = chess3.getChessboardPoint().getY();
                chessComponents[row3][col3] = chess3;
                int row4 = chess4.getChessboardPoint().getX(), col4 = chess4.getChessboardPoint().getY();
                chessComponents[row4][col4] = chess4;
                if(!judge(getCurrentColor())){
                        chess3.swapLocation(chess4);
                        int row5 = chess3.getChessboardPoint().getX(), col5 = chess3.getChessboardPoint().getY();
                        chessComponents[row5][col5] = chess3;
                        int row6 = chess4.getChessboardPoint().getX(), col6 = chess4.getChessboardPoint().getY();
                        chessComponents[row6][col6] = chess4;
                        swapColor();
                    JOptionPane.showMessageDialog(null, "Invalid", "标题",JOptionPane.WARNING_MESSAGE);
                }
                chess3.repaint();chess4.repaint();
            }
            else {
                ChessComponent chess3=chessComponents[chess1.getChessboardPoint().getX()][0];ChessComponent chess4=chessComponents[chess1.getChessboardPoint().getX()][3];
                chess3.swapLocation(chess4);
                int row3 = chess3.getChessboardPoint().getX(), col3 = chess3.getChessboardPoint().getY();
                chessComponents[row3][col3] = chess3;
                int row4 = chess4.getChessboardPoint().getX(), col4 = chess4.getChessboardPoint().getY();
                chessComponents[row4][col4] = chess4;
                if(!judge(getCurrentColor())){
                    chess3.swapLocation(chess4);
                    int row5 = chess3.getChessboardPoint().getX(), col5 = chess3.getChessboardPoint().getY();
                    chessComponents[row5][col5] = chess3;
                    int row6 = chess4.getChessboardPoint().getX(), col6 = chess4.getChessboardPoint().getY();
                    chessComponents[row6][col6] = chess4;
                    swapColor();
                    JOptionPane.showMessageDialog(null, "Invalid", "标题",JOptionPane.WARNING_MESSAGE);
                }
                chess3.repaint();chess4.repaint();
            }
            chess1.swapLocation(chess2);
            int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
            chessComponents[row1][col1] = chess1;
            int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
            chessComponents[row2][col2] = chess2;
            if(!judge(getCurrentColor())){
                chess1.swapLocation(chess2);
                int row5 = chess1.getChessboardPoint().getX(), col5 = chess1.getChessboardPoint().getY();
                chessComponents[row5][col5] = chess1;
                int row6 = chess2.getChessboardPoint().getX(), col6 = chess2.getChessboardPoint().getY();
                chessComponents[row6][col6] = chess2;
                swapColor();
                JOptionPane.showMessageDialog(null, "Invalid", "标题",JOptionPane.WARNING_MESSAGE);
            }
        }
        else if(chess1 instanceof PawnChessComponent&&chess2 instanceof EmptySlotComponent&&Math.abs(chess2.getChessboardPoint().getY()-chess1.getChessboardPoint().getY())==1){
            ChessComponent chess3=chessComponents[chess1.getChessboardPoint().getX()][chess2.getChessboardPoint().getY()];
            chess1.swapLocation(chess2);
            int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
            chessComponents[row1][col1] = chess1;
            int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
            chessComponents[row2][col2] = chess2;
            remove(chess3);
            add(chess3 = new EmptySlotComponent(chess3.getChessboardPoint(), chess3.getLocation(), clickController, CHESS_SIZE));
            chess3.swapLocation(chess2);
            int row3 = chess3.getChessboardPoint().getX(), col3 = chess3.getChessboardPoint().getY();
            chessComponents[row3][col3] = chess3;
            int row4 = chess2.getChessboardPoint().getX(), col4 = chess2.getChessboardPoint().getY();
            chessComponents[row4][col4] = chess2;
            if(!judge(getCurrentColor())){
                chess1.swapLocation(chess2);
                int row5 = chess1.getChessboardPoint().getX(), col5 = chess1.getChessboardPoint().getY();
                chessComponents[row5][col5] = chess1;
                int row6 = chess2.getChessboardPoint().getX(), col6 = chess2.getChessboardPoint().getY();
                chessComponents[row6][col6] = chess2;
                swapColor();
                JOptionPane.showMessageDialog(null, "Invalid", "标题",JOptionPane.WARNING_MESSAGE);
            }
            chess3.repaint();
        }
        else{
            ChessComponent chess3=chess2;
            if (!(chess2 instanceof EmptySlotComponent)) {
                remove(chess2);
                add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
            }
            chess1.swapLocation(chess2);
            int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
            chessComponents[row1][col1] = chess1;
            int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
            chessComponents[row2][col2] = chess2;
            if(!judge(getCurrentColor())){
                chess1.swapLocation(chess2);
                int row5 = chess1.getChessboardPoint().getX(), col5 = chess1.getChessboardPoint().getY();
                chessComponents[row5][col5] = chess1;
                int row6 = chess2.getChessboardPoint().getX(), col6 = chess2.getChessboardPoint().getY();
                chessComponents[row6][col6] = chess2;
                swapChess(chess3,chess2);chess3.repaint();
                swapColor();
                JOptionPane.showMessageDialog(null, "Invalid", "标题",JOptionPane.WARNING_MESSAGE);
            }
        }
        List<ChessComponent> step=new ArrayList<>();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                step.add(chessComponents[i][j]);
            }
        }
        steps.add(step);
        chess1.repaint();
        chess2.repaint();
    }
    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE));
            }
        }
    }
    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
    }
    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initKingOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }
    public void loadGame(List<String> chessData) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessData.get(i).charAt(j) == 'R') {
                    ChessComponent rook = new RookChessComponent(new ChessboardPoint(i, j), calculatePoint(i,j),ChessColor.BLACK,clickController, CHESS_SIZE);
                    putChessOnBoard(rook);rook.repaint();
                }
                if (chessData.get(i).charAt(j) == 'r') {
                    ChessComponent rook = new RookChessComponent(new ChessboardPoint(i, j),  calculatePoint(i,j),ChessColor.WHITE,clickController, CHESS_SIZE);
                    putChessOnBoard(rook);rook.repaint();
                }
                if (chessData.get(i).charAt(j) == 'K') {
                    ChessComponent king = new KingChessComponent(new ChessboardPoint(i, j), calculatePoint(i,j),ChessColor.BLACK,clickController, CHESS_SIZE);
                    putChessOnBoard(king);king.repaint();
                }
                if (chessData.get(i).charAt(j) == 'k') {
                    ChessComponent king = new KingChessComponent(new ChessboardPoint(i, j),  calculatePoint(i,j),ChessColor.WHITE,clickController, CHESS_SIZE);
                    putChessOnBoard(king);king.repaint();
                }
                if (chessData.get(i).charAt(j) == 'Q') {
                    ChessComponent queen = new QueenChessComponent(new ChessboardPoint(i, j), calculatePoint(i,j),ChessColor.BLACK,clickController, CHESS_SIZE);
                    putChessOnBoard(queen);queen.repaint();
                }
                if (chessData.get(i).charAt(j) == 'q') {
                    ChessComponent queen = new QueenChessComponent(new ChessboardPoint(i, j),  calculatePoint(i,j),ChessColor.WHITE,clickController, CHESS_SIZE );
                    putChessOnBoard(queen);queen.repaint();
                }
                if (chessData.get(i).charAt(j) == '_') {
                    ChessComponent empty = new EmptySlotComponent(new ChessboardPoint(i, j),  calculatePoint(i,j),clickController, CHESS_SIZE);
                    putChessOnBoard(empty);empty.repaint();
                }
                if (chessData.get(i).charAt(j) == 'N') {
                    ChessComponent knight = new KnightChessComponent(new ChessboardPoint(i, j), calculatePoint(i,j),ChessColor.BLACK,clickController, CHESS_SIZE);
                    putChessOnBoard(knight);knight.repaint();
                }
                if (chessData.get(i).charAt(j) == 'n') {
                    ChessComponent knight = new KnightChessComponent(new ChessboardPoint(i, j),  calculatePoint(i,j),ChessColor.WHITE,clickController, CHESS_SIZE);
                    putChessOnBoard(knight);knight.repaint();
                }
                if (chessData.get(i).charAt(j) == 'P') {
                    ChessComponent pawn = new PawnChessComponent(new ChessboardPoint(i, j), calculatePoint(i,j),ChessColor.BLACK,clickController, CHESS_SIZE);
                    putChessOnBoard(pawn);pawn.repaint();
                }
                if (chessData.get(i).charAt(j) == 'p') {
                    ChessComponent pawn = new PawnChessComponent(new ChessboardPoint(i, j),  calculatePoint(i,j),ChessColor.WHITE,clickController, CHESS_SIZE);
                    putChessOnBoard(pawn);pawn.repaint();
                }
                if (chessData.get(i).charAt(j) == 'B') {
                    ChessComponent bishop = new BishopChessComponent(new ChessboardPoint(i, j), calculatePoint(i,j),ChessColor.BLACK,clickController, CHESS_SIZE);
                    putChessOnBoard(bishop);bishop.repaint();
                }
                if (chessData.get(i).charAt(j) == 'b') {
                    ChessComponent bishop = new BishopChessComponent(new ChessboardPoint(i, j),  calculatePoint(i,j),ChessColor.WHITE,clickController, CHESS_SIZE);
                    putChessOnBoard(bishop);bishop.repaint();
                }
            }
        }
        if(chessData.get(8).equals("w")){
            currentColor=ChessColor.WHITE;
        }
        else {
            currentColor=ChessColor.BLACK;
        }
    }
    public List<String> saveGame() throws IOException {
        List<String> chessData=new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < 8; j++) {
                if (chessComponents[i][j] instanceof RookChessComponent && chessComponents[i][j].getChessColor() == ChessColor.BLACK) {
                    row.append('R');
                }
                if (chessComponents[i][j] instanceof RookChessComponent && chessComponents[i][j].getChessColor() == ChessColor.WHITE) {
                    row.append('r');
                }
                if (chessComponents[i][j] instanceof KingChessComponent && chessComponents[i][j].getChessColor() == ChessColor.BLACK) {
                    row.append('K');
                }
                if (chessComponents[i][j] instanceof KingChessComponent && chessComponents[i][j].getChessColor() == ChessColor.WHITE) {
                    row.append('k');
                }
                if (chessComponents[i][j] instanceof QueenChessComponent && chessComponents[i][j].getChessColor() == ChessColor.BLACK) {
                    row.append('Q');
                }
                if (chessComponents[i][j] instanceof QueenChessComponent && chessComponents[i][j].getChessColor() == ChessColor.WHITE) {
                    row.append('q');
                }
                if (chessComponents[i][j] instanceof EmptySlotComponent && chessComponents[i][j].getChessColor() == ChessColor.NONE) {
                    row.append('_');
                }
                if (chessComponents[i][j] instanceof KnightChessComponent && chessComponents[i][j].getChessColor() == ChessColor.BLACK) {
                    row.append('N');
                }
                if (chessComponents[i][j] instanceof KnightChessComponent && chessComponents[i][j].getChessColor() == ChessColor.WHITE) {
                    row.append('n');
                }
                if (chessComponents[i][j] instanceof PawnChessComponent && chessComponents[i][j].getChessColor() == ChessColor.BLACK) {
                    row.append('P');
                }
                if (chessComponents[i][j] instanceof PawnChessComponent && chessComponents[i][j].getChessColor() == ChessColor.WHITE) {
                    row.append('p');
                }
                if (chessComponents[i][j] instanceof BishopChessComponent && chessComponents[i][j].getChessColor() == ChessColor.BLACK) {
                    row.append('B');
                }
                if (chessComponents[i][j] instanceof BishopChessComponent && chessComponents[i][j].getChessColor() == ChessColor.WHITE) {
                    row.append('b');
                }
            }
            chessData.add(row.toString());
        }
        if(currentColor==ChessColor.WHITE){chessData.add("w");}
        else {chessData.add("b");}
        return chessData;
    }
    public void swapChess(ChessComponent chess1,ChessComponent chess2){
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;
        chess1.repaint();chess2.repaint();
    }
    public boolean judge(ChessColor color){
        if(getCurrentColor()==ChessColor.NONE){
            return true;
        }
        int i=0,j=0;
        for (int m=0;m<CHESSBOARD_SIZE;m++){
            for (int n=0;n<CHESSBOARD_SIZE;n++){
                if(chessComponents[m][n]instanceof KingChessComponent&&chessComponents[m][n].getChessColor()==color){
                    i=m;j=n;break;
                }
            }
        }
        for (int k=0;k<8;k++){
            for (int l=0;l<8;l++){
                if(chessComponents[k][l].getChessColor()!=ChessColor.NONE&&chessComponents[k][l].getChessColor()!=color){
                    boolean judge=chessComponents[k][l].canMoveTo(getChessComponents(),new ChessboardPoint(i,j));
                    if(judge){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public void Judge(){
        boolean judge=false;
        if(!judge(getCurrentColor())){
            int i=0,j=0;
            for (int k=0;k<CHESSBOARD_SIZE;k++){
                for (int l=0;l<CHESSBOARD_SIZE;l++){
                    if(chessComponents[k][l]instanceof KingChessComponent&&chessComponents[k][l].getChessColor()==getCurrentColor()){
                        i=k;j=l;break;
                    }
                }
            }
            for (int k=-1;k<=1;k++){
                for (int l=-1;l<=1;l++){
                    if(i+k>=0&&i+k<=7&&j+l>=0&&j+l<=7){
                        if(chessComponents[i][j].canMoveTo(chessComponents,chessComponents[i+k][j+l].getChessboardPoint())){
                            ChessComponent chess1= new KingChessComponent(chessComponents[i][j].getChessboardPoint(),chessComponents[i][j].getLocation(),chessComponents[i][j].getChessColor(),clickController,CHESS_SIZE);
                            System.out.println(i+k);
                            System.out.println(j+l);
                            if(chessComponents[i+k][j+l]instanceof RookChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[i+k][j+l].getChessboardPoint(),chessComponents[i+k][j+l].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[i+k][j+l].getChessboardPoint(),chessComponents[i+k][j+l].getLocation(),chessComponents[i+k][j+l].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[i+k][j+l]instanceof BishopChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[i+k][j+l].getChessboardPoint(),chessComponents[i+k][j+l].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[i+k][j+l].getChessboardPoint(),chessComponents[i+k][j+l].getLocation(),chessComponents[i+k][j+l].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[i+k][j+l]instanceof KnightChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[i+k][j+l].getChessboardPoint(),chessComponents[i+k][j+l].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new KnightChessComponent(chessComponents[i+k][j+l].getChessboardPoint(),chessComponents[i+k][j+l].getLocation(),chessComponents[i+k][j+l].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[i+k][j+l]instanceof QueenChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[i+k][j+l].getChessboardPoint(),chessComponents[i+k][j+l].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new QueenChessComponent(chessComponents[i+k][j+l].getChessboardPoint(),chessComponents[i+k][j+l].getLocation(),chessComponents[i+k][j+l].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[i+k][j+l]instanceof PawnChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[i+k][j+l].getChessboardPoint(),chessComponents[i+k][j+l].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new PawnChessComponent(chessComponents[i+k][j+l].getChessboardPoint(),chessComponents[i+k][j+l].getLocation(),chessComponents[i+k][j+l].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                        }
                    }
                }
            }
            int m=0,n=0;
            for (int k=0;k<CHESSBOARD_SIZE;k++){
                for (int l=0;l<CHESSBOARD_SIZE;l++){
                    if(chessComponents[k][l].canMoveTo(chessComponents,new ChessboardPoint(i,j))){
                        m=k;n=l;break;
                    }
                }
            }
            for (int k=0;k<CHESSBOARD_SIZE;k++){
                for (int l=0;l<CHESSBOARD_SIZE;l++){
                    if (chessComponents[k][l].canMoveTo(chessComponents,new ChessboardPoint(m,n))){
                        if(chessComponents[k][l]instanceof RookChessComponent){
                            ChessComponent chess1= new RookChessComponent(chessComponents[k][l].getChessboardPoint(),chessComponents[k][l].getLocation(),chessComponents[k][l].getChessColor(),clickController,CHESS_SIZE);
                            if(chessComponents[m][n]instanceof RookChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof BishopChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof KnightChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new KnightChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof QueenChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new QueenChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof PawnChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new PawnChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                        }
                        else if(chessComponents[k][l]instanceof BishopChessComponent){
                            ChessComponent chess1= new BishopChessComponent(chessComponents[k][l].getChessboardPoint(),chessComponents[k][l].getLocation(),chessComponents[k][l].getChessColor(),clickController,CHESS_SIZE);
                            if(chessComponents[m][n]instanceof RookChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof BishopChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof KnightChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new KnightChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof QueenChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new QueenChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof PawnChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new PawnChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                        }
                        else if(chessComponents[k][l]instanceof QueenChessComponent){
                            ChessComponent chess1= new QueenChessComponent(chessComponents[k][l].getChessboardPoint(),chessComponents[k][l].getLocation(),chessComponents[k][l].getChessColor(),clickController,CHESS_SIZE);
                            if(chessComponents[m][n]instanceof RookChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof BishopChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof KnightChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new KnightChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof QueenChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new QueenChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof PawnChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new PawnChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                        }
                        else if(chessComponents[k][l]instanceof KnightChessComponent){
                            ChessComponent chess1= new KnightChessComponent(chessComponents[k][l].getChessboardPoint(),chessComponents[k][l].getLocation(),chessComponents[k][l].getChessColor(),clickController,CHESS_SIZE);
                            if(chessComponents[m][n]instanceof RookChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof BishopChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof KnightChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new KnightChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof QueenChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new QueenChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof PawnChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new PawnChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                        }
                        else if(chessComponents[k][l]instanceof PawnChessComponent){
                            ChessComponent chess1= new BishopChessComponent(chessComponents[k][l].getChessboardPoint(),chessComponents[k][l].getLocation(),chessComponents[k][l].getChessColor(),clickController,CHESS_SIZE);
                            if(chessComponents[m][n]instanceof RookChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof BishopChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new RookChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof KnightChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new KnightChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof QueenChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new QueenChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                            else if(chessComponents[m][n]instanceof PawnChessComponent){
                                ChessComponent chess2=new EmptySlotComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),clickController,CHESS_SIZE);
                                ChessComponent chess3=new PawnChessComponent(chessComponents[m][n].getChessboardPoint(),chessComponents[m][n].getLocation(),chessComponents[m][n].getChessColor(),clickController,CHESS_SIZE);
                                swapChess(chess1,chess2);
                                if(judge(getCurrentColor())){judge=true;swapChess(chess1,chess2);swapChess(chess2,chess3);chess1.repaint();chess2.repaint();chess3.repaint();break;}
                                swapChess(chess1,chess2);swapChess(chess2,chess3);
                                chess1.repaint();chess2.repaint();chess3.repaint();
                            }
                        }
                    }
                }
            }
            if(!judge){
                if(getCurrentColor()==ChessColor.BLACK){
                    JOptionPane.showMessageDialog(null, "White win", "Result",JOptionPane.WARNING_MESSAGE);
                    currentColor=ChessColor.NONE;
                }
                else if(getCurrentColor()==ChessColor.WHITE){
                    JOptionPane.showMessageDialog(null, "Black win", "Result",JOptionPane.WARNING_MESSAGE);
                    currentColor=ChessColor.NONE;
                }
            }
        }
        else {
            for (int k=0;k<CHESSBOARD_SIZE;k++) {
                for (int l = 0; l<CHESSBOARD_SIZE; l++) {
                    if(chessComponents[k][l].getChessColor()==getCurrentColor()){
                        for (int m=0;m<CHESSBOARD_SIZE;m++){
                            for (int n=0;n<CHESSBOARD_SIZE;n++){
                                if(chessComponents[k][l].canMoveTo(chessComponents,new ChessboardPoint(m,n))){
                                    ChessComponent chess1=chessComponents[k][l];
                                    ChessComponent chess2=chessComponents[m][n];
                                    ChessComponent chess3=chessComponents[m][n];
                                    swapChess(chess1,chess2);
                                    if(judge(getCurrentColor())){
                                        swapChess(chess1,chess2);
                                        swapChess(chess2,chess3);
                                        judge=true;break;
                                    }
                                    swapChess(chess1,chess2);
                                    swapChess(chess2,chess3);
                                }
                            }
                        }
                    }
                }
            }
            if(!judge){
                JOptionPane.showMessageDialog(null, "Tie", "Result",JOptionPane.WARNING_MESSAGE);
                currentColor=ChessColor.NONE;
            }
        }
    }
}
