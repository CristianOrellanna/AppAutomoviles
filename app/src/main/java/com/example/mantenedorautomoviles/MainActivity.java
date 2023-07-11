package com.example.mantenedorautomoviles;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    Button btnIngresar, btnAgregar, btnBuscar, btnEliminar;
    EditText txtCodigo, txtModelo, txtPatente, txtPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtModelo = (EditText) findViewById(R.id.txtModelo);
        txtPatente = (EditText) findViewById(R.id.txtPatente);
        txtPrecio = (EditText) findViewById(R.id.txtPrecio);

        final GestorDB gestorDB = new GestorDB(getApplicationContext());

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = gestorDB.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(GestorDB.TablaDatos.COLUMNA_CODIGO,
                        txtCodigo.getText().toString());
                valores.put(GestorDB.TablaDatos.COLUMNA_MODELO,
                        txtModelo.getText().toString());
                valores.put(GestorDB.TablaDatos.COLUMNA_PATENTE,
                        txtPatente.getText().toString());
                valores.put(GestorDB.TablaDatos.COLUMNA_PRECIO,
                        txtPrecio.getText().toString());

                long codigoRegistrado =
                        db.insert(GestorDB.TablaDatos.NOMBRE_TABLA,
                                GestorDB.TablaDatos.COLUMNA_CODIGO, valores);
                txtCodigo.setText("");
                txtModelo.setText("");
                txtPatente.setText("");
                txtPrecio.setText("");
                Toast.makeText(getApplicationContext(), "Se registro el automovil:" +
                        codigoRegistrado, Toast.LENGTH_LONG).show();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //db en modo lectura
                SQLiteDatabase db = gestorDB.getReadableDatabase();
                //Paramatro de busqueda
                String[] paramBusqueda = {txtCodigo.getText().toString()};
                String[] salida = {GestorDB.TablaDatos.COLUMNA_MODELO,
                        GestorDB.TablaDatos.COLUMNA_PATENTE,
                        GestorDB.TablaDatos.COLUMNA_PRECIO};
                //Cursor
                Cursor c = db.query(GestorDB.TablaDatos.NOMBRE_TABLA,
                        salida, GestorDB.TablaDatos.COLUMNA_CODIGO + "=?", paramBusqueda,
                        null, null, null);
                if (c.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "No existe un automovil con el codigo: " +
                            txtCodigo.getText(), Toast.LENGTH_LONG).show();
                } else {
                    c.moveToFirst();
                    txtModelo.setText(c.getString(0));
                    txtPatente.setText(c.getString(1));
                    txtPrecio.setText(c.getString(2));
                }
            }
        });

        //Ingresar boton agregar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //db en modo escritura
                SQLiteDatabase db = gestorDB.getWritableDatabase();
                //Paramatro de busqueda
                String[] paramBusqueda ={txtCodigo.getText().toString()};
                String seleccion =GestorDB.TablaDatos.COLUMNA_CODIGO+"=?";
                int cantidad =
                        db.delete(GestorDB.TablaDatos.NOMBRE_TABLA, seleccion,paramBusqueda);
                txtCodigo.setText("");
                txtModelo.setText("");
                txtPatente.setText("");
                txtPrecio.setText("");
                Toast.makeText(getApplicationContext(), "Se eliminó :" +
                        cantidad + " registro", Toast.LENGTH_LONG).show();
            }
        });
    }
}