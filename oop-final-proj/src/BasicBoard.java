import java.util.ArrayList;

public class BasicBoard implements IFile,Cloneable{
    protected int gameType = -1;
    protected int SideLength = 8;
    //棋局是否在进行中, BlackWin是否有效的前置条件
    protected boolean playing = false;
    //黑子先下
    protected boolean BlackTurn = true;
    // 是否分出胜利结果, BlackWin是否有效的前置条件
    protected boolean winRes = false;
    protected boolean BlackWin = false;
    protected ArrayList<ArrayList<Integer>> board = new ArrayList<ArrayList<Integer>>();
    protected int last_pos_x = -1;
    protected int last_pos_y = -1;
    protected int maxRetractChanceLimit = 0;
    protected int maxRetractChanceBlack = 0;
    protected int maxRetractChanceWhite = 0;
    // 棋局上的黑子/白子数目
    protected int blackNums = 0;
    protected int whiteNums = 0;
    BasicBoard(int gT, int SL, int maxRetractChance){
        gameType = gT;
        SideLength = SL;
        ArrayList<Integer> rows = new ArrayList<Integer>();
        for(int i=0;i<SideLength;i++){rows.add(-1);}
        for(int i=0;i<SideLength;i++){board.add((ArrayList<Integer>) rows.clone());}
        // 初始化棋盘, 大小为[SideLength, SideLength], 初始值为-1, 表示该位置没有落子
        // 0表示黑子, 1表示白子
        maxRetractChanceBlack = maxRetractChance;
        maxRetractChanceWhite = maxRetractChance;
        maxRetractChanceLimit = maxRetractChance;
    }
    BasicBoard(int gT, int SL, int mRC, int mRCB, int mRCW, int lpx, int lpy, int bN, int wN, boolean pling, boolean BT, boolean wR, boolean BW,ArrayList<ArrayList<Integer>> bd){
        gameType = gT;
        SideLength = SL;
        maxRetractChanceLimit = mRC;
        maxRetractChanceBlack = mRCB;
        maxRetractChanceWhite = mRCW;
        last_pos_x = lpx;
        last_pos_y = lpy;
        blackNums = bN;
        whiteNums = wN;
        playing = pling;
        BlackTurn = BT;
        winRes = wR;
        BlackWin = BW;
        board = bd;

    }
    void put(int x, int y){
        if(!playing){
            System.out.println("\n游戏未开始或已结束, 不能落子！");
//            showBoard();
            return;
        }
        // 黑白子交替下棋,下完打印出棋盘最新的状态
        // 该位置如果有子, 则不能继续下
        if(x >= SideLength || y >= SideLength){
            System.out.println("\n该位置超出棋盘范围, 请重新选择位置！");
            showBoard();
            return;
        }
        if(board.get(x).get(y) != -1){
            System.out.println("\n该位置已存在棋子,请重新选择位置！");
            showBoard();
            return;
        }
        last_pos_x = x;
        last_pos_y = y;

        if(BlackTurn){
            board.get(x).set(y, 0);
            BlackTurn = false;
            blackNums += 1;
        }
        else{
            board.get(x).set(y, 1);
            BlackTurn = true;
            whiteNums += 1;
        }
        //testBoard();
        showBoard();
        // 每下完一个棋子判定是否存在胜利条件, 如果某方胜利则改写playing和winRes和BlackWin三个变量的值
        // 系统通过playing是否为true判定棋局是否结束
        isWin(x,y);
        return;
    }
    void retract(){
        if(!playing){
            System.out.println("\n棋局未开始或已结束, 无需悔棋！");
            showBoard();
            return;
        }
        if(last_pos_x == -1 || last_pos_y == -1){
            System.out.println("\n禁止开局悔棋or连续悔棋！");
            showBoard();
            return;
        }
        // 黑子下完棋准备悔棋时，应该是白子的turn
        if(!BlackTurn && maxRetractChanceBlack > 0){
            maxRetractChanceBlack -= 1;
            System.out.println("\n黑子悔棋成功,剩余悔棋次数："+maxRetractChanceBlack);
        }
        else if (!BlackTurn) {
            System.out.println("\n黑子悔棋次数不足,禁止悔棋！");
            showBoard();
            return;
        }
        else if(BlackTurn && maxRetractChanceWhite > 0){
            maxRetractChanceWhite -= 1;
            System.out.println("\n白子悔棋成功,剩余悔棋次数："+maxRetractChanceWhite);
        }
        else if(BlackTurn){
            System.out.println("\n白子悔棋次数不足,禁止悔棋！");
            showBoard();
            return;
        }
        // 最后一步棋撤回, 并修改Turn
        board.get(last_pos_x).set(last_pos_y, -1);
        // 修改last_pos状态, 禁止连续悔棋
        last_pos_x = -1;
        last_pos_y = -1;
        BlackTurn = !BlackTurn;
        showBoard();
        return;
    }
    void showBoard(){
        System.out.print("\n    ");
        for(int i=0;i<SideLength;i++){
            if(i<10){
                System.out.print(i+"   ");
            }
            else{
                System.out.print(i+"  ");
            }
        }
        System.out.print("\n  ");
        for(int i=0;i<SideLength;i++){
            System.out.print("————");
        }
        for(int i=0;i<SideLength;i++){
            if(i<10){
                System.out.print("\n"+i+" | ");
            }
            else{
                System.out.print("\n"+i+"| ");
            }
            ArrayList<Integer> row = board.get(i);
            for(int j=0;j<SideLength;j++){
                if(row.get(j) == 0){
                    // 打印黑子
                    System.out.print("X ");
                }
                else if(row.get(j) == 1){
                    // 打印白子
                    System.out.print("O ");
                }
                else{
                    // 打印空白格子
                    System.out.print("  ");
                }
                // 打印棋盘线
                System.out.print("| ");
            }
            System.out.print("\n  ");
            for(int k=0;k<SideLength;k++){
                System.out.print("————");
            }
        }
    }
    void startGame(){   playing = true;    }
    boolean isPlaying(){    return playing;     }

