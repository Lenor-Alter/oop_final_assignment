import java.util.Scanner;

public class Client {
    private static Originator o = new Originator();
    private static Creataker c = new Creataker();
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        // 游戏结束可以选择重新开始不同的棋类
        while (true){
            System.out.println("\n(新游戏)请选择棋类：1. 围棋 2. 五子棋 0. 退出游戏");
            int gameType = scan.nextInt();  //棋类选择
            if(gameType == 0){  break;  }
            System.out.println("请选择棋盘大小, 可选边长为[8,19]:");
            int gameSize = scan.nextInt();  //棋类选择
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
            newGame.showBoard();

            // test.start
//        newGame.put(0,0);
//        newGame.put(3,5);
//        newGame.put(1,2);
//        newGame.put(4,7);
//        newGame.put(1,1);
//        newGame.put(7,7);
//        newGame.put(2,2);
//        newGame.put(4,2);
//        newGame.put(3,3);
//        newGame.put(3,2);
//        newGame.put(4,4);
//        newGame.put(2,2);
//        newGame.put(0,5);
//        newGame.reStart();
            // test.end
            int next_x,next_y;
            boolean showMenu = true;
            while(newGame.isPlaying()){
                if(showMenu){
                    System.out.println("\n菜单：20.显示/隐藏菜单 21.悔棋 22.认输 23.保存游戏 24.载入游戏 25.重开游戏 26.不落子（仅限围棋）");
                }
                System.out.println("\n请选择落子位置/菜单选项（落子位置为两个数字，菜单选项为一个数字）：");
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
                else if(next_x == 26){  //不落子（仅限围棋）
                    if(gameType != 1){
                        System.out.println("游戏类型不是围棋, 不支持该操作.");
                    }
                    else{
                        newGame.putNone();
                    }
                    newGame.showBoard();
                    continue;
                }
                next_y = scan.nextInt();  //
                newGame.put(next_x,next_y);
            }
        }
    }
}
