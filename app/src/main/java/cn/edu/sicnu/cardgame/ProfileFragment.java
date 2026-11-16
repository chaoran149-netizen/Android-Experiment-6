package cn.edu.sicnu.cardgame;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.sicnu.cardgame.model.PlayerProfile;

public class ProfileFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        view.findViewById(R.id.edit_profile).setOnClickListener(button ->
                startActivity(new Intent(getActivity(), EditProfileActivity.class)));
        return view;
    }
    @Override public void onResume() {
        super.onResume();
        PlayerProfile p = ((CardGameApplication) getActivity().getApplication()).getProfile();
        row(R.id.profile_name, R.string.name_label, p.name);
        row(R.id.profile_student, R.string.student_label, p.student);
        row(R.id.profile_phone, R.string.phone_label, p.phone);
        row(R.id.profile_email, R.string.email_label, p.email);
        int[] genders = {R.string.male, R.string.female, R.string.unspecified};
        row(R.id.profile_gender, R.string.gender_label, getString(genders[p.gender]));
        int[] hobbies = {R.string.hobby_travel, R.string.hobby_reading, R.string.hobby_game, R.string.hobby_shopping};
        StringBuilder selected = new StringBuilder();
        for (int i = 0; i < hobbies.length; i++) if (p.hobbies[i]) {
            if (selected.length() > 0) selected.append(getString(R.string.separator));
            selected.append(getString(hobbies[i]));
        }
        row(R.id.profile_hobbies, R.string.hobbies_label, selected.length() == 0 ? getString(R.string.no_hobbies) : selected.toString());
        ((TextView) getView().findViewById(R.id.profile_best)).setText(getString(R.string.best_score, p.getHighestScore()));
        ImageView avatar = getView().findViewById(R.id.avatar);
        if (p.avatar == null) avatar.setImageResource(R.drawable.ic_avatar);
        else avatar.setImageBitmap(p.avatar);
    }
    private void row(int view, int label, String value) {
        ((TextView) getView().findViewById(view)).setText(getString(R.string.profile_row, getString(label),
                value.isEmpty() ? getString(R.string.unfilled) : value));
    }
}
