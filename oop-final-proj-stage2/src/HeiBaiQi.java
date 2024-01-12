import java.util.ArrayList;
import java.util.Scanner;

public class HeiBaiQi extends BasicBoard implements IFile,Cloneable{
    protected boolean prePutNone = false;
    HeiBaiQi(int gT,int SL, int maxRetractChance) {
        super(gT,SL, maxRetractChance);
    }
    HeiBaiQi(int gT, int SL, int mRC, int mRCB, int mRCW, int lpx, int lpy, int bN, int wN, boolean pling, boolean BT, boolean wR, boolean BW,ArrayList<ArrayList<Integer>> bd, ArrayList<ArrayList<ArrayList<Integer>>> bdr){
        super( gT,  SL,  mRC,  mRCB,  mRCW,  lpx,  lpy,  bN,  wN,  pling,  BT,  wR,  BW, bd, bdr);
    }
    void startGame(){
        playing = true;
        board.get(3).set(3,1);
        board.get(3).set(4,0);
        board.get(4).set(3,0);
        board.get(4).set(4,1);
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
        if(!permitOverturn(x,y)){
            System.out.println("\n该位置无法翻转对方棋子+,请重新选择位置！");
            showBoard();
            return;
        }
        last_pos_x = x;
        last_pos_y = y;

        if(BlackTurn){
            board.get(x).set(y, 0);
            turnDiff(x,y);
            BlackTurn = false;
            blackNums += 1;
        }
        else{
            board.get(x).set(y, 1);
            turnDiff(x,y);
            BlackTurn = true;
            whiteNums += 1;
        }
        record();
        showBoard();
        isWin(x,y);
        return;
    }
    void resetBoard(){
        for(int i=0;i<SideLength;i++){
            for(int j=0;j<SideLength;j++){
                board.get(i).set(j,-1);
            }
        }
        board.get(3).set(3,1);
        board.get(3).set(4,0);
        board.get(4).set(3,0);
        board.get(4).set(4,1);
        return;
    }

