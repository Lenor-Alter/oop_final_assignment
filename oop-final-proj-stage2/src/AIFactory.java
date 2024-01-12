public class AIFactory {
    AI createAI(int ailevel){
        if(ailevel == 1){
            return new AI_level_1();
        }
        else if (ailevel == 2) {
            return new AI_level_2();
        }
        return null;
    }
}
