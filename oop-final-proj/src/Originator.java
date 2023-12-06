import java.util.ArrayList;

public class Originator {
    protected int gameType = -1;
    protected int SL = 8;
    //棋局是否在进行中, BlackWin是否有效的前置条件
    protected boolean pling = false;
    //黑子先下
    protected boolean BT = true;
    // 是否分出胜利结果, BlackWin是否有效的前置条件
    protected boolean wR = false;
    protected boolean BW = false;
    protected ArrayList<ArrayList<Integer>> bd = new ArrayList<ArrayList<Integer>>();
    protected int lpx = -1;
    protected int lpy = -1;
    protected int mRC = 0;
    protected int mRCB = 0;
    protected int mRCW = 0;
    // 棋局上的黑子/白子数目
    protected int bN = 0;
    protected int wN = 0;
    protected boolean fileExist = false;
    public IFile createFile(){
        BoardFactory newfactory = new BoardFactory();
        return newfactory.createSavedBoard(gameType, SL, mRC, mRCB, mRCW, lpx, lpy, bN, wN, pling, BT, wR, BW, bd);
    }
    public void restoreFile(IFile file){
        if(file == null){
            fileExist = false;
            return;
        }
        fileExist = true;
        BasicBoard afile = (BasicBoard) file;
        this.setGT(afile.getGameType());
        this.setMRC(afile.getMaxRetractChance(),afile.getMaxRetractChanceBlack(),afile.getMaxRetractChanceWhite());
        this.setLastPos(afile.getLastPosX(),afile.getLastPosY());
        this.setSideLength(afile.getSideLength());
        this.setNums(afile.getBlackNums(),afile.getWhiteNums());
        this.setBooleans(afile.getPlaying(),afile.getBlackTurn(),afile.getWinRes(),afile.getBlackWin());
        this.setBoard(afile.getBoard());
    }
    public void setGT(int gameType){
        this.gameType = gameType;
    }
    public void setMRC(int mRC, int mRCB, int mRCW){
        this.mRC = mRC;
        this.mRCB = mRCB;
        this.mRCW = mRCW;
    }
    public void setLastPos(int lpx,int lpy){
        this.lpx = lpx;
        this.lpy = lpy;
    }
    public void setSideLength(int SL){
        this.SL = SL;
    }
    public void setNums(int bN,int wN){
        this.bN = bN;
        this.wN = wN;
    }
    public void setBooleans(boolean pling,boolean BT, boolean wR, boolean BW){
        this.pling = pling;
        this.BT = BT;
        this.wR = wR;
        this.BW = BW;
    }
    public void setBoard(ArrayList<ArrayList<Integer>> board){
        ArrayList<ArrayList<Integer>> tempbd = new ArrayList<>();
        for(int i=0;i<SL;i++){
            tempbd.add((ArrayList<Integer>) board.get(i).clone());
        }
        bd = tempbd;
    }
    public void showBoard(){
        System.out.print("\n    ");
        for(int i=0;i<SL;i++){
            if(i<10){
                System.out.print(i+"   ");
            }
            else{
                System.out.print(i+"  ");
            }
        }
        System.out.print("\n  ");
        for(int i=0;i<SL;i++){
            System.out.print("————");
        }
        for(int i=0;i<SL;i++){
            if(i<10){
                System.out.print("\n"+i+" | ");
            }
            else{
                System.out.print("\n"+i+"| ");
            }
            ArrayList<Integer> row = bd.get(i);
            for(int j=0;j<SL;j++){
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
            for(int k=0;k<SL;k++){
                System.out.print("————");
            }
        }
    }
    public void printInfo(){
        System.out.println("\n存档信息：");
        if(pling){System.out.print("本棋局进行中,");}

        else if(!pling && wR){
            System.out.print("本棋局已结束,胜者为：");
            if(BW){System.out.print("黑子");}
            else {System.out.print("白子");}
            showBoard();
            return;
        }
        else{
            System.out.print("本棋局未开始.");
            showBoard();
            return;
        }
        System.out.println("当前黑子已下"+bN+"子, "+"当前白子已下"+wN+"子");
        System.out.print("接下来落");
        if(BT){System.out.print("黑子");}
        else{System.out.print("白子");}
        showBoard();
        return;
    }
    public boolean checkFileExist(){    return fileExist;}
}
