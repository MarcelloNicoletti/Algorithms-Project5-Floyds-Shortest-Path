import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class MainDebugWrapper {

    public static void main (String[] args) {

        try {
            String fileName = args[0];
            FileInputStream inFile = new FileInputStream(new File(fileName));
            System.setIn(inFile);
            String[] unwrappedArgs = Arrays.copyOfRange(args, 1, args.length);
            Main.main(unwrappedArgs);
        } catch (FileNotFoundException e) {
            System.out.printf("File \"%s\" not found, can not use as input.",
                    args[0]);
            System.exit(1);
        }
    }
}
