package com.zbl.spider;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import com.zbl.spider.login.CrifanLib;

public class TestSpider {

	public static void main(String[] args) {
		TestSpider testSpider=new TestSpider();
		testSpider.startSpider();
	}
	
	//初始URL配置
	private static final String SEED_URL="http://s.taobao.com/search?q=%E6%89%8B%E6%9C%BA";
	//最大深度
	private static final int MAX_DEPTH=2;
	
	private DBAccess dbAccess;
	private CrifanLib crifanLib=new CrifanLib();

	public void startSpider(){
		dbAccess=DBAccess.getInstance();
		dbAccess.insertURL("http://www.taobao.com");
//		dbAccess.insertURL("www.baidu.com");
//		dbAccess.insertURL("www.12321.com");
		spider(SEED_URL,0);
		dbAccess.close();
	}
	
	private void spider(String url,int depth){
		if(depth>MAX_DEPTH){//深度限制
			return;
		}
		boolean result=dbAccess.insertURL(url);
		if(result){
			String htmltext;
			long time_start=System.currentTimeMillis();
			try {
				htmltext = crifanLib.getUrlRespHtml(url);
			} catch (Exception e) {
				return;
			}
			boolean flag0=handleContent(url,htmltext);
			if(!flag0){
				return;
			}
			List<String> linkList=HtmlTool.getLinksFromHtml(htmltext);
			System.out.println("get "+linkList.size()+" links");
			long time_end=System.currentTimeMillis();
			
			if(time_end-time_start<1000l){//最少间隔1秒每次读取网络
				try {
					Thread.sleep(1000l-(time_end-time_start));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			for(String link:linkList){
				spider(link,depth+1);
			}
		}
	}
	
	private boolean handleContent(String url,String htmltext){
		boolean result=true;
		if(url.startsWith("http://detail.tmall.com/")){
			System.out.println(url);
			result=false;
		}else if(url.startsWith("http://item.taobao.com/")){
			System.out.println(url);
			result=false;
		}else if(url.contains("1688")){
			result=false;
		}else if(url.contains("aliexpress")){
			result=false;
		}else if(url.contains("alibaba")){
			result=false;
		}
		return result;
	}
}
