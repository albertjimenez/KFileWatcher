package examples;

import io.sendros.enums.FileEventsEnum;
import io.sendros.handler.WatcherHandler;
import io.sendros.interfaces.VoidCallback;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class MainJava {
    public static void main(String[] args) {
        WatcherHandler watcherHandler = new WatcherHandler(System.getProperty("java.io.tmpdir"), FileEventsEnum.values());
        Function<File, String> getFileFolderString = file -> file.isFile() ? "File" : "Folder";
        watcherHandler.startWatching(new VoidCallback() {
            public void onCreate(@NotNull File file) {
                System.out.println(getFileFolderString.apply(file) + " created " + file.getName());

            }

            public void onEdit(@NotNull File file) {
                System.out.println(getFileFolderString.apply(file) + " edited " + file.getName());

            }

            public void onDelete(@NotNull File file) {
                System.out.println(getFileFolderString.apply(file) + " deleted " + file.getName());

            }
        });
        try {
            final boolean isCreated = new File(System.getProperty("java.io.tmpdir")).createNewFile();
            if (isCreated)
                System.out.println("Created new file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
