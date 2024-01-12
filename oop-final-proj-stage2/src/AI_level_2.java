import java.util.ArrayList;
import java.util.Random;
public class AI_level_2 extends AI{
    public int[] GiveCoordinate(int SideLength, ArrayList<ArrayList<Integer>> board, boolean BlackTurn){
        int color;
        if(BlackTurn){color=0;}
        else{color=1;}
        boolean find = false;
        int ai_x = -1;
        int ai_y = -1;
        for(int row=0;row<SideLength;row++){
            for(int col=0;col<SideLength;col++){
                //在己方落的子周围找空位
                if(board.get(row).get(col) == color){
                    //右 右下 下 左下 左 左上 上 右上
                    if(col+1<SideLength && board.get(row).get(col+1) == -1){
                        ai_x = row;
                        ai_y = col+1;
                        find = true;
                    }
                    else if(col+1<SideLength && row+1<SideLength && board.get(row+1).get(col+1) == -1){
                        ai_x = row+1;
                        ai_y = col+1;
                        find = true;
                    }
                    else if(row+1<SideLength && board.get(row+1).get(col) == -1){
                        ai_x = row+1;
                        ai_y = col;
                        find = true;
                    }
                    else if(row+1<SideLength && col-1>=0 && board.get(row+1).get(col-1) == -1){
                        ai_x = row+1;
                        ai_y = col-1;
                        find = true;
                    }
                    else if(col-1>=0 && board.get(row).get(col-1) == -1){
                        ai_x = row;
                        ai_y = col-1;
                        find = true;
                    }
                    else if(row-1>=0 && col-1>=0 && board.get(row-1).get(col-1) == -1){
                        ai_x = row-1;
                        ai_y = col-1;
                        find = true;
                    }
                    else if(row-1>=0 && board.get(row-1).get(col) == -1){
                        ai_x = row-1;
                        ai_y = col;
                        find = true;
                    }
                    else if(row-1>=0 && col+1<SideLength && board.get(row-1).get(col+1) == -1){
                        ai_x = row-1;
                        ai_y = col+1;
                        find = true;
                    }
                }
                if(find){break;}
            }
            if(find){break;}
        }
        if(find){
            return new int[]{ai_x,ai_y};
        }
        else{
            Random random = new Random();
            int randx = random.nextInt(SideLength);
            int randy = random.nextInt(SideLength);
            return new int[]{randx,randy};
        }

    }
}
