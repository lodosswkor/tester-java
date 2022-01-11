import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NaverPriceApplication {
	
	public static void main(String[] args) throws Exception {
		
		
		String query;
		int minPrice;
		int maxPrice; 
		
		try {
			query = args[0];
			minPrice = Integer.parseInt(args[1]);
			maxPrice = Integer.parseInt(args[2]);
		} catch(Exception ex) {
			System.out.println("사용법 > java NaverPriceApplication 검색어 최저금액 최고금액");
			return; 
		}
		
		String Url  = "https://search.shopping.naver.com/search/all?productSet=total&query=%s&sort=price_asc&maxPrice=%d&minPrice=%d";
		Url = String.format(Url,query, maxPrice, minPrice);
		System.out.println(Url);
		Connection conn = Jsoup.connect(Url);
		Document html = conn.get();
		
		Elements files = html.select(".list_basis > div > div");
        
		
		// TextFile 
		int rankInt = 1; 
		String conLine = ""; 
		String rankFormat = "%d. %s(%s) : %s\r\n"; 
        String time = ""; 
        
        for( Element elm : files ) {

            if(rankInt > 5) break;
            
        	String goodsName = elm.select(".basicList_link__1MaTN").text();
            String price = elm.select(".price_num__2WUXn").text();
            String link = elm.select(".basicList_link__1MaTN").attr("href").toString();
            //System.out.println(  goodsName );
            //System.out.println(  price );
            //System.out.println(  link );
            
            conLine += String.format(rankFormat, rankInt ++, goodsName, price, link );
        
        }
        
        System.out.println(conLine);

        File file = new File("D:\\01.txt");
        BufferedWriter writer = null; 
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(conLine);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	if(writer != null) writer.close();
        }

		// CSV 
        String title = "순위\t상품명\t가격\t상품링크\n";
		rankInt = 1; 
		conLine = ""; 
		rankFormat = "%d\t%s\t%s\t%s\r\n"; 
        time = ""; 

        for( Element elm : files ) {

            if(rankInt > 5) break;
            
        	String goodsName = elm.select(".basicList_link__1MaTN").text();
            String price = elm.select(".price_num__2WUXn").text();
            String link = elm.select(".basicList_link__1MaTN").attr("href").toString();
            conLine += String.format(rankFormat, rankInt ++, goodsName, price, link );
        
        }
        

        System.out.println(title + conLine);
        
        file = new File("D:\\01.tsv");

        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(title + conLine);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	if(writer != null) writer.close();
        }
        
	}
	
}