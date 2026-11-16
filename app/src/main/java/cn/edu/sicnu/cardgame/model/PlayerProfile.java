package cn.edu.sicnu.cardgame.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public final class PlayerProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public String student;
    public String phone;
    public String email;
    public int gender = 2;
    public final boolean[] hobbies = new boolean[4];
    public transient Bitmap avatar;
    private byte[] avatarBytes;

    public void setAvatar(Bitmap bitmap) {
        avatar = bitmap;
        avatarBytes = null;
        if (bitmap != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)) avatarBytes = bytes.toByteArray();
        }
    }

    public void restoreAvatar() {
        if (avatarBytes != null) avatar = BitmapFactory.decodeByteArray(avatarBytes, 0, avatarBytes.length);
    }
    private int highestScore;
    public PlayerProfile(String empty) { name = empty; student = empty; phone = empty; email = empty; }
    public int getHighestScore() { return highestScore; }
    public void recordScore(int score) { highestScore = Math.max(highestScore, score); }
}
