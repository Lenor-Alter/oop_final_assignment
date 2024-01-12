import java.util.Objects;

public class Account {
    private String name;
    private String pwd;
    private int battles;
    private int wins;
    Account(String name,String pwd){
        this.name = name;
        this.pwd = pwd;
        battles = 0;
        wins = 0;
    }
    boolean validate(String val_name,String val_pwd){
        if(Objects.equals(val_name, name) && Objects.equals(val_pwd, pwd)){
            return true;
        }
        return false;
    }
    String getName(){return name;}
    String getPwd(){return pwd;}
    int getBattles(){return battles;}
    int getWins(){return wins;}
    void updatePwd(String oldPwd,String newPwd){
        if(Objects.equals(oldPwd, pwd)){
            pwd = newPwd;
            System.out.println("\n密码更新成功.");
        }
        else{
            System.out.println("\n旧密码不正确，更新失败.");
        }
    }
    void updateBattle(boolean win){
        battles += 1;
        if(win){wins +=1;}
        double winRate = (double)wins/(double)battles;
        System.out.print("\n用户名："+getName()+" 总场次："+getBattles()+" 胜场："+getWins()+" 胜率：");
        System.out.printf("%.2f\n",winRate);
    }
    void showBattle(){
        double winRate = (double)wins/(double)battles;
        System.out.print("\n用户名："+getName()+" 总场次："+getBattles()+" 胜场："+getWins()+" 胜率：");
        System.out.printf("%.2f\n",winRate);
    }
}
