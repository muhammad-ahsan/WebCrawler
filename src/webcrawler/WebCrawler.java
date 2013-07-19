/*
 * MUHAMMAD Ahsan
 * <muhammad.ahsan@gmail.com>
 */
package webcrawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ahsan
 */
public class WebCrawler {

    // Pull URLS from the body for easy retrieval
    private static List<String> CrawlBuffer = new LinkedList<>();
    private String Store;
    static int PCount = 0;

    public WebCrawler(String Path) throws IOException {
        Store = Path;
        File Directory = new File(Store);
        if (Directory.exists()) {
            delete(Directory);
        }
        if (Directory.mkdir()) {
            System.out.println("Web directory is created!");
        } else {
            System.out.println("Failed to create web directory!");
        }
    }

    public void Start(String URL, int Iterations) {
        CrawlBuffer.add(URL);
        try {
            for (int i = 0; i < Iterations; i++) {
                if (CrawlBuffer.size() > 0) {
                    URL SeedURL = new URL(CrawlBuffer.get(0));
                    CrawlBuffer.remove(0);
                    URLConnection I = SeedURL.openConnection();
                    try (InputStream inStr = SeedURL.openConnection().getInputStream()) {
                        BufferedInputStream bins = new BufferedInputStream(inStr);
                        String DirectoryPath;
                        PCount++;
                        DirectoryPath = Store + "Page-" + (PCount) + ".html";
                        FileOutputStream fostreame = new FileOutputStream(DirectoryPath);
                        int c = bins.read();
                        while (c != -1) {
                            fostreame.write(c);
                            c = bins.read();
                        }
                        fostreame.close();
                        bins.close();
                    }
                    // Read PCount page
                    CrawlPage(PCount);
                }
            }

        } catch (Exception e) {
            // Any wrong thing is being skipped
        }
    }

    private void ExtractURLS(String text) {
        try {
            // Check if page content does not have anything
            if (!text.isEmpty()) {
                String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*"
                        + "[-A-Za-z0-9+&@#/%=~_()|]";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(text);
                while (m.find()) {
                    String urlStr = m.group();
                    if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                        urlStr = urlStr.substring(1, urlStr.length() - 1);
                    }
                    CrawlBuffer.add(urlStr);
                    System.out.println(urlStr);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Problem with link pulling");
        }
    }

    private void CrawlPage(int Count) throws FileNotFoundException,
            IOException {
        String PName = "WebDirectory//Page-" + Count + ".html";
        FileReader myFile = new FileReader(PName);
        BufferedReader br = new BufferedReader(myFile);
        String Content = null;
        String line = null;

        while ((line = br.readLine()) != null) {
            Content = Content + line;
        }
        if (Content != null) {
            ExtractURLS(Content);
        }
    }

    private void delete(File file)
            throws IOException {

        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
            } else {
                // List all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    // Construct the file structure
                    File fileDelete = new File(file, temp);
                    // Recursive delete
                    delete(fileDelete);
                }
                // Check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                }
            }

        } else {
            file.delete();
        }
    }
}
