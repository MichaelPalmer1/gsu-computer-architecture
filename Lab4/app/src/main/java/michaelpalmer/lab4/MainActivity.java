package michaelpalmer.lab4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

import michaelpalmer.lab4.alu.NBitALU;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    
    private CheckBox checkBoxA0, checkBoxA1, checkBoxA2, checkBoxA3, checkBoxA4, checkBoxA5, checkBoxA6, checkBoxA7;
    private CheckBox checkBoxB0, checkBoxB1, checkBoxB2, checkBoxB3, checkBoxB4, checkBoxB5, checkBoxB6, checkBoxB7;
    private CheckBox checkBoxOP1, checkBoxOP2, checkBoxOP3;

    private TextView txtA0, txtA1, txtA2, txtA3, txtA4, txtA5, txtA6, txtA7, txtA;
    private TextView txtB0, txtB1, txtB2, txtB3, txtB4, txtB5, txtB6, txtB7, txtB;
    private TextView txtC0, txtC1, txtC2, txtC3, txtC4, txtC5, txtC6, txtC7;
    private TextView txtS0, txtS1, txtS2, txtS3, txtS4, txtS5, txtS6, txtS7, txtS;
    private Spinner opCode;

    private NBitALU alu = new NBitALU(8);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        checkBoxA0 = (CheckBox) findViewById(R.id.checkboxA0);
        checkBoxA1 = (CheckBox) findViewById(R.id.checkboxA1);
        checkBoxA2 = (CheckBox) findViewById(R.id.checkboxA2);
        checkBoxA3 = (CheckBox) findViewById(R.id.checkboxA3);
        checkBoxA4 = (CheckBox) findViewById(R.id.checkboxA4);
        checkBoxA5 = (CheckBox) findViewById(R.id.checkboxA5);
        checkBoxA6 = (CheckBox) findViewById(R.id.checkboxA6);
        checkBoxA7 = (CheckBox) findViewById(R.id.checkboxA7);

        checkBoxB0 = (CheckBox) findViewById(R.id.checkboxB0);
        checkBoxB1 = (CheckBox) findViewById(R.id.checkboxB1);
        checkBoxB2 = (CheckBox) findViewById(R.id.checkboxB2);
        checkBoxB3 = (CheckBox) findViewById(R.id.checkboxB3);
        checkBoxB4 = (CheckBox) findViewById(R.id.checkboxB4);
        checkBoxB5 = (CheckBox) findViewById(R.id.checkboxB5);
        checkBoxB6 = (CheckBox) findViewById(R.id.checkboxB6);
        checkBoxB7 = (CheckBox) findViewById(R.id.checkboxB7);

        checkBoxOP1 = (CheckBox) findViewById(R.id.checkboxOP1);
        checkBoxOP2 = (CheckBox) findViewById(R.id.checkboxOP2);
        checkBoxOP3 = (CheckBox) findViewById(R.id.checkboxOP3);

        txtA0 = (TextView) findViewById(R.id.txtA0);
        txtA1 = (TextView) findViewById(R.id.txtA1);
        txtA2 = (TextView) findViewById(R.id.txtA2);
        txtA3 = (TextView) findViewById(R.id.txtA3);
        txtA4 = (TextView) findViewById(R.id.txtA4);
        txtA5 = (TextView) findViewById(R.id.txtA5);
        txtA6 = (TextView) findViewById(R.id.txtA6);
        txtA7 = (TextView) findViewById(R.id.txtA7);
        txtA = (TextView) findViewById(R.id.txtA);

        txtB0 = (TextView) findViewById(R.id.txtB0);
        txtB1 = (TextView) findViewById(R.id.txtB1);
        txtB2 = (TextView) findViewById(R.id.txtB2);
        txtB3 = (TextView) findViewById(R.id.txtB3);
        txtB4 = (TextView) findViewById(R.id.txtB4);
        txtB5 = (TextView) findViewById(R.id.txtB5);
        txtB6 = (TextView) findViewById(R.id.txtB6);
        txtB7 = (TextView) findViewById(R.id.txtB7);
        txtB = (TextView) findViewById(R.id.txtB);

        txtC0 = (TextView) findViewById(R.id.txtC0);
        txtC1 = (TextView) findViewById(R.id.txtC1);
        txtC2 = (TextView) findViewById(R.id.txtC2);
        txtC3 = (TextView) findViewById(R.id.txtC3);
        txtC4 = (TextView) findViewById(R.id.txtC4);
        txtC5 = (TextView) findViewById(R.id.txtC5);
        txtC6 = (TextView) findViewById(R.id.txtC6);
        txtC7 = (TextView) findViewById(R.id.txtC7);

        txtS0 = (TextView) findViewById(R.id.txtS0);
        txtS1 = (TextView) findViewById(R.id.txtS1);
        txtS2 = (TextView) findViewById(R.id.txtS2);
        txtS3 = (TextView) findViewById(R.id.txtS3);
        txtS4 = (TextView) findViewById(R.id.txtS4);
        txtS5 = (TextView) findViewById(R.id.txtS5);
        txtS6 = (TextView) findViewById(R.id.txtS6);
        txtS7 = (TextView) findViewById(R.id.txtS7);
        txtS = (TextView) findViewById(R.id.txtS);

        opCode = (Spinner) findViewById(R.id.opCode);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.op_codes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opCode.setAdapter(adapter);
        opCode.setOnItemSelectedListener(this);

        checkBoxA0.setOnClickListener(this);
        checkBoxA1.setOnClickListener(this);
        checkBoxA2.setOnClickListener(this);
        checkBoxA3.setOnClickListener(this);
        checkBoxA4.setOnClickListener(this);
        checkBoxA5.setOnClickListener(this);
        checkBoxA6.setOnClickListener(this);
        checkBoxA7.setOnClickListener(this);

        checkBoxB0.setOnClickListener(this);
        checkBoxB1.setOnClickListener(this);
        checkBoxB2.setOnClickListener(this);
        checkBoxB3.setOnClickListener(this);
        checkBoxB4.setOnClickListener(this);
        checkBoxB5.setOnClickListener(this);
        checkBoxB6.setOnClickListener(this);
        checkBoxB7.setOnClickListener(this);

        checkBoxOP1.setOnClickListener(this);
        checkBoxOP2.setOnClickListener(this);
        checkBoxOP3.setOnClickListener(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        txtA0.setText(checkBoxA0.isChecked() ? "1" : "0");
        txtA1.setText(checkBoxA1.isChecked() ? "1" : "0");
        txtA2.setText(checkBoxA2.isChecked() ? "1" : "0");
        txtA3.setText(checkBoxA3.isChecked() ? "1" : "0");
        txtA4.setText(checkBoxA4.isChecked() ? "1" : "0");
        txtA5.setText(checkBoxA5.isChecked() ? "1" : "0");
        txtA6.setText(checkBoxA6.isChecked() ? "1" : "0");
        txtA7.setText(checkBoxA7.isChecked() ? "1" : "0");

        txtB0.setText(checkBoxB0.isChecked() ? "1" : "0");
        txtB1.setText(checkBoxB1.isChecked() ? "1" : "0");
        txtB2.setText(checkBoxB2.isChecked() ? "1" : "0");
        txtB3.setText(checkBoxB3.isChecked() ? "1" : "0");
        txtB4.setText(checkBoxB4.isChecked() ? "1" : "0");
        txtB5.setText(checkBoxB5.isChecked() ? "1" : "0");
        txtB6.setText(checkBoxB6.isChecked() ? "1" : "0");
        txtB7.setText(checkBoxB7.isChecked() ? "1" : "0");
        
        executeALU();
    }

    private void setOpCode() {
        int code = 0;
        code += (int) (checkBoxOP1.isChecked() ? Math.pow(2, 2) : 0);
        code += (int) (checkBoxOP2.isChecked() ? Math.pow(2, 1) : 0);
        code += (int) (checkBoxOP3.isChecked() ? Math.pow(2, 0) : 0);
        opCode.setSelection(code);
    }

    private void executeALU() {
        setOpCode();
        alu.setA(checkBoxA0.isChecked(), checkBoxA1.isChecked(), checkBoxA2.isChecked(), checkBoxA3.isChecked(),
                checkBoxA4.isChecked(), checkBoxA5.isChecked(), checkBoxA6.isChecked(), checkBoxA7.isChecked());
        alu.setB(checkBoxB0.isChecked(), checkBoxB1.isChecked(), checkBoxB2.isChecked(), checkBoxB3.isChecked(),
                checkBoxB4.isChecked(), checkBoxB5.isChecked(), checkBoxB6.isChecked(), checkBoxB7.isChecked());
        alu.execute(checkBoxOP1.isChecked(), checkBoxOP2.isChecked(), checkBoxOP3.isChecked());

        txtA0.setText(alu.getA(0) ? "1" : "0");
        txtA1.setText(alu.getA(1) ? "1" : "0");
        txtA2.setText(alu.getA(2) ? "1" : "0");
        txtA3.setText(alu.getA(3) ? "1" : "0");
        txtA4.setText(alu.getA(4) ? "1" : "0");
        txtA5.setText(alu.getA(5) ? "1" : "0");
        txtA6.setText(alu.getA(6) ? "1" : "0");
        txtA7.setText(alu.getA(7) ? "1" : "0");

        txtB0.setText(alu.getB(0) ? "1" : "0");
        txtB1.setText(alu.getB(1) ? "1" : "0");
        txtB2.setText(alu.getB(2) ? "1" : "0");
        txtB3.setText(alu.getB(3) ? "1" : "0");
        txtB4.setText(alu.getB(4) ? "1" : "0");
        txtB5.setText(alu.getB(5) ? "1" : "0");
        txtB6.setText(alu.getB(6) ? "1" : "0");
        txtB7.setText(alu.getB(7) ? "1" : "0");

        txtS0.setText(alu.getSumBit(0) ? "1" : "0");
        txtS1.setText(alu.getSumBit(1) ? "1" : "0");
        txtS2.setText(alu.getSumBit(2) ? "1" : "0");
        txtS3.setText(alu.getSumBit(3) ? "1" : "0");
        txtS4.setText(alu.getSumBit(4) ? "1" : "0");
        txtS5.setText(alu.getSumBit(5) ? "1" : "0");
        txtS6.setText(alu.getSumBit(6) ? "1" : "0");
        txtS7.setText(alu.getSumBit(7) ? "1" : "0");

        txtC0.setText(alu.getCarryBit(0) ? "1" : "0");
        txtC1.setText(alu.getCarryBit(1) ? "1" : "0");
        txtC2.setText(alu.getCarryBit(2) ? "1" : "0");
        txtC3.setText(alu.getCarryBit(3) ? "1" : "0");
        txtC4.setText(alu.getCarryBit(4) ? "1" : "0");
        txtC5.setText(alu.getCarryBit(5) ? "1" : "0");
        txtC6.setText(alu.getCarryBit(6) ? "1" : "0");
        txtC7.setText(alu.getCarryBit(7) ? "1" : "0");

        txtA.setText(String.format(Locale.US, " (%4s)", alu.getA()));
        txtB.setText(String.format(Locale.US, " (%4s)", alu.getB()));
        txtS.setText(String.format(Locale.US, " (%4s)", alu.getSum()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkboxA0:
                txtA0.setText(checkBoxA0.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxA1:
                txtA1.setText(checkBoxA1.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxA2:
                txtA2.setText(checkBoxA2.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxA3:
                txtA3.setText(checkBoxA3.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxA4:
                txtA4.setText(checkBoxA4.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxA5:
                txtA5.setText(checkBoxA5.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxA6:
                txtA6.setText(checkBoxA6.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxA7:
                txtA7.setText(checkBoxA7.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxB0:
                txtB0.setText(checkBoxB0.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxB1:
                txtB1.setText(checkBoxB1.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxB2:
                txtB2.setText(checkBoxB2.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxB3:
                txtB3.setText(checkBoxB3.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxB4:
                txtB4.setText(checkBoxB4.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxB5:
                txtB5.setText(checkBoxB5.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxB6:
                txtB6.setText(checkBoxB6.isChecked() ? "1" : "0");
                break;

            case R.id.checkboxB7:
                txtB7.setText(checkBoxB7.isChecked() ? "1" : "0");
                break;
        }

        executeALU();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() != R.id.opCode)
            return;

        // Convert index to binary
        boolean[] ops = new boolean[3];
        int i = 2;
        int num = position;
        while (num > 0) {
            ops[i] = (num % 2) == 1;
            num /= 2;
            i--;
        }

        checkBoxOP1.setChecked(ops[0]);
        checkBoxOP2.setChecked(ops[1]);
        checkBoxOP3.setChecked(ops[2]);

        executeALU();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
