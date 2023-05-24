package io.github.charlesanjos.sqlitebdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.github.charlesanjos.sqlitebdapp.dbconn.BDsqlite;
import io.github.charlesanjos.sqlitebdapp.entities.Pessoa;

public class MainActivity extends AppCompatActivity {

    BDsqlite bd;
    TextView tv_criar_usuario;
    ImageButton bt_listar;
    TextInputLayout til_nome;
    TextInputEditText ti_nome;
    TextInputLayout til_idade;
    TextInputEditText ti_idade;
    Button bt_criar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_criar_usuario = findViewById(R.id.tv_criar_usuario);
        bt_listar = findViewById(R.id.bt_listar);
        til_nome = findViewById(R.id.til_nome);
        ti_nome = findViewById(R.id.ti_nome);
        til_idade = findViewById(R.id.til_idade);
        ti_idade = findViewById(R.id.ti_idade);
        bt_criar = findViewById(R.id.bt_criar);

        bd = new BDsqlite(this);
        bt_listar.setOnClickListener(listarPessoas());
        bt_criar.setOnClickListener(adicionarPessoa());

        Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
        if(pessoa != null){
            tv_criar_usuario.setText("Editar usuário");
            ti_nome.setText(pessoa.getNome());
            ti_idade.setText(String.valueOf(pessoa.getIdade()));
            bt_criar.setText("Editar");
            bt_criar.setOnClickListener(editarPessoa(pessoa));
        }
    }

    private View.OnClickListener listarPessoas() {
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent listarPessoasIntent = new Intent(getActivity(),ListaPessoas.class);
                startActivity(listarPessoasIntent);
            }
        };
    }

    private View.OnClickListener adicionarPessoa() {
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                String nomePessoa = ti_nome.getEditableText().toString();
                String idadePessoa = ti_idade.getEditableText().toString();

                if(!nomePessoa.equals("") && !idadePessoa.equals("")){
                    Pessoa pessoa = new Pessoa(nomePessoa, Integer.parseInt(idadePessoa));
                    ti_nome.setText("");
                    ti_idade.setText("");
                    bd.inserirPessoa(pessoa);
                    Intent listarPessoasIntent = new Intent(getActivity(),ListaPessoas.class);
                    startActivity(listarPessoasIntent);
                }
                else {
                    if(nomePessoa.equals("")) til_nome.setError("Campo Obrigatorio!");
                    if(idadePessoa.equals("")) til_idade.setError("Campo Obrigatorio!");
                }
            }
        };
    }

    private View.OnClickListener editarPessoa(Pessoa pessoa) {
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                String nomePessoa = ti_nome.getEditableText().toString();
                String idadePessoa = ti_idade.getEditableText().toString();

                if(!nomePessoa.equals("") && !idadePessoa.equals("")){
                    pessoa.setNome(nomePessoa);
                    pessoa.setIdade(Integer.parseInt(idadePessoa));
                    bd.update(pessoa.getId(),"NOME",pessoa.getNome());
                    bd.update(pessoa.getId(),"IDADE",String.valueOf(pessoa.getIdade()));
                    ti_nome.setText("");
                    ti_idade.setText("");

                    Intent listarPessoasIntent = new Intent(getActivity(),ListaPessoas.class);
                    startActivity(listarPessoasIntent);
                }
                else {
                    if(nomePessoa.equals("")) til_nome.setError("Campo Obrigatorio!");
                    if(idadePessoa.equals("")) til_idade.setError("Campo Obrigatorio!");
                }
            }
        };
    }

    public Context getActivity(){
        return this;
    }

    @Override
    protected void onDestroy() {
        bd.close();
        super.onDestroy();
    }
}