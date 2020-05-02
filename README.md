# FolderFileWatcher
This is a library with a simple API to allow developers to watch changes and give the ability to implement
features such as hot-reloading like Node.js [FSEvent](https://www.npmjs.com/package/fsevents).
Registers the given directory and its subdirectories recursively to monitor any **CREATE, EDIT** or **DELETE** event.


## Features

- Lightweight 33 kb jar file
- 100% Kotlin but interoperable with Java 8
- Threads in place to run in a non-blocking way
- Only log4j dependency is used
 
 ## TODOs
 - For now, the callback method that is called when an event gets triggered does not allow you to return something
 
 ## Dependency

 From Jitpack
 
 ### Maven
Add the JitPack repository to your build file
 ```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Add the dependency 
```
<dependency>
    <groupId>com.github.albertjimenez</groupId>
    <artifactId>KFileWatcher</artifactId>
    <version>f253365483</version>
</dependency>
```

### Gradle
Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```
dependencies {
    implementation 'com.github.albertjimenez:KFileWatcher:f253365483'
}
```
 
 ## Usage
 Under `src/kotlin/examples` you will find a Java 8 example and a Kotlin example
 
### **Java >=8:**
 ```
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
```

### **Kotlin**
```
val watcherHandler =
        WatcherHandler(System.getProperty("java.io.tmpdir"), *FileEventsEnum.values())

    watcherHandler.startWatching(object : VoidCallback {

        val getFileFolderString: ((file: File) -> String) = { file: File ->
            if (file.isFile) "File" else "Folder"
        }

        override fun onCreate(file: File) {
            println("${getFileFolderString(file)} created ${file.name}")
        }

        override fun onEdit(file: File) {
            println("${getFileFolderString(file)} edited ${file.name}")
        }

        override fun onDelete(file: File) {
            println("${getFileFolderString(file)} deleted ${file.name}")
        }
    })
    File(System.getProperty("java.io.tmpdir")).createNewFile().takeIf { it }?.apply {
        println("Created new temporary file")
    }
```