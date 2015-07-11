package com.zbl.spider;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;

import com.zbl.spider.login.CrifanLib;

public class HtmlTool {
	public static List<String> getLinksFromHtml(String htmlText) {
//		CrifanLib crifanLib=new CrifanLib();
//		String htmlcode = crifanLib.getUrlRespHtml("http://list.tmall.com/search_product.htm?spm=3.7396704.20000023.4.seZ0dl&abbucket=&acm=tt-1143110-39119.1003.8.161011&aldid=161011&vmarket=&q=%CA%D6%BB%FA&from=mallfp..pc_1_searchbutton&abtest=&type=p&scm=1003.8.tt-1143110-39119.OTHER_1435095054462_161011&pos=3");
//		String htmlcode = crifanLib.getUrlRespHtml("http://detail.tmall.com/item.htm?id=45828653654&amp;skuId=3100880640298&amp;areaId=320100&amp;cat_id=50024400&amp;rn=2430b818dd8c552541901b0e2672ab89&amp;standard=1&amp;user_id=2448323233&amp;is_b=1");
		Parser parser = Parser.createParser(htmlText, "UTF-8");
		HtmlPage page = new HtmlPage(parser);
		List<String> linkList=new ArrayList<String>();
		try {
			parser.visitAllNodesWith(page);
			NodeList nodelist = page.getBody();
			NodeFilter filter = new TagNameFilter("a");
			nodelist = nodelist.extractAllNodesThatMatch(filter, true);
			for (int i = 0; i < nodelist.size(); i++) {
				LinkTag link = (LinkTag) nodelist.elementAt(i);
				String linkString=link.getAttribute("href");
				if(linkString!=null&&linkString.length()>0){
//					System.out.println(linkString.replace("&#x2F;", "/") + "\n");
					linkString=linkString.replace("&#x2F;", "/");
					if(linkString.length()>5){
						if(linkString.startsWith("http")){
							linkList.add(linkString);
						}else if(linkString.startsWith("//")){
							linkList.add("http:"+linkString);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linkList;
	}
}
