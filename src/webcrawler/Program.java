/*
 * Programmed by Muhammad Ahsan
 * muhammad.ahsan@gmail.com
 */
package webcrawler;

import java.io.IOException;

public class Program {

    public static void main(String args[]) throws IOException {
        WebCrawler W = new WebCrawler("WebDirectory/");
        W.Start("http://www.google.com/", 10);
    }
}
