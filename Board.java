import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.google.caliper.internal.guava.base.Joiner;
import com.google.common.base.Ascii;
import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;

public class Board {

    int[][] board = new int[9][9];
    List<Integer> freeIndexes = new ArrayList<Integer>();

    public Board(final String name) {
        try {
            Scanner s = new Scanner(new File(name).getAbsoluteFile());
            for (int i = 0; s.hasNext(); i++) {
                board[i / 9][i % 9] = s.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initFreeIndexes() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    freeIndexes.add(i * 9 + j);
                }
            }
        }
    }

    private void print(final int[][] board) {
        String str = Joiner.on((char) Ascii.CR).join(
                Splitter.fixedLength(9 + 3).split(Joiner.on("|").join(Splitter.fixedLength(3).split(Ints.join("", Ints.concat(board))))));
        System.out.println(str);
    }

    public void print() {
        print(board);
    }

    public void printResult() {
        int[][] boardCopy = board;
        initFreeIndexes();
        for (int i = 0; i < freeIndexes.size(); i++) {
            Integer freeIndex = freeIndexes.get(i);
            int j = board[freeIndex / 9][freeIndex % 9];
            do {
                if (j == 9) {
                    board[freeIndex / 9][freeIndex % 9] = 0;
                    i = i - 2;
                    break;
                }
                board[freeIndex / 9][freeIndex % 9] = ++j;
            } while (violates());
        }
        print(board);
        board = boardCopy;
    }

    private boolean violates() {
        for (int i = 0; i < 9; i++) {
            Set<Integer> values = new HashSet<Integer>();
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    boolean contained = !values.add(board[i][j]);
                    if (contained) {
                        return true;
                    }
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            Set<Integer> values = new HashSet<Integer>();
            for (int j = 0; j < 9; j++) {
                if (board[j][i] != 0) {
                    boolean contained = !values.add(board[j][i]);
                    if (contained) {
                        return true;
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Set<Integer> values = new HashSet<Integer>();
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (board[i * 3 + k][j * 3 + l] != 0) {
                            boolean contained = !values.add(board[i * 3 + k][j * 3 + l]);
                            if (contained) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
