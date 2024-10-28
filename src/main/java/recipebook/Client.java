package recipebook;

import java.util.List;

public class Client {
    private Mongo db = new Mongo();

    public boolean processCommand(String command) {
        if (command.startsWith("_find_ ")) {
            String search = command.substring(7);
            String[] data = db.getData(search);
            System.out.println("Recipe: " + data[0] + "\n");
            System.out.println(data[1]);
        } else if (command.startsWith("_all_ ")) {
            List<String[]> allData = db.getAllData();
            for (String[] allDatum : allData) {
                System.out.println("Recipe: " + allDatum[0] + "\n");
                System.out.println(allDatum[1] + "\n");
            }
        } else if (command.startsWith("_delete_ ")) {
            if (db.delData(command.substring(9))) {
                System.out.println("Successful delete");
            } else
                System.out.println("Failure on delete!!!");
        }
        else if (command.startsWith("_quit_ ")) {
            return false;
        }
        return false;
    }
}
