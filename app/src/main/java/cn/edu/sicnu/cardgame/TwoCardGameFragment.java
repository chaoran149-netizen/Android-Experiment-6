package cn.edu.sicnu.cardgame;

public final class TwoCardGameFragment extends GameFragment {
    @Override int matchSize() { return 2; }
    @Override String saveFile() { return "two-card-game.dat"; }
}
