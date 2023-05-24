package io.github.charlesanjos.sqlitebdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import io.github.charlesanjos.sqlitebdapp.dbconn.BDsqlite;
import io.github.charlesanjos.sqlitebdapp.entities.Pessoa;

public class MainActivity extends AppCompatActivity {

    BDsqlite bd;
    ListView listaPessoas;
    ArrayAdapter<Pessoa> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Contatos");

        listaPessoas = findViewById(R.id.listaPessoas);

        bd = new BDsqlite(this);
        List<Pessoa> pessoas = bd.consultarDados();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, pessoas);
        listaPessoas.setAdapter(adapter);
        listaPessoas.setOnItemClickListener(this::onItemClick);
        listaPessoas.setOnItemLongClickListener(this::onItemLongClick);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Pessoa pessoa = (Pessoa) parent.getAdapter().getItem(position);
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle("Detalhes");
        dialog.setMessage("Nome: " + pessoa.getNome() + "\nIdade: " + pessoa.getIdade());
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Deletar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAlertDialog(pessoa);
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Editar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent addPessoaIntent = new Intent(getActivity(),
                                AddPessoa.class);
                        addPessoaIntent.putExtra("pessoa", pessoa);
                        startActivity(addPessoaIntent);
                    }
                });
        dialog.show();
    }

    public void deleteAlertDialog(Pessoa pessoa) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle("Deletando " + pessoa.getNome());
        dialog.setMessage("Esta acao e irreversivel. Tem certeza que deseja " +
                "prosseguir?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Deletar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bd.excluir(pessoa.getId());
                        adapter.remove(pessoa);
                        adapter.notifyDataSetChanged();
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Pessoa pessoa = (Pessoa) parent.getAdapter().getItem(position);
        Toast.makeText(this, pessoa.getNome(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivitymenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainActivityIntent = new Intent(getActivity(),
                        MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
                break;

            case R.id.add:
                Intent addPessoaIntent = new Intent(getActivity(),
                        AddPessoa.class);
                startActivity(addPessoaIntent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Context getActivity() {
        return this;
    }
}