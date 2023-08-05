package com.pjy.naverapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class ViewModelAdapter extends ArrayAdapter<BookVO> {

    Context context;
    ArrayList<BookVO> list;
    int resource;
    BookVO vo;

    public ViewModelAdapter(Context context, int resource, ArrayList<BookVO> list) {
        super(context, resource, list);

        this.context = context;
        this.list = list;
        this.resource = resource;
    }// 생성자

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //book_item.xml파일을 convertView형태로 변환
        LayoutInflater linf = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = linf.inflate(resource, null);

        vo = list.get(position);

        //position이 0일때 title에 넣음 1로 바뀔때 책 이름도 넣어주면됨
        TextView title = convertView.findViewById(R.id.book_title);
        TextView author = convertView.findViewById(R.id.book_author);
        TextView price = convertView.findViewById(R.id.book_price);
        ImageView img = convertView.findViewById(R.id.book_img);

        title.setText( vo.getB_title() ); //제목
        author.setText( vo.getB_author() ); //저자
        price.setText( vo.getB_price() + "원"); //가격

        //이미지를 가져올 Async를 실행
        //execute()를 통해 doInBackground()메서드가 실행됨
        new ImgAsync(img, vo).execute();

        return convertView;
    }//getView()

    //서버에서 vo.getB_img()에 담긴 경로로 접근하여
    //이미지를 불러와야함
    class ImgAsync extends AsyncTask<Void, Void, Bitmap>{

        Bitmap bm;
        ImageView mImg;
        BookVO vo;

        //생성자
        public  ImgAsync( ImageView mImg, BookVO vo ){
            this.mImg = mImg;
            this.vo = vo;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            //서버통신을 위한 주된 처리로직을 작성하는 메서드
            try {
                URL img_url = new URL(vo.getB_img());
                //url경로에 있는 이미지를 1byte씩 읽어서 bis가 이미지 정보 저장
                BufferedInputStream bis =
                        new BufferedInputStream(img_url.openStream());
                //bis가 가져온 정보로부터 Bitmap객체를 생성
                bm = BitmapFactory.decodeStream(bis);
                bis.close();

            }catch (Exception e){

            }

            return bm;
        }//doInBackground()

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //각 도서별 bitmap형태의 이미지임
            //서버에서 가져온 bitmap객체를
            //안드로이드에서 사용할 수 있도록 ImageView형태로 변환
            mImg.setImageBitmap(bitmap);
        }
    }

}
