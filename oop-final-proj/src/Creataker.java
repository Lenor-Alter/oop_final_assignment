import java.util.ArrayList;

public class Creataker {
    private ArrayList<String> fileNames = new ArrayList<String>();
    private ArrayList<IFile> files = new ArrayList<IFile>();
    public void showFiles(){
        for(int i=0;i< fileNames.size();i++){
            System.out.println("存档"+i+": "+fileNames.get(i));
        }
    }
    public IFile retrieveFile(int i){
        if(i >= files.size()){
            System.out.println("该存档不存在.");
            return  null;
        }
        return files.get(i);
    }
    public void saveFile(IFile file,String fileName){
        files.add(file);
        fileNames.add(fileName);
    }
}
