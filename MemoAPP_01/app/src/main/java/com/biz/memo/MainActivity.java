package com.biz.memo;

import android.os.Bundle;

import com.biz.memo.adapter.MemoViewAdapter;
import com.biz.memo.adapter.MemoViewModel;
import com.biz.memo.domain.MemoVO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // List<MemoVO> memoList = null;
    TextInputEditText m_input_memo = null;
    RecyclerView memo_list_view = null;
    MemoViewAdapter view_adapter = null;

    /*
        DB 연동을 위한 변수들 선언
     */
    MemoViewModel memoViewModel;

    ViewModelProvider.Factory viewMoFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // memoList = new ArrayList<MemoVO>();

        Button btn_save = findViewById(R.id.memo_save);
        btn_save.setOnClickListener(this);

        m_input_memo = findViewById(R.id.m_input_text);

        /*
        for(int i = 0 ; i < 30 ; i++) {
            MemoVO memoVO = new MemoVO();
            memoVO.setM_date(sd.format(date));
            memoVO.setM_time(st.format(date));
            memoVO.setM_text((i + 1) + "번째 메모");
            memoList.add(memoVO);
        }
        */

        memo_list_view = findViewById(R.id.memo_list_view);

        // RecyclerView에 데이터를 표시하기 위해서
        // Adapter를 부착하는 부분
        view_adapter = new MemoViewAdapter(this);
        memo_list_view.setAdapter(view_adapter);

        // memoList = new ArrayList<>();

        // DB 연동을 위한 준비

        // LifeCycle 2.2.0-beta01의 ViewModelProvider 사용
        memoViewModel = new ViewModelProvider(this).get(MemoViewModel.class);

        /*
            DB의 데이터가 변경되어 이전에
            selectAll() 가져온 리스트에 변동이 발생하면
            observe() 매서드가 알람을 주고 onChanged 이벤트가 발생을 한다.
            onchanged() method에서 데이터를 화면에 보여주는 코드를 작성한다.
         */
        /*
        memoViewModel.selectAll().observe(this, new Observer<List<MemoVO>>() {
            @Override
            public void onChanged(List<MemoVO> memoVOS) {
                view_adapter.setMemoList(memoVOS);
            }
        });
        */

        // 현재 memoList의 이름은 익명의 단독 method이다.
        // 현재 이름을 지정하는 것은 개발자가 임의로 지정을 해도 되지만
        // 실질적으로 현재 memoList가 값을 가져오는 것은 위의 주석의
        // List<MemoVO> memoVOS 이 부분의 List에서 값을 가져오는 것이다.
        memoViewModel.selectAll().observe(this, (memoList)->view_adapter.setMemoList(memoList));

        // 실질적으로 delete가 되는 method
        MemoViewAdapter.OnDeleteButtonClickListener deleteBtnEvent = new MemoViewAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onDeleteButtonCliked(MemoVO memoVO) {
                memoViewModel.delete(memoVO);
            }
        };

        // 함수 전달 방식
        view_adapter.setDeleteBtnClick(deleteBtnEvent);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        memo_list_view.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                memo_list_view.getContext(), LinearLayoutManager.VERTICAL);

        /*
            없으면 기본 구분선으로 된다.
            itemDecoration.setDrawable(this.getResources().getDrawable(
                R.drawable.decoration_line, getApplication().getTheme()));
         */

        memo_list_view.addItemDecoration(itemDecoration);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String m_memo_text = m_input_memo.getText().toString();

        if(m_memo_text.isEmpty()) {
            Toast.makeText(MainActivity.this, "메모를 입력하세요.",
                    Toast.LENGTH_SHORT).show();
            m_input_memo.setFocusable(true);
            return;
        }

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat st = new SimpleDateFormat("HH:mm:ss");

        Date date = new Date(System.currentTimeMillis());

        MemoVO memoVO = MemoVO.builder()
                .m_date(sd.format(date))
                .m_time(st.format(date))
                .m_text(m_memo_text)
                .build();

        // memoViewModel의 insert 메서드를 호출하여 DB에 memoVo 데이터를 저장
        memoViewModel.insert(memoVO);

        // memoList.add(memoVO);

        // RecyclerView의 Adapter한테 데이터가 변경되었으니
        // 리스트를 다시 그려라 통보
        // view_adapter.notifyDataSetChanged();

        m_input_memo.setText("");

    }

}