    // testBoard()测试用, 不影响最终功能
//    void testBoard(){
//        for(int i=0;i<SideLength;i++){
//            System.out.print("\n"+i+" ");
//            ArrayList<Integer> row = board.get(i);
//            for(int j=0;j<SideLength;j++){
//                System.out.print(row.get(j)+" ");
//            }
//        }
//    }
    void isWin(int x,int y){
        //下完子后判定胜利, 这里不实现具体逻辑
    }
    // 游戏重开
    void reStart(){
        playing = true;
        winRes = false;
        BlackTurn = true;
        last_pos_x = -1;
        last_pos_y = -1;
        blackNums = 0;
        whiteNums = 0;
        maxRetractChanceWhite = maxRetractChanceLimit;
        maxRetractChanceBlack = maxRetractChanceLimit;
        resetBoard();
        System.out.println("游戏已重开!");
        showBoard();
    }
    // 重置棋盘, 清空所有子, 慎用
    void resetBoard(){
        for(int i=0;i<SideLength;i++){
            for(int j=0;j<SideLength;j++){
                board.get(i).set(j,-1);
            }
        }
        System.out.println("\n不想玩？那都别玩了！棋盘一键清理成功!");
        return;
    }
    void surrender(){
        winRes = true;
        playing = false;
        if(BlackTurn){
            BlackWin = false;
            System.out.println("黑子投降, 白子获胜！");
        }
        else{
            BlackWin = true;
            System.out.println("白子投降, 黑子获胜！");
        }
    }
    int getSideLength(){    return SideLength;  }
    int getMaxRetractChance(){   return maxRetractChanceLimit;   }
    int getMaxRetractChanceBlack(){  return maxRetractChanceBlack;   }
    int getMaxRetractChanceWhite(){  return maxRetractChanceWhite;   }
    int getLastPosX(){   return last_pos_x;}
    int getLastPosY(){   return last_pos_y;}
    int getBlackNums(){    return blackNums;}
    int getWhiteNums(){    return whiteNums;}
    boolean getPlaying(){   return playing;}
    boolean getBlackTurn(){ return BlackTurn;}
    boolean getWinRes(){   return winRes;}
    boolean getBlackWin(){   return BlackWin;}
    ArrayList<ArrayList<Integer>> getBoard(){   return board;}
    int getGameType(){  return gameType;}
    void showInfo(){
        System.out.println("游戏是否进行中："+playing);
        System.out.println("当前黑子已下"+blackNums+"子, "+"当前白子已下"+whiteNums+"子");
    }
    void putNone(){}
    @Override
    public BasicBoard clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (BasicBoard) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
