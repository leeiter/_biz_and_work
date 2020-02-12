package com.biz.memo.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.biz.memo.domain.MemoVO;
import com.biz.memo.repository.MemoDao;

@Database(entities = {MemoVO.class}, version = 1)
abstract public class MemoDataBase extends RoomDatabase {

   /*
      데이터베이스 INSTANCE가 생성이 되면서
      MemoDao insterface를 가져다가 사용할 수 있는 class를 생성을 한다.
    */
   abstract public MemoDao getMemoDao();

   /*
      Database를 생성하는 클래스를 싱글톤으로 선언하기 위해
      외부에서 접근하는 변수 선언
    */
   private  static volatile MemoDataBase INSRTANCE;
   public static MemoDataBase getINSRTANCE(Context context) {
      if(INSRTANCE == null) {
         synchronized (MemoDataBase.class) {
            INSRTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MemoDataBase.class,
                    "memo.dbf"
            ).build();
         }
      }
      return INSRTANCE;
   }

}
