import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class WeiQi extends BasicBoard implements IFile,Cloneable{
    protected boolean prePutNone = false;
    WeiQi(int gT,int SL, int maxRetractChance) {
        super(gT,SL, maxRetractChance);
    }
    WeiQi(int gT, int SL, int mRC, int mRCB, int mRCW, int lpx, int lpy, int bN, int wN, boolean pling, boolean BT, boolean wR, boolean BW,ArrayList<ArrayList<Integer>> bd){
        super( gT,  SL,  mRC,  mRCB,  mRCW,  lpx,  lpy,  bN,  wN,  pling,  BT,  wR,  BW, bd);
    }
    // 人工拿走死棋, 在判定胜负时使用
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

        // 如果落子后没有气, 且不能吃掉对方一些棋子, 那么该位置不能落子
        // 如果落子后没有气, 但是可以吃掉对方一些棋子, 那么该位置可以落子, 并且需要提子
        // 逻辑：先落子,
        // 1. 判定邻居有没有气, 若存在无气的邻居, 把无气的邻居全吃了, 结束该轮
        // 2. 若邻居都有气, 则判定该子有没有气, 若无气, 则撤回这个子, 提示这里不能下, 结束该轮
        // 3. 若邻居都有气, 该子也有气, 则可以下, 结束该轮
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
        if(!neighborHaveGas(x,y)){
            //存在没气的邻居, 在判定时就已经完成提子, 结束该轮
            prePutNone = false;
            last_pos_x = x;
            last_pos_y = y;
        }
        else if(HaveGas(x,y)){
            // 邻居都有气, 这个子也有气
            prePutNone = false;
            last_pos_x = x;
            last_pos_y = y;
        }
        else if(!HaveGas(x,y)){
            System.out.println("这个位置没有气不能下, 请重新选择");
            board.get(x).set(y,-1);
            if(BlackTurn){
                whiteNums -=1;
                BlackTurn = false;
            }
            else{
                blackNums -=1;
                BlackTurn = true;
            }
        }
        showBoard();
        return;
    }
    // 邻居是否都有气
    boolean neighborHaveGas(int x,int y){
        boolean haveGas = true;
        ArrayList<Integer> neighbor_xs = new ArrayList<>();
        ArrayList<Integer> neighbor_ys = new ArrayList<>();
        neighbor_xs.add(x);
        neighbor_ys.add(y-1);
        neighbor_xs.add(x);
        neighbor_ys.add(y+1);
        neighbor_xs.add(x-1);
        neighbor_ys.add(y);
        neighbor_xs.add(x+1);
        neighbor_ys.add(y);
        for(int i=0;i<neighbor_xs.size();i++){
            int neighbor_x = neighbor_xs.get(i);
            int neighbor_y = neighbor_ys.get(i);
            if(neighbor_x < 0 || neighbor_x >= SideLength || neighbor_y < 0 || neighbor_y >= SideLength){
                //该坐标在棋盘外, 不计算
                continue;
            }
            else if(board.get(neighbor_x).get(neighbor_y) != -1 && HaveGas(neighbor_x,neighbor_y)){
                continue;
            }
            else if(board.get(neighbor_x).get(neighbor_y) != -1){
                // 存在没有气的合法棋子邻居, 这里直接把邻居全提走, 还未实现
                haveGas = false;
                // 把没有气的一片棋子都提走
                int removeRes = removeNoGas(neighbor_x,neighbor_y);
            }
        }
        return haveGas;
    }
    // 当前节点是否有气
    boolean HaveGas(int x,int y){
        int thisColor = board.get(x).get(y);
        int gas = 0;
        // 保存有气位置的坐标, 防止重复记录
        ArrayList<Integer> gas_x = new ArrayList<>();
        ArrayList<Integer> gas_y = new ArrayList<>();
        // 落子后保存邻居异色子的坐标, 防止重复记录
        ArrayList<Integer> scanned_x = new ArrayList<>();
        ArrayList<Integer> scanned_y = new ArrayList<>();
        // 等待扫描的异色子
        ArrayList<Integer> to_be_scanned_x = new ArrayList<>();
        ArrayList<Integer> to_be_scanned_y = new ArrayList<>();
        int this_x,this_y;
        // 检查gas需要查看相邻的四个位置
        int left_x, left_y, right_x, right_y, up_x, up_y, down_x, down_y;
        to_be_scanned_x.add(x);
        to_be_scanned_y.add(y);
        while(!to_be_scanned_x.isEmpty()){
            // 取出当前节点以及计算四个邻居的坐标
            this_x = to_be_scanned_x.get(0);
            this_y = to_be_scanned_y.get(0);
            to_be_scanned_x.removeFirst();
            to_be_scanned_y.removeFirst();
            scanned_x.add(this_x);
            scanned_y.add(this_y);
            ArrayList<Integer> adj_xs = new ArrayList<>();
            ArrayList<Integer> adj_ys = new ArrayList<>();
            adj_xs.add(this_x);
            adj_ys.add(this_y-1);
            adj_xs.add(this_x);
            adj_ys.add(this_y+1);
            adj_xs.add(this_x-1);
            adj_ys.add(this_y);
            adj_xs.add(this_x+1);
            adj_ys.add(this_y);
            for(int i=0;i<adj_xs.size();i++){
                int adj_x = adj_xs.get(i);
                int adj_y = adj_ys.get(i);
                if(adj_x < 0 || adj_x >= SideLength || adj_y < 0 || adj_y >= SideLength){
                    //这个邻居已经在棋盘之外, 直接略过
                    continue;
                }
                else if(board.get(adj_x).get(adj_y) == -1){
                    //该位置未落子, 有气, 先判断该气所在位置是否已经加入gas列表
                    boolean joined = false;
                    for(int j=0;j<gas_x.size();j++){
                        if(gas_x.get(j) == adj_x && gas_y.get(j) == adj_y){
                            joined = true;  //如果该气的位置已经保存, 那么joined变为true, 不重复添加坐标
                        }
                    }
                    if(!joined){
                        gas +=1 ;
                        gas_x.add(adj_x);
                        gas_y.add(adj_y);
                    }
                }
                else if(board.get(adj_x).get(adj_y) != -1 && board.get(adj_x).get(adj_y) != thisColor){
                    //该位置落子, 且和本棋子颜色不同, 这个方向没气且不用遍历
                    continue;
                }
                else if(board.get(adj_x).get(adj_y) == thisColor){
                    //颜色相同, 如果该子不在to_be_scanned和scanned, 则加入如果该子不在to_be_scanned和scanned
                    boolean exist = false;
                    for(int j=0;j<to_be_scanned_x.size();j++) {
                        if (to_be_scanned_x.get(j) == adj_x && to_be_scanned_y.get(j) == adj_y) {
                            exist = true;  //如果该气的位置已经保存, 那么joined变为true, 不重复添加坐标
                        }
                    }
                    for(int k=0;k<scanned_x.size();k++){
                        if(scanned_x.get(k) == adj_x && scanned_y.get(k) == adj_y){
                            exist = true;  //如果该气的位置已经保存, 那么joined变为true, 不重复添加坐标
                        }
                    }
                    if(!exist){
                        to_be_scanned_x.add(adj_x);
                        to_be_scanned_y.add(adj_y);
                    }
                }
            }
        }
        return gas > 0 ;
    }

    int removeNoGas(int x,int y){
        int thisColor = board.get(x).get(y);
        int gas = 0;
        // 保存有气位置的坐标, 防止重复记录
        ArrayList<Integer> gas_x = new ArrayList<>();
        ArrayList<Integer> gas_y = new ArrayList<>();
        // 落子后保存邻居异色子的坐标, 防止重复记录
        ArrayList<Integer> scanned_x = new ArrayList<>();
        ArrayList<Integer> scanned_y = new ArrayList<>();
        // 等待扫描的异色子
        ArrayList<Integer> to_be_scanned_x = new ArrayList<>();
        ArrayList<Integer> to_be_scanned_y = new ArrayList<>();
        int this_x,this_y;
        // 检查gas需要查看相邻的四个位置
        int left_x, left_y, right_x, right_y, up_x, up_y, down_x, down_y;
        to_be_scanned_x.add(x);
        to_be_scanned_y.add(y);
        while(!to_be_scanned_x.isEmpty()){
            // 取出当前节点以及计算四个邻居的坐标
            this_x = to_be_scanned_x.get(0);
            this_y = to_be_scanned_y.get(0);
            to_be_scanned_x.removeFirst();
            to_be_scanned_y.removeFirst();
            scanned_x.add(this_x);
            scanned_y.add(this_y);
            ArrayList<Integer> adj_xs = new ArrayList<>();
            ArrayList<Integer> adj_ys = new ArrayList<>();
            adj_xs.add(this_x);
            adj_ys.add(this_y-1);
            adj_xs.add(this_x);
            adj_ys.add(this_y+1);
            adj_xs.add(this_x-1);
            adj_ys.add(this_y);
            adj_xs.add(this_x+1);
            adj_ys.add(this_y);
            for(int i=0;i<adj_xs.size();i++){
                int adj_x = adj_xs.get(i);
                int adj_y = adj_ys.get(i);
                if(adj_x < 0 || adj_x >= SideLength || adj_y < 0 || adj_y >= SideLength){
                    //这个邻居已经在棋盘之外, 直接略过
                    continue;
                }
                else if(board.get(adj_x).get(adj_y) == -1){
                    //该位置未落子, 有气, 先判断该气所在位置是否已经加入gas列表
                    boolean joined = false;
                    for(int j=0;j<gas_x.size();j++){
                        if(gas_x.get(j) == adj_x && gas_y.get(j) == adj_y){
                            joined = true;  //如果该气的位置已经保存, 那么joined变为true, 不重复添加坐标
                        }
                    }
                    if(!joined){
                        gas +=1 ;
                        gas_x.add(adj_x);
                        gas_y.add(adj_y);
                    }
                }
                else if(board.get(adj_x).get(adj_y) != -1 && board.get(adj_x).get(adj_y) != thisColor){
                    //该位置落子, 且和本棋子颜色不同, 这个方向没气且不用遍历
                    continue;
                }
                else if(board.get(adj_x).get(adj_y) == thisColor){
                    //颜色相同, 如果该子不在to_be_scanned和scanned, 则加入如果该子不在to_be_scanned和scanned
                    boolean exist = false;
                    for(int j=0;j<to_be_scanned_x.size();j++) {
                        if (to_be_scanned_x.get(j) == adj_x && to_be_scanned_y.get(j) == adj_y) {
                            exist = true;  //如果该气的位置已经保存, 那么joined变为true, 不重复添加坐标
                        }
                    }
                    for(int k=0;k<scanned_x.size();k++){
                        if(scanned_x.get(k) == adj_x && scanned_y.get(k) == adj_y){
                            exist = true;  //如果该气的位置已经保存, 那么joined变为true, 不重复添加坐标
                        }
                    }
                    if(!exist){
                        to_be_scanned_x.add(adj_x);
                        to_be_scanned_y.add(adj_y);
                    }
                }
            }
        }
        // 前面部分和HasGas相同, 但此时要利用scanned数组完成提子
        int removedNum = 0;
        for(int i=0;i<scanned_x.size();i++){
            int removed_x = scanned_x.get(i);
            int removed_y = scanned_y.get(i);
            board.get(removed_x).set(removed_y,-1);
            removedNum += 1;
        }
        if(thisColor == 0){
            System.out.println("黑子被提走"+removedNum+"个");
            blackNums -= removedNum;
        }
        else{
            System.out.println("白子被提走"+removedNum+"个");
            whiteNums -= removedNum;
        }
        return  removedNum;
    }
    void removeDead(int x,int y){
        int dead_color = board.get(x).get(y);
        if(dead_color == 0){
            blackNums -= 1;
        }
        else if(dead_color == 1){
            whiteNums -= 1;
        }
        board.get(x).set(y,-1);
    }
    void putNone(){
        // 如果对手刚刚没落子, 自己也没落子, 则判定游戏结束
        if(prePutNone){
            System.out.println("双方均不落子, 游戏结束.");
            // 开始清除死棋
            Scanner scan_dead = new Scanner(System.in);
            int dead_x,dead_y;
            while(true){
                System.out.println("\n请输入死棋坐标,(-1,-1)表示清除完成:");
                dead_x = scan_dead.nextInt();
                dead_y = scan_dead.nextInt();
                if(dead_x == -1 && dead_y == -1){
                    System.out.println("死棋清除完成");
                    break;
                }
                else if(dead_x < 0 || dead_x >= SideLength || dead_y < 0 || dead_y >= SideLength){
                    System.out.println("坐标异常,请重新输入:");
                }
                else{
                    removeDead(dead_x, dead_y);
                    System.out.println("清除成功");
                }
                showBoard();
            }
//            System.out.println("Test：清除完死棋, 开始计算结果");
            // 游戏结束, 计算胜负
            playing = false;
            winRes = true;
//            System.out.println("Test：清除完死棋, 开始计算结果2");
            int[] terriRes = calculateTerritory();
//            System.out.println("Test：清除完死棋, 开始计算结果3");
            int blackTerri = terriRes[0]+blackNums;
            int whiteTerri = terriRes[1]+whiteNums;
//            System.out.println("Test：清除完死棋, 开始计算结果4");
            System.out.println("黑色方占"+blackTerri+"子"+", 白色方占"+whiteTerri+"子");
            // 黑子贴3.75目
            double minus = (double) blackTerri - (double) whiteTerri - 7.5;
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
    int[] calculateTerritory(){
        // 该函数返回值的意义还没想好，应该是返回两个值的数组, 分别记录黑子白子的领地
        // 保存扫描过的坐标, 防止重复记录
        int blackTerriCount = 0;
        int whiteTerriCount = 0;
        ArrayList<Integer> scanned_x = new ArrayList<>();
        ArrayList<Integer> scanned_y = new ArrayList<>();
        int colorNow;
        // 遍历棋盘
        for(int row=0;row<SideLength;row++){
            for(int col=0;col<SideLength;col++){
//                System.out.println("\nTest:当前在"+row+"行, "+col+"列");
                colorNow = board.get(row).get(col);
                // 当前位置有子才计算可能的领地
                if(colorNow == -1){
                    continue;
                }
                // 计算黑子围起来的区域
                else{
//                    System.out.println("\nTest:开始找邻居了");
                    ArrayList<Integer> neighbors_x = new ArrayList<>();
                    ArrayList<Integer> neighbors_y = new ArrayList<>();
                    neighbors_x.add(row);
                    neighbors_y.add(col-1);
                    neighbors_x.add(row);
                    neighbors_y.add(col+1);
                    neighbors_x.add(row-1);
                    neighbors_y.add(col);
                    neighbors_x.add(row+1);
                    neighbors_y.add(col);
                    for(int nb_num=0;nb_num<neighbors_x.size();nb_num++){
                        if(neighbors_x.get(nb_num) < 0 || neighbors_x.get(nb_num) >= SideLength || neighbors_y.get(nb_num) < 0 || neighbors_y.get(nb_num) >= SideLength){
                            //这个邻居已经在棋盘之外, 直接略过
                            continue;
                        }
                        else if(board.get(neighbors_x.get(nb_num)).get(neighbors_y.get(nb_num)) != -1){
                            //这个邻居有棋子, 直接略过
                            continue;
                        }
                        else{
//                            System.out.println("\nTest:对当前空邻居开始探测了");
                            // 对该方向的空位置进行试探, 如果成功则加入总的子数中
                            boolean scanned_flag = false;
                            // scan之前, 查询该点是否已经扫描过
                            for(int i=0;i<scanned_x.size();i++){
                                if (Objects.equals(scanned_x.get(i), neighbors_x.get(nb_num)) && Objects.equals(scanned_y.get(i), neighbors_y.get(nb_num))) {
                                    scanned_flag = true;
                                    break;
                                }
                            }
//                            System.out.println("\nTest:当前空邻居等待扫描");
                            // 如果邻居全是同一个色的棋子, 说明计数有效, 该区域被这个颜色包围
                            boolean countValid = true;
                            int countNum = 0;
                            ArrayList<Integer> temp_to_scan_x = new ArrayList<>();
                            ArrayList<Integer> temp_to_scan_y = new ArrayList<>();
                            ArrayList<Integer> temp_scanned_x = new ArrayList<>();
                            ArrayList<Integer> temp_scanned_y = new ArrayList<>();
                            // 如果该点没被扫描过
                            if(!scanned_flag){
//                                System.out.println("\nTest:当前空邻居加入to scan队列");
                                temp_to_scan_x.add(neighbors_x.get(nb_num));
                                temp_to_scan_y.add(neighbors_y.get(nb_num));
                            }
                            while(!temp_to_scan_x.isEmpty()){
//                                System.out.println("\nTest:temp_to_scan新轮次, 此时队列size为："+temp_to_scan_x.size());
                                int temp_scan_x = temp_to_scan_x.getFirst();
                                int temp_scan_y = temp_to_scan_y.getFirst();
                                temp_to_scan_x.removeFirst();
                                temp_to_scan_y.removeFirst();
                                temp_scanned_x.add(temp_scan_x);
                                temp_scanned_y.add(temp_scan_y);
                                countNum += 1;
                                // 计算当前点的邻居
                                ArrayList<Integer> temp_neibor_x = new ArrayList<>();
                                ArrayList<Integer> temp_neibor_y = new ArrayList<>();
                                temp_neibor_x.add(temp_scan_x);
                                temp_neibor_y.add(temp_scan_y-1);
                                temp_neibor_x.add(temp_scan_x);
                                temp_neibor_y.add(temp_scan_y+1);
                                temp_neibor_x.add(temp_scan_x-1);
                                temp_neibor_y.add(temp_scan_y);
                                temp_neibor_x.add(temp_scan_x+1);
                                temp_neibor_y.add(temp_scan_y);
                                for(int tmp_nb_id=0;tmp_nb_id<temp_neibor_x.size();tmp_nb_id++){
                                    if(temp_neibor_x.get(tmp_nb_id) < 0 || temp_neibor_x.get(tmp_nb_id) >= SideLength || temp_neibor_y.get(tmp_nb_id) < 0 || temp_neibor_y.get(tmp_nb_id) >= SideLength){
                                        //这个邻居已经在棋盘之外, 直接略过
                                        continue;
                                    }
                                    else if(board.get(temp_neibor_x.get(tmp_nb_id)).get(temp_neibor_y.get(tmp_nb_id)) == -1){
                                        //该邻居是空位置, 先查询是否在总的scanned里, 若无再加入to_scan
                                        boolean nb_scanned_flag = false;
                                        for(int sc_id=0;sc_id<scanned_x.size();sc_id++){
                                            if(Objects.equals(scanned_x.get(sc_id), temp_neibor_x.get(tmp_nb_id)) && Objects.equals(scanned_y.get(sc_id), temp_neibor_y.get(tmp_nb_id))){
                                                nb_scanned_flag = true;
                                                break;
                                            }
                                        }
                                        // 查询邻居是否在to_scanned列表里
                                        for(int sc_id=0;sc_id<temp_to_scan_x.size();sc_id++){
                                            if(Objects.equals(temp_to_scan_x.get(sc_id), temp_neibor_x.get(tmp_nb_id)) && Objects.equals(temp_to_scan_y.get(sc_id), temp_neibor_y.get(tmp_nb_id))){
                                                nb_scanned_flag = true;
                                                break;
                                            }
                                        }
                                        // 查询邻居是否在temp_scanned列表里
                                        for(int sc_id=0;sc_id<temp_scanned_x.size();sc_id++){
                                            if (Objects.equals(temp_scanned_x.get(sc_id), temp_neibor_x.get(tmp_nb_id)) && Objects.equals(temp_scanned_y.get(sc_id), temp_neibor_y.get(tmp_nb_id))) {
                                                nb_scanned_flag = true;
                                                break;
                                            }
                                        }
                                        // 若该点没有在三种scann列表中, 则加入to_scan列表
                                        if(!nb_scanned_flag){
                                            temp_to_scan_x.add(temp_neibor_x.get(tmp_nb_id));
                                            temp_to_scan_y.add(temp_neibor_y.get(tmp_nb_id));
                                        }
                                    }
                                    // 如果该空区域与不同颜色的棋子相连, 那么不属于任何一方的地盘, 但是scanned仍然有效
                                    else if(board.get(temp_neibor_x.get(tmp_nb_id)).get(temp_neibor_y.get(tmp_nb_id)) != colorNow){
                                        countValid = false;
                                    }
                                }

                            }
                            // 上述循环结束时, 说明一个连通的空区域已经被扫描完毕, 扫描过的空位置都存储在temp_scanned数组里
                            if(countValid){
                                if(colorNow == 0){
                                    blackTerriCount += countNum;
                                }
                                else if(colorNow == 1){
                                    whiteTerriCount += countNum;
                                }
                            }
                            // 把该轮扫描过的空位置加入总的scanned队列
                            for(int tmp_sc_id=0;tmp_sc_id<temp_scanned_x.size();tmp_sc_id++){
                                int tmp_x = temp_scanned_x.get(tmp_sc_id);
                                int tmp_y = temp_scanned_y.get(tmp_sc_id);
                                scanned_x.add(tmp_x);
                                scanned_y.add(tmp_y);
                            }
                        }
                    }
//                    System.out.println("\nTest:邻居找完了");
                }
            }
        }
        return new int[]{blackTerriCount, whiteTerriCount};
    }

    @Override
    public WeiQi clone() {
        return (WeiQi) super.clone();
    }
}
