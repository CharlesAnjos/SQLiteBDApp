package io.github.charlesanjos.sqlitebdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import io.github.charlesanjos.sqlitebdapp.dbconn.BDsqlite;
import io.github.charlesanjos.sqlitebdapp.entities.Pessoa;

public class AddPessoa extends AppCompatActivity {
    BDsqlite bd;
    TextView tv_criar_usuario;
    TextInputEditText ti_nome;
    TextInputEditText ti_idade;
    TextInputEditText ti_telefone;
    TextInputEditText ti_email;
    Button bt_criar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pessoa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_criar_usuario = findViewById(R.id.tv_criar_usuario);
        ti_nome = findViewById(R.id.ti_nome);
        ti_idade = findViewById(R.id.ti_idade);
        ti_telefone = findViewById(R.id.ti_telefone);
        ti_email = findViewById(R.id.ti_email);
        bt_criar = findViewById(R.id.bt_criar);

        bd = new BDsqlite(this);
        bt_criar.setOnClickListener(adicionarPessoa());

        Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
        if (pessoa != null) {
            tv_criar_usuario.setText("Editar contato");
            ti_nome.setText(pessoa.getNome());
            ti_idade.setText(String.valueOf(pessoa.getIdade()));
            ti_telefone.setText(pessoa.getTelefone());
            ti_email.setText(pessoa.getEmail());
            bt_criar.setText("Editar");
            bt_criar.setOnClickListener(editarPessoa(pessoa));
        }
    }

    private View.OnClickListener adicionarPessoa() {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePessoa = ti_nome.getEditableText().toString();
                String idadePessoa = ti_idade.getEditableText().toString();
                String telefonePessoa =
                        ti_telefone.getEditableText().toString();
                String emailPessoa = ti_email.getEditableText().toString();

                if (!nomePessoa.equals("") &&
                        !idadePessoa.equals("") &&
                        !telefonePessoa.equals("") &&
                        !emailPessoa.equals("")) {
                    Pessoa pessoa = new Pessoa(
                            nomePessoa,
                            Integer.parseInt(idadePessoa),
                            telefonePessoa,
                            emailPessoa
                    );
                    ti_nome.setText("");
                    ti_idade.setText("");
                    ti_telefone.setText("");
                    ti_email.setText("");
                    bd.inserirPessoa(pessoa);
                    bd.close();

                    backToMain();

                } else {
                    if (nomePessoa.equals(""))
                        ti_nome.setError("Campo Obrigatorio!");
                    if (idadePessoa.equals(""))
                        ti_idade.setError("Campo Obrigatorio!");
                    if (nomePessoa.equals("")) ti_telefone.setError("Campo " +
                            "Obrigatorio!");
                    if (idadePessoa.equals("")) ti_email.setError("Campo " +
                            "Obrigatorio!");
                }
            }
        };
    }

    private View.OnClickListener editarPessoa(Pessoa pessoa) {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePessoa = ti_nome.getEditableText().toString();
                String idadePessoa = ti_idade.getEditableText().toString();
                String telefonePessoa =
                        ti_telefone.getEditableText().toString();
                String emailPessoa = ti_email.getEditableText().toString();

                if (!nomePessoa.equals("") &&
                        !idadePessoa.equals("") &&
                        !telefonePessoa.equals("") &&
                        !emailPessoa.equals("")) {
                    pessoa.setNome(nomePessoa);
                    pessoa.setIdade(Integer.parseInt(idadePessoa));
                    pessoa.setTelefone(telefonePessoa);
                    pessoa.setEmail(emailPessoa);
                    bd.update(pessoa.getId(), "NOME", pessoa.getNome());
                    bd.update(pessoa.getId(), "IDADE", String.valueOf(pessoa.getIdade()));
                    bd.update(pessoa.getId(), "TELEFONE", pessoa.getTelefone());
                    bd.update(pessoa.getId(), "EMAIL", pessoa.getEmail());
                    bd.close();
                    ti_nome.setText("");
                    ti_idade.setText("");
                    ti_telefone.setText("");
                    ti_email.setText("");

                    backToMain();
                } else {
                    if (nomePessoa.equals(""))
                        ti_nome.setError("Campo Obrigatorio!");
                    if (idadePessoa.equals(""))
                        ti_idade.setError("Campo Obrigatorio!");
                    if (telefonePessoa.equals(""))
                        ti_telefone.setError("Campo " +
                                "Obrigatorio!");
                    if (emailPessoa.equals("")) ti_email.setError("Campo " +
                            "Obrigatorio!");
                }
            }
        };
    }

    public void backToMain() {
        Intent mainActivityIntent = new Intent(getActivity(),
                MainActivity.class);
        startActivity(mainActivityIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //identificar a ação de voltar a tela
            case android.R.id.home:
                //encerra a activity
                backToMain();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public Context getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        bd.close();
        super.onDestroy();
    }
}