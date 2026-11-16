package cn.edu.sicnu.cardgame;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.sicnu.cardgame.model.CardMatchingGame;

public abstract class GameFragment extends Fragment {
    private CardMatchingGame game;
    private CardAdapter adapter;
    private int message = R.string.game_ready;

    abstract int matchSize();
    abstract String saveFile();

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return inflater.inflate(R.layout.activity_game, container, false);
    }

    @Override public void onViewCreated(View view, Bundle state) {
        super.onViewCreated(view, state);
        boolean pair = matchSize() == 2;
        game = StateStore.read(getActivity(), saveFile(), CardMatchingGame.class);
        if (game == null) {
            game = new CardMatchingGame(getResources().getInteger(R.integer.card_count));
        }

        ((TextView) view.findViewById(R.id.game_heading)).setText(
                pair ? R.string.two_cards : R.string.three_cards);
        ((TextView) view.findViewById(R.id.game_rules)).setText(
                pair ? R.string.rules : R.string.triple_rules_lab6);
        RecyclerView grid = view.findViewById(R.id.card_grid);
        grid.setLayoutManager(new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.columns)));
        grid.setItemAnimator(null);
        adapter = new CardAdapter(game, position -> choose(view, position));
        grid.setAdapter(adapter);
        view.findViewById(R.id.exit_game).setVisibility(View.GONE);
        view.findViewById(R.id.reset_game).setOnClickListener(button ->
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.restart_title)
                        .setMessage(R.string.restart_message)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.restart_confirm, (dialog, which) -> {
                            game.reset(getResources().getInteger(R.integer.card_count));
                            message = R.string.game_ready;
                            saveGame();
                            render(view);
                        })
                        .show());
        render(view);
    }

    private void choose(View view, int position) {
        boolean pair = matchSize() == 2;
        switch (game.chooseCardAtIndex(position, matchSize())) {
            case IGNORED: return;
            case CLOSED: message = R.string.game_closed; break;
            case RANK_MATCH: message = pair ? R.string.game_rank_match : R.string.triple_match; break;
            case SUIT_MATCH: message = pair ? R.string.game_suit_match : R.string.triple_match; break;
            case MISMATCH: message = pair ? R.string.game_mismatch : R.string.triple_fail; break;
            default: message = pair ? R.string.game_choose : R.string.triple_pending; break;
        }
        saveGame();
        CardGameApplication application = (CardGameApplication) getActivity().getApplication();
        if (application.getProfile().getHighestScore() < game.getScore()) {
            application.getProfile().recordScore(game.getScore());
            application.saveProfile();
        }
        render(view);
    }

    private void saveGame() {
        if (!StateStore.write(getActivity(), saveFile(), game)) {
            Toast.makeText(getActivity(), R.string.save_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void render(View view) {
        adapter.notifyDataSetChanged();
        ((TextView) view.findViewById(R.id.score)).setText(getString(R.string.score, game.getScore()));
        ((TextView) view.findViewById(R.id.progress)).setText(
                getString(R.string.progress, game.matchedCount(), game.size()));
        ((TextView) view.findViewById(R.id.game_status)).setText(game.isComplete()
                ? R.string.game_complete : game.hasPossibleMatch(matchSize()) ? message : R.string.game_no_pairs);
    }
}
