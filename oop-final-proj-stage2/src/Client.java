import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Random;

public class Client {
    private static Originator o = new Originator();
    private static Creataker c = new Creataker();
    private static ArrayList<Account> accounts = new ArrayList<>();
    public static void showPlayer(Account b, Account w, boolean bAI, boolean wAI){
        if(b!=null){System.out.print("\n黑方："+b.getName());}
        else if(!bAI){System.out.print("\n黑方：游客");}
        else{System.out.print("\n黑方：AI");}

        if(w!=null){System.out.println(" 白方："+w.getName());}
        else if(!wAI){System.out.println(" 白方：游客");}
        else{System.out.println(" 白方：AI");}
    }
//    public static int[] AIlevel_1(int SideLength){
//        Random random = new Random();
//        int randx = random.nextInt(SideLength);
//        int randy = random.nextInt(SideLength);
//        return new int[]{randx,randy};
//    }
//    public static int[] AIlevel_2(int SideLength, ArrayList<ArrayList<Integer>> board, boolean BlackTurn){
//        int color;
//        if(BlackTurn){color=0;}
//        else{color=1;}
//        boolean find = false;
//        int ai_x = -1;
//        int ai_y = -1;
//        for(int row=0;row<SideLength;row++){
//            for(int col=0;col<SideLength;col++){
//                //在己方落的子周围找空位
//                if(board.get(row).get(col) == color){
//                    //右 右下 下 左下 左 左上 上 右上
//                    if(col+1<SideLength && board.get(row).get(col+1) == -1){
//                        ai_x = row;
//                        ai_y = col+1;
//                        find = true;
//                    }
//                    else if(col+1<SideLength && row+1<SideLength && board.get(row+1).get(col+1) == -1){
//                        ai_x = row+1;
//                        ai_y = col+1;
//                        find = true;
//                    }
//                    else if(row+1<SideLength && board.get(row+1).get(col) == -1){
//                        ai_x = row+1;
//                        ai_y = col;
//                        find = true;
//                    }
//                    else if(row+1<SideLength && col-1>=0 && board.get(row+1).get(col-1) == -1){
//                        ai_x = row+1;
//                        ai_y = col-1;
//                        find = true;
//                    }
//                    else if(col-1>=0 && board.get(row).get(col-1) == -1){
//                        ai_x = row;
//                        ai_y = col-1;
//                        find = true;
//                    }
//                    else if(row-1>=0 && col-1>=0 && board.get(row-1).get(col-1) == -1){
//                        ai_x = row-1;
//                        ai_y = col-1;
//                        find = true;
//                    }
//                    else if(row-1>=0 && board.get(row-1).get(col) == -1){
//                        ai_x = row-1;
//                        ai_y = col;
//                        find = true;
//                    }
//                    else if(row-1>=0 && col+1<SideLength && board.get(row-1).get(col+1) == -1){
//                        ai_x = row-1;
//                        ai_y = col+1;
//                        find = true;
//                    }
//                }
//                if(find){break;}
//            }
//            if(find){break;}
//        }
//        if(find){
//            return new int[]{ai_x,ai_y};
//        }
//        else{
//            return AIlevel_1(SideLength);
//        }
//
//    }
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        // 游戏结束可以选择重新开始不同的棋类
        while(true){
            //选择账号
            System.out.println("\n新对局！请输入双方账号：");
            boolean bAI = false;
            boolean wAI = false;
            int bAILevel = 1;
            int wAILevel = 1;
            //创建双方账号
            Account bAccount = null;
            Account wAccount = null;
            //黑方
            while(true){
                System.out.println("黑方：1、登录账号 2、注册账号 3、游客游玩 4、AI模式");
                int choose = scan.nextInt();  //黑方账号选择
                if(choose == 1){
                    System.out.println("请输入用户名：");
                    String bName = scan.next();
                    System.out.println("请输入密码：");
                    String bPWD = scan.next();
                    boolean bLogged = false;
                    for(int i=0;i<accounts.size();i++){
                        if(accounts.get(i).validate(bName,bPWD)){
                            bAccount = accounts.get(i);
                            bLogged = true;
                        }
                    }
                    //如果登录失败
                    if(!bLogged){
                        System.out.println("黑方登录失败，账号未注册/密码不正确！");
                        continue;
                    }
                    else{
                        System.out.println("登陆成功！");
                        bAccount.showBattle();
                        break;
                    }
                }
                else if(choose == 2){
                    System.out.println("请输入用户名：");
                    String bName = scan.next();
                    boolean nameExisted = false;
                    for(int i=0;i<accounts.size();i++){
                        if(Objects.equals(accounts.get(i).getName(), bName)){
                            nameExisted = true;
                            break;
                        }
                    }
                    if(nameExisted){
                        System.out.println("该账号已存在，请登录/选择其他用户名");
                        continue;
                    }
                    System.out.println("请输入密码：");
                    String bPWD = scan.next();
                    Account newAcc = new Account(bName,bPWD);
                    bAccount = newAcc;
                    accounts.add(newAcc);
                    System.out.println("账号注册成功，已自动登录.");
                    break;
                }
                else if(choose == 3){
                    System.out.println("黑方以游客模式登录，将不记录战绩");
                    break;
                }
                else if(choose == 4){
                    System.out.println("(警告)当前AI模式只支持五子棋，若棋类非五子棋将自动退出对局");
                    System.out.println("请选择AI级别：1、简单AI 2、不太简单的AI (其他输入默认为简单AI)");
                    int ail = scan.nextInt();
                    if(ail == 1 || ail == 2){bAILevel = ail;}
                    else{bAILevel = 1;}
                    bAI = true;
                    System.out.println("黑方为AI角色，级别为"+bAILevel);
                    break;
                }
                else{
                    System.out.println("输入无效, 请重新选择");
                    continue;
                }
            }
            //白方
            while(true){
                System.out.println("白方：1、登录账号 2、注册账号 3、游客游玩 4、AI模式");
                int choose = scan.nextInt();  //白方账号选择
                if(choose == 1){
                    System.out.println("请输入用户名：");
                    String wName = scan.next();
                    System.out.println("请输入密码：");
                    String wPWD = scan.next();
                    boolean wLogged = false;
                    for(int i=0;i<accounts.size();i++){
                        if(accounts.get(i).validate(wName,wPWD)){
                            wAccount = accounts.get(i);
                            wLogged = true;
                        }
                    }
                    //如果登录失败
                    if(!wLogged){
                        System.out.println("白方登录失败，账号未注册/密码不正确！");
                        continue;
                    }
                    else{
                        System.out.println("登陆成功！");
                        wAccount.showBattle();
                        break;
                    }
                }
                else if(choose == 2){
                    System.out.println("请输入用户名：");
                    String wName = scan.next();
                    boolean nameExisted = false;
                    for(int i=0;i<accounts.size();i++){
                        if(Objects.equals(accounts.get(i).getName(), wName)){
                            nameExisted = true;
                            break;
                        }
                    }
                    if(nameExisted){
                        System.out.println("该账号已存在，请登录/选择其他用户名");
                        continue;
                    }
                    System.out.println("请输入密码：");
                    String wPWD = scan.next();
                    Account newAcc = new Account(wName,wPWD);
                    wAccount = newAcc;
                    accounts.add(newAcc);
                    System.out.println("账号注册成功，已自动登录.");
                    break;
                }
                else if(choose == 3){
                    System.out.println("白方以游客模式登录，将不记录战绩");
                    break;
                }
                else if(choose == 4){
                    System.out.println("(警告)当前AI模式只支持五子棋，若棋类非五子棋将自动退出对局");
                    System.out.println("请选择AI级别：1、简单AI 2、不太简单的AI (其他输入默认为简单AI)");
                    int ail = scan.nextInt();
                    if(ail == 1 || ail == 2){wAILevel = ail;}
                    else{wAILevel = 1;}
                    wAI = true;
                    System.out.println("白方为AI角色，级别为"+wAILevel);
                    break;
                }
                else{
                    System.out.println("输入无效, 请重新选择");
                    continue;
                }
            }
            //对局
            while (true){
                AIFactory newAIFactory = new AIFactory();
                System.out.println("\n(新游戏)请选择棋类：1. 围棋 2. 五子棋 3. 黑白棋 0. 退出对局");
                int gameType = scan.nextInt();  //棋类选择
                if(gameType == 0){break;}
                if(gameType != 2 && (bAI || wAI)){
                    System.out.println("\n非五子棋对局不能有AI玩家.");
                    break;
                }
                AI bAIPlayer = null;
                AI wAIPlayer = null;
                if(gameType == 2 && bAI){
                    bAIPlayer = newAIFactory.createAI(bAILevel);
                }
                if(gameType == 2 && wAI){
                    wAIPlayer = newAIFactory.createAI(wAILevel);
                }
                int gameSize = 8;
                if(gameType != 3){
                    System.out.println("请选择棋盘大小, 可选边长为[8,19]:");
                    gameSize = scan.nextInt();  //棋类选择
                }
                if(gameSize < 8 || gameSize > 19){
                    System.out.println(gameSize+"不是合法的棋盘大小, 棋盘大小将默认为8格");
                    gameSize = 8;
                }
                System.out.println("请选择最大悔棋次数, 可选范围为[0,99]:");
                int gameRetract = scan.nextInt();  //悔棋次数选择
                if(gameRetract < 0 || gameRetract > 99){
                    System.out.println(gameSize+"不是合法的悔棋次数, 本局禁止悔棋");
                    gameRetract = 0;
                }
                BoardFactory newGameFactory = new BoardFactory();

                BasicBoard newGame = newGameFactory.createNewBoard(gameType, gameSize, gameRetract);
                //开始游戏, 打印棋盘
                System.out.println("游戏开始, 黑子先下！");
                newGame.startGame();
                showPlayer(bAccount,wAccount,bAI,wAI);
                newGame.showBoard();

                int next_x,next_y;
                boolean showMenu = true;
                while(newGame.isPlaying()){
                    showPlayer(bAccount,wAccount,bAI,wAI);
                    if(showMenu){
                        System.out.println("\n菜单：20.显示/隐藏菜单 21.悔棋 22.认输 23.保存游戏 24.载入游戏 25.重开游戏 26.不落子（仅限围棋、黑白棋）27.查看回放");
                    }
                    System.out.println("\n请选择落子位置/菜单选项（落子位置为两个数字，菜单选项为一个数字）：");
                    if(gameType == 2 && bAI && newGame.getBlackTurn()){
                        //五子棋且黑色是AI, 该轮由AI落子
                        //如果还是黑色轮次说明该落子坐标不合法，继续尝试落子
                        while(newGame.getBlackTurn()){
                            //生成个坐标然后落子
                            int[] ai_coordinate = bAIPlayer.GiveCoordinate(newGame.getSideLength(),newGame.getBoard(),newGame.getBlackTurn());

                            int ai_x = ai_coordinate[0];
                            int ai_y = ai_coordinate[1];
                            System.out.println("\nAI尝试落子在("+ai_x+","+ai_y+")");
                            newGame.put(ai_x,ai_y);
                        }
                        continue;
                    }
                    if(gameType == 2 && wAI && !newGame.getBlackTurn()){
                        //五子棋且白色是AI, 该轮由AI落子
                        //如果还是白色轮次说明该落子坐标不合法，继续尝试落子
                        while(!newGame.getBlackTurn()){
                            //生成个坐标然后落子
                            int[] ai_coordinate = wAIPlayer.GiveCoordinate(newGame.getSideLength(),newGame.getBoard(),newGame.getBlackTurn());

                            int ai_x = ai_coordinate[0];
                            int ai_y = ai_coordinate[1];
                            System.out.println("\nAI尝试落子在("+ai_x+","+ai_y+")");
                            newGame.put(ai_x,ai_y);
                        }
                        continue;
                    }
                    next_x = scan.nextInt();  //横坐标or指令
                    if(next_x == 20){
                        if(showMenu){
                            showMenu = false;
                            newGame.showBoard();
                            System.out.println("\n菜单已隐藏！");
                        }
                        else{
                            showMenu = true;
                            newGame.showBoard();
                            System.out.println("\n菜单已显示！");
                        }
                        continue;
                    }
                    else if(next_x == 21){  //悔棋
                        newGame.retract();
                        continue;
                    }
                    else if(next_x == 22){  //投降
                        newGame.surrender();
                        continue;
                    }
                    else if(next_x == 23){
                        System.out.println("请输入保存的存档名称：");
                        String saveName = scan.next();
                        o.restoreFile(newGame.clone());
                        c.saveFile(o.createFile(),saveName);
                        System.out.println("存档保存成功！");
                        newGame.showBoard();
                        continue;
                    }
                    else if(next_x == 24){
                        System.out.println("现有存档如下：");
                        c.showFiles();
                        System.out.println("请选择恢复的存档编号");
                        int resNo = scan.nextInt();
                        o.restoreFile(c.retrieveFile(resNo));
                        if(o.checkFileExist()){
                            newGame = (BasicBoard) o.createFile();
//                        newGame.showInfo();
//                        o.printInfo();
                            System.out.println("存档恢复成功");
                        }
                        else{System.out.println("存档恢复失败, 继续当前对局");}
                        newGame.showBoard();
                        continue;
                    }
                    else if(next_x == 25){  //重开游戏
                        newGame.reStart();
                        continue;
                    }
                    else if(next_x == 26){  //不落子（仅限围棋、黑白棋）
                        if(gameType == 1){
                            newGame.putNone();
                        }
                        else if(gameType == 2){
                            System.out.println("五子棋不支持该操作.");
                        }
                        else if(gameType == 3){
                            newGame.putNone();
                        }
                        newGame.showBoard();
                        continue;
                    }
                    else if(next_x == 27){  //查看回放
                        newGame.showRecord();
                        newGame.showBoard();
                        continue;
                    }
                    next_y = scan.nextInt();  //
                    newGame.put(next_x,next_y);
                }
                //对局结束，更新账号胜场信息

                //黑方胜利
                if(newGame.getBlackWin() && newGame.getWinRes()){
                    if(bAccount != null){
                        System.out.print("黑方");
                        for(Account account:accounts){
                            if(Objects.equals(account.getName(), bAccount.getName())){
                                account.updateBattle(true);
//                                account.showBattle();
                            }
                        }
                    }
                    else if(!bAI){System.out.println("黑方为游客，不记录对局信息");}
                    else{System.out.println("黑方为AI，不记录对局信息");}
                    if(wAccount != null){
                        System.out.print("白方");
                        for(Account account:accounts){
                            if(Objects.equals(account.getName(), wAccount.getName())){
                                account.updateBattle(false);
//                                account.showBattle();
                            }
                        }
                    }
                    else if(!wAI){System.out.println("白方为游客，不记录对局信息");}
                    else{System.out.println("白方为AI，不记录对局信息");}
                }
                //白方胜利
                else if(!newGame.getBlackWin() && newGame.getWinRes()){
                    if(bAccount != null){
                        System.out.print("黑方");
                        for(Account account:accounts){
                            if(Objects.equals(account.getName(), bAccount.getName())){
                                account.updateBattle(false);
//                                account.showBattle();
                            }
                        }
                    }
                    else if(!bAI){System.out.println("黑方为游客，不记录对局信息");}
                    else{System.out.println("黑方为AI，不记录对局信息");}
                    if(wAccount != null){
                        System.out.print("白方");
                        for(Account account:accounts){
                            if(Objects.equals(account.getName(), wAccount.getName())){
                                account.updateBattle(true);
//                                account.showBattle();
                            }
                        }
                    }
                    else if(!wAI){System.out.println("白方为游客，不记录对局信息");}
                    else{System.out.println("白方为AI，不记录对局信息");}
                }
            }
        }
    }
}
