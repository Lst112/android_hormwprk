package share;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapp.db.domain.User;

import org.jetbrains.annotations.NotNull;

public class ShareViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> log = new MutableLiveData<>();
    private User user;
    private SharedPreferences shp;
    public ShareViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.shp = application.getApplicationContext().getSharedPreferences("shared",application.getApplicationContext().MODE_PRIVATE);
    }

    public String getUser() {
        String u = shp.getString("user","未登录");
        return u;
    }

    public void setUser(String user) {
        SharedPreferences.Editor editor = shp.edit();
        editor.putString("user",user);
        editor.apply();
    }

    public LiveData<Boolean> getLog( ) {
        boolean flag = shp.getBoolean("log",false);
        this.log.setValue(flag);
        return this.log;
    }
    public void setLog(Boolean b){
        SharedPreferences.Editor editor = shp.edit();
        editor.putBoolean("log",b);
        this.log.setValue(b);
        editor.apply();
    }
}
