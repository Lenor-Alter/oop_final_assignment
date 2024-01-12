import java.util.ArrayList;

public class BoardFactory {
    BasicBoard createNewBoard(int type, int SL, int maxRetractChance){
        if(type == 1){
            return new WeiQi(type, SL, maxRetractChance);
        }
        else if (type == 2) {
            return new WuZiQi(type, SL, maxRetractChance);
        }
        else if (type == 3) {
            return new HeiBaiQi(type, SL, maxRetractChance);
        }
        return null;
    }

    BasicBoard createSavedBoard(int gT, int SL, int mRC, int mRCB, int mRCW, int lpx, int lpy, int bN, int wN, boolean pling, boolean BT, boolean wR, boolean BW,ArrayList<ArrayList<Integer>> bd, ArrayList<ArrayList<ArrayList<Integer>>> bdr){
        if(gT == 1){
            return new WeiQi(gT,  SL,  mRC,  mRCB,  mRCW,  lpx,  lpy,  bN,  wN,  pling,  BT,  wR,  BW, bd, bdr);
        }
        else if (gT == 2) {
            return new WuZiQi(gT,  SL,  mRC,  mRCB,  mRCW,  lpx,  lpy,  bN,  wN,  pling,  BT,  wR,  BW, bd, bdr);
        }
        else if (gT == 3) {
            return new HeiBaiQi(gT,  SL,  mRC,  mRCB,  mRCW,  lpx,  lpy,  bN,  wN,  pling,  BT,  wR,  BW, bd, bdr);
        }
        return null;
    }
}
