package com.pjy.naverapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NaverActivity extends AppCompatActivity {

    public static EditText search;
    Button search_btn;
    ListView my_List_View;

    ArrayList<BookVO> list;
    Parser parser = new Parser(); //connectNaver쓰기위해 꼭 필요함

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver);

        search = findViewById(R.id.search);
        search_btn = findViewById(R.id.search_btn);
        my_List_View = findViewById(R.id.my_ListView);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //한글자 이상 검색어를 입력했을 때만 서버와 통신
                if( search.getText().toString().trim().length() > 0 ){

                    list = new ArrayList<>();
                    //서버통신 작업은 반드시 AsyncTask클래스를 통해서 수행되어야 한다

                    new NaverAsync().execute("홍", "길", "동"); //.execute()-> NaverAsync클래스의 doInBackground메서드를 호출

                }else{
                    Toast.makeText(NaverActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }//onCreate()
    
    //AsyncTask클래스의 세 가지 제너릭 타입
    //1) doInBackground의 파라미터 타입
    //2) onProgressUpdate()가 있다면 내부에서 사용할 자료형 타입
    //3) doInBackground의 반환형이자 onPostExeceute()의 파라미터 타입

    class NaverAsync extends AsyncTask< String, Void, ArrayList<BookVO> >{

        @Override
        protected ArrayList<BookVO> doInBackground(String... strings) {
            //string[0] -> "홍"
            //string[1] -> "길"
            //string[2] -> "동"


            //백그라운드 작업을 수행하기 위한 영역
            //각종 반복, 제어등의 주된 처리 로직을 담당

            return parser.connectNaver(list);
        }

        @Override
        protected void onPostExecute(ArrayList<BookVO> bookVOS) {
            //doInBackground메서드에서 return한 값을 현재 메서드의 파라미터(bookVOS)가 받는다
            //Log.i("MY", "" + bookVOS.size());
            //검색이 완료된 객체인 bookVOS를 ListView로 표시하기 위해
            //ViewModelAdapter클래스에게 전달
            ViewModelAdapter adapter = new ViewModelAdapter(
                    NaverActivity.this, R.layout.book_item, bookVOS);

            //생성된 adapter를 리스트뷰에 담는다
            //이때 adapter의 getView()메서드가 bookVOS의 size()만큼 반복된다
            my_List_View.setAdapter(adapter);
        }
    }

}