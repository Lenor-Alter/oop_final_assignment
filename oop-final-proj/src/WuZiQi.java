import java.util.ArrayList;

public class WuZiQi extends BasicBoard implements IFile,Cloneable{
    WuZiQi(int gT, int SL, int maxRetractChance) {
        super(gT, SL, maxRetractChance);
    }
    WuZiQi(int gT, int SL, int mRC, int mRCB, int mRCW, int lpx, int lpy, int bN, int wN, boolean pling, boolean BT, boolean wR, boolean BW,ArrayList<ArrayList<Integer>> bd){
        super( gT,  SL,  mRC,  mRCB,  mRCW,  lpx,  lpy,  bN,  wN,  pling,  BT,  wR,  BW, bd);
    }
    // 每下完一步棋, 判定当前位置能够构成5个连着的同色棋子
    boolean fivePieces(int x, int y){
        int pos_now = board.get(x).get(y);
        // 判断水平方向是否满足
        int initLength = 1;
        int a = y-1;
        int b = y+1;
        while(a>=0 && board.get(x).get(a) == pos_now){
            initLength += 1;
            a -= 1;
        }
        while(b< SideLength && board.get(x).get(b) == pos_now){
            initLength += 1;
            b += 1;
        }
        // 水平方向满足5子
        if(initLength >= 5){    return true;    }

        // 判断垂直方向是否满足
        initLength = 1;
        a = x-1;
        b = x+1;
        while(a>=0 && board.get(a).get(y) == pos_now){
            initLength += 1;
            a -= 1;
        }
        while(b< SideLength && board.get(b).get(y) == pos_now){
            initLength += 1;
            b += 1;
        }
        // 垂直方向满足5子
        if(initLength >= 5){    return true;    }

        // 判断主对角线方向是否满足
        initLength = 1;
        int a_x = x-1;
        int a_y = y-1;
        int b_x = x+1;
        int b_y = y+1;
        while(a_x >= 0 && a_y >= 0 && board.get(a_x).get(a_y) == pos_now){
            initLength += 1;
            a_x -= 1;
            a_y -= 1;
        }
        while(b_x < SideLength && b_y < SideLength && board.get(b_x).get(b_y) == pos_now){
            initLength += 1;
            b_x += 1;
            b_y += 1;
        }
        // 垂直方向满足5子
        if(initLength >= 5){    return true;    }

        // 判断副对角线方向是否满足
        initLength = 1;
        a_x = x+1;
        a_y = y-1;
        b_x = x-1;
        b_y = y+1;
        while(a_x < SideLength && a_y >= 0 && board.get(a_x).get(a_y) == pos_now){
            initLength += 1;
            a_x += 1;
            a_y -= 1;
        }
        while(b_x >= 0 && b_y < SideLength && board.get(b_x).get(b_y) == pos_now){
            initLength += 1;
            b_x -= 1;
            b_y += 1;
        }
        // 垂直方向满足5子
        if(initLength >= 5){    return true;    }

        //四个方向均不满足胜利条件, 游戏继续
        return false;
    }
    void isWin(int x, int y){
        boolean win = fivePieces(x,y);
        if(win){
            winRes = true;
            if(board.get(x).get(y) == 0){
                BlackWin = true;
                System.out.println("\n游戏结束, 胜者为黑子！");
            }
            else{
                BlackWin = false;
                System.out.println("\n游戏结束, 胜者为白子！");
            }
            playing = false;
        }
    }

    @Override
    public WuZiQi clone() {
        return (WuZiQi) super.clone();
    }
}
