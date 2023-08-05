package com.pjy.naverapi;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Parser {

    BookVO vo;
    String myQuery = ""; //검색어 담을 변수

    //통신을 위한 메서드
    public ArrayList<BookVO> connectNaver( ArrayList<BookVO> list ){

        try {

            myQuery = URLEncoder.encode(NaverActivity.search.getText().toString(), "UTF-8");

            String urlStr = "https://openapi.naver.com/v1/search/book.xml?query="+myQuery+"&start=1&display=100";

            //URl클래스를 생성하여 위의 urlStr경로로 접근
            URL url = new URL(urlStr);

            //url클래스가 접근한 경로에 id, secret정보를 담아준다
            //.openConnection(); -> 연결 시도하는거
            //서버를 접근하기 위해 거치는 인증절차(id, secret)
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("X-Naver-Client-Id", "VAZQxOV9gyWFFGrdmcI3");
            connection.setRequestProperty("X-Naver-Client-Secret", "VsnBb5TBqe");

            //위의 url경로로 접근에 성공했다면
            //도서 검색결과를 가지고 있는 네이버의 xml 문서로 접근
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); //접속용

            //xml 내부로 진입할 클래스
            XmlPullParser parser = factory.newPullParser();

            //parser를 통해 xml문서를 읽어오도록 준비
            parser.setInput( connection.getInputStream(), null );

            //xml문서의 내용을 반복수행처리 하면서 모조리 읽어오자 !!!
            int parserEvent = parser.getEventType();

            //xml문서의 끝을 만날때까지 while문을 반복
            while( parserEvent != XmlPullParser.END_DOCUMENT ){

                //시작태그를 찾았을때 if문으로 접근
                if ( parserEvent == XmlPullParser.START_TAG ){
                    String tagName = parser.getName(); //<title>, <link>, <image> ...

                    if ( tagName.equalsIgnoreCase("title") ){
                        vo = new BookVO();
                        String title = parser.nextText(); //태그의 내용을 가져온다
                        vo.setB_title(title); //vo에 담기
                    } else if (tagName.equalsIgnoreCase("image")) {
                        String img = parser.nextText();
                        vo.setB_img(img);
                    } else if (tagName.equalsIgnoreCase("author")) {
                        String author = parser.nextText();
                        vo.setB_author(author);
                    } else if (tagName.equalsIgnoreCase("discount")) {
                        String price = parser.nextText();
                        vo.setB_price(price);
                        list.add(vo);
                    }

                }//if ( parserEvent == XmlPullParser.START_TAG )

                parserEvent = parser.next();

            }//while

        }catch (Exception e){

        }

        //서버에서 가져온 도서정보를 들고있는 Arraylist를 반환
        return list;

    }//connectNaver();

}
