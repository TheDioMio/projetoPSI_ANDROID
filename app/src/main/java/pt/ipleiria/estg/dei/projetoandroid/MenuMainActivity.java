package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        carregarCabecalho(); //TODO:criar método

        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        carregarFragmentoInicial();
    }

    private void carregarFragmentoInicial() {
       Fragment fragment = new HomeFragment();
       fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

    }

    private void carregarCabecalho() {
//        email= getIntent().getStringExtra(EMAIL);
//
//        //aceder à sharedPreference e definir o modo de acesso
//        sharedPreferences = getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
//        //definir o Editor para permitir guardar / alterar o valor
//        editor = sharedPreferences.edit();
//        if (email!=null) {
//            editor.putString(EMAIL, email);
//            editor.apply();
//        }else{
//            email = sharedPreferences.getString(EMAIL, "Sem email");
//        }

        View headerView = navigationView.getHeaderView(0);
        TextView nav_tvEmail = headerView.findViewById(R.id.tvEmail);
        //nav_tvEmail.setText(email);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        if (menuItem.getItemId() == R.id.navHome){
            fragment = new HomeFragment();
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav Home");
        } else if (menuItem.getItemId() == R.id.navDetalhesAnimal){
            //fragment = new DinamicoFragment();
            //setTitle(menuItem.getTitle());
            System.out.println("--> Nav Dinamico");
        }


        if (fragment != null){
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

        }


        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


}