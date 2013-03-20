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

    int[] board = new int[9 * 9];
    List<Integer> freeIndexes = new ArrayList<Integer>();

    public Board(final String name) {
        try {
            Scanner s = new Scanner(new File(name).getAbsoluteFile());
            for (int i = 0; s.hasNext();) {
                board[i++] = s.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initFreeIndexes() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                freeIndexes.add(i);
            }
        }
    }

    private void print(final int[] board) {
        String str = Joiner.on((char) Ascii.CR).join(
                Splitter.fixedLength(9 + 3).split(Joiner.on("|").join(Splitter.fixedLength(3).split(Ints.join("", board)))));
        System.out.println(str);
    }

    public void print() {
        print(board);
    }

    public void printResult() {
        int[] boardCopy = board;
        initFreeIndexes();
        for (int i = 0; i < freeIndexes.size(); i++) {
            int j = board[freeIndexes.get(i)];
            do {
                if (j == 9) {
                    board[freeIndexes.get(i)] = 0;
                    i = i - 2;
                    break;
                }
                board[freeIndexes.get(i)] = ++j;
            } while (violates());
        }
        print(board);
        board = boardCopy;
    }

    private boolean violates() {
        for (int i = 0; i < 9; i++) {
            Set<Integer> values = new HashSet<Integer>();
            for (int j = 0; j < 9; j++) {
                if (board[i * 9 + j] != 0) {
                    boolean contained = !values.add(board[i * 9 + j]);
                    if (contained) {
                        return true;
                    }
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            Set<Integer> values = new HashSet<Integer>();
            for (int j = 0; j < 9; j++) {
                if (board[i + j * 9] != 0) {
                    boolean contained = !values.add(board[i + j * 9]);
                    if (contained) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
