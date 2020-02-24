package com.biz.memo.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.biz.memo.db.MemoDataBase;
import com.biz.memo.domain.MemoVO;

import java.util.List;

/*
    DB 접근할 때 사용할 Service 클래스
 */
public class MemoRepository {

    private MemoDao mDao;

    public MemoRepository(Application application) {
        MemoDataBase db = MemoDataBase.getINSRTANCE(application);
        mDao = db.getMemoDao();
    }

    public LiveData<List<MemoVO>> selectAll() {
        return mDao.selectAll();
    }

    /* thread로 insert 실행 */
    public void insert(final MemoVO memoVO) {
        // 기본자바 코드
        /*
        Runnable() : method를 실행시키는 일만 한다.

        MemoDataBase.dbWriterThread.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insert(memoVO);
            }
        });
        */

        // ()-> : run method와 같다고 생각하면 된다.
        MemoDataBase.dbWriterThread.execute(()-> mDao.save(memoVO));

    }

    public MemoRepository(MemoDao mDao) {
        this.mDao = mDao;
    }

    public void delete(MemoVO memoVO) {
        MemoDataBase.dbWriterThread.execute(()-> mDao.delete(memoVO));
    }

}
