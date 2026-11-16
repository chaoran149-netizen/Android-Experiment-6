package cn.edu.sicnu.cardgame;

public final class ThreeCardGameFragment extends GameFragment {
    @Override int matchSize() { return 3; }
    @Override String saveFile() { return "three-card-game.dat"; }
}
