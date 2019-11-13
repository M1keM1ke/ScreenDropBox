import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyThread extends Thread {
    //парсим дату согласно условию
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMd_kms");
    private static File screen;
    private static final String ACCESS_TOKEN = "sample"; //сгенерировать свой токен на дропбоксе

    MyThread() {
        this.start();
    }

    public static void createScreen()  {
        screen = new File(simpleDateFormat.format(new Date()) + ".png");

        try {
            ImageIO.write(new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())), "png", screen);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void downloadScreenOnDropbox() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN); //подключаем акк дропбокса

        try (InputStream in = new FileInputStream(screen)) {
            client.files().uploadBuilder("/" + screen.getName()).uploadAndFinish(in); //загружаем скрин на дропбокс
        }
        catch (DbxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (true) {
            MyThread.createScreen();
            MyThread.downloadScreenOnDropbox();
            screen.delete();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
