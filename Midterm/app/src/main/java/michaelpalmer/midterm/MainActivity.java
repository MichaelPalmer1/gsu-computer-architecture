package michaelpalmer.midterm;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView[][] cells = new TextView[3][3];
    private Button btnNewGame;
    private static final String PLAYER_1 = "X", PLAYER_2 = "O";
    private String player = PLAYER_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(this);

        TextView cellA1 = (TextView) findViewById(R.id.cellA1);
        cells[0][0] = cellA1;
        TextView cellA2 = (TextView) findViewById(R.id.cellA2);
        cells[0][1] = cellA2;
        TextView cellA3 = (TextView) findViewById(R.id.cellA3);
        cells[0][2] = cellA3;

        TextView cellB1 = (TextView) findViewById(R.id.cellB1);
        cells[1][0] = cellB1;
        TextView cellB2 = (TextView) findViewById(R.id.cellB2);
        cells[1][1] = cellB2;
        TextView cellB3 = (TextView) findViewById(R.id.cellB3);
        cells[1][2] = cellB3;

        TextView cellC1 = (TextView) findViewById(R.id.cellC1);
        cells[2][0] = cellC1;
        TextView cellC2 = (TextView) findViewById(R.id.cellC2);
        cells[2][1] = cellC2;
        TextView cellC3 = (TextView) findViewById(R.id.cellC3);
        cells[2][2] = cellC3;

        cellA1.setOnClickListener(this);
        cellA2.setOnClickListener(this);
        cellA3.setOnClickListener(this);

        cellB1.setOnClickListener(this);
        cellB2.setOnClickListener(this);
        cellB3.setOnClickListener(this);

        cellC1.setOnClickListener(this);
        cellC2.setOnClickListener(this);
        cellC3.setOnClickListener(this);
    }

    private boolean checkGame() {
        boolean result = false;

        int rows = checkRows();
        int cols = checkColumns();
        int diagonals = checkDiagonals();

        if (rows != -1) {
            result = true;
            for (TextView cell : cells[rows]) {
                cell.setBackgroundColor(Color.GREEN);
            }
        }

        if (cols != -1) {
            result = true;
            for (int i = 0; i < 3; i++) {
                cells[i][cols].setBackgroundColor(Color.GREEN);
            }
        }

        if (diagonals != -1) {
            result = true;
            if (diagonals == 0) {
                // \
                cells[0][0].setBackgroundColor(Color.GREEN);
                cells[1][1].setBackgroundColor(Color.GREEN);
                cells[2][2].setBackgroundColor(Color.GREEN);
            } else {
                // /
                cells[2][0].setBackgroundColor(Color.GREEN);
                cells[1][1].setBackgroundColor(Color.GREEN);
                cells[0][2].setBackgroundColor(Color.GREEN);
            }
        }

        if (checkBadGame()) {
            result = true;
        }

        return result;
    }

    private boolean checkBadGame() {
        for (TextView[] rows: cells) {
            for (TextView cell : rows) {
                if (cell.getText().equals("")) {
                    return false;
                }
            }
        }

        return true;
    }

    private int checkRows() {
        for (int row = 0; row < 3; row++) {
            CharSequence cell = cells[row][0].getText();
            boolean match = true;
            for (int col = 1; col < 3; col++) {
                match = cells[row][col].getText().equals(cell) && !cell.equals("");
                if (!match) {
                    break;
                }
            }
            if (match) {
                return row;
            }
        }
        return -1;
    }

    private int checkColumns() {
        for (int col = 0; col < 3; col++) {
            CharSequence cell = cells[0][col].getText();
            boolean match = true;
            for (int row = 1; row < 3; row++) {
                match = cells[row][col].getText().equals(cell) && !cell.equals("");
                if (!match) {
                    break;
                }
            }
            if (match) {
                return col;
            }
        }
        return -1;
    }

    private int checkDiagonals() {
        CharSequence cell = cells[1][1].getText();
        if (cell.equals("")) {
            return -1;
        }

        if (cells[0][0].getText().equals(cell) && cells[0][0].getText().equals(cells[2][2].getText())) {
            return 0;
        } else if (cells[2][0].getText().equals(cell) && cells[2][0].getText().equals(cells[0][2].getText())) {
            return 1;
        } else {
            return -1;
        }
    }

    private void switchPlayer() {
        player = player.equals(PLAYER_1) ? PLAYER_2 : PLAYER_1;
    }

    @Override
    public void onClick(View v) {
        // New game
        if (v.getId() == btnNewGame.getId()) {
            for (TextView[] rows : cells) {
                for (TextView cell: rows) {
                    cell.setBackgroundColor(getResources().getColor(R.color.cellBackground));
                    cell.setText("");
                    cell.setEnabled(true);
                    player = PLAYER_1;
                }
            }
            return;
        }

        // Cell selected
        for (TextView[] rows : cells) {
            for (TextView cell : rows) {
                if (cell.getId() == v.getId()) {
                    cell.setText(player);
                    break;
                }
            }
        }

        // Check the game
        if (!checkGame()) {
            switchPlayer();
        } else {
            for (TextView[] rows : cells) {
                for (TextView cell : rows) {
                    cell.setEnabled(false);
                }
            }
        }
    }
}
