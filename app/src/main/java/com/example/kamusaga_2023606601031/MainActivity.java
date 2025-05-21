package com.example.kamusaga_2023606601031;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private KamusAdapter adapter;
    private List<KamusEntry> entries = new ArrayList<>();
    private EditText inputIndo, inputInggris, inputKorea;
    private Button btnAdd, btnUpdate, btnDelete;
    private ProgressBar progressBar;
    private ApiService apiService;
    private KamusEntry selectedEntry;
    private boolean isDestroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        inputIndo = findViewById(R.id.inputIndo);
        inputInggris = findViewById(R.id.inputInggris);
        inputKorea = findViewById(R.id.inputKorea);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KamusAdapter(entries, entry -> {
            selectedEntry = entry; // Simpan entri yang dipilih
            inputIndo.setText(entry.getBahasaIndo());
            inputInggris.setText(entry.getBahasaInggris());
            inputKorea.setText(entry.getBahasaKorea());
        },
                (entry, position) -> {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Hapus Data")
                            .setMessage("Yakin ingin menghapus entri ini?")
                            .setPositiveButton("Ya", (dialog, which) -> {
                                entries.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, entries.size());
                                showToast("Data dihapus");
                            })
                            .setNegativeButton("Tidak", null)
                            .show();
                });

        recyclerView.setAdapter(adapter);

        apiService = RetrofitClient.getInstance().create(ApiService.class);
        loadEntries();

        btnAdd.setOnClickListener(view -> addEntry());
        btnUpdate.setOnClickListener(view -> updateEntry());
        btnDelete.setOnClickListener(view -> confirmDeleteEntry());
    }

    private void loadEntries() {
        showLoading(true);
        apiService.getAllEntries().enqueue(new Callback<List<KamusEntry>>() {
            @Override
            public void onResponse(Call<List<KamusEntry>> call, Response<List<KamusEntry>> response) {
                showLoading(false);
                if (isDestroyed) return;
                if (response.isSuccessful() && response.body() != null) {
                    entries.clear();
                    entries.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("Gagal memuat data!");
                }
            }

            @Override
            public void onFailure(Call<List<KamusEntry>> call, Throwable t) {
                showLoading(false);
                if (isDestroyed) return;
                showToast("Error: " + t.getMessage());
            }
        });
    }

    private void addEntry() {
        String indo = inputIndo.getText().toString().trim();
        String inggris = inputInggris.getText().toString().trim();
        String korea = inputKorea.getText().toString().trim();

        if (indo.isEmpty() || inggris.isEmpty() || korea.isEmpty()) {
            showToast("Semua field harus diisi!");
            return;
        }

        KamusEntry newEntry = new KamusEntry();
        newEntry.setBahasaIndo(indo);
        newEntry.setBahasaInggris(inggris);
        newEntry.setBahasaKorea(korea);

        Log.d("JSON_SEND", new Gson().toJson(newEntry));

        showLoading(true);

        apiService.createEntry(newEntry).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                if (isDestroyed) return;
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    showToast(response.body().getMessage());
                    clearInputFields();
                    loadEntries();
                } else {
                    showToast("Gagal menambahkan data!");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                if (isDestroyed) return;
                showLoading(false);
                showToast("Error: " + t.getMessage());
            }
        });
    }

    private void updateEntry() {
        if (selectedEntry == null) {
            showToast("Pilih data untuk diperbarui!");
            return;
        }

        String indo = inputIndo.getText().toString().trim();
        String inggris = inputInggris.getText().toString().trim();
        String korea = inputKorea.getText().toString().trim();

        if (indo.isEmpty() || inggris.isEmpty() || korea.isEmpty()) {
            showToast("Semua field harus diisi!");
            return;
        }

        selectedEntry.setBahasaIndo(indo);
        selectedEntry.setBahasaInggris(inggris);
        selectedEntry.setBahasaKorea(korea);

        showLoading(true);
        apiService.updateEntry(selectedEntry.getId(), selectedEntry).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showLoading(false);
                if (isDestroyed) return;
                if (response.isSuccessful()) {
                    loadEntries();
                    clearInputFields();
                    selectedEntry = null;
                    showToast("Data berhasil diperbarui.");
                } else {
                    showToast("Gagal memperbarui data.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showLoading(false);
                if (isDestroyed) return;
                showToast("Error: " + t.getMessage());
            }
        });
    }



    private void confirmDeleteEntry() {
        if (selectedEntry == null) {
            showToast("Pilih data terlebih dahulu untuk dihapus!");
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Hapus Data")
                .setMessage("Yakin ingin menghapus data ini?")
                .setPositiveButton("Ya", (dialog, which) -> deleteEntry())
                .setNegativeButton("Tidak", null)
                .show();
    }


    private void deleteEntry() {
        if (selectedEntry == null) return;

        showLoading(true);

        apiService.deleteEntry(selectedEntry.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showLoading(false);
                if (isDestroyed) return;

                if (response.isSuccessful()) {
                    Log.d("DELETE", "Berhasil hapus dari server");

                    // Hapus dari list dan perbarui tampilan
                    int position = entries.indexOf(selectedEntry);
                    if (position != -1) {
                        entries.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, entries.size());
                    }

                    selectedEntry = null;
                    clearInputFields();
                    showToast("Data berhasil dihapus.");
                } else {
                    Log.e("DELETE", "Gagal hapus di server: " + response.code());
                    showToast("Gagal menghapus data di server.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showLoading(false);
                Log.e("DELETE", "Error: " + t.getMessage());
                showToast("Error: " + t.getMessage());
            }
        });
    }




    private void clearInputFields() {
        inputIndo.setText("");
        inputInggris.setText("");
        inputKorea.setText("");
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showToast(String message) {
        if (!isFinishing() && !isDestroyed) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }
}