    boolean permitOverturn(int x,int y){
        //八个方向分别探测一遍，有一个方向可以就直接返回true
        boolean permit = false;
        int color;
        if(BlackTurn){color = 0;}
        else{color = 1;}
        boolean meetSame = false;
        boolean meetDiff = false;

        int x_tmp = x;
        int y_tmp = y-1;
        // 往左探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
            }
            y_tmp -= 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){return true;}

        meetSame = false;
        meetDiff = false;
        x_tmp = x;
        y_tmp = y+1;
        // 往右探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
            }
            y_tmp += 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){return true;}

        meetSame = false;
        meetDiff = false;
        x_tmp = x-1;
        y_tmp = y;
        // 往上探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
            }
            x_tmp -= 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){return true;}

        meetSame = false;
        meetDiff = false;
        x_tmp = x+1;
        y_tmp = y;
        // 往下探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
            }
            x_tmp += 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){return true;}

        meetSame = false;
        meetDiff = false;
        x_tmp = x-1;
        y_tmp = y-1;
        // 往左上探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
            }
            x_tmp -= 1;
            y_tmp -= 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){return true;}

        meetSame = false;
        meetDiff = false;
        x_tmp = x+1;
        y_tmp = y-1;
        // 往左下探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
            }
            x_tmp += 1;
            y_tmp -= 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){return true;}

        meetSame = false;
        meetDiff = false;
        x_tmp = x+1;
        y_tmp = y+1;
        // 往右下探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
            }
            x_tmp += 1;
            y_tmp += 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){return true;}

        meetSame = false;
        meetDiff = false;
        x_tmp = x-1;
        y_tmp = y+1;
        // 往右上探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
            }
            x_tmp -= 1;
            y_tmp += 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){return true;}

        return false;
    }

    void turnDiff(int x,int y){
        int color = board.get(x).get(y);
        int turn_end_x = x;
        int turn_end_y = y;
        boolean meetSame = false;
        boolean meetDiff = false;

        int x_tmp = x;
        int y_tmp = y-1;
        // 往左探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
                // 遇到第一个同色位置，记录并直接退出
                turn_end_x = x_tmp;
                turn_end_y = y_tmp;
                break;
            }
            y_tmp -= 1;
        }
        //该方向达成翻转条件，开始翻转
        if(meetDiff && meetSame){
            turn_end_y += 1;
            while(!(turn_end_x == x && turn_end_y == y)){
                if(color == 0){
                    board.get(turn_end_x).set(turn_end_y,0);
                    blackNums += 1;
                    whiteNums -= 1;
                }
                else{
                    board.get(turn_end_x).set(turn_end_y,1);
                    blackNums -= 1;
                    whiteNums += 1;
                }
                turn_end_y += 1;
            }
        }

        meetSame = false;
        meetDiff = false;
        x_tmp = x;
        y_tmp = y+1;
        // 往右探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
                turn_end_x = x_tmp;
                turn_end_y = y_tmp;
                break;
            }
            y_tmp += 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){
            turn_end_y -= 1;
            while(!(turn_end_x == x && turn_end_y == y)){
                if(color == 0){
                    board.get(turn_end_x).set(turn_end_y,0);
                    blackNums += 1;
                    whiteNums -= 1;
                }
                else{
                    board.get(turn_end_x).set(turn_end_y,1);
                    blackNums -= 1;
                    whiteNums += 1;
                }
                turn_end_y -= 1;
            }
        }

        meetSame = false;
        meetDiff = false;
        x_tmp = x-1;
        y_tmp = y;
        // 往上探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
                turn_end_x = x_tmp;
                turn_end_y = y_tmp;
                break;
            }
            x_tmp -= 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){
            turn_end_x += 1;
            while(!(turn_end_x == x && turn_end_y == y)){
                if(color == 0){
                    board.get(turn_end_x).set(turn_end_y,0);
                    blackNums += 1;
                    whiteNums -= 1;
                }
                else{
                    board.get(turn_end_x).set(turn_end_y,1);
                    blackNums -= 1;
                    whiteNums += 1;
                }
                turn_end_x += 1;
            }
        }

        meetSame = false;
        meetDiff = false;
        x_tmp = x+1;
        y_tmp = y;
        // 往下探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
                turn_end_x = x_tmp;
                turn_end_y = y_tmp;
                break;
            }
            x_tmp += 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){
            turn_end_x -= 1;
            while(!(turn_end_x == x && turn_end_y == y)){
                if(color == 0){
                    board.get(turn_end_x).set(turn_end_y,0);
                    blackNums += 1;
                    whiteNums -= 1;
                }
                else{
                    board.get(turn_end_x).set(turn_end_y,1);
                    blackNums -= 1;
                    whiteNums += 1;
                }
                turn_end_x -= 1;
            }
        }

        meetSame = false;
        meetDiff = false;
        x_tmp = x-1;
        y_tmp = y-1;
        // 往左上探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
                turn_end_x = x_tmp;
                turn_end_y = y_tmp;
                break;
            }
            x_tmp -= 1;
            y_tmp -= 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){
            turn_end_x += 1;
            turn_end_y += 1;
            while(!(turn_end_x == x && turn_end_y == y)){
                if(color == 0){
                    board.get(turn_end_x).set(turn_end_y,0);
                    blackNums += 1;
                    whiteNums -= 1;
                }
                else{
                    board.get(turn_end_x).set(turn_end_y,1);
                    blackNums -= 1;
                    whiteNums += 1;
                }
                turn_end_x += 1;
                turn_end_y += 1;
            }
        }

        meetSame = false;
        meetDiff = false;
        x_tmp = x+1;
        y_tmp = y-1;
        // 往左下探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
                turn_end_x = x_tmp;
                turn_end_y = y_tmp;
                break;
            }
            x_tmp += 1;
            y_tmp -= 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){
            turn_end_x -= 1;
            turn_end_y += 1;
            while(!(turn_end_x == x && turn_end_y == y)){
                if(color == 0){
                    board.get(turn_end_x).set(turn_end_y,0);
                    blackNums += 1;
                    whiteNums -= 1;
                }
                else{
                    board.get(turn_end_x).set(turn_end_y,1);
                    blackNums -= 1;
                    whiteNums += 1;
                }
                turn_end_x -= 1;
                turn_end_y += 1;
            }
        }

        meetSame = false;
        meetDiff = false;
        x_tmp = x+1;
        y_tmp = y+1;
        // 往右下探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
                turn_end_x = x_tmp;
                turn_end_y = y_tmp;
                break;
            }
            x_tmp += 1;
            y_tmp += 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){
            turn_end_x -= 1;
            turn_end_y -= 1;
            while(!(turn_end_x == x && turn_end_y == y)){
                if(color == 0){
                    board.get(turn_end_x).set(turn_end_y,0);
                    blackNums += 1;
                    whiteNums -= 1;
                }
                else{
                    board.get(turn_end_x).set(turn_end_y,1);
                    blackNums -= 1;
                    whiteNums += 1;
                }
                turn_end_x -= 1;
                turn_end_y -= 1;
            }
        }

        meetSame = false;
        meetDiff = false;
        x_tmp = x-1;
        y_tmp = y+1;
        // 往右上探测
        while(x_tmp >= 0 && y_tmp >=0 && x_tmp < SideLength && y_tmp < SideLength){
            if(board.get(x_tmp).get(y_tmp) == -1){
                //该方向碰到空位置直接退出
                break;
            }
            else if(board.get(x_tmp).get(y_tmp) != color && meetSame == false){
                meetDiff = true;
            }
            else if(board.get(x_tmp).get(y_tmp) == color){
                meetSame = true;
                turn_end_x = x_tmp;
                turn_end_y = y_tmp;
                break;
            }
            x_tmp -= 1;
            y_tmp += 1;
        }
        //该方向达成翻转条件，可以落子
        if(meetDiff && meetSame){
            turn_end_x += 1;
            turn_end_y -= 1;
            while(!(turn_end_x == x && turn_end_y == y)){
                if(color == 0){
                    board.get(turn_end_x).set(turn_end_y,0);
                    blackNums += 1;
                    whiteNums -= 1;
                }
                else{
                    board.get(turn_end_x).set(turn_end_y,1);
                    blackNums -= 1;
                    whiteNums += 1;
                }
                turn_end_x += 1;
                turn_end_y -= 1;
            }
        }
    }


    void putNone(){
        // 如果对手刚刚没落子, 自己也没落子, 则判定游戏结束
        if(prePutNone){
            System.out.println("双方均不落子, 游戏结束.");
            // 游戏结束, 计算胜负
            playing = false;
            winRes = true;
            int minus = blackNums - whiteNums;
            if(minus >= 0){
                System.out.println("黑色方胜"+minus+"子");
                BlackWin = true;
            }
            else{
                System.out.println("白色方胜"+(-minus)+"子");
                BlackWin = false;
            }
            return;

        }
        if(BlackTurn){System.out.println("黑方不落子");}
        else{System.out.println("白方不落子");}
        BlackTurn = !BlackTurn;
        prePutNone = true;

    }

    @Override
    public HeiBaiQi clone() {
        return (HeiBaiQi) super.clone();
    }
}
