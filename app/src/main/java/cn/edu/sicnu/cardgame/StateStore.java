package cn.edu.sicnu.cardgame;

import android.content.Context;
import android.util.Log;
import java.io.*;

final class StateStore {
    private static final String TAG = "StateStore";

    static <T> T read(Context context, String file, Class<T> type) {
        try (ObjectInputStream input = new ObjectInputStream(context.openFileInput(file))) {
            Object value = input.readObject();
            return type.isInstance(value) ? type.cast(value) : null;
        } catch (FileNotFoundException missing) {
            return null;
        } catch (IOException | ClassNotFoundException error) {
            Log.w(TAG, "Could not load " + file, error);
            return null;
        }
    }

    static boolean write(Context context, String file, Serializable value) {
        String temporary = file + ".tmp";
        try {
            try (ObjectOutputStream output = new ObjectOutputStream(
                    context.openFileOutput(temporary, Context.MODE_PRIVATE))) {
                output.writeObject(value);
            }
            if (context.getFileStreamPath(temporary).renameTo(context.getFileStreamPath(file))) {
                return true;
            }
            Log.w(TAG, "Could not replace " + file);
        } catch (IOException error) {
            Log.w(TAG, "Could not save " + file, error);
        }
        context.deleteFile(temporary);
        return false;
    }
}
