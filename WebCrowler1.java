/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrowler1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
public class WebCrowler1 {
    
    public static Queue<String> queue = new LinkedList<>();
    public static Set<String> marked = new HashSet<>();
    public static String regex = "http[s]*://(\\w+\\.)*(\\w+)";
    
    public static void bfsAlgorithm(String root) throws IOException{
        queue.add(root);
        BufferedReader br = null;
        while(!queue.isEmpty()){
            String crawledUrl = queue.poll();
            System.out.println("\n=== Site crawled : " + crawledUrl + "===");
            
            //we limit to n websites here
            if(marked.size() > 100)
                return;
            
            boolean ok = false;
            URL url = null;
            while(!ok){
                try{
                    url = new URL(crawledUrl);
                    br = new BufferedReader(new InputStreamReader(url.openStream()));
                    ok = true;
                    
                } catch(MalformedURLException e){
                    System.out.println("*** Maformed URL : " + crawledUrl);
                    crawledUrl = queue.poll();
                    ok = false;
                    
                }catch(IOException ioe){
                    System.out.println("*** IOException for URL : "+ crawledUrl);
                    crawledUrl = queue.poll();
                    ok = false;
                }
            }
            
            StringBuilder sb = new StringBuilder();
            String tmp = null;
            
            while((tmp = br.readLine()) != null){
                sb.append(tmp);
            }
            tmp = sb.toString();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(tmp);
            
            while(matcher.find()){
                String w = matcher.group();
                
                if(!marked.contains(w)){
                    marked.add(w);
                    System.out.println("Site added for crawling : " + w);
                    queue.add(w);
                }
            }
        }
        if(br != null)
            br.close();
    }
    
    public static void showResults(){
        System.out.println("\n\nResults : ");
        System.out.println("Web sites crawled : " + marked.size() + "\n");
        
        for(String s : marked){
            System.out.println("* " + s);
        }
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        Scanner cin = new Scanner(System.in);
        //int n = cin.nextInt();
        String path = cin.next();//"https://blog.faradars.org/"
        try{
            bfsAlgorithm("https://"+path+"/");
            showResults();
            String httpsURL = "https://"+path+"/";
            String FILENAME = "c:\\temp\\filename.html";
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME));
            URL myurl = new URL(httpsURL);
            HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
            con.setRequestProperty ( "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0" );
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins, "Windows-1252");
            BufferedReader in = new BufferedReader(isr);
            String inputLine;

            // Write each line into the file
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                bw.write(inputLine);
            }
            in.close(); 
            bw.close();
        }catch(Exception e){}
        
    }
    
}