import java.util.ArrayList;
import java.util.Random;

public class AI_level_1 extends AI{
    public int[] GiveCoordinate(int SideLength, ArrayList<ArrayList<Integer>> board, boolean BlackTurn){
        Random random = new Random();
        int randx = random.nextInt(SideLength);
        int randy = random.nextInt(SideLength);
        return new int[]{randx,randy};
    }
}
